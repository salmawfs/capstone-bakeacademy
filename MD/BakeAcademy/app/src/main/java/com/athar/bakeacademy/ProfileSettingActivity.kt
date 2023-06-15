package com.athar.bakeacademy

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.athar.bakeacademy.databinding.ActivityProfileSettingBinding
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProfileSettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileSettingBinding
    private lateinit var mUserPreference: UserPreference
    private var alergiUser: String = "null"
    private var imageResult: Uri? = null
    private var retrievedImgFile: File? = null
    val langArray = arrayOf("Telur", "Susu", "Gula", "Kacang")
    var selectedLanguage = BooleanArray(langArray.size)
    val langList = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityProfileSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mUserPreference = UserPreference(this)

        showCurrentData()
        showAlergenDialog()
        setOnClick()
    }

    private fun setOnClick() {
        binding.btnOpenGalery.setOnClickListener {
            startGallery()
        }

        binding.btnSubmit.setOnClickListener {
            updateProfil()
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            imageResult = selectedImg
            binding.imProfile.setImageURI(imageResult)

            val convertedFile = imageResult.let { uriToFile(it as Uri, this@ProfileSettingActivity) }
            retrievedImgFile = convertedFile
        }
    }

    private fun showCurrentData() {
        val name = mUserPreference.getUser().nama
        val username = mUserPreference.getUser().username
        val email = mUserPreference.getUser().email
        val foto = "https://storage.googleapis.com/bakeacademy-bucket/upload-foto/profile/" + mUserPreference.getUser().foto

        binding.apply {
            editTextUserNama.setText(name)
            editTextUserUsername.setText(username)
            editTextEmail.setText(email)

            Glide.with(imProfile)
                .load(foto)
                .into(imProfile)

        }
    }

    private fun updateProfil() {
        showLoading(true)
        val token = mUserPreference.getUser().token
        val idUser = mUserPreference.getUser().userId
        val nama: String? = binding.editTextUserNama.text.toString()
        val username: String? = binding.editTextUserUsername.text.toString()
        val email: String? = binding.editTextEmail.text.toString()
        val id_alergi: String = alergiUser
        val foto = mUserPreference.getUser().foto

        val namePart = nama?.let { MultipartBody.Part.createFormData("nama", it) }
        val UsernamePart = username?.let { MultipartBody.Part.createFormData("username", it) }
        val emailPart = email?.let { MultipartBody.Part.createFormData("email", it) }
        val alergiPart = MultipartBody.Part.createFormData("id_alergi", id_alergi)

        if (imageResult == null) {
            val client = ApiConfig.getApiService().updateProfilWithoutPicture("Bearer $token", idUser, namePart, UsernamePart, emailPart, alergiPart)
            client.enqueue(object : Callback<UpdateProfileResponse> {
                override fun onResponse(call: Call<UpdateProfileResponse>, response: Response<UpdateProfileResponse>) {
                    showLoading(false)
                    if (response.isSuccessful) {
                        binding.progressBar.visibility = View.INVISIBLE
                        savePreference(token, idUser, nama, username, email, id_alergi, foto)
                        val main = Intent(this@ProfileSettingActivity, MainActivity::class.java)
                        Toast.makeText(this@ProfileSettingActivity, "Profile Berhasil diubah", Toast.LENGTH_SHORT).show()
                        startActivity(main)
                        finishAffinity()
                    } else {
                        Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                    Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                }
            })
        } else {

            val file = reduceFileImage(retrievedImgFile as File)
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "file",
                file.name,
                requestImageFile
            )

            val client = ApiConfig.getApiService().updateProfil("Bearer $token", idUser, namePart, UsernamePart, emailPart, alergiPart, imageMultipart)
            client.enqueue(object : Callback<UpdateProfilWithPhotoResponse> {
                override fun onResponse(call: Call<UpdateProfilWithPhotoResponse>, response: Response<UpdateProfilWithPhotoResponse>) {
                    showLoading(false)
                    val responseBody = response.body()
                    if (response.isSuccessful) {
                        binding.progressBar.visibility = View.INVISIBLE
                        savePreference(token, idUser, nama, username, email, id_alergi, responseBody?.foto)
                        val main = Intent(this@ProfileSettingActivity, MainActivity::class.java)
                        Toast.makeText(this@ProfileSettingActivity, "Profile Berhasil diubah", Toast.LENGTH_SHORT).show()
                        startActivity(main)
                        finishAffinity()
                    } else {
                        Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<UpdateProfilWithPhotoResponse>, t: Throwable) {
                    Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                }
            })
        }
    }

    private fun showAlergenDialog() {

        val currentAlergen = mUserPreference.getUser().alergi
        binding.tvPilihAlergen.text = currentAlergen.toString()



        for (i in 0 until langArray.size) {
            selectedLanguage[i] = false
        }

        if (currentAlergen != null) {
            if (currentAlergen.contains("telur", ignoreCase = true)) {
                langList.add(0)
                selectedLanguage[0] = true
            }
            if (currentAlergen.contains("susu", ignoreCase = true)) {
                langList.add(1)
                selectedLanguage[1] = true
            }
            if (currentAlergen.contains("gula", ignoreCase = true)) {
                langList.add(2)
                selectedLanguage[2] = true
            }
            if (currentAlergen.contains("kacang", ignoreCase = true)) {
                langList.add(3)
                selectedLanguage[3] = true
            }
        }

        binding.tvPilihAlergen.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Pilih Alergen")
            builder.setCancelable(false)

            builder.setMultiChoiceItems(langArray, selectedLanguage) { dialog, which, isChecked ->
                if (isChecked) {
                    langList.add(which)
                    langList.sort()
                } else {
                    langList.remove(which)
                }
            }

            builder.setPositiveButton("OK") { dialog, which ->
                val stringBuilder = StringBuilder()

                for (i in 0 until langList.size) {
                    stringBuilder.append(langArray[langList[i]])

                    if (i != langList.size - 1) {
                        stringBuilder.append(", ")
                    }
                }

                binding.tvPilihAlergen.text = stringBuilder.toString()
                alergiUser = stringBuilder.toString()
            }

            builder.setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }

            builder.setNeutralButton("Clear All") { dialog, which ->
                for (i in 0 until selectedLanguage.size) {
                    selectedLanguage[i] = false
                    langList.clear()
                    binding.tvPilihAlergen.text = ""
                }
            }

            builder.show()
        }
    }

    private fun savePreference (
        token: String?,
        idUser: Int,
        nama: String?,
        username: String?,
        email: String?,
        alergi: String?,
        foto: String?
    ) {
        val userPreference = UserPreference(this)

        val userModel = UserModel(
            token = token,
            userId = idUser,
            nama = nama,
            username = username,
            email = email,
            alergi = alergi,
            foto = foto
        )

        userPreference.setUser(userModel)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}