package com.example.smart_trash_bin

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Inisialisasi ProgressBar
        progressBar = findViewById(R.id.progress_bar)

        // Inisialisasi animasi untuk logo_eatera
        val logoAnimation = AnimationUtils.loadAnimation(this, R.anim.from_top)
        findViewById<ImageView>(R.id.logo).startAnimation(logoAnimation)

        // Set durasi untuk animasi logo_eatera
        val logoAnimationDuration = 1500L

        // Set durasi yang dibutuhkan untuk mencapai kemajuan 100
        val progressDuration = 3000L
        val progressInterval = 100L
        val totalSteps = (progressDuration / progressInterval).toInt()

        // Menunda tampilan ProgressBar setelah animasi logo_eatera selesai
        Handler(Looper.getMainLooper()).postDelayed({

            // Jalankan proses dengan interval tertentu untuk memperbarui ProgressBar
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed(object : Runnable {
                var currentStep = 0

                override fun run() {
                    progressBar.progress = ((currentStep.toFloat() / totalSteps.toFloat()) * 100).toInt()
                    if (currentStep < totalSteps) {
                        currentStep++
                        handler.postDelayed(this, progressInterval)
                    } else {
                        // Pindah ke MainActivity setelah ProgressBar selesai
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        finish()
                    }
                }
            }, progressInterval)
        }, logoAnimationDuration)
    }
}
