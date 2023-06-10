const mysql = require('mysql2/promise');
require('dotenv').config();

const conn = mysql.createPool({
    host: process.env.DB_HOST,
    user: process.env.DB_USER,
    password: process.env.DB_PASSWORD,
    database: process.env.DB_NAME 
});

if(!conn) {
    return "Connection failed";
}

module.exports = conn;