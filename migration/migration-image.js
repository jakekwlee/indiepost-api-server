var Promise = require('bluebird');
var gm = require('gm');
Promise.promisifyAll(gm.prototype);
var fs = require('fs-extra');
Promise.promisifyAll(fs);
var path = reqre('path');

var migrateImages = {
    oldBaseUrl: '/home/jake/backup',
    newBaseUrl: '/data',
    selectTitleImagesQuery: 'SELECT no, IMAGEURL as data, REGDATE as date FROM contentlist',
    selectContentImagesQuery: 'SELECT d.no, d.data, c.REGDATE as date FROM contentlist AS c INNER JOIN detaillist AS d ON c.no = d.parent WHERE d.type = 2 AND d.iorder > 2',
    updateTitleImageQuery: 'UPDATE contentlist SET IMAGEURL = ?? WHERE NO = ?',
    updateContentImageQuery: 'UPDATE detaillist SET data = ?? WHERE NO = ?',
    _getImages: function(query) {
        console.log(query);
        return sqlPool.query(query);
    },
    _copyImages: function(oldFilePath, newFilePath) {
        var dirname = path.dirname(newFilePath);
        fs.accessAsync(dirname, fs.constants.F_OK).catch(function(err) {
            fs.mkdirpAsync(dirname);
        });
    },
    _migrateImages: function(row) {
        rows.forEach(function(row) {
            var oldFilePath = path.join(oldBaseUrl, row.data);
            var newFileUrl = path.join('/uploads/images', row.date.substring(0, 4), row.date.substring(4, 6), row.date.substring(6, 8));
            var newFilePath = path.join(newBaseUrl, newFileUrl);

        });
    }


};


fs.copyAsync('aaa.jpg', 'bbb.jpg')
    .then(console.log('Success!'))
    .catch(function(e) {
        console.log(e);
    });

function retrieveImagesInfoFromDb(sqlPool, query) {
    return sqlPool.query(query);
}