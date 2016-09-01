const Promise = require('bluebird');
const path = require('path');
const mysql = require('mysql');
const randomstring = require('randomstring');
const sharp = require('sharp');
const fs = require('fs-extra');

Promise.promisifyAll(fs);

module.exports = (()=> {

    const OLD_BASE_PATH = '/home/jake/backup';
    const NEW_BASE_PATH = '/data';
    const NEW_BASE_URL = '/uploads/images';
    const SELECT_TITLE_IMAGES_QUERY = 'SELECT no, IMAGEURL as data, REGDATE as date FROM __contentlist';
    const SELECT_CONTENT_IMAGES_QUERY = 'SELECT d.no, d.data, c.REGDATE as date FROM __contentlist AS c INNER JOIN __detaillist AS d ON c.no = d.parent WHERE d.type = 2 AND d.iorder > 2';
    const UPDATE_TITLE_IMAGE_QUERY = 'UPDATE __contentlist SET IMAGEURL = ?, width = ?, height = ?, thumbnail = ? WHERE NO = ?';
    const UPDATE_CONTENT_IMAGE_QUERY = 'UPDATE __detaillist SET data = ?, width = ?, height = ?, thumbnail = ? WHERE NO = ?';

    function getImagesFromDb(pool, query) {
        console.log('query: ' + query);
        return pool.query(query).then(rows => {
            return Promise.map(rows, (row) => {
                return {
                    oldFilePath: path.join(OLD_BASE_PATH, row.data),
                    no: row.no,
                    date: row.date
                }
            });
        });
    }

    function updateDb(imagesArray, pool, query) {
        return Promise.each(imagesArray, (image, index, length) => {
            let sql = mysql.format(query, [image.newFileUrl, image.width, image.height, image.thumbnailUrl, image.no]);
            console.log('query: ' + sql);
            return pool.query(sql);
        });
    }

    function makeNewFilenames(imagesArray) {
        return Promise.map(imagesArray, image => {
            return sharp(image.oldFilePath).metadata().then(metadata => {
                let newFileStr = randomstring.generate({
                    length: 6,
                    charset: 'alphanumeric'
                });
                let newFileName = newFileStr + '-' + metadata.width + 'x' + metadata.height + '.' + metadata.format;
                let thumbnail = newFileStr + '-' + '120' + 'x' + '80' + '.' + metadata.format;
                let baseDir = path.join(NEW_BASE_URL, image.date.substring(0, 4), image.date.substring(4, 6));
                let newFileUrl = path.join(baseDir, newFileName);
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
                }
            })
        })
    }

    function makeDirectories(imagesArray) {
        return Promise.each(imagesArray, (image, index, length) => {
            let dirPath = path.dirname(image.newFilePath);
            return fs.mkdirsAsync(dirPath).catch(err => {
                console.log('directory already exists: ' + dirPath)
            });
        });
    }

    function copyImages(imagesArray) {
        return Promise.each(imagesArray, (image, index, length) => {
            return fs.copyAsync(image.oldFilePath, image.newFilePath)
                .then(() => {
                    console.log('copy: ' + image.oldFilePath + ' -> ' + image.newFilePath);
                })
                .catch(console.log);
        });
    }

    function makeThumbnails(imagesArray) {
        return Promise.each(imagesArray, image => {
            return sharp(image.oldFilePath)
                .resize(120, 80)
                .crop(sharp.strategy.center)
                .toFile(image.thumbnailPath)
                .then(() => {
                    console.log('thumbnail: ' + image.thumbnailPath);
                })
                .catch(console.log);
        });
    }

    function migrate(pool) {
        return Promise.all([
            getImagesFromDb(pool, SELECT_TITLE_IMAGES_QUERY)
                .then(makeNewFilenames).then(makeDirectories)
                .then(imagesArray => {
                    return Promise.all([copyImages(imagesArray), makeThumbnails(imagesArray), updateDb(imagesArray, pool, UPDATE_TITLE_IMAGE_QUERY)]);
                }),
            getImagesFromDb(pool, SELECT_CONTENT_IMAGES_QUERY)
                .then(makeNewFilenames).then(makeDirectories)
                .then(imagesArray => {
                    return Promise.all([copyImages(imagesArray), makeThumbnails(imagesArray), updateDb(imagesArray, pool, UPDATE_CONTENT_IMAGE_QUERY)]);
                })
        ]).catch(console.log);
    }

    return {
        migrate: migrate
    };
})();