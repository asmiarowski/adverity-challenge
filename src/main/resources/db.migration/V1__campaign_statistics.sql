CREATE TABLE `campaign_statistics`
(
    `id`          INT          NOT NULL AUTO_INCREMENT,
    `campaign`    VARCHAR(255) NOT NULL,
    `datasource`  VARCHAR(255) NOT NULL,
    `impressions` BIGINT       NOT NULL,
    `clicks`      INT          NOT NULL,
    `date`        TIMESTAMP    NOT NULL,

    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8;
