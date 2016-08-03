"use strict";
const mysql = require('mysql');
const moment = require('moment');
const dateFormat = 'YYYY-MM-DD 14:00:00';

const mysqlPool = mysql.createPool({
    host: 'localhost',
    user: 'indiepost',
    password: 'indiepost',
    database: 'indiepost'
});

let updateDates = () => {
    "use strict";
    mysqlPool.getConnection((err, conn) => {
        let query = 'select c.no, c.REGDATE as createdAt, d.data as publishedAt from contentlist c inner join detaillist d on c.no = d.parent where d.type=1 and d.iorder=1;'
        conn.query(query, (err, results) => {
            if (err) {
                console.log(err);
            }
            for (let i in results) {
                let row = results[i];
                let createdAt = row.createdAt;
                createdAt = moment(createdAt, 'YYYYMMDD').format(dateFormat);
                let publishedAt = row.publishedAt.match(/\d\d\d\d\/\d\d\/\d\d/m);
                if (publishedAt) {
                    publishedAt = moment(publishedAt[0], 'YYYY/MM/DD').format(dateFormat);
                }
                let update =
                    "update Posts set createdAt='" + createdAt + "', modifiedAt='" + createdAt + "', publishedAt='" + publishedAt + "', reservedAt= '" + publishedAt + "' where id = " + row.no;
                conn.query(update, (err) => {
                    if(err) {
                        console.log(err);
                    }
                    console.log(update);
                });
            }
        });
    });
};

updateDates();