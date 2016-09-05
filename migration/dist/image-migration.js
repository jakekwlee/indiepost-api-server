'use strict';

var Promise = require('bluebird');
var path = require('path');
var mysql = require('mysql');
var randomstring = require('randomstring');
var sharp = require('sharp');
var fs = require('fs-extra');
var spawn = require('child_process').spawn;

Promise.promisifyAll(fs);

module.exports = function () {

    function selectFromDB(pool, query) {
        console.log('query: ' + query);
        return pool.query(query);
    }

    function makeImagesArray(rows, oldBasePath) {
        return Promise.map(rows, function (row) {
            return {
                oldFilePath: path.join(oldBasePath, row.data),
                no: row.no,
                date: row.date
            };
        });
    }

    function updateDB(imagesArray, pool, query) {
        return Promise.each(imagesArray, function (image, index, length) {
            var sql = mysql.format(query, [image.newFileUrl, image.width, image.height, image.thumbnailUrl, image.no]);
            console.log('query: ' + sql);
            return pool.query(sql);
        });
    }

    function makeNewFilenames(imagesArray, newBasePath, newBaseUrl) {
        return Promise.map(imagesArray, function (image) {
            return sharp(image.oldFilePath).metadata().then(function (metadata) {
                var newFileStr = randomstring.generate({
                    length: 6,
                    charset: 'alphanumeric'
                });
                var newFileName = newFileStr + '-' + metadata.width + 'x' + metadata.height + '.' + metadata.format;
                var thumbnail = newFileStr + '-120x80.' + metadata.format;
                var baseDir = path.join(newBaseUrl, image.date.substring(0, 4), image.date.substring(4, 6));
                var newFileUrl = path.join(baseDir, newFileName);
                thumbnail = path.join(baseDir, thumbnail);

                return {
                    oldFilePath: image.oldFilePath,
                    newFilePath: path.join(newBasePath, newFileUrl),
                    newFileUrl: newFileUrl,
                    newFileStr: newFileStr,
                    width: metadata.width,
                    height: metadata.height,
                    thumbnailPath: path.join(newBasePath, thumbnail),
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
            }).catch(console.log);
        });
    }

    function makeThumbnails(imagesArray) {
        return Promise.each(imagesArray, function (image) {
            return sharp(image.oldFilePath).resize(120, 80).crop(sharp.strategy.center).toFile(image.thumbnailPath).then(function () {
                console.log('thumbnail: ' + image.thumbnailPath);
            }).catch(console.log);
        });
    }

    function migrate(pool, options) {
        var oldBasePath = options.oldBasePath;
        var newBasePath = options.newBasePath;
        var newBaseUrl = options.newBaseUrl;
        var selectTitleImagesQuery = options.selectTitleImagesQuery;
        var selectContentImagesQuery = options.selectContentImagesQuery;
        var updateTitleImageQuery = options.updateTitleImageQuery;
        var updateContentImageQuery = options.updateContentImageQuery;

        return Promise.all([selectFromDB(pool, selectTitleImagesQuery).then(function (rows) {
            return makeImagesArray(rows, oldBasePath);
        }).then(function (imagesArray) {
            return makeNewFilenames(imagesArray, newBasePath, newBaseUrl);
        }).then(makeDirectories).then(function (imagesArray) {
            return Promise.all([copyImages(imagesArray), makeThumbnails(imagesArray), updateDB(imagesArray, pool, updateTitleImageQuery)]);
        }), selectFromDB(pool, selectContentImagesQuery).then(function (rows) {
            return makeImagesArray(rows, oldBasePath);
        }).then(function (imagesArray) {
            return makeNewFilenames(imagesArray, newBasePath, newBaseUrl);
        }).then(makeDirectories).then(function (imagesArray) {
            return Promise.all([copyImages(imagesArray), makeThumbnails(imagesArray), updateDB(imagesArray, pool, updateContentImageQuery)]);
        })]).catch(console.log);
    }

    return { migrate: migrate };
}();
//# sourceMappingURL=image-migration.js.map
