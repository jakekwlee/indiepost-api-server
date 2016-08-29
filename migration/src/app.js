const mysql = require('promise-mysql');
const migrate = require('./migrate-image');
mysql.createConnection({
    host: 'localhost',
    user: 'indiepost',
    password: 'indiepost',
    database: 'indiepost'
}).then(conn => {
    migrate.migrate(conn);
});