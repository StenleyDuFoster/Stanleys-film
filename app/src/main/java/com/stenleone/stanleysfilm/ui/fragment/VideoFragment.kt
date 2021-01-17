package com.stenleone.stanleysfilm.ui.fragment

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.github.vkay94.dtpv.youtube.YouTubeOverlay
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.databinding.FragmentVideoBinding
import com.stenleone.stanleysfilm.ui.activity.MainActivity
import com.stenleone.stanleysfilm.ui.fragment.base.BaseFragment
import com.stenleone.stanleysfilm.util.constant.BindingConstant
import com.stenleone.stanleysfilm.util.extencial.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import kotlin.math.abs

class VideoFragment : BaseFragment() {

    companion object {

        const val TAG = "VideoFragment"

        const val SAVE_STATE_FRAGMENT = "video_fragment_state"
        const val SAVE_POSITION_VIDEO = "video_player_position"
        const val SAVE_WINDOW_INDEX_VIDEO = "video_player_window_index"
        const val SAVE_VIDEO_URL = "video_url"

        fun newInstance(url: String?): VideoFragment = VideoFragment().also { f ->
            f.videoUrl = url
        }
    }

    private lateinit var binding: FragmentVideoBinding
    private var videoUrl: String? = null

    override fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_video, container, false)
        return binding.root
    }

    override fun setup(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            videoUrl = it.getString(SAVE_VIDEO_URL)
        }

        configurationWindow()
        setupMotionLay()
        setupDoubleClick()
        setupVideoView()
        setupClicks()
    }

    private fun configurationWindow() {
        requireActivity().apply {
            if (getOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                hideStatusBar()
                hideSystemButtons()
            } else {
                showStatusBar()
                showSystemButtons()
            }
        }
    }

    private fun setupMotionLay() {
        binding.apply {
            videoMotionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
                val controls = videoView.findViewById<ConstraintLayout>(R.id.exoPlayerControlsView)
                override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {
                    (activity as MainActivity).also {
                        it.binding.mainMotionLayout.progress = abs(progress)
                    }

                    if (progress < 0.5) {
                        controls.visibility = View.GONE
                        closeButton.visibility = View.VISIBLE
                    } else {
                        closeButton.visibility = View.GONE
                        controls.visibility = View.VISIBLE
                    }
                }

                override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                }

                override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                }

                override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
                }
            })
            videoMotionLayout.transitionToState(R.id.expanded)

            if (resources.configuration.screenWidthDp > resources.configuration.screenHeightDp) {
                (activity as MainActivity).also {
                    it.binding.mainMotionLayout.progress = 1f
                }
            }
        }
    }

    private fun setupDoubleClick() {
        binding.apply {
            playerDoubleTapContainer.setPlayerView(videoView)
            playerDoubleTapContainer.animationDuration = 500
            playerDoubleTapContainer.fastForwardRewindDuration = 15000

            playerDoubleTapContainer.performListener = object : YouTubeOverlay.PerformListener {
                override fun onAnimationStart() {
                    videoView.showController()
                    playerDoubleTapContainer.visibility = View.VISIBLE
                }

                override fun onAnimationEnd() {
                    playerDoubleTapContainer.visibility = View.GONE
                }
            }

            videoView.activateDoubleTap(true)
                .setDoubleTapDelay(800)
                .setDoubleTapListener(playerDoubleTapContainer)
        }
    }

    private fun setupClicks() {
        binding.apply {
            val playControlButton = videoView.findViewById<ImageButton>(R.id.playPauseButton)
            val fullScreenButtonControls = videoView.findViewById<ImageButton>(R.id.fullscreenButton)

            closeButton.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    (requireActivity() as MainActivity).closeVideoFragment()
                }
                .launchIn(lifecycleScope)


            playControlButton.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    val playWhenReady = (videoView.player as SimpleExoPlayer).playWhenReady
                    if (playWhenReady) {
                        playControlButton.setImageResource(R.drawable.ic_player_play)
                    } else {
                        playControlButton.setImageResource(R.drawable.ic_player_pause)
                    }
                    (videoView.player as SimpleExoPlayer).playWhenReady = !playWhenReady
                }
                .launchIn(lifecycleScope)

            fullScreenButtonControls.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    if (requireActivity().getOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    } else {
                        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    }
                }
                .launchIn(lifecycleScope)
        }
    }

    private fun setupVideoView() {
        binding.apply {
            val player = SimpleExoPlayer.Builder(requireContext()).build()
            videoView.player = player
            playerDoubleTapContainer.setPlayer(player)
            reSetupPlayer()
        }
    }

    fun updateVideoUrl(url: String) {
        binding.apply {
            videoUrl = url
            reSetupPlayer()
        }
    }

    private fun reSetupPlayer() {
        binding.apply {
            val userAgent = Util.getUserAgent(requireContext(), requireContext().getString(R.string.app_name))
            val mediaSource = ProgressiveMediaSource
                .Factory(DefaultDataSourceFactory(context, userAgent))
                .createMediaSource(Uri.parse(videoUrl))

            (videoView.player as SimpleExoPlayer).apply {
                prepare(mediaSource)
                playWhenReady = true
            }
        }
    }

    override fun onPause() {
        super.onPause()
        binding.videoView.player?.playWhenReady = false
    }

    override fun onResume() {
        super.onResume()
        binding.videoView.player?.playWhenReady = true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        videoUrl?.let {
            outState.putString(SAVE_VIDEO_URL, it)
        }
        (binding.videoView.player as SimpleExoPlayer).let {
            outState.putLong(SAVE_POSITION_VIDEO, it.contentPosition)
            outState.putInt(SAVE_WINDOW_INDEX_VIDEO, it.currentWindowIndex)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        binding.apply {
            videoView.player?.seekTo(savedInstanceState?.getLong(SAVE_POSITION_VIDEO) ?: 0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).also {
            it.binding.mainMotionLayout.progress = 0f
            if (it.getOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                it.showStatusBar()
                it.showSystemButtons()
            }
        }
    }
}