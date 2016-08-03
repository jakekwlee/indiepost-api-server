"use strict";
const mysql = require('mysql');

const mysqlPool = mysql.createPool({
    host: 'localhost',
    user: 'indiepost',
    password: 'indiepost',
    database: 'indiepost'
});

const authors = [
    {
        id: 1,
        name: 'Indiepost',
        username: 'admin'
    },
    {
        id: 4,
        name: '임유청',
        username: 'imyou'
    },
    {
        id: 5,
        name: '이사민',
        username: 'lsm'
    },
    {
        id: 2,
        name: '안병태',
        username: 'byungtae.ahn'
    },
    {
        id: 6,
        name: 'LEE YI JAE',
        username: 'rheejae'
    },
    {
        id: 7,
        name: '유미래',
        username: 'you'
    },
    {
        id: 8,
        name: 'GRAYE',
        username: 'graye'
    },
    {
        id: 9,
        name: 'Yi Youngsoo',
        username: 'youngsoo'
    }
    
]

let updateEditor = () => {
    "use strict";
    mysqlPool.getConnection((err, conn) => {
        let query = 'select no, WRITERID as editorId from contentlist order by WRITERID';
        conn.query(query, (err, results) => {
            if (err) {
                console.log(err);
            }
            for (let i in results) {
                let row = results[i];
                let author;
                for(let j in authors) {
                    author = authors[j];
                    if (author.username === row.editorId) {
                        break;
                    }
                }
                let update =
                    "update Posts set editorId=" + author.id  + " where id = " + row.no;
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

let updateAuthor = () => {
    "use strict";
    mysqlPool.getConnection((err, conn) => {
        let query = 'select c.no, d.parent, d.data from detaillist as d inner join contentlist as c on d.parent = c.no where type = 1 AND iorder = 1';
        conn.query(query, (err, results) => {
            if (err) {
                console.log(err);
            }
            for (let i in results) {
                let row = results[i];
                let author;
                for(let  j in authors) {
                    author = authors[j];
                    if(row.data.includes(author.name)) {
                        break;
                    }
                }
                if(author.name === 'Indiepost') {
                    continue;
                }
                let update =
                    "update Posts set authorId=" + author.id  + " where id = " + row.no;
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

updateEditor();
updateAuthor();