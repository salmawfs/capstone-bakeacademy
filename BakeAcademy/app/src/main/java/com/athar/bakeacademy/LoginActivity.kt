package com.athar.bakeacademy

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.athar.bakeacademy.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        passValidate()
        binding.btnLogin.setOnClickListener {
            val username = binding.editTextUserName.text.toString()
            val password = binding.editTextPassword.text.toString()

            loginFunction(username, password)
        }

        binding.tvRegister.setOnClickListener {
            val register = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(register)
            finishAffinity()
        }
    }

    private fun loginFunction(email: String, pass: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().getUser(email, pass)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                showLoading(false)
                val responseBody = response.body()
                if (response.isSuccessful) {
                    binding.progressBar.visibility = View.INVISIBLE

                    if(responseBody != null) {
                        savePreference(responseBody)
                    }


                    val main = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(main)
                    finishAffinity()
                } else {
                    Toast.makeText(this@LoginActivity, "Cek kembali Username & Password", Toast.LENGTH_SHORT).show()
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun passValidate() {
        binding.editTextPassword.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (s.length < 8) {
                    binding.editTextPassword.error =  "Password tidak boleh kurang dari 8"
                }
            }
        })
    }

    private fun savePreference (user: UserResponse) {
        val userPreference = UserPreference(this)
        val token = user.token
        val userId = user.user.id
        val nama = user.user.nama
        val username = user.user.username
        val email = user.user.email
        val alergi = user.user.idAlergi
        val foto = user.user.foto

        val userModel = UserModel(
            userId = userId,
            token = token,
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