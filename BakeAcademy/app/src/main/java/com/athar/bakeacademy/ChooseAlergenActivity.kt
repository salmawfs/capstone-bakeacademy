package com.athar.bakeacademy

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.athar.bakeacademy.databinding.ActivityChooseAlergenBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ChooseAlergenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChooseAlergenBinding
    private var imageResult: Uri? = null
    private lateinit var mUserPreference: UserPreference
    private var alergiUser: String = "null"
    private var retrievedImgFile: File? = null
    val langArray = arrayOf("Telur", "Susu", "Gula", "Kacang")
    var selectedLanguage = BooleanArray(langArray.size)
    val langList = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityChooseAlergenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mUserPreference = UserPreference(this)


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

        binding.tvLewati.setOnClickListener {
            lewati()
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

            val convertedFile = imageResult.let { uriToFile(it as Uri, this@ChooseAlergenActivity) }
            retrievedImgFile = convertedFile
        }
    }

    private fun updateProfil() {
        showLoading(true)
        val token = mUserPreference.getUser().token
        val idUser = mUserPreference.getUser().userId
        val nama: String? = mUserPreference.getUser().nama
        val username: String? = mUserPreference.getUser().username
        val email: String? = mUserPreference.getUser().email
        val id_alergi: String = alergiUser

        val namePart = nama?.let { MultipartBody.Part.createFormData("nama", it) }
        val UsernamePart = username?.let { MultipartBody.Part.createFormData("username", it) }
        val emailPart = email?.let { MultipartBody.Part.createFormData("email", it) }
        val alergiPart = MultipartBody.Part.createFormData("id_alergi", id_alergi)

        if (imageResult == null) {
            val client = ApiConfig.getApiService().updateProfilWithoutPicture("Bearer $token", idUser, namePart, UsernamePart, emailPart, alergiPart)
            client.enqueue(object : Callback<UpdateProfileResponse> {
                override fun onResponse(call: Call<UpdateProfileResponse>, response: Response<UpdateProfileResponse>) {
                    showLoading(false)
                    val responseBody = response.body()
                    if (response.isSuccessful) {
                        binding.progressBar.visibility = View.INVISIBLE
                        savePreference(token, idUser, nama, username, email, id_alergi, null)
                        val main = Intent(this@ChooseAlergenActivity, MainActivity::class.java)
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
                        val main = Intent(this@ChooseAlergenActivity, MainActivity::class.java)
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

    private fun lewati(){
        val token = mUserPreference.getUser().token
        val idUser = mUserPreference.getUser().userId
        val nama: String? = mUserPreference.getUser().nama
        val username: String? = mUserPreference.getUser().username
        val email: String? = mUserPreference.getUser().email
        val idAlergi = "null"

        savePreference(token, idUser, nama, username, email, idAlergi, null)
        val main = Intent(this@ChooseAlergenActivity, MainActivity::class.java)
        startActivity(main)
        finishAffinity()
    }

    private fun showAlergenDialog() {
        for (i in 0 until langArray.size) {
            selectedLanguage[i] = false
        }

        binding.textView.setOnClickListener {
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

                binding.textView.text = stringBuilder.toString()
                alergiUser = stringBuilder.toString()
            }

            builder.setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }

            builder.setNeutralButton("Clear All") { dialog, which ->
                for (i in 0 until selectedLanguage.size) {
                    selectedLanguage[i] = false
                    langList.clear()
                    binding.textView.text = ""
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