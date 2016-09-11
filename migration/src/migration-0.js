const mysql = require('promise-mysql');
const migrate = require('./migration-image').migrate;

const pool = mysql.createPool({
    host: 'localhost',
    user: 'indiepost',
    password: 'indiepost',
    database: 'indiepost',
    connectionLimit: 30
});

const options = {
    oldBasePath: '/home/jake/backup',
    newBasePath: '/data',
    newBaseUrl: '/uploads/images',
    selectImagesQuery: 'SELECT no, data, date,isTitle, dId FROM __image_union ORDER BY no',
    insertImageSetQuery: 'INSERT __image_sets (postId, contentType, isFeatured, uploadedAt) VALUES (?, ?, ?, ?)',
    insertImageQuery: 'INSERT __images (imageSetId, sizeType, filename, fileSize, fileUrl, width, height) VALUES (?, ?, ?, ?, ?, ?, ?)',
    updateContentlistQuery: 'UPDATE __contentlist SET IMAGEURL = ? WHERE no = ?',
    updateDetaillistQuery: 'UPDATE __detaillist SET data = ?, width = ?, height = ? WHERE no = ?'
};

migrate(pool, options).then(() => {
    pool.end();
});