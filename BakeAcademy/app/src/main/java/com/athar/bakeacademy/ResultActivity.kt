package com.athar.bakeacademy

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.athar.bakeacademy.databinding.ActivityResultBinding
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var mUserPreference: UserPreference
    private lateinit var mShimmerViewContainer: ShimmerFrameLayout
    private lateinit var alergenAlert: String
    private var rotiUpload: File? = null
    private var resepId: Int = 0
    var bakeryId: String? = null
    var bakeryPicture: Uri? = null
    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_nutrisi,
            R.string.tab_text_bahan,
            R.string.tab_text_langkah
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mShimmerViewContainer = binding.shimmerViewContainer

        mUserPreference = UserPreference(this)

        bakeryId = intent.getStringExtra("id")
        bakeryPicture = intent.getParcelableExtra("gambar")

        binding.imgBakery.setImageURI(bakeryPicture)
        binding.tvAlergen.visibility = View.GONE

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.token = mUserPreference.getUser().token
        sectionsPagerAdapter.id = bakeryId
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        getBakery()

        setSupportActionBar(binding.toolbarNih)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = " "
        binding.btnOpenCam.setOnClickListener {
            val cam = Intent(this@ResultActivity, CameraActivity::class.java)
            startActivity(cam)
            finish()
        }
        binding.btnAddBookmark.setOnClickListener {
            postImage()
        }
        binding.apply {

            bakeryPicture?.let { processImage(it) }

            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position,positionOffset,positionOffsetPixels)
                    if (position>0 && positionOffset==0.0f && positionOffsetPixels==0){
                        viewPager.layoutParams.height =
                            viewPager.getChildAt(0).height
                    }
                }
            })
        }
    }

    private fun processImage(takenImageUri: Uri) {
        val convertedFile = takenImageUri.let { uriToFile(it, this@ResultActivity) }

        rotiUpload = convertedFile
    }

    private fun getBakery() {
        val token: String? = mUserPreference.getUser().token
        val id = "naan"
        val client = ApiConfig.getApiService().getBakery("Bearer $token", bakeryId)
        client.enqueue(object : Callback<BakeryResponse> {

            override fun onResponse(
                call: Call<BakeryResponse>,
                response: Response<BakeryResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setDetailResepData(responseBody)
                    }
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.visibility = View.GONE;
                } else if (response.code() == 401 ){
                    Toast.makeText(this@ResultActivity, "Sesi Anda Telah Habis. Silahkan Login Kembali", Toast.LENGTH_SHORT).show()
                    mUserPreference.removeUser()
                    val login = Intent(this@ResultActivity, LoginActivity::class.java)
                    startActivity(login)
                    finishAffinity()
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<BakeryResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun postImage() {
        showLoading(true)
        val file = reduceFileImage(rotiUpload as File)
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "file",
            file.name,
            requestImageFile
        )
        val token: String? = mUserPreference.getUser().token
        val client = ApiConfig.getApiService().postImage(imageMultipart,"Bearer $token")
        client.enqueue(object : Callback<UploadImageResponse> {

            override fun onResponse(
                call: Call<UploadImageResponse>,
                response: Response<UploadImageResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        postBookmark(responseBody.image)
                    }
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UploadImageResponse>, t: Throwable) {
                showLoading(false)
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun postBookmark(foto: String) {
        showLoading(true)
        val token: String? = mUserPreference.getUser().token
        val id: Int = mUserPreference.getUser().userId
        val idResep: Int = resepId
        val foto: String = foto

        val client = ApiConfig.getApiService().postBookmark("Bearer $token", id, idResep, foto)
        client.enqueue(object : Callback<RegisterResponse> {

            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Toast.makeText(this@ResultActivity, "Resep ditambahkan ke Favorit", Toast.LENGTH_SHORT).show()
                        val main = Intent(this@ResultActivity, MainActivity::class.java)
                        startActivity(main)
                        finishAffinity()
                    }
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                showLoading(false)
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }


    private fun setDetailResepData(bakery: BakeryResponse) {
        binding.tvNamaRoti.text = bakery.data.nama
        binding.tvDeskripsi.text = bakery.data.deskripsi
        checkAlergen(bakery.trsBahan)
        resepId = bakery.data.id

    }

    override fun onResume() {
        super.onResume()
        mShimmerViewContainer.startShimmerAnimation()
    }

    override fun onPause() {
        mShimmerViewContainer.stopShimmerAnimation()
        super.onPause()
    }

    private fun checkAlergen(bahan: String){
        val alergen : String? = mUserPreference.getUser().alergi

        if (alergen != null) {
            if(alergen.contains("telur", ignoreCase = true)) {
                if(bahan.contains("telur", ignoreCase = true)) {
                    alergenAlert = "Resep ini mengandung Bahan yang dapat menyebabkan anda alergi."
                    binding.tvAlergen.visibility = View.VISIBLE
                    binding.tvAlergen.text = alergenAlert
                } else {
                    binding.tvAlergen.visibility = View.GONE
                }
            } else if(alergen.contains("susu", ignoreCase = true)) {
                if(bahan.contains("susu", ignoreCase = true)) {
                    alergenAlert = "Resep ini mengandung Bahan yang dapat menyebabkan anda alergi."
                    binding.tvAlergen.visibility = View.VISIBLE
                    binding.tvAlergen.text = alergenAlert
                } else {
                    binding.tvAlergen.visibility = View.GONE
                }
            } else if(alergen.contains("gula", ignoreCase = true)) {
                if(bahan.contains("gula", ignoreCase = true)) {
                    alergenAlert = "Resep ini mengandung Bahan yang dapat menyebabkan anda alergi."
                    binding.tvAlergen.visibility = View.VISIBLE
                    binding.tvAlergen.text = alergenAlert
                } else {
                    binding.tvAlergen.visibility = View.GONE
                }
            } else if(alergen.contains("kacang", ignoreCase = true)) {
                if(bahan.contains("kacang", ignoreCase = true)) {
                    alergenAlert = "Resep ini mengandung Bahan yang dapat menyebabkan anda alergi."
                    binding.tvAlergen.visibility = View.VISIBLE
                    binding.tvAlergen.text = alergenAlert
                } else {
                    binding.tvAlergen.visibility = View.GONE
                }
            } else if(alergen == "null") {
                binding.tvAlergen.visibility = View.GONE
            } else {
                binding.tvAlergen.visibility = View.GONE
            }
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val main = Intent(this@ResultActivity, MainActivity::class.java)
                startActivity(main)
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}