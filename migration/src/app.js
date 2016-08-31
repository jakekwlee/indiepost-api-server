const mysql = require('promise-mysql');
const imageMigration = require('./image-migration');
const pool = mysql.createPool({
    host: 'localhost',
    user: 'indiepost',
    password: 'indiepost',
    database: 'indiepost'
});

imageMigration.migrate(pool);