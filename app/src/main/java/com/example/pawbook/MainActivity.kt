package com.example.pawbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loadingGif: ImageView = findViewById(R.id.loading_gif)
        Glide.with(this).load(R.drawable.dog_loading).into(loadingGif)

        Handler().postDelayed({
             startActivity(Intent(this, BreedsActivity::class.java))
             finish()
        }, 2100)
    }
}