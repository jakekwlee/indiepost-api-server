const Promise = require('bluebird');
const path = require('path');
const mysql = require('mysql');
const randomstring = require('randomstring');
// let gm = require('gm');
let fs = require('fs-extra');

Promise.promisifyAll(fs);
// Promise.promisifyAll(gm.prototype);

module.exports = (function() {
    const OLD_BASE_PATH = '/home/jake/backup';
    const NEW_BASE_PATH = '/data';
    const NEW_BASE_URL = '/uploads/images';
    const SELECT_TITLE_IMAGES_QUERY = 'SELECT no, IMAGEURL as data, REGDATE as date FROM contentlist';
    const SELECT_CONTENT_IMAGES_QUERY = 'SELECT d.no, d.data, c.REGDATE as date FROM contentlist AS c INNER JOIN detaillist AS d ON c.no = d.parent WHERE d.type = 2 AND d.iorder > 2';
    const UPDATE_TITLE_IMAGE_QUERY = 'UPDATE contentlist SET IMAGEURL = ? WHERE NO = ?';
    const UPDATE_CONTENT_IMAGE_QUERY = 'UPDATE detaillist SET data = ? WHERE NO = ?';

    function getImagesFromDb(conn, query) {
        console.log(query);
        return conn.query(query).then(rows => {
            let imagesArray = [];
            rows.forEach((row) => {
                let newFileName = randomstring.generate({
                    length: 6,
                    charset: 'alphanumeric'
                });
                let extension = row.data.split('.')[1];
                newFileName = newFileName + '.' + extension;
                let newFileUrl = path.join(NEW_BASE_URL, row.date.substring(0, 4), row.date.substring(4, 6), newFileName);
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
        let dirname = path.dirname(newFilePath);
        return fs.mkdirsAsync(dirname).catch(err => {
            console.log('directory already exists: ' + dirname)
        }).finally(() => {
            return fs.copyAsync(oldFilePath, newFilePath)
                .then(() => {
                    console.log('copy: ' + oldFilePath + ' -> ' + newFilePath);
                })
                .catch((err) => {
                    console.log(err);
                })
        });
    }

    function updateImagesAndDb(imagesArray, conn, query) {
        return Promise.each(imagesArray, (image, index, length) => {
            let sql = mysql.format(query, [image.newFileUrl, image.no]);
            console.log(sql);
            return conn.query(sql);
        });
    }

    function copyImages(imagesArray) {
        return Promise.each(imagesArray, (image, index, length) => {
            return copyFile(image.oldFilePath, image.newFilePath);
        });
    }

    function migrate(conn) {
        return Promise.all([
            getImagesFromDb(conn, SELECT_TITLE_IMAGES_QUERY)
                .then(imagesArray => {
                    return updateImagesAndDb(imagesArray, conn, UPDATE_TITLE_IMAGE_QUERY);
                })
                .then(imagesArray => {
                    return copyImages(imagesArray);
                }),
            getImagesFromDb(conn, SELECT_CONTENT_IMAGES_QUERY)
                .then(imagesArray => {
                    return updateImagesAndDb(imagesArray, conn, UPDATE_CONTENT_IMAGE_QUERY);
                })
                .then(imagesArray => {
                    return copyImages(imagesArray);
                })

        ]).catch(err => {
            console.log(err);
        }).finally(() => {
                conn.end();
        });
    }

    return {
        migrate: migrate
    }
})();