alter table indiepost.Banners
  add column isCover bit not null;

alter table indiepost.Contributors
  add column isDescriptionVisible bit not null;
alter table indiepost.Contributors
  add column isEmailVisible bit not null;
alter table indiepost.Contributors
  add column isPhoneVisible bit not null;
alter table indiepost.Contributors
  add column isPictureVisible bit not null;
alter table indiepost.Contributors
  add column isTitleVisible bit not null;
alter table indiepost.Contributors
  add column isUrlVisible bit not null;

alter table indiepost.Stats
  add column isLandingPage bit(1) default b'0' not null;
alter table indiepost.Visitors
  add column isAdVisitor bit not null;

alter table indiepost.Posts
  add column isFeatured bit not null;
alter table indiepost.Posts
  add column isPicked bit not null;
alter table indiepost.Posts
  add column isShowLastUpdated bit not null;
alter table indiepost.Posts
  add column isSplash bit not null;

alter table indiepost.PostReadings
  add column isVisible bit not null;