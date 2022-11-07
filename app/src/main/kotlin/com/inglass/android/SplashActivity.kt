package com.inglass.android

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val bundle = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out).toBundle()
//        startActivity(Intent(this, AppActivity::class.java), bundle)
//        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }
}
