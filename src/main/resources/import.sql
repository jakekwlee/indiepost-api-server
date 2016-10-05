SET NAMES 'utf8';

INSERT indiepost.Users
  SELECT *
  FROM indiepost.`__users`;

INSERT indiepost.Roles
  SELECT *
  FROM indiepost.`__roles`;

INSERT indiepost.Users_Roles
  SELECT *
  FROM indiepost.`__users_roles`;

INSERT indiepost.Categories
  SELECT *
  FROM indiepost.`__categories`;

INSERT indiepost.Posts (id, bookmarkedCount, commentsCount, content, createdAt, excerpt, featuredImage, likesCount, modifiedAt, publishedAt, status, title, postType, authorId, categoryId)
  SELECT id, bookmarkedCount, commentsCount, content, createdAt, excerpt, featuredImage, likesCount, modifiedAt, publishedAt, status, title, postType, authorId, categoryId
  FROM indiepost.`__posts`;

INSERT indiepost.Tags
  SELECT *
  FROM indiepost.`__tags`;

INSERT indiepost.Posts_Tags
  SELECT *
  FROM indiepost.`__posts_tags`;

INSERT indiepost.ImageSets
  SELECT *
  FROM indiepost.`__image_sets`;

INSERT indiepost.Images
  SELECT *
  FROM indiepost.`__images`;