const { indexHandler, 
        registerHandler, 
        loginHandler, 
        logoutHandler, 
        chatHandler,
        uploadImageHandler,
        getResepHandler,
        saveBookmarkHandler,
        updateProfileHandler,
        getBookmarkHandler,
        deleteBookmarkHandler
    } = require('./handler');

const routes = [
    {
        method: 'GET',
        path: '/',
        handler: indexHandler,
        config: {
            auth: 'jwt'
        },
    },  
    {
        method: 'POST',
        path: '/register',
        handler: registerHandler,
        options: {
            auth: false
        }
    },
    {
        method: 'POST',
        path: '/login',
        handler: loginHandler,
        config: {
            auth: false
        }
    },
    {
        method: 'GET',
        path: '/logout',
        handler: logoutHandler,
        config: {
            auth: 'jwt'
        }
    },
    {
        method: 'POST',
        path: '/resep',
        handler: chatHandler,
        config: {
            auth: false
        }
    },
    {
        method: 'POST',
        path: '/upload',
        handler: uploadImageHandler,
        options: {
            payload: {
                output: 'stream',
                parse: true,
                multipart: true,
                allow: 'multipart/form-data',
                maxBytes: 5 * 1024 * 1024, // 5MB limit (adjust as needed)
                timeout: false,
              },
            auth: 'jwt'
        }
    },
    {
        method: 'GET',
        path: '/recipes/{id}',
        handler: getResepHandler,
        config: {
            auth: 'jwt'
        }
    }, 
    {
        method: 'POST',
        path: '/save/recipes',
        handler: saveBookmarkHandler,
        config: {
            auth: 'jwt'
        }
    },
    {
        method: 'POST',
        path: '/update/profile/{id}',
        handler: updateProfileHandler,
        options: {
            payload: {
                output: 'stream',
                parse: true,
                multipart: true,
                allow: 'multipart/form-data',
                maxBytes: 5 * 1024 * 1024, // 5MB limit (adjust as needed)
                timeout: false,
              },
            auth: 'jwt'
        }
    },
    {
        method: 'GET',
        path: '/bookmark/{id}',
        handler: getBookmarkHandler,
        config: {
            auth: 'jwt'
        },
    }, 
    {
        method: 'GET',
        path: '/bookmark/delete/{id}',
        handler: deleteBookmarkHandler,
        config: {
            auth: 'jwt'
        }
    }
];

module.exports = routes;