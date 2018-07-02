alter table `indiepost`.`Posts`
  drop column `likesCount`,
  drop column `commentsCount`;

alter table `indiepost`.`Users`
  drop column `password`;

update Users
set state = 'ACTIVATED'
where id > 0;

alter table `indiepost`.`Users`
  change column `email` `email` varchar(50) character set 'utf8' null default null;

alter table `indiepost`.`Users`
  drop index `UK_ncoa9bfasrql0x4nhmh1plxxy`;

alter table `indiepost`.`Users_Roles`
  drop foreign key `FKnftu1x2o0innaat555leqkoay`;

alter table `indiepost`.`Users_Roles`
  add constraint `FKnftu1x2o0innaat555leqkoay`
foreign key (`userId`)
references `indiepost`.`Users` (`id`)
  on delete cascade;

alter table `indiepost`.`Users`
  change column `username` `username` varchar(200) character set 'utf8' not null;

drop table `indiepost`.`Bookmarks`;

ALTER TABLE `indiepost`.`Posts_Tags`
DROP FOREIGN KEY `FK1hep4uir8xq9jdybjva00ryy1`,
DROP FOREIGN KEY `FK3y8elo7f5epa2hdsn0ijgmrd4`,
DROP FOREIGN KEY `FK5r36la691aih5iar838pm2v46`,
DROP FOREIGN KEY `FKa9m9vkeg1q9kwn118qri9ns0e`;
ALTER TABLE `indiepost`.`Posts_Tags`
CHANGE COLUMN `post_id` `postId` BIGINT(20) NOT NULL ,
CHANGE COLUMN `tag_id` `tagId` BIGINT(20) NOT NULL ;
ALTER TABLE `indiepost`.`Posts_Tags`
ADD CONSTRAINT `FK1hep4uir8xq9jdybjva00ryy1`
  FOREIGN KEY (`tagId`)
  REFERENCES `indiepost`.`Tags` (`id`),
ADD CONSTRAINT `FK3y8elo7f5epa2hdsn0ijgmrd4`
  FOREIGN KEY (`tagId`)
  REFERENCES `indiepost`.`Tags` (`id`),
ADD CONSTRAINT `FK5r36la691aih5iar838pm2v46`
  FOREIGN KEY (`postId`)
  REFERENCES `indiepost`.`Posts` (`id`),
ADD CONSTRAINT `FKa9m9vkeg1q9kwn118qri9ns0e`
  FOREIGN KEY (`postId`)
  REFERENCES `indiepost`.`Posts` (`id`);

ALTER TABLE `indiepost`.`Posts_Contributors`
DROP FOREIGN KEY `FKfecvvbgnwyijynuyniex8oa5h`,
DROP FOREIGN KEY `FKqvy8nltmf230ra8lf811xftfb`;
ALTER TABLE `indiepost`.`Posts_Contributors`
CHANGE COLUMN `contributor_id` `contributorId` BIGINT(20) NOT NULL ,
CHANGE COLUMN `post_id` `postId` BIGINT(20) NOT NULL ;
ALTER TABLE `indiepost`.`Posts_Contributors`
ADD CONSTRAINT `FKfecvvbgnwyijynuyniex8oa5h`
  FOREIGN KEY (`contributorId`)
  REFERENCES `indiepost`.`Contributors` (`id`),
ADD CONSTRAINT `FKqvy8nltmf230ra8lf811xftfb`
  FOREIGN KEY (`postId`)
  REFERENCES `indiepost`.`Posts` (`id`);



