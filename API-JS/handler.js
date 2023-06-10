const conn = require('./connection');
const Moment = require('moment');
const bcrypt = require('bcrypt');
const fs = require('fs');
const path = require('path');
const Joi = require('joi');
const axios = require('axios');
const jwt = require('jsonwebtoken');
const secretKey = process.env.SECRET_KEY_JWT;
const { Storage } = require('@google-cloud/storage');
const { Translate } = require('@google-cloud/translate').v2;
const tf = require('@tensorflow/tfjs-node');
require('dotenv').config();

// Home
const indexHandler = (req, h) => { 
    return h.response({ message: 'Halaman utama' }).code(200);
}
// Register
const registerHandler = async (req, h) => {
    // Cek email apakah sudah ada atau belum
    const { email, username } = req.payload;

    const query = 'SELECT COUNT(*) as count FROM users WHERE email = ?';
    const [rows] = await conn.query(query, [email]);
    const count = rows[0].count;
    const isTaken = count > 0;
    if(isTaken) {
        return h.response({message: 'Email sudah digunakan.'}).code(200);
    }

    // validasi username
    const queryUsername = 'SELECT COUNT(*) as count FROM users WHERE username = ?';
    const [rowsUsername] = await conn.query(queryUsername, [username]);
    const countUsername = rowsUsername[0].count;
    const isTakenUsername = countUsername > 0;
    if (isTakenUsername) {
        return h.response({ message: 'Username sudah digunakan.' }).code(200);
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
           const result = await conn.query('INSERT INTO users (username, email, nama, password) VALUES (?, ?, ?, ?)', 
           [value.username, value.email, value.nama, hashedPassword], (err) => {
                if(err) return h.response('Internal Server Error', err).code(500);
            });
    
            return h.response({ message: 'Registrasi akun sukses.' }).code(200);
        } catch (error) {
            return h.response({message: 'Gagal Registrasi:'+err}).code(500);
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
            // generate jwt token
            const token = jwt.sign(credentials, secretKey, { expiresIn: '1h' });
            const decodedToken = jwt.verify(token, process.env.SECRET_KEY_JWT);
      
            // return h.response().state('token', token).redirect('/home');
            //h.state('token', token);
            return h.response({message: 'Login sukses', token, user: decodedToken}).code(200);
        }
    }

    return h.response({ message: 'Username atau password salah'}).code(422);
}
// Logout
const logoutHandler = async (req, h) => {
    return h.response({ message: 'Logout sukses' }).code(200);
}
// CHATGPT
const chatHandler = async (req, h) => {
    return 'tes';
    // const token = req.state.token;
    // if(token) {
    //     const { q } = req.payload;
    //     const options = {
    //         method: 'POST',
    //         url: 'https://simple-chatgpt-api.p.rapidapi.com/ask',
    //         headers: {
    //           'content-type': 'application/json',
    //           'X-RapidAPI-Key': '15d1ea6959mshc5fb5c0f2a8e5b4p11a5bejsn16d38c8f6efd',
    //           'X-RapidAPI-Host': 'simple-chatgpt-api.p.rapidapi.com'
    //         },
    //         data: {
    //           question: q
    //         }
    //       };
    
    //       try {
    //         const response = await axios.request(options);
    //         return h.response(response.data).code(200);
    //     } catch (error) {
    //         return h.response({message: error.message});
    //     }
    // } else {
    //     return h.response({message: 'Akses dilarang, silahkan login terlebih dahulu.'}).code(403);
    // }
}
// Upload Image to Bucket
const uploadImageHandler = async (req, h) => {
    const file = req.payload.file;

    // Inisiasi GCS
    const storage = new Storage({
        projectId: process.env.PROJECT_ID_GCP,
        keyFilename: process.env.KEY_STORAGE,
    });

    const bucketName = process.env.BUCKET_NAME;
    const bucket = storage.bucket(bucketName);

    // Cek apakah file terupload
    if (file.hapi.filename === '') {
        return h.response({ message: 'Belum ada file yang terupload' }).code(400);
    }

    // Validate tipe gambar
    const allowedExtensions = ['.jpg', '.jpeg', '.png', '.gif'];
    // Check if the file extension is allowed
    const ext = path.extname(file.hapi.filename).toLowerCase();
    if (!allowedExtensions.includes(ext)) {
        return h.response({ message: 'Format gambar tidak sesuai, hanya menerima file dengan ekstensi .jpg, .jpeg, .png, dan .gif ' }).code(400);
    }

    // Membuat custom image name
    const fileExtension = file.hapi.filename.split('.').pop().toLowerCase();
    const filename = 'bakery' + '_' + Date.now() + '.' + fileExtension;
    const filePath = `upload-foto/roti/${filename}`;
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

    return h.response({ message: 'File berhasil diupload.', image: filename }).code(200);
}
// Get recipes data
const getResepHandler = async (req, h) => {
    const id = req.params.id;

    const [rows] = await conn.execute('SELECT * FROM resep WHERE nama = ?', [id]);
    const data = rows[0];

    if (data) {
        // TRANSLATE BAHAN
        const translate = new Translate({
            projectId: process.env.PROJECT_ID_GCP,
            keyFilename: process.env.KEY_TRANSLATE
        });

        // Define the target language and text to translate
        const targetLanguage = 'id'; // Indonesian language code
        const textToTranslate = data.bahan;

        // Translate the text
        try {
            const [translation] = await translate.translate(textToTranslate, targetLanguage);
            // console.log(`Translated text (${targetLanguage}): ${translation}`);

            // Open gizi json
            const fileData = fs.readFileSync(data.nutrisi, 'utf8');
            const jsonNutrisi = JSON.parse(fileData);

            return h.response({ data: data, trsBahan: translation, nutrisi: jsonNutrisi }).code(200);
        } catch (error) {
            return h.response({ message: error }).code(500);
        }
    } else {
        return h.response({ message: 'Data tidak ditemukan' }).code(404);
    }
}
// Save resep favorit
const saveBookmarkHandler = async (req, h) => {
    const { id_user, id_resep, foto } = req.payload;
    const tanggal = Moment().format('YYYY-MM-DD HH:mm');
    
    try {
        const query = 'INSERT INTO bookmark (id_user, id_resep, foto, tanggal) VALUES(?,?,?,?)';
        const result = await conn.query(query, [id_user, id_resep, foto, tanggal]);

        return h.response({message: 'Data berhasil disimpan'}).code(200);
    } catch(err) {
        return h.response({message: 'Data gagal disimpan', err}).code(500);
    }
}
// Edit profile
const updateProfileHandler = async (req, h) => { 
    const { id, username, email, nama, id_alergi } = req.payload;
    const file = req.payload.file;

    if(file != null || file == '') {
        // Inisiasi GCS
        const storage = new Storage({
            projectId: process.env.PROJECT_ID_GCP,
            keyFilename: process.env.KEY_STORAGE,
        });

        const bucketName = process.env.BUCKET_NAME;
        const bucket = storage.bucket(bucketName);

        // Cek apakah file terupload
        if (file.hapi.filename === '') {
            return h.response({ message: 'Belum ada file yang terupload' }).code(400);
        }

        // Validate tipe gambar
        const allowedExtensions = ['.jpg', '.jpeg', '.png', '.gif'];
        // Check if the file extension is allowed
        const ext = path.extname(file.hapi.filename).toLowerCase();
        if (!allowedExtensions.includes(ext)) {
            return h.response({ message: 'Format gambar tidak sesuai, hanya menerima file dengan ekstensi .jpg, .jpeg, .png, dan .gif ' }).code(400);
        }

        // Membuat custom image name
        const fileExtension = file.hapi.filename.split('.').pop().toLowerCase();
        filename = 'profile' + '_' + Date.now() + '.' + fileExtension;
        const filePath = `upload-foto/profile/${filename}`;
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

        try {
            const query = 'UPDATE users SET username = ? , email = ?, nama = ?, id_alergi = ?, foto = ? WHERE id = ?';
            const result = await conn.query(query, [username, email, nama, id_alergi, filename, id]);
            
            return h.response({message: 'Data berhasil diubah'}).code(200);
        } catch(err) {
            return h.response({message: 'Data gagal diubah', err}).code(500);
        }
    } else {
        try {
            const query = 'UPDATE users SET username = ? , email = ?, nama = ?, id_alergi = ? WHERE id = ?';
            const result = await conn.query(query, [username, email, nama, id_alergi, id]);
            
            return h.response({message: 'Data berhasil diubah'}).code(200);
        } catch(err) {
            return h.response({message: 'Data gagal diubah', err}).code(500);
        }
    }
}
// Get bookmark
const getBookmarkHandler = async(req, h) =>{
    const id_user = req.params.id;

    try {
        const query = `SELECT a.*, b.nama as nama_roti 
        FROM bookmark as a
        JOIN resep as b ON a.id_resep = b.id
        WHERE a.id_user = ?`;
        const [bookmark] = await conn.query(query, [id_user]);

        if(bookmark.length > 0) {
            return { bookmark };
        } else {
            return h.response({message: 'Data tidak ditemukan'}).code(404);
        }
        
    } catch(err) {
        return h.response({message: 'Invalid Error: '+err}).code(404);
    }
}
// Delete bookmark
const deleteBookmarkHandler = async(req, h) => {
    const id = req.params.id;
    
    try {
        const [bookmark] = await conn.query('SELECT foto FROM bookmark WHERE id = ?', [id]);
        const resFoto = bookmark[0];

        // Inisiasi GCS
        const storage = new Storage({
            projectId: process.env.PROJECT_ID_GCP,
            keyFilename: process.env.KEY_STORAGE,
        });

        const bucketName = process.env.BUCKET_NAME;
        const filePath = `upload-foto/roti/${resFoto.foto}`;
        const deleteImg = storage.bucket(bucketName).file(filePath).delete();
        if(deleteImg) {
            console.log('Image deleted successfully.');
        }
        
        const result = await conn.query(`DELETE FROM bookmark WHERE id = ?`, [id]);
        if(result) {
            return h.response({status: 'success', message: 'Data berhasil dihapus'}).code(200);
        }
    } catch(err) {
        return h.response({status: 'error', message: 'Internal Server Error: '+err}).code(200);
    }
}
module.exports = {
    indexHandler, 
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
};