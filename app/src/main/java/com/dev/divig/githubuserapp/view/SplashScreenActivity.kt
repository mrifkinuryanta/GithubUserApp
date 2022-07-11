package com.dev.divig.githubuserapp.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.dev.divig.githubuserapp.BuildConfig
import com.dev.divig.githubuserapp.R
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val appName = getString(R.string.app_name)
        val versionName = BuildConfig.VERSION_NAME
        tv_splash_app_title.text = appName
        tv_splash_app_version.text = versionName

        Handler().postDelayed({
            val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }, delay)
    }

    companion object {
        const val delay: Long = 3000
    }
}