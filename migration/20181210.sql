insert into Profiles (id, displayName, slug, fullName, etc, email, subEmail, phone, picture, label, description,
                      showDescription, showEmail, showPicture, showLabel, profileType, profileState, created,
                      lastUpdated)
select
  id,
  fullName,
  fullName,
  fullName,
  null,
  email,
  subEmail,
  phone,
  picture,
  title,
  description,
  isDescriptionVisible,
  isEmailVisible,
  isPictureVisible,
  isTitleVisible,
  'Editor',
  'ACTIVE',
  created,
  lastUpdated
from
  Contributors;

insert into Posts_Profiles (id, postId, profileId, priority)
select id, postId, contributorId, priority
from Posts_Contributors;
