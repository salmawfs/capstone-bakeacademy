package com.athar.bakeacademy

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.athar.bakeacademy.databinding.ActivityCameraResultBinding
import com.athar.bakeacademy.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {



    private lateinit var selectedFragment: Fragment
    private var fragmentManager = supportFragmentManager
    private lateinit var binding: ActivityMainBinding
    private lateinit var mUserPreference: UserPreference
    private lateinit var userModel: UserModel
    private lateinit var binding1: ActivityCameraResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)
        binding1 = ActivityCameraResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userCheckPreference()

        binding.bottomNavigationView.menu.getItem(1).isEnabled = false
        binding.bottomNavigationView.setOnItemSelectedListener { navbarItem ->
            when(navbarItem.itemId) {
                R.id.home -> selectedFragment = HomeFragment()
                R.id.profile -> selectedFragment = ProfilFragment()
            }

            fragmentManager.commit {
                replace(R.id.fragment_container, selectedFragment)
            }

            true
        }
        binding.bottomNavigationView.selectedItemId = R.id.home
        binding.fab.setOnClickListener {
            startCameraX()
        }
    }

    private fun validToken() {
        val token: String? = mUserPreference.getUser().token
        val id = "naan"
        val client = ApiConfig.getApiService().getBakery("Bearer $token", id)
        client.enqueue(object : Callback<BakeryResponse> {

            override fun onResponse(
                call: Call<BakeryResponse>,
                response: Response<BakeryResponse>
            ) {

                if (response.isSuccessful) {

                } else if (response.code() == 401 ){
                    Toast.makeText(this@MainActivity, "Sesi Anda Telah Habis. Silahkan Login Kembali", Toast.LENGTH_SHORT).show()
                    mUserPreference.removeUser()
                    val login = Intent(this@MainActivity, LoginActivity::class.java)
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

    private fun userCheckPreference() {
        mUserPreference = UserPreference(this)
        userModel = mUserPreference.getUser()
        userModel.token = null
        userModel = mUserPreference.getUser()

        if (userModel.alergi == null) {
            val alergi = Intent(this@MainActivity, ChooseAlergenActivity::class.java)
            startActivity(alergi)

            finishAffinity()
        }

        if(userModel.token == null) {
            val login = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(login)

            finishAffinity()
        } else {
            validToken()
        }

    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)
    }



}