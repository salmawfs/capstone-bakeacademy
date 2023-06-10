'use strict';
const Hapi = require('@hapi/hapi');
const routes = require('./routes');
const secretKey = process.env.SECRET_KEY_JWT;

const init = async () => {
    const server = Hapi.server({
        port: 3000,
        host: 'localhost',
        routes: {
            cors: {
                origin: ['*'],
            },
        },
    });

    // Cookies
    server.state('token', {
        ttl: 24 * 60 * 60 * 1000,     // 1 day
        isSecure: false,
        isHttpOnly: true,
        encoding: 'base64json',
        clearInvalid: false,
       strictHeader: true
     });

     // Register hapi-auth-jwt2 plugin
    await server.register(require('hapi-auth-jwt2'));

    // Configure JWT authentication strategy
    server.auth.strategy('jwt', 'jwt', {
        key: secretKey,
        validate: (decoded) => {
        // Perform additional validation if required
        return { isValid: true };
        },
        verifyOptions: { algorithms: ['HS256'] }
    });
    

    server.route(routes);

    // Enable the JWT authentication strategy
    server.auth.default('jwt');
    
    await server.start();
    console.log(`Server berjalan pada ${server.info.uri}`);
};

init();