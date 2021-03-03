package com.stenleone.stanleysfilm.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.databinding.ActivityYouTubePlayerBinding

class YoutubeFullScreenActivity : AppCompatActivity() {

  companion object {

    fun start(context: Context, videoId: String, fromSecond: Float) {
      val intent = Intent(context, YoutubeFullScreenActivity::class.java)
      intent.putExtra("videoId", videoId)
      intent.putExtra("fromSecond", fromSecond)
      context.startActivity(intent)
    }
  }

  private lateinit var binding: ActivityYouTubePlayerBinding
  private var videoId: String? = null
  private var fromSecond: Float? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_you_tube_player)
    binding.lifecycleOwner = this

    parseIntent()
    initYouTubePlayer()
  }

  private fun parseIntent() {
    videoId = intent?.getStringExtra("videoId")
    fromSecond = intent?.getFloatExtra("fromSecond", 0f) ?: 0f
    if (videoId.isNullOrEmpty()) {
      finish()
    }
  }

  private fun initYouTubePlayer() {

    binding.apply {
      lifecycle.addObserver(youtubePlayerView)

      youtubePlayerView.initialize(object : AbstractYouTubePlayerListener() {
        override fun onReady(youTubePlayer: YouTubePlayer) {
          super.onReady(youTubePlayer)
          youTubePlayer.loadVideo(videoId ?: "", fromSecond ?: 0f)
        }
      }, true)

      youtubePlayerView.enterFullScreen()

      youtubePlayerView.getPlayerUiController().setFullScreenButtonClickListener {
        finish()
      }
    }
  }

  override fun onResume() {
    super.onResume()
    hideStatusBarUi()
  }

  private fun hideStatusBarUi() {
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
  }

  override fun onDestroy() {
    super.onDestroy()
    binding.youtubePlayerView.release()
  }

}