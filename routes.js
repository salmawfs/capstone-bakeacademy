const { indexHandler, 
    registerHandler, 
    loginHandler, 
    logoutHandler, 
    chatHandler,
    uploadImageHandler } = require('./handler');

const routes = [
    {
        method: 'GET',
        path: '/',
        handler: indexHandler,
        config: {
            state: {
                parse: true,
                failAction: 'error'
            },
            auth: 'jwt'
        }
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
            state: {
                parse: true,
                failAction: 'error'
            },
            auth: false
        }
    },
    {
        method: 'GET',
        path: '/logout',
        handler: logoutHandler,
        config: {
            state: {
                parse: true,
                failAction: 'error'
            },
            auth: 'jwt'
        }
    },
    {
        method: 'POST',
        path: '/resep',
        handler: chatHandler,
        config: {
            state: {
                parse: true,
                failAction: 'error'
            },
            auth: 'jwt'
        }
    },
    {
        method: 'POST',
        path: '/upload',
        handler: uploadImageHandler,
        options: {
            state: {
                parse: true,
                failAction: 'error'
            },
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
    }
];

module.exports = routes;