package com.example.instapro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.instapro.databinding.ActivityPhotoProfileBinding

class PhotoProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPhotoProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPhotoProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(MainActivity.EXTRA_USERNAME)
        val fullName = intent.getStringExtra(MainActivity.EXTRA_NAME)
        val imageUriString = intent.getStringExtra(MainActivity.EXTRA_IMAGE_URI)

        with(binding) {
            if (!username.isNullOrEmpty()) {
                tvUserName.text = username
            }
            if (!fullName.isNullOrEmpty()) {
                tvFullName.text = fullName
            }
            if (!imageUriString.isNullOrEmpty()) {
                ivProfilePicture.setImageURI(imageUriString.toUri())
            }

            btnBack.setOnClickListener {
                finish()
            }
        }
    }
}