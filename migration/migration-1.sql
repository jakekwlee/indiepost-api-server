SET @@group_concat_max_len = 15000;
SET NAMES 'utf8';


-- Migrate Posts
DROP TABLE IF EXISTS indiepost.__posts;
CREATE TABLE indiepost.__posts
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  bookmarkedCount INT(11),
  commentsCount INT(11),
  content LONGTEXT,
  createdAt DATETIME,
  excerpt VARCHAR(300),
  featuredImage VARCHAR(120),
  likesCount INT(11),
  modifiedAt DATETIME,
  publishedAt DATETIME,
  status VARCHAR(255),
  title VARCHAR(100),
  postType VARCHAR(255),
  authorId INT(11),
  categoryId INT(11),
  editorId INT(11)
);

DROP TABLE IF EXISTS indiepost.__post_content;
CREATE TABLE indiepost.__post_content (
  `id`      INT(11)    NOT NULL AUTO_INCREMENT,
  `content` LONGTEXT   NOT NULL,
  PRIMARY KEY (`id`)
);

INSERT INTO indiepost.__post_content
  SELECT parent AS id,
         GROUP_CONCAT(IF(type = 1, CONCAT('<p>', data, '</p>'), CONCAT('<figure><img src="', data, '" width="', width, '" height="', height,'"></figure>')) ORDER BY iorder ASC SEPARATOR '') AS content
  FROM indiepost.__detaillist
  WHERE iorder > 2
  GROUP BY parent, ispay;


INSERT INTO indiepost.__posts (id, title, featuredImage, excerpt, content, status, postType, bookmarkedCount, commentsCount, likesCount,
                               createdAt, modifiedAt, publishedAt, authorId, editorId, categoryId)
  SELECT c.no, c.CONTENTNAME, c.IMAGEURL, c.CONTENTTEXT,
    d.content, 'PUBLISHED', 'POST', c.jjim, 0, c.goods, STR_TO_DATE(c.REGDATE, '%Y%m%d'), STR_TO_DATE(c.REGDATE, '%Y%m%d'), STR_TO_DATE(c.REGDATE, '%Y%m%d'), 1, 1, c.MENUNO
  FROM indiepost.__contentlist AS c
    INNER JOIN indiepost.__post_content AS d
      ON c.no = d.id;

UPDATE indiepost.__posts SET content = REPLACE(content, '\r\n', '<br>') WHERE id < 99999999;

DROP TABLE IF EXISTS indiepost.__tags;
CREATE TABLE indiepost.__tags
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  name VARCHAR(50),
  slug VARCHAR(50)
);
CREATE UNIQUE INDEX UK_qx0tc0a246fh8sfhfdw1v3esb ON indiepost.__tags (slug);
CREATE UNIQUE INDEX UK_yjphpxl5plfb14aaptyfiflh ON indiepost.__tags (name);

DROP TABLE IF EXISTS indiepost.__posts_tags;
CREATE TABLE indiepost.__posts_tags
(
  postId INT(11) NOT NULL,
  tagId INT(11) NOT NULL,
  CONSTRAINT `PRIMARY` PRIMARY KEY (postId, tagId)
);
CREATE INDEX FK_s6o61as0pydf325jlsti4p85f ON indiepost.__posts_tags (tagId);