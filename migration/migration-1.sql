SET @@group_concat_max_len = 15000;
SET NAMES 'utf8';

-- Migrate Images
DROP TABLE IF EXISTS indiepost.__images;
INSERT INTO indiepost.__images (id, directory, original, thumbnail, postId, filesize, width, height, isFeatured, uploadedAt)
  SELECT d.no, SUBSTRING_INDEX(d.data, '/', 5), SUBSTRING_INDEX(d.data, '/', -1), SUBSTRING_INDEX(d.thumbnail, '/', -1), 0, d.parent, d.width, d.height, FALSE, STR_TO_DATE(c.REGDATE, '%Y%m%d')
  FROM indiepost.__detaillist AS d
    INNER JOIN
    indiepost.__contentlist AS c
      ON d.parent = c.no
  WHERE type = 2 AND iorder > 2;