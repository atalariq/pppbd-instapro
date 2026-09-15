package com.example.instapro

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.instapro.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private var selectedImageUriString: String? = null

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            try {
                contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: Exception) {
            }
            selectedImageUriString = it.toString()
            binding.ivProfilePicture.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(intent) {
            binding.etUsername.setText(getStringExtra(MainActivity.EXTRA_USERNAME))
            binding.etName.setText(getStringExtra(MainActivity.EXTRA_NAME))
            binding.etPronoun.setText(getStringExtra(MainActivity.EXTRA_PRONOUN))
            binding.etBio.setText(getStringExtra(MainActivity.EXTRA_BIO))
            binding.etLink.setText(getStringExtra(MainActivity.EXTRA_LINK))

            selectedImageUriString = getStringExtra(MainActivity.EXTRA_IMAGE_URI)
            selectedImageUriString?.let {
                binding.ivProfilePicture.setImageURI(it.toUri())
            }
        }

        with(binding) {
            val openGalleryListener = { pickImageLauncher.launch("image/*") }
            ivProfilePicture.setOnClickListener { openGalleryListener() }
            tvChangePhoto.setOnClickListener { openGalleryListener() }

            btnSave.setOnClickListener {
                val resultIntent = Intent().apply {
                    putExtra(MainActivity.EXTRA_USERNAME, etUsername.text.toString())
                    putExtra(MainActivity.EXTRA_NAME, etName.text.toString())
                    putExtra(MainActivity.EXTRA_PRONOUN, etPronoun.text.toString())
                    putExtra(MainActivity.EXTRA_BIO, etBio.text.toString())
                    putExtra(MainActivity.EXTRA_LINK, etLink.text.toString())
                    putExtra(MainActivity.EXTRA_IMAGE_URI, selectedImageUriString)
                }

                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }
    }
}