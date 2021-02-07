package com.stenleone.stanleysfilm.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.network.ApiConstant

class YoutubePlayerActivity: YouTubeBaseActivity() {

    companion object {
        fun start(context: Context?, youtubeLink: String) {
            val intent = Intent(context, YoutubePlayerActivity::class.java)
            intent.putExtra("youtubeLink", youtubeLink)
            context?.startActivity(intent)
        }
    }

    private var youtubePlayer: YouTubePlayer? = null
    private lateinit var youtubeLink: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_you_tube_player)
        youtubeLink = intent?.getStringExtra("youtubeLink") ?: ""
    }

    private fun initYoutubePlayer() {
        findViewById<YouTubePlayerView>(R.id.youtubech_parent).initialize(ApiConstant.YouTubeApi, object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(provider: YouTubePlayer.Provider?, player: YouTubePlayer?, wasRestored: Boolean) {
                if (!wasRestored) {
                    youtubePlayer = player
                    youtubePlayer?.loadVideo(youtubeLink)
                    youtubePlayer?.setShowFullscreenButton(false)
                } else {
                    youtubePlayer?.play()
                }
            }

            override fun onInitializationFailure(provider: YouTubePlayer.Provider?, errorReason: YouTubeInitializationResult?) {
                Toast.makeText(this@YoutubePlayerActivity, (errorReason.toString()), Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun hideStatusBarUi() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun onResume() {
        super.onResume()
        hideStatusBarUi()
        initYoutubePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        youtubePlayer?.release()
        youtubePlayer = null
    }

}