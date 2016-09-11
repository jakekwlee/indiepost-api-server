const Promise = require('bluebird');
const path = require('path');
const mysql = require('mysql');
const randomstring = require('randomstring');
const sharp = require('sharp');
const fs = require('fs-extra');

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
                dId: row.dId,
                no: row.no,
                date: row.date,
                isFeatured: row.isTitle
            };
        });
    }

    function updateLegacyDB(images, pool, query) {
        return Promise.each(images, (image) => {
            let sql = mysql.format(query, [image.newFileUrl, image.width, image.height, image.dId]);
            console.log('query: ' + sql);
            return pool.query(sql);
        });
    }

    function insertImageSetsToDB(images, pool, query) {
        return Promise.map(images, (image) => {
            let sql = mysql.format(query, [image.no, image.contentType, image.isFeatured, image.uploadedAt]);
            console.log('query: ' + sql);
            return pool.query(sql).then(result => {
                return {
                    ...image,
                    imageSetId: result.insertId
                };
            });
        });
    }

    function insertImagesToDB(images, pool, insertImageQuery) {
        return Promise.each(images, (image, index, length) => {
            let insertOriginalSql = mysql.format(insertImageQuery, [image.imageSetId, 'Original', image.newFileName, 0, image.newFileUrl, image.width, image.height]);
            let insertThumbnailSql = mysql.format(insertImageQuery, [image.imageSetId, 'Thumbnail', image.thumbnail, 0, image.thumbnailUrl, 120, 80]);
            console.log('query: ' + insertOriginalSql);
            console.log('query: ' + insertThumbnailSql);
            return Promise.all([pool.query(insertOriginalSql), pool.query(insertThumbnailSql)]);
        });
    }

    function makeNewFilenames(images, newBasePath, newBaseUrl) {
        const mimeTypes = ['image/jpeg', 'image/png', 'image/gif'];
        return Promise.map(images, image => {
            return sharp(image.oldFilePath).metadata().then(metadata => {
                let newFileStr = randomstring.generate({
                    length: 8,
                    charset: 'alphanumeric'
                });
                let extension = metadata.format;
                let newFileName = `${newFileStr}-${metadata.width}x${metadata.height}.${extension}`;
                let thumbnail = `${newFileStr}-120x80.${extension}`;
                let year = image.date.getFullYear().toString();
                let month = ('0' + (image.date.getMonth() + 1)).slice(-2);
                let baseDir = path.join(newBaseUrl, year, month);
                let newFileUrl = path.join(baseDir, newFileName);
                let thumbnailUrl = path.join(baseDir, thumbnail);
                let contentType = '';

                mimeTypes.forEach(mimeType => {
                    if (mimeType.search(extension) > -1) {
                        contentType = mimeType;
                    }
                });

                return {
                    no: image.no,
                    dId: image.dId,
                    isFeatured: image.isFeatured,
                    oldFilePath: image.oldFilePath,
                    newFileName,
                    newFilePath: path.join(newBasePath, newFileUrl),
                    newFileUrl,
                    contentType,
                    uploadedAt: image.date,
                    width: metadata.width,
                    height: metadata.height,
                    thumbnailPath: path.join(newBasePath, thumbnailUrl),
                    thumbnail,
                    thumbnailUrl
                }
            }).catch(console.log);
        });
    }

    function makeDirectories(images) {
        return Promise.each(images, (image, index, length) => {
            let dirPath = path.dirname(image.newFilePath);
            return fs.mkdirsAsync(dirPath).catch(err => {
                console.log('directory already exists: ' + dirPath)
            });
        });
    }

    function copyImages(images) {
        return Promise.each(images, (image, index, length) => {
            return fs.copyAsync(image.oldFilePath, image.newFilePath)
                .then(() => {
                    console.log(`copy: ${image.oldFilePath} -> ${image.newFilePath}`);
                })
                .catch(console.log);
        });
    }

    function makeThumbnails(images) {
        return Promise.each(images, image => {
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
            selectImagesQuery,
            insertImageSetQuery, insertImageQuery,
            updateLegacyContentQuery
        } = options;
        return selectFromDB(pool, selectImagesQuery)
            .then(rows => {
                return makeImagesArray(rows, oldBasePath);
            })
            .then(images => {
                return makeNewFilenames(images, newBasePath, newBaseUrl);
            })
            .then(makeDirectories)
            .then(images => {
                return insertImageSetsToDB(images, pool, insertImageSetQuery);
            })
            .then(images => {
                return insertImagesToDB(images, pool, insertImageQuery);
            })
            .then(images => {
                return updateLegacyDB(images, pool, updateLegacyContentQuery);
            })
            .then(copyImages)
            .then(makeThumbnails)
            .catch(console.err);
    }

    return {migrate};
})();