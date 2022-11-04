package com.vasukammedia.kawsar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vasukammedia.kawsar.databinding.ActivitySplashScreenBinding
import java.util.Timer
import kotlin.concurrent.timerTask

class splashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    fun openMain() {

        val tmtask = timerTask {
            if (!isDestroyed) {
                startActivity(Intent(this@splashScreen,MainActivity::class.java))
            }
        }

        val timer = Timer()
        timer.schedule(tmtask, 3000)

    }
}