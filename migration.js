"use strict";
const mysql = require('mysql');
const fs = require('fs-extra');
const path = require('path');
const randomstring = require('randomstring');
const mkdirp = require('mkdirp');
const EventEmitter = require('events');

const oldDirBase = '/home/jake/backup';
const newDirBase = '/data/uploads';

const mysqlPool = mysql.createPool({
    host: 'localhost',
    user: 'indiepost',
    password: 'indiepost',
    database: 'indiepost'
});


let updateTitleImages = () => {
    "use strict";
    mysqlPool.getConnection((err, conn) => {
        let query = 'SELECT no, IMAGEURL as data, REGDATE as date FROM contentlist';

        conn.query(query, (err, results) => {
            if (err) {
                console.log(err);
            }
            for (let i in results) {
                let row = results[i];
                let oldFilePath = path.resolve(oldDirBase, row.data);
                let newFileName = randomstring.generate({length: 8, charset: 'alphanumeric'}) + '.' + row.data.split('.')[1];
                let newFileDir = path.resolve(newDirBase, row.date.substring(0, 4), row.date.substring(4, 6), row.date.substring(6, 8));
                let newFilepath = path.resolve(newFileDir, newFileName);
                mkdirp(newFileDir, (err) => {
                    if (err) {
                    }
                    fs.copy(oldFilePath, newFilepath, (err) => {
                        if (err) {
                        }
                        console.log(oldFilePath + ' -> ' + newFilepath);
                        newFilepath = '/uploads' + newFilepath.replace(newDirBase, '');
                        let update = "UPDATE contentlist SET IMAGEURL='" + newFilepath + "' WHERE no=" + row.no;
                        conn.query(update, (err) => {
                            if(err) {}
                        });
                        console.log(update);
                    });
                });
            }
            conn.release();
        });
    });
};

let updateContentImages = () => {
    "use strict";
    mysqlPool.getConnection((err, conn) => {
        let query = 'SELECT d.no as no, d.data as data, c.REGDATE as date FROM contentlist as c INNER JOIN detaillist as d ON c.no = d.parent WHERE d.type = 2';

        conn.query(query, (err, results) => {
            if (err) {
                console.log(err);
            }
            for (let i in results) {
                let row = results[i];
                let oldFilePath = path.resolve(oldDirBase, row.data);
                let newFileName = randomstring.generate({length: 8, charset: 'alphanumeric'}) + '.' + row.data.split('.')[1];
                let newFileDir = path.resolve(newDirBase, row.date.substring(0, 4), row.date.substring(4, 6), row.date.substring(6, 8));
                let newFilepath = path.resolve(newFileDir, newFileName);
                mkdirp(newFileDir, (err) => {
                    if (err) {
                    }
                    fs.copy(oldFilePath, newFilepath, (err) => {
                        if (err) {
                        }
                        console.log(oldFilePath + ' -> ' + newFilepath);
                        newFilepath = '/uploads' + newFilepath.replace(newDirBase, '');
                        let update = "UPDATE detaillist SET data='" + newFilepath + "' WHERE no=" + row.no;
                        conn.query(update, (err) => {
                            if(err) {}
                        });
                        console.log(update);
                    });
                });
            }
            conn.release();
        });
    });
};
updateTitleImages();
updateContentImages();