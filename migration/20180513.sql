alter table `indiepost`.`Stats`
  drop foreign key `FKc2y288748ubg1o0ts6u07a4xm`;

alter table `indiepost`.`Stats`
  add constraint `FKc2y288748ubg1o0ts6u07a4xm`
foreign key (`postId`)
references `indiepost`.`Posts` (`id`)
  on delete set null;

alter table `indiepost`.`Posts`
  drop foreign key `FKjotxf6psgur0id650um9ehst7`;

alter table `indiepost`.`Posts`
  add constraint `FKjotxf6psgur0id650um9ehst7`
foreign key (`originalId`)
references `indiepost`.`Posts` (`id`)
  on delete set null;

alter table `indiepost`.`CachedPostStats`
  drop foreign key `FKsxvkf74ei6w4jml9jq9hy77mm`;

alter table `indiepost`.`CachedPostStats`
  add constraint `FKsxvkf74ei6w4jml9jq9hy77mm`
foreign key (`postId`)
references `indiepost`.`Posts` (`id`)
  on delete cascade
  on update restrict;

create table `Posts_Tags1` (
  `post_id`  bigint(20) not null,
  `tag_id`   bigint(20) not null,
  `id`       bigint(20) not null auto_increment,
  `priority` int        not null default 0,
  primary key (`id`),
  constraint `FK3y8elo7f5epa2hdsn0ijgmrd4` foreign key (`tag_id`)
  references `Tags` (`id`),
  constraint `FK5r36la691aih5iar838pm2v46` foreign key (`post_id`)
  references `Posts` (`id`)
)
  engine = INNODB
  default charset = UTF8;

insert into indiepost.Posts_Tags1 (tag_id, post_id) select
                                                      tagId,
                                                      postId
                                                    from indiepost.Posts_Tags;
drop table Posts_Tags;
alter table Posts_Tags1
rename to Posts_Tags;

