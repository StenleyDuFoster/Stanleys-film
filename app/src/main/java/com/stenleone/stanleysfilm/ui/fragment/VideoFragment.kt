package com.stenleone.stanleysfilm.ui.fragment

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.vkay94.dtpv.youtube.YouTubeOverlay
import com.goodbarber.sharjah.eventbus.MessageEventBus
import com.goodbarber.sharjah.eventbus.eventmodels.OpenFilmEvent
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.databinding.FragmentVideoBinding
import com.stenleone.stanleysfilm.interfaces.ItemClickParcelable
import com.stenleone.stanleysfilm.network.entity.movie.Movie
import com.stenleone.stanleysfilm.ui.activity.MainActivity
import com.stenleone.stanleysfilm.ui.adapter.recyclerView.ListMovieAdapter
import com.stenleone.stanleysfilm.ui.fragment.base.BaseFragment
import com.stenleone.stanleysfilm.util.constant.BindingConstant
import com.stenleone.stanleysfilm.util.extencial.*
import com.stenleone.stanleysfilm.viewModel.network.VideoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks
import javax.inject.Inject
import kotlin.math.abs

@AndroidEntryPoint
class VideoFragment : BaseFragment() {

    companion object {

        const val TAG = "VideoFragment"

        const val SAVE_STATE_FRAGMENT = "video_fragment_state"
        const val SAVE_POSITION_VIDEO = "video_player_position"
        const val SAVE_WINDOW_INDEX_VIDEO = "video_player_window_index"
        const val SAVE_VIDEO_URL = "video_url"
        const val SAVE_MOVIE = "movie_obj"

        fun newInstance(url: String?, movie: Movie): VideoFragment = VideoFragment().also { f ->
            f.videoUrl = url
            f.movie = movie
        }
    }

    private lateinit var binding: FragmentVideoBinding
    private val viewModel: VideoViewModel by viewModels()
    private var videoUrl: String? = null
    private var movie: Movie? = null
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
            movie = it.getParcelable(SAVE_MOVIE)
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
//            if (getOrientation() == Configuration.ORIENTATION_LANDSCAPE) {
//                hideStatusBar()
//                hideSystemButtons()
//            } else {
//                showStatusBar()
//                showSystemButtons()
//            }
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
                        }
                        R.id.expanded -> {
                            fullscreen = true
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
                if (item is Movie) {
                    lifecycleScope.launch {
                        MessageEventBus.send(OpenFilmEvent(item))
                    }
                    binding.videoMotionLayout.transitionToState(R.id.collapsed)
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
                getSimilarMovieList(movie?.id ?: 0, 1)
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
            val playControlButton = videoView.findViewById<ImageButton>(R.id.playPauseButton)
            val fullScreenButtonControls = videoView.findViewById<ImageButton>(R.id.fullscreenButton)
            title.text = movie?.title ?: movie?.originalTitle

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
            videoView.setOnTouchListener { v, event ->
                videoMotionLayout.onTouchEvent(event)
                return@setOnTouchListener false
            }
            playerDoubleTapContainer.setOnTouchListener { v, event ->
                videoMotionLayout.onTouchEvent(event)
                return@setOnTouchListener false
            }
            val player = SimpleExoPlayer.Builder(requireContext()).build()
            videoView.player = player
            playerDoubleTapContainer.setPlayer(player)
            reSetupPlayer()
        }
    }

    fun updateVideoUrl(url: String, movie: Movie) {
        viewModel.getSimilarMovieList(movie.id, 1)
        binding.apply {
            videoMotionLayout.transitionToState(R.id.collapsed)
            this@VideoFragment.movie = movie
            title.text = movie.title
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
        movie?.let {
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