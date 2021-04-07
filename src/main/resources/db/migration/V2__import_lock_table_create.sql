CREATE TABLE `import_lock`
(
    `id`       INT                   NOT NULL AUTO_INCREMENT,
    `importer` VARCHAR(255)          NOT NULL,
    `locked`   BOOLEAN DEFAULT FALSE NOT NULL,

    PRIMARY KEY (`id`),
    UNIQUE KEY (`importer`)
)
    CHARSET = utf8;