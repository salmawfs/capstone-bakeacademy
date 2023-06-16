package com.athar.bakeacademy

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.athar.bakeacademy.databinding.ActivityCameraResultBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class CameraResultActivity : AppCompatActivity() {

    private var retrievedImgFile: File? = null
    private lateinit var binding: ActivityCameraResultBinding
    var image: Uri? = null
    companion object {
        const val CAMERA_X_RESULT = 200
        const val GALLERY_X_RESULT = 100
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityCameraResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var galleryImage: Uri? = intent.getParcelableExtra("image_gallery")
        var takenImage: Uri? = intent.getParcelableExtra("imageCameraTaken")


        if (takenImage != null) {
            image = takenImage
        }
        if (galleryImage != null) {
            image = galleryImage
        }


        binding.apply {

            takenImage?.let { processImageFromCameraX(it) }
            galleryImage?.let { processImageFromGallery(it) }

            btnPredict.setOnClickListener {
                predictImage()
            }

            btnRetake.setOnClickListener {
                val cam = Intent(this@CameraResultActivity, CameraActivity::class.java)
                startActivity(cam)

                finish()
            }

        }

    }

    private fun processImageFromCameraX(takenImageUri: Uri) {
        val convertedFile = takenImageUri.let { uriToFile(it, this@CameraResultActivity) }

        retrievedImgFile = convertedFile
        binding.previewImageView.setImageURI(takenImageUri)
    }

    private fun processImageFromGallery(galleryImage: Uri) {
        val convertedFile = galleryImage.let { uriToFile(it, this@CameraResultActivity) }

        retrievedImgFile = convertedFile
        binding.previewImageView.setImageURI(galleryImage)
    }

    private fun predictImage() {
        showLoading(true)
        val file = reduceFileImage(retrievedImgFile as File)
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "image",
            file.name,
            requestImageFile
        )

        val client = ApiConfigPredict.getApiServicePredict().getBakery(imageMultipart)
        client.enqueue(object : Callback<PredictResponse> {

            override fun onResponse(
                call: Call<PredictResponse>,
                response: Response<PredictResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setPredictResult(responseBody)
                    }
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                showLoading(false)
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setPredictResult(result: PredictResponse) {
        val bakeryResult = result.predictedClass
        val accuracy = result.accuracy

        if (bakeryResult == "Random" || accuracy < 0.8) {
            val failIntent = Intent(this@CameraResultActivity, FailPredictActivity::class.java)
            startActivity(failIntent)
            finish()
        } else {
            val resultact = Intent(this@CameraResultActivity, ResultActivity::class.java)
            resultact.putExtra("id", bakeryResult)
            resultact.putExtra("gambar", image)
            startActivity(resultact)
            finish()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}