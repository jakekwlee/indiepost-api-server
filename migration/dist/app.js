'use strict';

var mysql = require('promise-mysql');
var imageMigration = require('./image-migration');
var pool = mysql.createPool({
    host: 'localhost',
    user: 'indiepost',
    password: 'indiepost',
    database: 'indiepost'
});

imageMigration.migrate(pool).then(function () {
    pool.end();
});
//# sourceMappingURL=app.js.map
