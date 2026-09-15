package com.example.instapro

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.net.toUri
import com.example.instapro.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val sharedPref by lazy {
        getSharedPreferences(PREF_NAME, MODE_PRIVATE)
    }

    private var postsCount = 9
    private var followersCount = 434
    private var followingCount = 728
    private var profileImageUriString: String? = null

    private val editProfileLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data ?: return@registerForActivityResult

            val updatedUsername = data.getStringExtra(EXTRA_USERNAME)
            val updatedName = data.getStringExtra(EXTRA_NAME)
            val updatedPronoun = data.getStringExtra(EXTRA_PRONOUN)
            val updatedBio = data.getStringExtra(EXTRA_BIO)
            val updatedLink = data.getStringExtra(EXTRA_LINK)
            val updatedImageUri = data.getStringExtra(EXTRA_IMAGE_URI)

            sharedPref.edit {
                updatedUsername?.let { putString(KEY_USERNAME, it) }
                updatedName?.let { putString(KEY_NAME, it) }
                updatedPronoun?.let { putString(KEY_PRONOUN, it) }
                updatedBio?.let { putString(KEY_BIO, it) }
                updatedLink?.let { putString(KEY_LINK, it) }
                updatedImageUri?.let { putString(KEY_IMAGE_URI, it) }
            }

            loadProfileData()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadProfileData()
        updateStatsUI()

        with(binding) {
            btnEditProfile.setOnClickListener {
                val intentEditProfile =
                    Intent(this@MainActivity, EditProfileActivity::class.java).apply {
                        putExtra(EXTRA_USERNAME, tvUsername.text.toString())
                        putExtra(EXTRA_NAME, tvFullName.text.toString())
                        putExtra(EXTRA_PRONOUN, tvPronoun.text.toString())
                        putExtra(EXTRA_BIO, tvBio.text.toString())
                        putExtra(EXTRA_LINK, tvLink.text.toString())
                        putExtra(EXTRA_IMAGE_URI, profileImageUriString)
                    }
                editProfileLauncher.launch(intentEditProfile)
            }

            ivProfilePicture.setOnClickListener {
                val intentPhotoProfile =
                    Intent(this@MainActivity, PhotoProfileActivity::class.java).apply {
                        putExtra(EXTRA_USERNAME, tvUsername.text.toString())
                        putExtra(EXTRA_NAME, tvFullName.text.toString())
                        putExtra(EXTRA_IMAGE_URI, profileImageUriString)
                    }
                startActivity(intentPhotoProfile)
            }

            tvLink.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = ("https://" + tvLink.text.toString()).toUri()
                }
                startActivity(intent)
            }
        }
    }

    private fun loadProfileData() {
        val username = sharedPref.getString(KEY_USERNAME, getString(R.string.user_name))
        val name = sharedPref.getString(KEY_NAME, getString(R.string.full_name))
        val pronoun = sharedPref.getString(KEY_PRONOUN, getString(R.string.pronoun))
        val bio = sharedPref.getString(KEY_BIO, getString(R.string.bio))
        val link = sharedPref.getString(KEY_LINK, getString(R.string.social_link))
        profileImageUriString = sharedPref.getString(KEY_IMAGE_URI, null)

        followersCount = sharedPref.getInt(KEY_FOLLOWERS, 434)
        postsCount = sharedPref.getInt(KEY_POSTS, 9)
        followingCount = sharedPref.getInt(KEY_FOLLOWING, 728)

        with(binding) {
            tvUsername.text = username
            tvFullName.text = name
            tvPronoun.text = pronoun
            tvBio.text = bio
            tvLink.text = link

            profileImageUriString?.let {
                ivProfilePicture.setImageURI(it.toUri())
            }
        }
    }

    fun updateStatsUI() {
        binding.tvStatsPosts.text = getString(R.string.stats_posts_format, postsCount)
        binding.tvStatsFollowers.text = getString(R.string.stats_followers_format, followersCount)
        binding.tvStatsFollowing.text = getString(R.string.stats_following_format, followingCount)
    }

    companion object {
        const val PREF_NAME = "UserProfilePref"
        const val KEY_USERNAME = "key_username"
        const val KEY_NAME = "key_name"
        const val KEY_PRONOUN = "key_pronoun"
        const val KEY_BIO = "key_bio"
        const val KEY_LINK = "key_link"
        const val KEY_IMAGE_URI = "key_image_uri"
        const val KEY_FOLLOWERS = "key_followers"
        const val KEY_POSTS = "key_posts"
        const val KEY_FOLLOWING = "key_following"

        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_PRONOUN = "extra_pronoun"
        const val EXTRA_BIO = "extra_bio"
        const val EXTRA_LINK = "extra_link"
        const val EXTRA_IMAGE_URI = "extra_image_uri"
    }
}