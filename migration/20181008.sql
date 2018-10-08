  alter table Banners add column isCover bit not null;
  update Banners set isCover = cover;
  alter table Banners drop column cover;
    
  alter table Contributors add column isDescriptionVisible bit not null;
  update Contributors set isDescriptionVisible = descriptionVisible;
  alter table Contributors drop column descriptionVisible;
    
  alter table Contributors add column isEmailVisible bit not null;
  update Contributors set isEmailVisible = emailVisible;
  alter table Contributors drop column emailVisible;
    
  alter table Contributors add column isPhoneVisible bit not null;
  update Contributors set isPhoneVisible = phoneVisible;
  alter table Contributors drop column phoneVisible;
    
  alter table Contributors add column isPictureVisible bit not null;
  update Contributors set isPictureVisible = pictureVisible;
  alter table Contributors drop column pictureVisible;
    
  alter table Contributors add column isTitleVisible bit not null;
  update Contributors set isTitleVisible = titleVisible;
  alter table Contributors drop column titleVisible;
    
  alter table Contributors add column isUrlVisible bit not null;
  update Contributors set isUrlVisible = urlVisible;
  alter table Contributors drop column urlVisible;
    
  alter table PostReadings add column isVisible bit not null;
  update PostReadings set isVisible = visible;
  alter table PostReadings drop column visible;
    
  alter table Posts add column isFeatured bit not null;
  update Posts set isFeatured = featured;
  alter table Posts drop column featured;

  alter table Posts add column isPicked bit not null;
  update Posts set isPicked = picked;
  alter table Posts drop column picked;

  alter table Posts add column isShowLastUpdated bit not null;
  update Posts set isShowLastUpdated = showLastUpdated;
  alter table Posts drop column showLastUpdated;

  alter table Posts add column isSplash bit not null;
  update Posts set isSplash = splash;
  alter table Posts drop column splash;

  alter table Visitors add column isAdVisitor bit not null;
  update Visitors set isAdVisitor = adVisitor;
  alter table Visitors drop column adVisitor;
