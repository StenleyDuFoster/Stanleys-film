package com.stenleone.stanleysfilm.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.databinding.HolderYouTubeVideoBinding
import com.stenleone.stanleysfilm.ui.activity.YoutubePlayerActivity
import com.stenleone.stanleysfilm.util.extencial.throttleClicks

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

        binding.title = arguments?.getString(SAVE_TITLE) ?: ""

        binding.clickCard.throttleClicks(
            {
                YoutubePlayerActivity.start(requireContext(), arguments?.getString(SAVE_URL) ?: "")
            }, lifecycleScope
        )
    }
}