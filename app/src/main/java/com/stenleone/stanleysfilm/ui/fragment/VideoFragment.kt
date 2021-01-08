package com.stenleone.stanleysfilm.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.DataBindingUtil
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.databinding.FragmentVideoBinding
import com.stenleone.stanleysfilm.ui.activity.MainActivity
import com.stenleone.stanleysfilm.ui.fragment.base.BaseFragment
import kotlin.math.abs

class VideoFragment : BaseFragment() {

    companion object {

        const val SAVE_STATE_FRAGMENT = "video_fragment_state"

        fun newInstance(url: String): VideoFragment = VideoFragment().also { f ->
            f.arguments = Bundle().also { b ->

            }
        }
    }

    private lateinit var binding: FragmentVideoBinding

    override fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_video, container, false)
        return binding.root
    }

    override fun setup(savedInstanceState: Bundle?) {
        setupMotionLay()
    }

    private fun setupMotionLay() {
        binding.apply {
            videoMotionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
                override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {
                    (activity as MainActivity).also {
                        it.binding.mainMotionLayout.progress = abs(progress)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        outState.put
    }
}