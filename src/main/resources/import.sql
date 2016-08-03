SET @@group_concat_max_len = 15000;
SET NAMES 'utf8';

DROP TABLE IF EXISTS `DetaillistReformed`;
CREATE TABLE `DetaillistReformed` (
  `id`      INT(11)    NOT NULL AUTO_INCREMENT,
  `content` LONGTEXT   NOT NULL,
  `is_pay`  TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 28880
  DEFAULT CHARSET = utf8;

INSERT INTO DetaillistReformed
  SELECT parent AS id,
    GROUP_CONCAT(IF(type = 1, CONCAT('<p>', data, '</p>'), CONCAT('<p><img src="', data, '"></p>'))ORDER BY iorder ASC SEPARATOR '') AS content,
    ispay
  FROM detaillist
  WHERE iorder > 2
  GROUP BY parent;

-- Create User Roles

INSERT INTO Roles (name) VALUES ('User');
INSERT INTO Roles (name) VALUES ('Author');
INSERT INTO Roles (name) VALUES ('Editor');
INSERT INTO Roles (name) VALUES ('EditorInChief');
INSERT INTO Roles (name) VALUES ('Administrator');

-- Create Users

INSERT INTO Users (username, password, displayname, email, phone, registeredAt, state, gender) VALUES
  ('indiepost', '*4ACFE3202A5FF5CF467898FC58AAB1D615029441', 'Indiepost', 'sysadmin@indiepost.co.kr', '01073691070', NOW(),
   'ACTIVATED', 'UNIDENTIFIED');

INSERT INTO Users (username, password, displayname, email, phone, registeredAt, state, gender) VALUES
  ('byungtae.ahn', '*F0CAB2377492477BE19E6515B27CAC0DD63D9CC2', 'Byungtae Ahn', 'byungtae.ahn@indiepost.co.kr',
   '01037702475', NOW(), 'ACTIVATED', 'MALE');

INSERT INTO Users (username, password, displayname, email, phone, registeredAt, state, gender) VALUES
  ('woo.sim', '*4DF6D15ED2E8F9B395708C711F833B003458415D', 'Woo Sim', 'woo.sim@indiepost.co.kr', '01050091575', NOW(),
   'ACTIVATED', 'MALE');

INSERT INTO Users (username, password, displayname, email, phone, registeredAt, state, gender) VALUES
  ('imyou', '*8AD29C47922B10281FFAC430F7842C905CEC26E4', 'Im Youchung', 'habibono@naver.com', '01091564828', NOW(),
   'ACTIVATED', 'FEMALE');

INSERT INTO Users (username, password, displayname, email, phone, registeredAt, state, gender) VALUES
  ('lsm', '*C568277650E828192B3EE6217888DCE96B0B007E', 'LEE SAMIN', 'lsm@indiepost.co.kr', '01092515860', NOW(),
   'ACTIVATED', 'FEMALE');

INSERT INTO Users (username, password, displayname, email, phone, registeredAt, state, gender) VALUES
  ('rheejae', '*FC05031C3F911B8873C55D5C3273FCCFA905B242', 'LEE YI JAE', 'leeyijae@indiepost.co.kr', '01075155197',
   NOW(), 'ACTIVATED', 'FEMALE');

INSERT INTO Users (username, password, displayname, email, phone, registeredAt, state, gender) VALUES
  ('you', '*0C32C69841EBA547CBD06BF2BDC5A806CFBE55F5', 'You Mirae', 'youandmirae@indiepost.co.kr', '01042690922', NOW(),
   'ACTIVATED', 'FEMALE');

INSERT INTO Users (username, password, displayName, email, phone, registeredAt, state, gender)
VALUES ('graye', 'indiepost', 'GRAYE', 'graye@indiepost.co.kr', '01000000000', NOW(), 'ACTIVATED', 'UNIDENTIFIED');

INSERT INTO Users (username, password, displayName, email, phone, registeredAt, state, gender) VALUES
  ('youngsoo', 'indiepost', 'Yi Youngsoo', 'yiisoungsoo@gmail.com', '01030381084', NOW(), 'ACTIVATED', 'UNIDENTIFIED');

-- Grant Authorities to Users

INSERT INTO Users_Roles (userId, roleId) VALUES (1, 1);
INSERT INTO Users_Roles (userId, roleId) VALUES (1, 2);
INSERT INTO Users_Roles (userId, roleId) VALUES (1, 3);
INSERT INTO Users_Roles (userId, roleId) VALUES (1, 4);
INSERT INTO Users_Roles (userId, roleId) VALUES (1, 5);
INSERT INTO Users_Roles (userId, roleId) VALUES (2, 1);
INSERT INTO Users_Roles (userId, roleId) VALUES (2, 2);
INSERT INTO Users_Roles (userId, roleId) VALUES (2, 3);
INSERT INTO Users_Roles (userId, roleId) VALUES (2, 4);
INSERT INTO Users_Roles (userId, roleId) VALUES (2, 5);
INSERT INTO Users_Roles (userId, roleId) VALUES (3, 1);
INSERT INTO Users_Roles (userId, roleId) VALUES (3, 2);
INSERT INTO Users_Roles (userId, roleId) VALUES (3, 3);
INSERT INTO Users_Roles (userId, roleId) VALUES (3, 4);
INSERT INTO Users_Roles (userId, roleId) VALUES (3, 5);
INSERT INTO Users_Roles (userId, roleId) VALUES (4, 1);
INSERT INTO Users_Roles (userId, roleId) VALUES (4, 2);
INSERT INTO Users_Roles (userId, roleId) VALUES (4, 3);
INSERT INTO Users_Roles (userId, roleId) VALUES (4, 4);
INSERT INTO Users_Roles (userId, roleId) VALUES (5, 1);
INSERT INTO Users_Roles (userId, roleId) VALUES (5, 2);
INSERT INTO Users_Roles (userId, roleId) VALUES (5, 3);
INSERT INTO Users_Roles (userId, roleId) VALUES (5, 4);
INSERT INTO Users_Roles (userId, roleId) VALUES (6, 1);
INSERT INTO Users_Roles (userId, roleId) VALUES (6, 2);
INSERT INTO Users_Roles (userId, roleId) VALUES (6, 3);
INSERT INTO Users_Roles (userId, roleId) VALUES (7, 1);
INSERT INTO Users_Roles (userId, roleId) VALUES (7, 2);
INSERT INTO Users_Roles (userId, roleId) VALUES (7, 3);
INSERT INTO Users_Roles (userId, roleId) VALUES (8, 1);
INSERT INTO Users_Roles (userId, roleId) VALUES (8, 2);
INSERT INTO Users_Roles (userId, roleId) VALUES (9, 1);
INSERT INTO Users_Roles (userId, roleId) VALUES (9, 2);

-- Create Categories

INSERT INTO Categories (id, displayOrder, name, slug) VALUES (2, 1, 'Film', 'film');
INSERT INTO Categories (id, displayOrder, name, slug) VALUES (3, 7, 'Award', 'award');
INSERT INTO Categories (id, displayOrder, name, slug) VALUES (7, 6, 'Visual', 'visual');
INSERT INTO Categories (id, displayOrder, name, slug) VALUES (8, 5, 'Venue', 'venue');
INSERT INTO Categories (id, displayOrder, name, slug) VALUES (9, 8, 'Event', 'event');
INSERT INTO Categories (id, displayOrder, name, slug) VALUES (10, 4, 'People', 'people');
INSERT INTO Categories (id, displayOrder, name, slug) VALUES (11, 2, 'Music', 'music');
INSERT INTO Categories (id, displayOrder, name, slug) VALUES (21, 3, 'Short', 'short');
INSERT INTO Categories (id, displayOrder, name, slug) VALUES (22, 9, 'Project', 'project');

-- Migrate Media Contents
INSERT INTO MediaContents (id, location, mimeType, price, isPaidContent)
  SELECT no, data, 'image/jpeg', 0, 0
  FROM detaillist
  WHERE type = 2 AND iorder > 2;

-- Migrate Posts
INSERT INTO Posts (id, title, titleImage, excerpt, content, status, bookmarkedCount, commentsCount, likesCount,
                   createdAt, modifiedAt, publishedAt, reservedAt, authorId, editorId, categoryId)
  SELECT c.no, c.CONTENTNAME, c.IMAGEURL, c.CONTENTTEXT,
    d.content, 'PUBLISHED', 0, 0, 0, NOW(), NOW(), NOW(), NOW(), 1, 1, c.MENUNO
  FROM contentlist AS c
    INNER JOIN DetaillistReformed AS d
      ON c.no = d.id;

-- Make Relation Posts and Media Contents
INSERT INTO Posts_MediaContents (postId, mediaContentId)
  SELECT parent, no
  FROM detaillist
  WHERE type = 2 AND iorder > 2;

-- Delete Temporal Table
DROP TABLE IF EXISTS `DetaillistReformed`;