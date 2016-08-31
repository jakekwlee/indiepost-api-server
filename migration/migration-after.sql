SET @@group_concat_max_len = 15000;
SET NAMES 'utf8';

DROP TABLE IF EXISTS indiepost.__post_content;
CREATE TABLE indiepost.__post_content (
  `id`      INT(11)    NOT NULL AUTO_INCREMENT,
  `content` LONGTEXT   NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 28880
  DEFAULT CHARSET = utf8;

INSERT INTO indiepost.__post_content
  SELECT parent AS id,
         GROUP_CONCAT(IF(type = 1, CONCAT('<p>', data, '</p>'), CONCAT('<p><img src="', data, '"></p>')) ORDER BY iorder ASC SEPARATOR '') AS content
  FROM indiepost.__detaillist
  WHERE iorder > 2
  GROUP BY parent, ispay;