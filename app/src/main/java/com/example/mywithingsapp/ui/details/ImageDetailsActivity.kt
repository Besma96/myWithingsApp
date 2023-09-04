package com.example.mywithingsapp.ui.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.mywithingsapp.R
import com.example.mywithingsapp.commons.utils.Constants.Companion.EXTRA_IMAGE
import com.example.mywithingsapp.databinding.ActivityDetailsBinding

class ImageDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailsBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_details
        )

        if (intent != null && intent.hasExtra(EXTRA_IMAGE)) {
            binding.hitsList = intent.getParcelableExtra(EXTRA_IMAGE)
            binding.callback = this
        }
    }

    fun moveToPreviousScreen() = finish()
}
