package com.kartago.blacklotus.importer;

import com.kartago.blacklotus.card.model.Card;
import com.kartago.blacklotus.expansion.model.Expansion;
import com.kartago.blacklotus.printing.model.Printing;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.DeserializationFeature;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class Importer implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(Importer.class);
    private final EntityManager entityManager;
    private final ObjectMapper mapper;
    private final TransactionTemplate transaction;

    @Value("${scryfall.import.enabled:false}")
    private boolean enabled;
    @Value("${scryfall.import.file:data/commander-cards-scryfall.jsonl}")
    private Path file;
    @Value("${scryfall.import.batch-size:250}")
    private int batchSize;

    public Importer(EntityManager entityManager, ObjectMapper mapper, PlatformTransactionManager manager) {
        this.entityManager = entityManager;
        this.mapper = mapper;
        this.transaction = new TransactionTemplate(manager);
        this.transaction.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    @Override
    public void run(ApplicationArguments args) {
        if (enabled) importFile(file, batchSize);
    }

    /** Insert-only, restartable import. Memory and persistence context are bounded by batchSize. */
    public synchronized ImportResult importFile(Path file, int batchSize) {
        if (batchSize < 1 || batchSize > 1000) throw new IllegalArgumentException("batchSize debe estar entre 1 y 1000");
        Path source = file.toAbsolutePath().normalize();
        ImportResult result = new ImportResult(0, 0, 0, 0, 0);
        List<Record> batch = new ArrayList<>(batchSize);
        long lineNumber = 0;
        try (BufferedReader reader = Files.newBufferedReader(source, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1 && line.startsWith("\uFEFF")) line = line.substring(1);
                if (line.isBlank()) continue;
                JsonNode node = null;
                try {
                    node = mapper.reader().with(DeserializationFeature.FAIL_ON_TRAILING_TOKENS).readTree(line);
                    if (node == null || !node.isObject()) throw new IllegalArgumentException("se esperaba un objeto JSON por línea (JSONL)");
                    batch.add(new Record(lineNumber, node, ScryfallMapper.uuid(node, "id"),
                            ScryfallMapper.oracleId(node), ScryfallMapper.uuid(node, "set_id")));
                } catch (RuntimeException ex) {
                    throw failure(source, lineNumber, node, ex);
                }
                if (batch.size() == batchSize) {
                    result = result.plus(saveBatch(source, batch));
                    batch.clear();
                    log.info("Importación {}: {}", source, result);
                }
            }
            if (!batch.isEmpty()) result = result.plus(saveBatch(source, batch));
            log.info("Importación completada {}: {}", source, result);
            return result;
        } catch (IOException | RuntimeException ex) {
            String message = "Importación detenida: archivo=" + source + ", línea leída=" + lineNumber
                    + ", confirmado=" + result + ". El lote pendiente no se guardó; los lotes anteriores permanecen. "
                    + "Se puede volver a ejecutar sin duplicar. Causa: " + ex.getMessage();
            log.error(message, ex);
            throw new ImportException(message, ex);
        }
    }

    private ImportResult saveBatch(Path file, List<Record> batch) {
        try {
            return transaction.execute(status -> persistBatch(file, batch, false));
        } catch (RuntimeException ex) {
            if (ex instanceof ImportException) throw ex;
            // A deferred constraint failure may surface only at flush/commit. Replay in a
            // rollback-only transaction, flushing each record, to locate the offending row.
            try {
                transaction.executeWithoutResult(status -> {
                    status.setRollbackOnly();
                    persistBatch(file, batch, true);
                });
            } catch (RuntimeException diagnostic) {
                ex.addSuppressed(diagnostic);
                throw new ImportException("Lote líneas " + batch.getFirst().line + "-" + batch.getLast().line
                        + " revertido. Diagnóstico: " + diagnostic.getMessage() + ". Error original: " + cause(ex), ex);
            }
            throw new ImportException("No se pudo confirmar el lote de " + file + ", líneas "
                    + batch.getFirst().line + "-" + batch.getLast().line + ": " + cause(ex), ex);
        }
    }

    private ImportResult persistBatch(Path file, List<Record> batch, boolean diagnostic) {
        Set<UUID> printingIds = batch.stream().map(Record::printingId).collect(Collectors.toSet());
        Set<UUID> oracleIds = batch.stream().map(Record::oracleId).collect(Collectors.toSet());
        Set<UUID> setIds = batch.stream().map(Record::setId).collect(Collectors.toSet());
        Set<UUID> existing = new HashSet<>(entityManager.createQuery(
                "select p.scryfallId from Printing p where p.scryfallId in :ids", UUID.class)
                .setParameter("ids", printingIds).getResultList());
        Map<UUID, Card> cards = entityManager.createQuery("select c from Card c where c.oracleId in :ids", Card.class)
                .setParameter("ids", oracleIds).getResultStream().collect(Collectors.toMap(Card::getOracleId, Function.identity()));
        Map<UUID, Expansion> expansions = entityManager.createQuery(
                "select e from Expansion e where e.scryfallId in :ids", Expansion.class)
                .setParameter("ids", setIds).getResultStream().collect(Collectors.toMap(Expansion::getScryfallId, Function.identity()));
        long newCards = 0, newExpansions = 0, newPrintings = 0, skipped = 0;
        for (Record record : batch) {
            if (!existing.add(record.printingId)) {
                skipped++;
                continue;
            }
            try {
                Card card = cards.get(record.oracleId);
                if (card == null) {
                    card = ScryfallMapper.card(record.node, record.oracleId);
                    entityManager.persist(card);
                    cards.put(record.oracleId, card);
                    newCards++;
                }
                Expansion expansion = expansions.get(record.setId);
                if (expansion == null) {
                    expansion = ScryfallMapper.expansion(record.node, record.setId);
                    entityManager.persist(expansion);
                    expansions.put(record.setId, expansion);
                    newExpansions++;
                }
                Printing printing = ScryfallMapper.printing(record.node, record.printingId, card, expansion);
                // Set the owning side without loading all historical printings of the card.
                entityManager.persist(printing);
                newPrintings++;
                if (diagnostic) entityManager.flush();
            } catch (RuntimeException ex) {
                throw failure(file, record.line, record.node, ex);
            }
        }
        entityManager.flush();
        entityManager.clear();
        return new ImportResult(batch.size(), newCards, newPrintings, newExpansions, skipped);
    }

    private static ImportException failure(Path file, long line, JsonNode node, RuntimeException ex) {
        String context = node == null ? "JSON inválido" : "id=" + node.path("id") + ", oracle_id="
                + node.path("oracle_id") + ", set_id=" + node.path("set_id") + ", name=" + node.path("name");
        return new ImportException("Archivo=" + file + ", línea=" + line + ", " + context
                + ". " + cause(ex) + ". Revisar el campo indicado, el enum, la longitud/tipo de columna o la restricción SQL del modelo.", ex);
    }

    private static String cause(Throwable error) {
        StringJoiner details = new StringJoiner(" -> ");
        for (Throwable current = error; current != null; current = current.getCause()) {
            details.add(current.getClass().getSimpleName() + ": " + current.getMessage());
        }
        return details.toString();
    }

    private record Record(long line, JsonNode node, UUID printingId, UUID oracleId, UUID setId) {}

    public record ImportResult(long records, long cards, long printings, long expansions, long skipped) {
        ImportResult plus(ImportResult other) {
            return new ImportResult(records + other.records, cards + other.cards, printings + other.printings,
                    expansions + other.expansions, skipped + other.skipped);
        }
    }

    public static class ImportException extends RuntimeException {
        public ImportException(String message, Throwable cause) { super(message, cause); }
    }
}
