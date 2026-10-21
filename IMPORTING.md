# Importación de Scryfall

`Importer` es un componente Spring que lee **JSONL UTF-8**: un objeto completo por línea,
como `data/commander-cards-scryfall.jsonl`. No admite el array JSON de una descarga bulk
sin convertir previamente a JSONL. Las líneas vacías se ignoran.

Para ejecutar desde la raíz del proyecto con la base MySQL configurada:

```powershell
.\mvnw.cmd spring-boot:run '-Dspring-boot.run.arguments=--scryfall.import.enabled=true --spring.jpa.show-sql=false'
```

También se puede llamar al bean `importer.importFile(path, batchSize)` desde código.
La activación automática está deshabilitada por defecto. Propiedades:

```properties
scryfall.import.enabled=false
scryfall.import.file=data/commander-cards-scryfall.jsonl
scryfall.import.batch-size=250
```

El tamaño admite entre 1 y 1000 registros. Cada lote tiene una transacción nueva,
consulta los IDs existentes en tres consultas y libera su contexto de persistencia.
No mantiene una lista global de cartas ni carga las impresiones históricas de una carta.
Los IDs únicos de la base respaldan la deduplicación: `Card.oracleId`,
`Printing.scryfallId` y `Expansion.scryfallId` (campo `set_id` de Scryfall).
Repetir la carga omite impresiones existentes; **no actualiza** precios ni datos ya guardados.
Los datos compartidos de una carta o expansión se toman de su primera aparición.

Los lotes limitan memoria y transacciones. Aunque se configura batching JDBC,
los INSERT de entidades con `GenerationType.IDENTITY` se ejecutan individualmente
para recuperar sus IDs; no se promete un único INSERT SQL por lote.

## Mapeo

- Una carta simple genera una `CardFace` y una `PrintingFace`.
- `card_faces` genera las caras de reglas. Si existe `image_uris` en la raíz
  (split/adventure), se guarda una sola cara de impresión; si no, se guardan las imágenes por cara.
- Las `reversible_card` con el mismo `oracle_id` en ambas caras usan una única carta
  de reglas y conservan ambas caras de impresión. Si tienen IDs distintos, se informa
  explícitamente que la relación `Printing.card` requiere adaptación.
- Se admiten los campos de imagen actuales y los anteriores (`small`, `normal`, `large`,
  `border_crop`, `art_crop`); precios e imágenes ausentes permanecen nulos.
- `manaValue` es `int` en `Card` y `CardFace`: acepta números como `3.0`,
  pero rechaza fracciones y valores fuera del rango de `int`, sin truncar ni redondear.
  `oracleText` se almacena como `TEXT`.
- `Expansion.releasedAt` queda nulo: el `released_at` del registro corresponde a
  la impresión, no garantiza la fecha de lanzamiento de la expansión.

## Fallos y reanudación

Se detiene ante el primer error; el lote afectado se revierte completamente y los lotes
anteriores permanecen confirmados. El error incluye archivo, línea, IDs, nombre,
campo/valor de conversión y cadena de causas (incluido SQL cuando corresponde).
Si el fallo aparece al hacer flush/commit, se reproduce el lote en una transacción
que siempre se revierte y hace flush por registro para intentar localizarlo.
Los fallos de conexión/commit que no se reproduzcan conservan el rango de líneas y la causa original.
Corregir el archivo/modelo y volver a ejecutar es suficiente para reanudar sin duplicados.
Ejecutar una sola importación a la vez; las restricciones únicas impiden duplicados
entre procesos concurrentes, pero una colisión puede abortar un lote.

## Verificación

### Base MySQL creada antes de agregar los tokens

Si aparece `Check constraint 'card_chk_1' is violated` al importar `Spirit`,
revisar el CHECK existente con la consulta de
`sql/2026-10-20-expand-card-layout-check.sql`. Si limita `layout` a 0..16,
ejecutar el ALTER de ese archivo con la aplicación detenida y la base `blacklotus`
seleccionada. Amplía el rango a 0..18 sin borrar datos ni cambiar los ordinales
existentes. Si el CHECK corresponde a otra columna, no ejecutar ese ALTER.

`ddl-auto=update` no debe usarse como sustituto de esta migración del CHECK.
Las pruebas H2 crean tablas nuevas y, por eso, no detectan restricciones antiguas
de una instalación MySQL existente. Después de migrar, reiniciar la importación.
No cambiar directamente a `EnumType.STRING`: los datos existentes son ordinales
y ese cambio necesitaría otra migración.

```powershell
.\mvnw.cmd '-Dtest=ImporterTests,BulkMappingTests,BlacklotusApplicationTests' test
# Validar además TODOS los registros locales, sin guardar en MySQL:
.\mvnw.cmd '-Dtest=ImporterTests,BulkMappingTests,BlacklotusApplicationTests' -DvalidateBulk=true test
```

Las pruebas de persistencia usan H2. La prueba opcional del archivo completo valida
el mapeo en memoria, registro a registro; no sustituye una carga real contra MySQL.
