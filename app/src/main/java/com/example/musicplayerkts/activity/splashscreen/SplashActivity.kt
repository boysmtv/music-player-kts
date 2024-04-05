package com.example.musicplayerkts.activity.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.musicplayerkts.R
import com.example.musicplayerkts.activity.music.presentation.SearchActivity
import timber.log.Timber

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar!!.hide()
        setupScreen()
    }

    private fun setupScreen() {
        val background: Thread = object : Thread() {
            override fun run() {
                try {
                    // Thread will sleep for 5 seconds
                    sleep((1 * 1000).toLong())

                    val i = Intent(baseContext, SearchActivity::class.java)
                    startActivity(i)

                    //Remove activity
                    finish()
                } catch (e: Exception) {
                    Timber.tag("SplashActivity").e("Err : %s", e.message)
                }
            }
        }
        // start thread
        background.start()
    }
}