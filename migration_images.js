"use strict";
const mysql = require('mysql');
const fs = require('fs-extra');
const path = require('path');
const randomstring = require('randomstring');
const mkdirp = require('mkdirp');
const async = require('async');

const oldDirBase = '/home/jake/backup';
const newDirBase = '/data/uploads';

const mysqlAuth = {
  host: 'localhost',
  user: 'indiepost',
  password: 'indiepost',
  database: 'indiepost'
};

function migrateTitleImages(callback) {
  "use strict";
  const connection = mysql.createConnection(mysqlAuth);
  const retrieveQuery = 'SELECT no, IMAGEURL as data, REGDATE as date FROM contentlist';
  const updateQueryFunc = (id, newFilePath) => {
    return "UPDATE contentlist SET IMAGEURL='" + newFilePath + "' WHERE no=" + id
  }
  callback(null, connection, retrieveQuery, updateQueryFunc);
};

function migrateContentImages(callback) {
  "use strict";
  const connection = mysql.createConnection(mysqlAuth);
  const retrieveQuery = 'SELECT d.no as no, d.data as data, c.REGDATE as date FROM contentlist as c INNER JOIN detaillist as d ON c.no = d.parent WHERE d.type = 2 AND d.iorder > 2';
  const updateQueryFunc = (id, newFilePath) => {
    return "UPDATE detaillist SET data='" + newFilePath + "' WHERE no=" + id;
  }
  callback(null, connection, retrieveQuery, updateQueryFunc);
};

function retrieveImagesInfo(conn, query, updateQueryFunc, callback) {
  "use strict";
  let imagesInfo = [];
  conn.query(query, (err, results) => {
    if (err) {
      console.log(err);
    }
    for (let i in results) {
      let row = results[i];
      let oldFilePath = path.resolve(oldDirBase, row.data);
      let newFileName = randomstring.generate({
        length: 8,
        charset: 'alphanumeric'
      }) + '.' + row.data.split('.')[1];
      let newFileDir = path.resolve(newDirBase, row.date.substring(0, 4), row.date.substring(4, 6), row.date.substring(6, 8));
      let newFilePath = path.resolve(newFileDir, newFileName);
      imagesInfo.push({
        oldFilePath: oldFilePath,
        newFileName: newFileName,
        newFileDir: newFileDir,
        newFilePath: newFilePath
      });
    }
    callback(null, conn, imagesInfo, updateQueryFunc);
  });
};

function copyImagesToNewLocation(conn, imagesInfo, updateQueryFunc, callback) {
  "use strict";
  for (i in imagesInfo) {
    let image = imagesInfo[i];
    mkdirp(image.newFileDir, (err) => {
      if (err) console.log(err);
      fs.copy(image.oldFilePath, image.newFilePath, (err) => {
        if (err) console.log(err);
        console.log('Copy: ' + iamge.oldFilePath + ' -> ' + image.newFilePath);
      });
    });
  }
  callback(conn, imagesInfo, updateQueryFunc);
};

function updateImagesInfo(conn, imagesInfo, updateQueryFunc, callback) {
  "use strict";
  for (let i in imagesInfo) {
    let image = imagesInfo[i];
    let newFilePath = '/uploads' + image.newFilePath.replace(newDirBase, '');
    let query = updateQueryFunc(iamge.id, newFilePath);
    conn.query(query, (err) => {
      if (err) {
        console.log(err);
      }
      console.log(query);
    });
  }
};

async.series([

  (callback) => {
    async.waterfall([
      migrateTitleImages,
      retrieveImagesInfo,
      copyImagesToNewLocation,
      updateImagesInfo
    ], (conn, err) => {
      conn.end();
      if (err) {
        console.log(err);
      }
    });
    callback(null, callback);
  }, (callback) => {
    async.waterfall([
      migrateContentImages,
      retrieveImagesInfo,
      copyImagesToNewLocation,
      updateImagesInfo
    ], (conn, err) => {
      conn.end();
      if (err) {
        console.log(err);
      }
    });
    callback(null, callback);
  }
], (err) => {
  if (err) {
    console.log(err);
  }
});