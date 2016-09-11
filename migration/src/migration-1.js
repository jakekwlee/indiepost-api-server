const mysql = require('promise-mysql');
const migrate = require('./migration-post').migrate;

const pool = mysql.createPool({
    host: 'localhost',
    user: 'indiepost',
    password: 'indiepost',
    database: 'indiepost',
    connectionLimit: 30
});

const options = {
    users: [
        {id: 1, name: 'Indiepost', username: 'admin'},
        {id: 4, name: '임유청', username: 'imyou'},
        {id: 5, name: '이사민', username: 'lsm'},
        {id: 2, name: '안병태', username: 'byungtae.ahn'},
        {id: 6, name: 'LEE YI JAE', username: 'rheejae'},
        {id: 7, name: '유미래', username: 'you'},
        {id: 8, name: 'GRAYE', username: 'graye'},
        {id: 9, name: 'Yi Youngsoo', username: 'youngsoo'}
    ],
    selectLegacyQuery: 'SELECT c.no, c.KEYWORD, c.WRITERID, d.data FROM detaillist AS d INNER JOIN contentlist AS c ON d.parent = c.no WHERE d.type = 1 AND d.iorder = 1 ORDER BY c.no',
    updateEditorsQuery: 'UPDATE __posts SET editorId = ? WHERE id = ?',
    updateAuthorsQuery: 'UPDATE __posts SET authorId = ? WHERE id = ?',
    insertTagsQuery: 'INSERT INTO __tags (name, slug) VALUES (?, ?)',
    insertPostsTagsQuery: 'INSERT INTO __posts_tags (postId, tagId) VALUES (?, ?)'
};

migrate(pool, options).then(() => {
    pool.end();
});