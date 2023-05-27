const conn = require('./connection');
const bcrypt = require('bcrypt');
const Joi = require('joi');
const axios = require('axios');
const jwt = require('jsonwebtoken');
const secretKey = process.env.SECRET_KEY_JWT;
const { Storage } = require('@google-cloud/storage');

require('dotenv').config();

// Set Koneksi


// Home
const indexHandler = (req, h) => {
    const login = req.state.login;
    const data = req.state.data;

    console.log(login);
    if(login) {
        return h.response({login, data}).code(200);
    } else {
        return h.response({message: 'Akses dilarang, silahkan login terlebih dahulu.'}).code(403);
    }
}
// Register
const registerHandler = async (req, h) => {
    // Cek email apakah sudah ada atau belum
    const { email } = req.payload;

    const query = 'SELECT COUNT(*) as count FROM users WHERE email = ?';
    const [rows] = await conn.query(query, [email]);
    const count = rows[0].count;
    const isTaken = count > 0;
    if(isTaken) {
        return h.response({message: 'Email sudah digunakan.'}).code(200);
    }

    const validationSchema = Joi.object({
        username: Joi.string().required().messages({
            'any.required': 'Username tidak boleh kosong'
        }),
        email: Joi.string().email().required().messages({
            'any.required': 'Email tidak boleh kosong',
        }),
        nama: Joi.string().required().messages({
            'any.required': 'Nama tidak boleh kosong'
        }),
        password: Joi.string().pattern(new RegExp('^[a-zA-Z0-9]{3,30}$')).required().messages({
            'any.required': 'Password tidak boleh kosong.'
        }),
        alergi: Joi.string(),
        foto: Joi.string()
      });

    const { error, value } = validationSchema.validate(req.payload, {
        abortEarly: false,
    });

    if (error) {
        // Invalid input, return an error response
        const errorMessages = error.details.map((err) => err.message);
        return h.response({warning: errorMessages}).code(400);
      } else {
        const hashedPassword = await bcrypt.hash(value.password, 10);

        try {
            // Insert data
           const result = await conn.query('INSERT INTO users (username, email, nama, password, alergi, foto) VALUES (?, ?, ?, ?, ?, ?)', 
           [value.username, value.email, value.nama, hashedPassword, value.alergi, value.foto], (err) => {
                if(err) return h.response('Internal Server Error').code(500);
            });
    
            return { message: 'Registrasi akun sukses.' };
        } catch (error) {
            console.error('Error executing MySQL query:', error);
            return h.response('Internal Server Error').code(500);
        }
      }
   
}
// Login
const loginHandler = async (req, h) => {
    const { username, password } = req.payload;
    
    const [rows] = await conn.execute('SELECT * FROM users WHERE username = ?', [username]);
    const user = rows[0];

    if(user) {
        const isValid = await bcrypt.compare(password, user.password);
        const credentials = { id: user.id, username: user.username, nama: user.nama, email: user.email, password: user.password, alergi: user.alergi, foto:user.foto};

        if(isValid) {
            h.state('login', { isLogin: true});
            h.state('data', { user: credentials});

            // generate jwt token
            const token = jwt.sign({ userId: user.id, username: user.username },secretKey, { expiresIn: '1h' });
            return h.response({message: 'Login sukses', token}).code(200);
        }
    }

    return { message: 'Username atau password salah'};
}
// Logout
const logoutHandler = async (req, h) => {
    const login = req.state.login;
    const data = req.state.data;
    if(login) {
        if(data) {
            h.unstate('data');
            h.unstate('login');

            return h.response({message: 'Logout sukses'}).code(200); 
        }
    } else {
        return h.response({message: 'Akses dilarang, silahkan login terlebih dahulu.'}).code(403);
    }
}
// CHATGPT
const chatHandler = async (req, h) => {
    const login = req.state.login;
    if(login) {
        const { q } = req.payload;
        const options = {
            method: 'POST',
            url: 'https://simple-chatgpt-api.p.rapidapi.com/ask',
            headers: {
              'content-type': 'application/json',
              'X-RapidAPI-Key': '15d1ea6959mshc5fb5c0f2a8e5b4p11a5bejsn16d38c8f6efd',
              'X-RapidAPI-Host': 'simple-chatgpt-api.p.rapidapi.com'
            },
            data: {
              question: q
            }
          };
    
          try {
            const response = await axios.request(options);
            return h.response(response.data).code(200);
        } catch (error) {
            return h.response({message: error.message});
        }
    } else {
        return h.response({message: 'Akses dilarang, silahkan login terlebih dahulu.'}).code(403);
    }
}
// Upload Image to Bucket
const uploadImageHandler = async (req, h) => {
    const login = req.state.login;  
    const file = req.payload.file;
    // console.log(file);
    if(login) {
        // Inisiasi GCS
        const storage = new Storage({
            projectId: process.env.PROJECT_ID_GCP,
            keyFilename: process.env.KEY_STORAGE,
        });

        const bucketName = process.env.BUCKET_NAME;
        const bucket = storage.bucket(bucketName);

        // Cek apakah file terupload
        if (file.hapi.filename === '') {
            return h.response({message: 'Belum ada file yang terupload'}).code(400);
        }

        // Membuat custom image name
        const fileExtension = file.hapi.filename.split('.').pop().toLowerCase();
        const filename = 'bakery' + '_' + Date.now() + '.' + fileExtension;
        const filePath = `upload-foto/${filename}`;
        // Upload the file to GCS
        const gcsFile = bucket.file(filePath);
        const gcsStream = gcsFile.createWriteStream();

        gcsStream.on('error', (err) => {
            console.error('Error upload file ke GCS:', err);
        });

        gcsStream.on('finish', () => {
            console.log('File berhasil diupload ke GCS:', filePath);
        });

        file.pipe(gcsStream);

        return h.response({message: 'File berhasil diupload.'}).code(200);
        
    } else {
        return h.response({message: 'Akses dilarang, silahkan login terlebih dahulu.'}).code(403);
    }
}

module.exports = {
    indexHandler, 
    registerHandler,
    loginHandler,
    logoutHandler,
    chatHandler,
    uploadImageHandler
};