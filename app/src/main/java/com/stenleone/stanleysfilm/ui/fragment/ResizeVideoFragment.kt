package com.stenleone.stanleysfilm.ui.fragment

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.vkay94.dtpv.youtube.YouTubeOverlay
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.material.card.MaterialCardView
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.databinding.FragmentVideoBinding
import com.stenleone.stanleysfilm.interfaces.ItemClickParcelable
import com.stenleone.stanleysfilm.network.entity.movie.MovieUI
import com.stenleone.stanleysfilm.ui.activity.MainActivity
import com.stenleone.stanleysfilm.ui.adapter.recyclerView.ListMovieAdapter
import com.stenleone.stanleysfilm.ui.fragment.base.BaseFragment
import com.stenleone.stanleysfilm.util.constant.BindingConstant
import com.stenleone.stanleysfilm.util.extencial.*
import com.stenleone.stanleysfilm.viewModel.network.VideoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import javax.inject.Inject

@AndroidEntryPoint
class ResizeVideoFragment : BaseFragment() {

    companion object {

        const val TAG = "VideoFragment"

        const val SAVE_STATE_FRAGMENT = "video_fragment_state"
        const val SAVE_POSITION_VIDEO = "video_player_position"
        const val SAVE_WINDOW_INDEX_VIDEO = "video_player_window_index"
        const val SAVE_VIDEO_URL = "video_url"
        const val SAVE_MOVIE = "movie_obj"

        fun newInstance(url: String?, movieUI: MovieUI): ResizeVideoFragment = ResizeVideoFragment().also { f ->
            f.videoUrl = url
            f.movieUI = movieUI
        }
    }

    private lateinit var binding: FragmentVideoBinding
    private val viewModel: VideoViewModel by viewModels()
    private var videoUrl: String? = null
    private var movieUI: MovieUI? = null
    var fullscreen = false

    @Inject
    lateinit var movieAdapterAdapter: ListMovieAdapter

    override fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_video, container, false)
        return binding.root
    }

    override fun setup(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            videoUrl = it.getString(SAVE_VIDEO_URL)
            movieUI = it.getParcelable(SAVE_MOVIE)
        }

        configurationWindow()
        setupMotionLay()
        setupRecyclerView()
        setupViewModelCallBack()
        setupDoubleClick()
        setupVideoView()
        setupClicks()
    }

    private fun configurationWindow() {
        requireActivity().apply {
            if (getOrientation() == Configuration.ORIENTATION_LANDSCAPE) {
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
                        it.binding.mainMotionLayout.progress = if (progress < 0.01f) {
                            0f
                        } else {
                            progress
                        }
                    }

                    if (progress < 0.5) {
                        fullscreen = false
                        controls.visibility = View.GONE
                        closeButton.visibility = View.VISIBLE
                    } else {
                        fullscreen = true
                        closeButton.visibility = View.GONE
                        controls.visibility = View.VISIBLE
                    }
                }

                override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                    when (id) {
                        R.id.collapsed -> {
                            fullscreen = false
                            (activity as MainActivity).also {
                                it.binding.mainMotionLayout.progress = 0f
                            }
                        }
                        R.id.expanded -> {
                            fullscreen = true
                            (activity as MainActivity).also {
                                it.binding.mainMotionLayout.progress = 1f
                            }
                        }
                    }
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

    private fun setupRecyclerView() {
        val filmClickListener = object : ItemClickParcelable {
            override fun click(item: Parcelable) {
                if (item is MovieUI) {

                    binding.videoMotionLayout.transitionToState(R.id.collapsed)

                    (requireActivity() as MainActivity).openFilmFromResizeFragment(item)
                }
            }
        }
        binding.apply {
            movieAdapterAdapter.also {
                it.listener = filmClickListener
                it.typeHolder = ListMovieAdapter.TYPE_VERTICAL
            }
            recyclerView.adapter = movieAdapterAdapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
    }

    private fun setupViewModelCallBack() {
        viewModel.apply {
            if (movieList.value?.movies?.size ?: 0 == 0) {
                getSimilarMovieList(movieUI?.id ?: 0, 1)
            }

            movieList.observe(viewLifecycleOwner) {
                movieAdapterAdapter.itemList.clear()
                movieAdapterAdapter.itemList.addAll(it.movies)
                movieAdapterAdapter.notifyDataSetChanged()
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
            val playControlButton = videoView.findViewById<MaterialCardView>(R.id.playPauseButton)
            val fullScreenButtonControls = videoView.findViewById<MaterialCardView>(R.id.fullscreenButton)
            title.text = movieUI?.title ?: movieUI?.originalTitle

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
                        (playControlButton.getChildAt(0) as ImageView).setImageResource(R.drawable.ic_player_play)
                    } else {
                        (playControlButton.getChildAt(0) as ImageView).setImageResource(R.drawable.ic_player_pause)
                    }
                    (videoView.player as SimpleExoPlayer).playWhenReady = !playWhenReady
                }
                .launchIn(lifecycleScope)

            fullScreenButtonControls.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    if (requireActivity().getOrientation() == Configuration.ORIENTATION_PORTRAIT) {
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
            videoView.setOnTouchListener { view, event ->
                videoMotionLayout.onTouchEvent(event)
                return@setOnTouchListener false
            }
            playerDoubleTapContainer.setOnTouchListener { view, event ->
                videoMotionLayout.onTouchEvent(event)
                return@setOnTouchListener false
            }
            val player = SimpleExoPlayer.Builder(requireContext()).build()
            videoView.player = player
            playerDoubleTapContainer.setPlayer(player)
            reSetupPlayer()
        }
    }

    fun updateVideoUrl(url: String, movieUI: MovieUI) {
        viewModel.getSimilarMovieList(movieUI.id, 1)
        binding.apply {
            videoMotionLayout.transitionToState(R.id.expanded)
            this@ResizeVideoFragment.movieUI = movieUI
            title.text = movieUI.title
            videoUrl = url
            reSetupPlayer()
        }
    }

    fun collapseDown() {
        binding.apply {
            requireActivity().apply {
                if (getOrientation() == Configuration.ORIENTATION_LANDSCAPE) {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                } else {
                    videoMotionLayout.transitionToState(R.id.collapsed)
                }
            }

        }
    }

    private fun reSetupPlayer() {
        binding.apply {
            val userAgent = Util.getUserAgent(requireContext(), requireContext().getString(R.string.app_name))
            val mediaSource = ProgressiveMediaSource
                .Factory(DefaultDataSourceFactory(requireContext(), userAgent))
                .createMediaSource(MediaItem.fromUri(Uri.parse(videoUrl)))

            (videoView.player as SimpleExoPlayer).apply {
                addMediaSource(mediaSource)
                prepare()
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
        movieUI?.let {
            outState.putParcelable(SAVE_MOVIE, it)
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
            if (it.getOrientation() == Configuration.ORIENTATION_LANDSCAPE) {
                it.showStatusBar()
                it.showSystemButtons()
            }
        }
    }
}