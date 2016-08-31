SET @@group_concat_max_len = 15000;
SET NAMES 'utf8';

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