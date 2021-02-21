package com.stenleone.stanleysfilm.ui.dialog

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.databinding.DialogMessageBinding
import com.stenleone.stanleysfilm.databinding.DialogRateMovieBinding
import com.stenleone.stanleysfilm.util.constant.BindingConstant
import com.stenleone.stanleysfilm.util.extencial.throttleFirst
import com.stenleone.stanleysfilm.viewModel.network.RateMovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

@AndroidEntryPoint
class RateMovieDialog : DialogFragment() {

    companion object {
        private const val TAG = "RateMovieDialog"
        private const val TITLE = "title"
        private const val ID = "id"

        fun show(fragmentManager: FragmentManager, movieTitle: String, movieId: Int) {
            val fragment = RateMovieDialog()
            val bundle = Bundle()
            bundle.putString(TITLE, movieTitle)
            bundle.putInt(ID, movieId)
            fragment.arguments = bundle
            fragment.show(fragmentManager, TAG)
        }
    }

    lateinit var binding: DialogRateMovieBinding
    private val viewModel: RateMovieViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
        binding = DialogRateMovieBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            title = arguments?.getString(TITLE)

            rateBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    rateText.text = (progress/10).toString()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }
            })

            buttonOkCard.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    arguments?.getInt(ID)?.let {
                        viewModel.rateMovie(it, (rateBar.progress/10).toDouble())
                    }
                }
                .launchIn(lifecycleScope)

            viewModel.success.observe(viewLifecycleOwner) {
                if (it) {
                    Toast.makeText(requireContext(), getString(R.string.rate_send, (rateBar.progress/10).toString(), arguments?.getString(TITLE)), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), getString(R.string.rate_error_send), Toast.LENGTH_SHORT).show()
                }
                dialog?.dismiss()
            }

        }
    }
}