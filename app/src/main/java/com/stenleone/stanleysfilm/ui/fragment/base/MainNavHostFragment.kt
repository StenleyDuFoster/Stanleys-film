package com.stenleone.stanleysfilm.ui.fragment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.goodbarber.sharjah.eventbus.MessageEventBus
import com.goodbarber.sharjah.eventbus.eventmodels.OpenFilmEvent
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.databinding.FragmentMainNavHostBinding
import com.stenleone.stanleysfilm.interfaces.FragmentWithNavController
import com.stenleone.stanleysfilm.ui.fragment.FilmFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainNavHostFragment : BaseFragment(), FragmentWithNavController {

    override fun getNavControllerId(): Int {
        return R.id.navHostMainFragment
    }

    override fun popToStart() {
        Navigation.findNavController(requireActivity(), getNavControllerId()).popBackStack()
    }

    private lateinit var binding: FragmentMainNavHostBinding

    override fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_nav_host, container, false)
        return binding.root
    }

    override fun setup(savedInstanceState: Bundle?) {
        binding.apply {
            var subscription = MessageEventBus.asChannel<OpenFilmEvent>()

            lifecycleScope.launch {
                subscription.consumeEach { event ->
                    if (event is OpenFilmEvent) {
                        Navigation.findNavController(requireActivity(), getNavControllerId()).navigate(
                            FilmFragmentDirections.actionGlobalFilmFragment(
                                event.movie
                            )
                        )
                    }
                }
            }
        }
    }
}