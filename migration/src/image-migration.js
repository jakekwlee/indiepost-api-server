const Promise = require('bluebird');
const path = require('path');
const mysql = require('mysql');
const randomstring = require('randomstring');
const sharp = require('sharp');
const fs = require('fs-extra');
const spawn = require('child_process').spawn;

Promise.promisifyAll(fs);

module.exports = (()=> {

    function selectFromDB(pool, query) {
        console.log('query: ' + query);
        return pool.query(query);
    }

    function makeImagesArray(rows, oldBasePath) {
        return Promise.map(rows, row => {
            return {
                oldFilePath: path.join(oldBasePath, row.data),
                no: row.no,
                date: row.date
            };
        });
    }

    function updateDB(imagesArray, pool, query) {
        return Promise.each(imagesArray, (image, index, length) => {
            let sql = mysql.format(query, [image.newFileUrl, image.width, image.height, image.thumbnailUrl, image.no]);
            console.log('query: ' + sql);
            return pool.query(sql);
        });
    }

    function makeNewFilenames(imagesArray, newBasePath, newBaseUrl) {
        return Promise.map(imagesArray, image => {
            return sharp(image.oldFilePath).metadata().then(metadata => {
                let newFileStr = randomstring.generate({
                    length: 6,
                    charset: 'alphanumeric'
                });
                let newFileName = `${newFileStr}-${metadata.width}x${metadata.height}.${metadata.format}`;
                let thumbnail = `${newFileStr}-120x80.${metadata.format}`;
                let baseDir = path.join(newBaseUrl, image.date.substring(0, 4), image.date.substring(4, 6));
                let newFileUrl = path.join(baseDir, newFileName);
                thumbnail = path.join(baseDir, thumbnail);

                return {
                    oldFilePath: image.oldFilePath,
                    newFilePath: path.join(newBasePath, newFileUrl),
                    newFileUrl,
                    newFileStr,
                    width: metadata.width,
                    height: metadata.height,
                    thumbnailPath: path.join(newBasePath, thumbnail),
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
                    console.log(`copy: ${image.oldFilePath} -> ${image.newFilePath}`);
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

    function migrate(pool, options) {
        let {
            oldBasePath, newBasePath, newBaseUrl,
            selectTitleImagesQuery, selectContentImagesQuery,
            updateTitleImageQuery, updateContentImageQuery
        } = options;
        return Promise.all([
            selectFromDB(pool, selectTitleImagesQuery)
                .then(rows => {
                    return makeImagesArray(rows, oldBasePath);
                })
                .then(imagesArray => {
                    return makeNewFilenames(imagesArray, newBasePath, newBaseUrl);
                })
                .then(makeDirectories)
                .then(imagesArray => {
                    return Promise.all([
                        copyImages(imagesArray),
                        makeThumbnails(imagesArray),
                        updateDB(imagesArray, pool, updateTitleImageQuery)
                    ]);
                }),
            selectFromDB(pool, selectContentImagesQuery)
                .then(rows => {
                    return makeImagesArray(rows, oldBasePath);
                })
                .then(imagesArray => {
                    return makeNewFilenames(imagesArray, newBasePath, newBaseUrl);
                })
                .then(makeDirectories)
                .then(imagesArray => {
                    return Promise.all([
                        copyImages(imagesArray),
                        makeThumbnails(imagesArray),
                        updateDB(imagesArray, pool, updateContentImageQuery)
                    ]);
                })
        ]).catch(console.log);
    }

    return { migrate };
})();