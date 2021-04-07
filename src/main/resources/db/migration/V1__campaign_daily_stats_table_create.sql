CREATE TABLE `campaign_daily_stats`
(
    `id`          INT          NOT NULL AUTO_INCREMENT,
    `campaign`    VARCHAR(255) NOT NULL,
    `datasource`  VARCHAR(255) NOT NULL,
    `impressions` BIGINT       NOT NULL,
    `clicks`      INT          NOT NULL,
    `day`         DATE         NOT NULL,

    PRIMARY KEY (`id`)
)
    CHARSET = utf8;