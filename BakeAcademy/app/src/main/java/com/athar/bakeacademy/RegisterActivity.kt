package com.athar.bakeacademy

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.athar.bakeacademy.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        passValidate()
        emailValidate()

        binding.btnRegister.setOnClickListener {
            val nama = binding.editTextNama.text.toString()
            val username = binding.editTextUserName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            registerFunction(nama, username, email, password)
        }

        binding.tvLogin.setOnClickListener {
            val login = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(login)
            finishAffinity()
        }
    }

    private fun registerFunction(nama: String, username: String, email: String, pass: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().postUser(nama, username, email, pass)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    binding.progressBar.visibility = View.INVISIBLE
                    val main = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(main)
                    Toast.makeText(this@RegisterActivity, "Registrasi Berhasil", Toast.LENGTH_SHORT).show()
                    finishAffinity()
                } else {
                    Toast.makeText(this@RegisterActivity, "Email atau Username sudah terdaftar", Toast.LENGTH_SHORT).show()
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
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

    private fun emailValidate() {
        binding.editTextEmail.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (!Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches() ) {
                    binding.editTextEmail.error = "Pastikan Format Email Sesuai"
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