package com.stenleone.stanleysfilm.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.databinding.HolderYouTubeVideoBinding
import com.stenleone.stanleysfilm.ui.activity.YoutubeFullScreenActivity

class YouTubeVideoFragment : Fragment() {

    companion object {

        private const val SAVE_URL = "save_url"
        private const val SAVE_TITLE = "save_title"

        fun instance(url: String, title: String): Fragment {
            val fragment = YouTubeVideoFragment()
            val bundle = Bundle()
            bundle.putString(SAVE_URL, "$url")
            bundle.putString(SAVE_TITLE, "$title")
            fragment.arguments = bundle
            return fragment
        }
    }

    lateinit var binding: HolderYouTubeVideoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.holder_you_tube_video, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            val tracker = YouTubePlayerTracker()
            getLifecycle().addObserver(youtubePlayerView)
            youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(arguments?.getString(SAVE_URL) ?: "", 0f)
                    youTubePlayer.addListener(tracker)
                    youTubePlayer.pause()
                }
            })
            youtubePlayerView.getPlayerUiController().setFullScreenButtonClickListener(object : View.OnClickListener {

                override fun onClick(v: View?) {
                    YoutubeFullScreenActivity.start(requireContext(), arguments?.getString(SAVE_URL) ?: "", tracker.currentSecond)
                }

            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (this::binding.isInitialized) {
            binding.apply {
                youtubePlayerView.release()
            }
        }
    }
}