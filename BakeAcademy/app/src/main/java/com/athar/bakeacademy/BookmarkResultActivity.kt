package com.athar.bakeacademy

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.athar.bakeacademy.databinding.ActivityBookmarkResultBinding
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookmarkResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookmarkResultBinding
    private lateinit var mUserPreference: UserPreference
    private lateinit var alergenAlert: String
    private var resepId: Int = 0

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
        binding = ActivityBookmarkResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mUserPreference = UserPreference(this)

        binding.tvAlergen2.visibility = View.GONE
        setToolbar()
        setPhoto()
        callFragment()
        getBakery()
        onClick()
    }

    private fun onClick() {
        binding.btnDeleteBookmark.setOnClickListener {
            deleteBookmark()
        }
        binding.btnOpenCam.setOnClickListener {
            val cam = Intent(this@BookmarkResultActivity, CameraActivity::class.java)
            startActivity(cam)
            finish()
        }
    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolbarNih)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = " "
    }

    private fun getBakery() {
        showLoading(true)
        val token: String? = mUserPreference.getUser().token
        val id = intent.getStringExtra("NAMAROTI")
        val client = ApiConfig.getApiService().getBakery("Bearer $token", id)
        client.enqueue(object : Callback<BakeryResponse> {

            override fun onResponse(
                call: Call<BakeryResponse>,
                response: Response<BakeryResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setDetailResepData(responseBody)
                    }
                } else if (response.code() == 401 ){
                    Toast.makeText(this@BookmarkResultActivity, "Sesi Anda Telah Habis. Silahkan Login Kembali", Toast.LENGTH_SHORT).show()
                    mUserPreference.removeUser()
                    val login = Intent(this@BookmarkResultActivity, LoginActivity::class.java)
                    startActivity(login)
                    finishAffinity()
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<BakeryResponse>, t: Throwable) {
                showLoading(false)
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun deleteBookmark() {
        showLoading(true)
        val token: String? = mUserPreference.getUser().token
        val id = intent.getIntExtra("ID", 0)
        val client = ApiConfig.getApiService().deleteBookmark("Bearer $token", id)
        client.enqueue(object : Callback<RegisterResponse> {

            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if (responseBody != null) {
                            Toast.makeText(
                                this@BookmarkResultActivity,
                                "Resep berhasil dihapus",
                                Toast.LENGTH_SHORT
                            ).show()
                            val main = Intent(this@BookmarkResultActivity, MainActivity::class.java)
                            startActivity(main)
                            finishAffinity()
                        }
                    }
                } else if (response.code() == 401 ){
                    Toast.makeText(this@BookmarkResultActivity, "Sesi Anda Telah Habis. Silahkan Login Kembali", Toast.LENGTH_SHORT).show()
                    mUserPreference.removeUser()
                    val login = Intent(this@BookmarkResultActivity, LoginActivity::class.java)
                    startActivity(login)
                    finishAffinity()
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
        val name = bakery.data.nama
        val deskripsi = bakery.data.deskripsi
        binding.tvNamaRoti.text = name
        binding.tvDeskripsi.text = deskripsi
        checkAlergen(bakery.trsBahan)
        resepId = bakery.data.id


    }

    private fun setPhoto() {
        val image = "https://storage.googleapis.com/bakeacademy-bucket/upload-foto/roti/" + intent.getStringExtra("FOTOROTI")
        Glide.with(binding.imgBakery)
            .load(image)
            .fitCenter()
            .into(binding.imgBakery)

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val main = Intent(this@BookmarkResultActivity, MainActivity::class.java)
                startActivity(main)
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    private fun checkAlergen(bahan: String){
        val alergen : String? = mUserPreference.getUser().alergi

        if (alergen != null) {
            if(alergen.contains("1", ignoreCase = true)) {
                if(bahan.contains("telur", ignoreCase = true)) {
                    alergenAlert = "Resep ini mengandung Bahan yang dapat menyebabkan anda alergi."
                    binding.tvAlergen2.visibility = View.VISIBLE
                    binding.tvAlergen2.text = alergenAlert
                } else {
                    binding.tvAlergen2.visibility = View.GONE
                }
            } else if(alergen.contains("2", ignoreCase = true)) {
                if(bahan.contains("susu", ignoreCase = true)) {
                    alergenAlert = "Resep ini mengandung Bahan yang dapat menyebabkan anda alergi."
                    binding.tvAlergen2.visibility = View.VISIBLE
                    binding.tvAlergen2.text = alergenAlert
                } else {
                    binding.tvAlergen2.visibility = View.GONE
                }
            } else if(alergen.contains("3", ignoreCase = true)) {
                if(bahan.contains("gula", ignoreCase = true)) {
                    alergenAlert = "Resep ini mengandung Bahan yang dapat menyebabkan anda alergi."
                    binding.tvAlergen2.visibility = View.VISIBLE
                    binding.tvAlergen2.text = alergenAlert
                } else {
                    binding.tvAlergen2.visibility = View.GONE
                }
            } else if(alergen.contains("4", ignoreCase = true)) {
                if(bahan.contains("kacang", ignoreCase = true)) {
                    alergenAlert = "Resep ini mengandung Bahan yang dapat menyebabkan anda alergi."
                    binding.tvAlergen2.visibility = View.VISIBLE
                    binding.tvAlergen2.text = alergenAlert
                } else {
                    binding.tvAlergen2.visibility = View.GONE
                }
            } else if(alergen == "null") {
                binding.tvAlergen2.visibility = View.GONE
            }
        }
    }

    private fun callFragment() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        val tabs: TabLayout = binding.tabs

        sectionsPagerAdapter.token = mUserPreference.getUser().token
        sectionsPagerAdapter.id = intent.getStringExtra("NAMAROTI")
        viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(BookmarkResultActivity.TAB_TITLES[position])
        }.attach()

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

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}