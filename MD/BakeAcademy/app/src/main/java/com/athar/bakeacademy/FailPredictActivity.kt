package com.athar.bakeacademy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.athar.bakeacademy.databinding.ActivityFailPredictBinding

class FailPredictActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFailPredictBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityFailPredictBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnReTake.setOnClickListener {
                val cam = Intent(this@FailPredictActivity, CameraActivity::class.java)
                startActivity(cam)
                finish()
            }
        }
    }
}