'use strict';

var mysql = require('promise-mysql');
var migrate = require('./migrate-image');
mysql.createConnection({
    host: 'localhost',
    user: 'indiepost',
    password: 'indiepost',
    database: 'indiepost'
}).then(function (conn) {
    migrate.migrate(conn);
});
//# sourceMappingURL=app.js.map
