package com.stenleone.stanleysfilm.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.databinding.FragmentVideoBinding
import com.stenleone.stanleysfilm.ui.activity.MainActivity
import com.stenleone.stanleysfilm.ui.fragment.base.BaseFragment
import com.stenleone.stanleysfilm.util.constant.BindingConstant
import com.stenleone.stanleysfilm.util.extencial.throttleFirst
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import kotlin.math.abs

class VideoFragment : BaseFragment() {

    companion object {

        const val SAVE_STATE_FRAGMENT = "video_fragment_state"
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

        setupMotionLay()
        setupVideoView()
        setupClicks()
    }

    private fun setupMotionLay() {
        binding.apply {
            videoMotionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
                override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {
                    (activity as MainActivity).also {
                        it.binding.mainMotionLayout.progress = abs(progress)
                    }

                    if (progress < 0.5) {
                        binding.closeButton.visibility = View.VISIBLE
                    } else {
                        binding.closeButton.visibility = View.GONE
                    }
                }

                override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                }

                override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                }

                override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
                }
            })
            videoMotionLayout.transitionToEnd()
        }
    }

    private fun setupClicks() {
        binding.apply {
            closeButton.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    (requireActivity() as MainActivity).closeVideoFragment()
                }
                .launchIn(lifecycleScope)
        }
    }

    private fun setupVideoView() {
        binding.apply {
            videoView.setVideoURI(Uri.parse(videoUrl))
            val mediaController = MediaController(requireContext())
            videoView.setMediaController(mediaController)
            mediaController.setAnchorView(videoView)
            videoView.start()
        }
    }

    fun updateVideoUrl(url: String) {
        binding.apply {
            (activity as MainActivity).also {
                it.binding.mainMotionLayout.progress = 1f
            }
            videoMotionLayout.transitionToState(R.id.expanded)
            videoUrl = url
            videoView.setVideoURI(Uri.parse(videoUrl))
            val mediaController = MediaController(requireContext())
            videoView.setMediaController(mediaController)
            mediaController.setAnchorView(videoView)
            videoView.start()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        videoUrl?.let {
            outState.putString(SAVE_VIDEO_URL, it)
        }
    }
}