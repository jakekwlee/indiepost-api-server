const mysql = require('promise-mysql');
const imageMigration = require('./image-migration');

const pool = mysql.createPool({
    host: 'localhost',
    user: 'indiepost',
    password: 'indiepost',
    database: 'indiepost'
});

const options = {
    oldBasePath: '/home/jake/backup',
    newBasePath: '/data',
    newBaseUrl: '/uploads/images',
    selectTitleImagesQuery: 'SELECT no, IMAGEURL as data, REGDATE as date FROM __contentlist', 
    selectContentImagesQuery: 'SELECT d.no, d.data, c.REGDATE as date FROM __contentlist AS c INNER JOIN __detaillist AS d ON c.no = d.parent WHERE d.type = 2 AND d.iorder > 2',
    updateTitleImageQuery: 'UPDATE __contentlist SET IMAGEURL = ?, width = ?, height = ?, thumbnail = ? WHERE NO = ?',
    updateContentImageQuery: 'UPDATE __detaillist SET data = ?, width = ?, height = ?, thumbnail = ? WHERE NO = ?',
};

imageMigration.migrate(pool, options).then(() => {
    pool.end();
});