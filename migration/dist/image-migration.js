'use strict';

var Promise = require('bluebird');
var path = require('path');
var mysql = require('mysql');
var randomstring = require('randomstring');
var sharp = require('sharp');
var fs = require('fs-extra');

Promise.promisifyAll(fs);

module.exports = function () {

    var OLD_BASE_PATH = '/home/jake/backup';
    var NEW_BASE_PATH = '/data';
    var NEW_BASE_URL = '/uploads/images';
    var SELECT_TITLE_IMAGES_QUERY = 'SELECT no, IMAGEURL as data, REGDATE as date FROM __contentlist';
    var SELECT_CONTENT_IMAGES_QUERY = 'SELECT d.no, d.data, c.REGDATE as date FROM __contentlist AS c INNER JOIN __detaillist AS d ON c.no = d.parent WHERE d.type = 2 AND d.iorder > 2';
    var UPDATE_TITLE_IMAGE_QUERY = 'UPDATE __contentlist SET IMAGEURL = ?, width = ?, height = ?, thumbnail = ? WHERE NO = ?';
    var UPDATE_CONTENT_IMAGE_QUERY = 'UPDATE __detaillist SET data = ?, width = ?, height = ?, thumbnail = ? WHERE NO = ?';

    function getImagesFromDb(pool, query) {
        console.log('query: ' + query);
        return pool.query(query).then(function (rows) {
            return Promise.map(rows, function (row) {
                return {
                    oldFilePath: path.join(OLD_BASE_PATH, row.data),
                    no: row.no,
                    date: row.date
                };
            });
        });
    }

    function updateDb(imagesArray, pool, query) {
        return Promise.each(imagesArray, function (image, index, length) {
            var sql = mysql.format(query, [image.newFileUrl, image.width, image.height, image.thumbnailUrl, image.no]);
            console.log('query: ' + sql);
            return pool.query(sql);
        });
    }

    function makeNewFilenames(imagesArray) {
        return Promise.map(imagesArray, function (image) {
            return sharp(image.oldFilePath).metadata().then(function (metadata) {
                var newFileStr = randomstring.generate({
                    length: 6,
                    charset: 'alphanumeric'
                });
                var newFileName = newFileStr + '-' + metadata.width + 'x' + metadata.height + '.' + metadata.format;
                var thumbnail = newFileStr + '-' + '120' + 'x' + '80' + '.' + metadata.format;
                var baseDir = path.join(NEW_BASE_URL, image.date.substring(0, 4), image.date.substring(4, 6));
                var newFileUrl = path.join(baseDir, newFileName);
                thumbnail = path.join(baseDir, thumbnail);

                return {
                    oldFilePath: image.oldFilePath,
                    newFilePath: path.join(NEW_BASE_PATH, newFileUrl),
                    newFileUrl: newFileUrl,
                    newFileStr: newFileStr,
                    width: metadata.width,
                    height: metadata.height,
                    thumbnailPath: path.join(NEW_BASE_PATH, thumbnail),
                    thumbnailUrl: thumbnail,
                    no: image.no
                };
            });
        });
    }

    function makeDirectories(imagesArray) {
        return Promise.each(imagesArray, function (image, index, length) {
            var dirPath = path.dirname(image.newFilePath);
            return fs.mkdirsAsync(dirPath).catch(function (err) {
                console.log('directory already exists: ' + dirPath);
            });
        });
    }

    function copyImages(imagesArray) {
        return Promise.each(imagesArray, function (image, index, length) {
            return fs.copyAsync(image.oldFilePath, image.newFilePath).then(function () {
                console.log('copy: ' + image.oldFilePath + ' -> ' + image.newFilePath);
            }).catch(function (err) {
                console.log(err);
            });
        });
    }

    function makeThumbnails(imagesArray) {
        return Promise.each(imagesArray, function (image) {
            return sharp(image.oldFilePath).resize(120, 80).crop(sharp.strategy.center).toFile(image.thumbnailPath).then(function () {
                console.log('thumbnail: ' + image.thumbnailPath);
            }).catch(function (err) {
                console.log(err);
            });
        });
    }

    function migrate(pool) {
        return Promise.all([getImagesFromDb(pool, SELECT_TITLE_IMAGES_QUERY).then(function (imagesArray) {
            return makeNewFilenames(imagesArray);
        }).then(function (imagesArray) {
            return makeDirectories(imagesArray);
        }).then(function (imagesArray) {
            return Promise.all([copyImages(imagesArray), makeThumbnails(imagesArray), updateDb(imagesArray, pool, UPDATE_TITLE_IMAGE_QUERY)]);
        }), getImagesFromDb(pool, SELECT_CONTENT_IMAGES_QUERY).then(function (imagesArray) {
            return makeNewFilenames(imagesArray);
        }).then(function (imagesArray) {
            return makeDirectories(imagesArray);
        }).then(function (imagesArray) {
            return Promise.all([copyImages(imagesArray), makeThumbnails(imagesArray), updateDb(imagesArray, pool, UPDATE_CONTENT_IMAGE_QUERY)]);
        })]).then(function () {
            pool.end();
        }).catch(function (err) {
            console.log(err);
        });
    }

    return {
        migrate: migrate
    };
}();
//# sourceMappingURL=image-migration.js.map
