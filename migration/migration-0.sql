SET @@group_concat_max_len = 15000;
SET NAMES 'utf8';

DROP TABLE IF EXISTS indiepost.__post_content;
CREATE TABLE indiepost.__post_content (
  `id`      INT(11)    NOT NULL AUTO_INCREMENT,
  `content` LONGTEXT   NOT NULL,
  PRIMARY KEY (`id`)
);

INSERT INTO indiepost.__post_content
  SELECT parent AS no,
         GROUP_CONCAT(IF(type = 1, CONCAT('<p>', data, '</p>'), CONCAT('<figure><img src="', data, '"><figcaption>###</figcaption>###</figure>')) ORDER BY iorder ASC SEPARATOR '') AS content
  FROM indiepost.__detaillist
  WHERE iorder > 2
  GROUP BY parent, ispay;

DROP TABLE IF EXISTS indiepost.__users;
CREATE TABLE indiepost.__users
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  birthday DATETIME,
  displayName VARCHAR(20),
  email VARCHAR(50) NOT NULL,
  gender VARCHAR(255) NOT NULL,
  password VARCHAR(50) NOT NULL,
  phone VARCHAR(15),
  registeredAt DATETIME NOT NULL,
  state VARCHAR(255) NOT NULL,
  username VARCHAR(30) NOT NULL,
  uuid VARCHAR(100)
);


INSERT INTO indiepost.__users (username, password, displayName, email, phone, registeredAt, state, gender) VALUES
  ('indiepost', '*4ACFE3202A5FF5CF467898FC58AAB1D615029441', 'Indiepost', 'sysadmin@indiepost.co.kr', '01073691070', NOW(),
   'ACTIVATED', 'UNIDENTIFIED');

INSERT INTO indiepost.__users (username, password, displayName, email, phone, registeredAt, state, gender) VALUES
  ('byungtae.ahn', '*F0CAB2377492477BE19E6515B27CAC0DD63D9CC2', 'Byungtae Ahn', 'byungtae.ahn@indiepost.co.kr',
   '01037702475', NOW(), 'ACTIVATED', 'MALE');

INSERT INTO indiepost.__users (username, password, displayName, email, phone, registeredAt, state, gender) VALUES
  ('woo.sim', '*4DF6D15ED2E8F9B395708C711F833B003458415D', 'Woo Sim', 'woo.sim@indiepost.co.kr', '01050091575', NOW(),
   'ACTIVATED', 'MALE');

INSERT INTO indiepost.__users (username, password, displayName, email, phone, registeredAt, state, gender) VALUES
  ('imyou', '*8AD29C47922B10281FFAC430F7842C905CEC26E4', 'Im Youcheong', 'habibono@naver.com', '01091564828', NOW(),
   'ACTIVATED', 'FEMALE');

INSERT INTO indiepost.__users (username, password, displayName, email, phone, registeredAt, state, gender) VALUES
  ('lsm', '*C568277650E828192B3EE6217888DCE96B0B007E', 'LEE SAMIN', 'lsm@indiepost.co.kr', '01092515860', NOW(),
   'ACTIVATED', 'FEMALE');

INSERT INTO indiepost.__users (username, password, displayName, email, phone, registeredAt, state, gender) VALUES
  ('rheejae', '*FC05031C3F911B8873C55D5C3273FCCFA905B242', 'LEE YI JAE', 'leeyijae@indiepost.co.kr', '01075155197',
   NOW(), 'ACTIVATED', 'FEMALE');

INSERT INTO indiepost.__users (username, password, displayName, email, phone, registeredAt, state, gender) VALUES
  ('you', '*0C32C69841EBA547CBD06BF2BDC5A806CFBE55F5', 'You Mirae', 'youandmirae@indiepost.co.kr', '01042690922', NOW(),
   'ACTIVATED', 'FEMALE');

INSERT INTO indiepost.__users (username, password, displayName, email, phone, registeredAt, state, gender)
VALUES ('graye', 'indiepost', 'GRAYE', 'graye@indiepost.co.kr', '01000000000', NOW(), 'ACTIVATED', 'UNIDENTIFIED');

INSERT INTO indiepost.__users (username, password, displayName, email, phone, registeredAt, state, gender) VALUES
  ('youngsoo', 'indiepost', 'Yi Youngsoo', 'yiisoungsoo@gmail.com', '01030381084', NOW(), 'ACTIVATED', 'UNIDENTIFIED');


DROP TABLE IF EXISTS indiepost.__roles;
CREATE TABLE indiepost.__roles
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  name VARCHAR(20) NOT NULL
);

INSERT INTO indiepost.__roles (name) VALUES ('User');
INSERT INTO indiepost.__roles (name) VALUES ('Author');
INSERT INTO indiepost.__roles (name) VALUES ('Editor');
INSERT INTO indiepost.__roles (name) VALUES ('EditorInChief');
INSERT INTO indiepost.__roles (name) VALUES ('Administrator');

DROP TABLE IF EXISTS indiepost.__users_roles;
CREATE TABLE indiepost.__users_roles
(
  userId INT(11) NOT NULL,
  roleId INT(11) NOT NULL
);



-- Grant Authorities to Users

INSERT INTO indiepost.__users_roles (userId, roleId) VALUES (1, 1);
INSERT INTO indiepost.__users_roles (userId, roleId) VALUES (1, 2);
INSERT INTO indiepost.__users_roles (userId, roleId) VALUES (1, 3);
INSERT INTO indiepost.__users_roles (userId, roleId) VALUES (1, 4);
INSERT INTO indiepost.__users_roles (userId, roleId) VALUES (1, 5);
INSERT INTO indiepost.__users_roles (userId, roleId) VALUES (2, 1);
INSERT INTO indiepost.__users_roles (userId, roleId) VALUES (2, 2);
INSERT INTO indiepost.__users_roles (userId, roleId) VALUES (2, 3);
INSERT INTO indiepost.__users_roles (userId, roleId) VALUES (2, 4);
INSERT INTO indiepost.__users_roles (userId, roleId) VALUES (2, 5);
INSERT INTO indiepost.__users_roles (userId, roleId) VALUES (3, 1);
INSERT INTO indiepost.__users_roles (userId, roleId) VALUES (3, 2);
INSERT INTO indiepost.__users_roles (userId, roleId) VALUES (3, 3);
INSERT INTO indiepost.__users_roles (userId, roleId) VALUES (3, 4);
INSERT INTO indiepost.__users_roles (userId, roleId) VALUES (3, 5);
INSERT INTO indiepost.__users_roles (userId, roleId) VALUES (4, 1);
INSERT INTO indiepost.__users_roles (userId, roleId) VALUES (4, 2);
INSERT INTO indiepost.__users_roles (userId, roleId) VALUES (4, 3);
INSERT INTO indiepost.__users_roles (userId, roleId) VALUES (4, 4);
INSERT INTO indiepost.__users_roles (userId, roleId) VALUES (5, 1);
INSERT INTO indiepost.__users_roles (userId, roleId) VALUES (5, 2);
INSERT INTO indiepost.__users_roles (userId, roleId) VALUES (5, 3);
INSERT INTO indiepost.__users_roles (userId, roleId) VALUES (5, 4);
INSERT INTO indiepost.__users_roles (userId, roleId) VALUES (6, 1);
INSERT INTO indiepost.__users_roles (userId, roleId) VALUES (6, 2);
INSERT INTO indiepost.__users_roles (userId, roleId) VALUES (6, 3);
INSERT INTO indiepost.__users_roles (userId, roleId) VALUES (7, 1);
INSERT INTO indiepost.__users_roles (userId, roleId) VALUES (7, 2);
INSERT INTO indiepost.__users_roles (userId, roleId) VALUES (7, 3);
INSERT INTO indiepost.__users_roles (userId, roleId) VALUES (8, 1);
INSERT INTO indiepost.__users_roles (userId, roleId) VALUES (8, 2);
INSERT INTO indiepost.__users_roles (userId, roleId) VALUES (9, 1);
INSERT INTO indiepost.__users_roles (userId, roleId) VALUES (9, 2);


DROP TABLE IF EXISTS indiepost.__categories;
CREATE TABLE indiepost.__categories
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  displayOrder INT(11) NOT NULL,
  name VARCHAR(20) NOT NULL,
  slug VARCHAR(20) NOT NULL,
  parentId INT(11)
);

INSERT INTO indiepost.__categories (id, displayOrder, name, slug) VALUES (2, 1, 'Film', 'film');
INSERT INTO indiepost.__categories (id, displayOrder, name, slug) VALUES (3, 7, 'Award', 'award');
INSERT INTO indiepost.__categories (id, displayOrder, name, slug) VALUES (7, 6, 'Visual', 'visual');
INSERT INTO indiepost.__categories (id, displayOrder, name, slug) VALUES (8, 5, 'Venue', 'venue');
INSERT INTO indiepost.__categories (id, displayOrder, name, slug) VALUES (9, 8, 'Event', 'event');
INSERT INTO indiepost.__categories (id, displayOrder, name, slug) VALUES (10, 4, 'People', 'people');
INSERT INTO indiepost.__categories (id, displayOrder, name, slug) VALUES (11, 2, 'Music', 'music');
INSERT INTO indiepost.__categories (id, displayOrder, name, slug) VALUES (21, 3, 'Short', 'short');
INSERT INTO indiepost.__categories (id, displayOrder, name, slug) VALUES (22, 9, 'Project', 'project');


DROP TABLE IF EXISTS indiepost.__posts;
CREATE TABLE indiepost.__posts
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  bookmarkedCount INT(11) NOT NULL,
  commentsCount INT(11) NOT NULL,
  content LONGTEXT NOT NULL,
  createdAt DATETIME NOT NULL,
  excerpt VARCHAR(300) NOT NULL,
  featuredImage VARCHAR(120) NOT NULL,
  likesCount INT(11) NOT NULL,
  modifiedAt DATETIME NOT NULL,
  publishedAt DATETIME NOT NULL,
  status VARCHAR(255) NOT NULL,
  title VARCHAR(100) NOT NULL,
  postType VARCHAR(255) NOT NULL,
  authorId INT(11) NOT NULL,
  categoryId INT(11) NOT NULL,
  editorId INT(11) NOT NULL
);

DROP TABLE IF EXISTS indiepost.__contentlist;
CREATE TABLE indiepost.__contentlist LIKE indiepost.contentlist;
INSERT indiepost.__contentlist
  SELECT * FROM indiepost.contentlist;
ALTER TABLE indiepost.__contentlist ADD (thumbnail VARCHAR(200));
ALTER TABLE indiepost.__contentlist ADD (width INT);
ALTER TABLE indiepost.__contentlist ADD (height INT);

DROP TABLE IF EXISTS indiepost.__detaillist;
CREATE TABLE indiepost.__detaillist LIKE indiepost.detaillist;
INSERT indiepost.__detaillist
  SELECT * FROM indiepost.detaillist;
ALTER TABLE indiepost.__detaillist ADD (thumbnail VARCHAR(200));
ALTER TABLE indiepost.__detaillist ADD (width INT);
ALTER TABLE indiepost.__detaillist ADD (height INT);

INSERT INTO indiepost.__posts (id, title, featuredImage, excerpt, content, status, type, bookmarkedCount, commentsCount, likesCount,
                   createdAt, modifiedAt, publishedAt, authorId, editorId, categoryId)
  SELECT c.no, c.CONTENTNAME, c.IMAGEURL, c.CONTENTTEXT,
    d.content, 'PUBLISHED', 'POST', c.jjim, 0, c.goods, STR_TO_DATE(c.REGDATE, '%Y%m%d'), STR_TO_DATE(c.REGDATE, '%Y%m%d'), STR_TO_DATE(c.REGDATE, '%Y%m%d'), 1, 1, c.MENUNO
  FROM indiepost.__contentlist AS c
    INNER JOIN indiepost.__post_content AS d
      ON c.no = d.no;

