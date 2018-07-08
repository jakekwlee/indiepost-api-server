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

alter table `indiepost`.`Posts_Tags`
  drop foreign key `FK1hep4uir8xq9jdybjva00ryy1`,
  drop foreign key `FK3y8elo7f5epa2hdsn0ijgmrd4`,
  drop foreign key `FK5r36la691aih5iar838pm2v46`,
  drop foreign key `FKa9m9vkeg1q9kwn118qri9ns0e`;
alter table `indiepost`.`Posts_Tags`
  change column `post_id` `postId` bigint(20) not null,
  change column `tag_id` `tagId` bigint(20) not null;
alter table `indiepost`.`Posts_Tags`
  add constraint `FK1hep4uir8xq9jdybjva00ryy1`
foreign key (`tagId`)
references `indiepost`.`Tags` (`id`),
  add constraint `FK3y8elo7f5epa2hdsn0ijgmrd4`
foreign key (`tagId`)
references `indiepost`.`Tags` (`id`),
  add constraint `FK5r36la691aih5iar838pm2v46`
foreign key (`postId`)
references `indiepost`.`Posts` (`id`),
  add constraint `FKa9m9vkeg1q9kwn118qri9ns0e`
foreign key (`postId`)
references `indiepost`.`Posts` (`id`);

alter table `indiepost`.`Posts_Contributors`
  drop foreign key `FKfecvvbgnwyijynuyniex8oa5h`,
  drop foreign key `FKqvy8nltmf230ra8lf811xftfb`;
alter table `indiepost`.`Posts_Contributors`
  change column `contributor_id` `contributorId` bigint(20) not null,
  change column `post_id` `postId` bigint(20) not null;
alter table `indiepost`.`Posts_Contributors`
  add constraint `FKfecvvbgnwyijynuyniex8oa5h`
foreign key (`contributorId`)
references `indiepost`.`Contributors` (`id`),
  add constraint `FKqvy8nltmf230ra8lf811xftfb`
foreign key (`postId`)
references `indiepost`.`Posts` (`id`);


