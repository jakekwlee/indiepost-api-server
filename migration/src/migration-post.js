const Promise = require('bluebird');
const mysql = require('mysql');
const _ = require('lodash');

module.exports = (()=> {

    function selectFromDB(pool, query) {
        console.log('query: ' + query);
        return pool.query(query);
    }

    function makePostsArray(rows) {
        return Promise.map(rows, row => {
            return {
                no: row.no,
                tags: tagStringSpilt(row.KEYWORD),
                writerId: row.WRITERID,
                byline: row.data
            };
        });
    }

    function tagStringSpilt(tagString) {
        let tags = tagString.split(', ').map(tag => {
            let t = _.trim(tag, ' ,');
            t = t.replace(/\s/g, '_');
            return t;
        });
        return _.uniq(tags);
    }

    function updateEditorsFromDB(posts, users, pool, query) {
        return Promise.each(posts, post => {
            for (let i in users) {
                let user = users[i];
                if (user.username == post.writerId && user.id !== 1) {
                    let sql = mysql.format(query, [user.id, post.no]);
                    console.log('query: ' + sql);
                    return pool.query(sql);
                }
            }
        });
    }

    function updateAuthorsFromDB(posts, users, pool, query) {
        return Promise.each(posts, post => {
            for (let i in users) {
                let user = users[i];
                if (user.id !== 1 && _.includes(post.byline, user.name)) {
                    let sql = mysql.format(query, [user.id, post.no]);
                    console.log('query: ' + sql);
                    return pool.query(sql);
                }
            }
        });
    }

    function makeTagSet(posts) {
        let tags = [];
        let GRAYE = 'GRAYE';
        posts.forEach(post => {
            post.tags.forEach(tag => {
                if(tag.toUpperCase() === GRAYE) {
                    tags.push(GRAYE);
                    return;
                }
                tags.push(tag);
            });
        });
        tags = _.pull(tags, '', ' ');
        return _.uniq(tags);
    }

    function insertTagsToDB(posts, pool, query) {
        let tags = makeTagSet(posts);
        return Promise.map(tags, tag => {
            let sql = mysql.format(query, [tag, tag]);
            console.log('query: ' + sql);
            return pool.query(sql).then(result => {
                return {
                    id: result.insertId,
                    name: tag
                };
            });
        });
    }

    function makePostsTagsArray(posts, tags) {
        let postsTags = [];
        tags.forEach(tag => {
            posts.forEach(post => {
                post.tags.forEach(postTag => {
                    if(postTag.toUpperCase() === tag.name.toUpperCase()) {
                        postsTags.push({postId: post.no, tagId: tag.id});
                    }
                });
            });
        });
        return postsTags;
    }

    function insertPostsTagsToDB(posts, tags, pool, query) {
        let postsTags = makePostsTagsArray(posts, tags);
        return Promise.each(postsTags, link => {
            let sql = mysql.format(query, [link.postId, link.tagId]);
            console.log('query: ' + sql);
            return pool.query(sql);
        });
    }

    function migrate(pool, options) {
        const {
            users,
            selectLegacyQuery, updateEditorsQuery,
            updateAuthorsQuery, insertTagsQuery, insertPostsTagsQuery
        } = options;

        return selectFromDB(pool, selectLegacyQuery)
            .then(rows => {
                return makePostsArray(rows);
            })
            .then(posts => {
                return Promise.all([
                    updateEditorsFromDB(posts, users, pool, updateEditorsQuery),
                    updateAuthorsFromDB(posts, users, pool, updateAuthorsQuery),
                    insertTagsToDB(posts, pool, insertTagsQuery)
                        .then(tags => {
                            return insertPostsTagsToDB(posts, tags, pool, insertPostsTagsQuery);
                        })
                ]);
            });
    }

    return {migrate};
})();