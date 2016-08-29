'use strict';

var Promise = require('bluebird');
var path = require('path');
var mysql = require('mysql');
var randomstring = require('randomstring');
// let gm = require('gm');
var fs = require('fs-extra');

Promise.promisifyAll(fs);
// Promise.promisifyAll(gm.prototype);

module.exports = function () {
    var OLD_BASE_PATH = '/home/jake/backup';
    var NEW_BASE_PATH = '/data';
    var NEW_BASE_URL = '/uploads/images';
    var SELECT_TITLE_IMAGES_QUERY = 'SELECT no, IMAGEURL as data, REGDATE as date FROM contentlist';
    var SELECT_CONTENT_IMAGES_QUERY = 'SELECT d.no, d.data, c.REGDATE as date FROM contentlist AS c INNER JOIN detaillist AS d ON c.no = d.parent WHERE d.type = 2 AND d.iorder > 2';
    var UPDATE_TITLE_IMAGE_QUERY = 'UPDATE contentlist SET IMAGEURL = ? WHERE NO = ?';
    var UPDATE_CONTENT_IMAGE_QUERY = 'UPDATE detaillist SET data = ? WHERE NO = ?';

    function getImagesFromDb(conn, query) {
        console.log(query);
        return conn.query(query).then(function (rows) {
            var imagesArray = [];
            rows.forEach(function (row) {
                var newFileName = randomstring.generate({
                    length: 6,
                    charset: 'alphanumeric'
                });
                var extension = row.data.split('.')[1];
                newFileName = newFileName + '.' + extension;
                var newFileUrl = path.join(NEW_BASE_URL, row.date.substring(0, 4), row.date.substring(4, 6), newFileName);
                imagesArray.push({
                    oldFilePath: path.join(OLD_BASE_PATH, row.data),
                    newFileUrl: newFileUrl,
                    newFilePath: path.join(NEW_BASE_PATH, newFileUrl),
                    no: row.no
                });
            });
            return Promise.all(imagesArray);
        });
    }

    function copyFile(oldFilePath, newFilePath) {
        var dirname = path.dirname(newFilePath);
        return fs.mkdirsAsync(dirname).catch(function (err) {
            console.log('directory already exists: ' + dirname);
        }).finally(function () {
            return fs.copyAsync(oldFilePath, newFilePath).then(function () {
                console.log('copy: ' + oldFilePath + ' -> ' + newFilePath);
            }).catch(function (err) {
                console.log(err);
            });
        });
    }

    function updateImagesAndDb(imagesArray, conn, query) {
        return Promise.each(imagesArray, function (image, index, length) {
            var sql = mysql.format(query, [image.newFileUrl, image.no]);
            console.log(sql);
            return conn.query(sql);
        });
    }

    function copyImages(imagesArray) {
        return Promise.each(imagesArray, function (image, index, length) {
            return copyFile(image.oldFilePath, image.newFilePath);
        });
    }

    function migrate(conn) {
        return Promise.all([getImagesFromDb(conn, SELECT_TITLE_IMAGES_QUERY).then(function (imagesArray) {
            return updateImagesAndDb(imagesArray, conn, UPDATE_TITLE_IMAGE_QUERY);
        }).then(function (imagesArray) {
            return copyImages(imagesArray);
        }), getImagesFromDb(conn, SELECT_CONTENT_IMAGES_QUERY).then(function (imagesArray) {
            return updateImagesAndDb(imagesArray, conn, UPDATE_CONTENT_IMAGE_QUERY);
        }).then(function (imagesArray) {
            return copyImages(imagesArray);
        })]).catch(function (err) {
            console.log(err);
        }).finally(function () {
            conn.end();
        });
    }

    return {
        migrate: migrate
    };
}();
//# sourceMappingURL=migrate-image.js.map
