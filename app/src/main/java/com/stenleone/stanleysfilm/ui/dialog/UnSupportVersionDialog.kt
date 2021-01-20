package com.stenleone.stanleysfilm.ui.dialog

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.stenleone.stanleysfilm.databinding.DialogMessageBinding
import com.stenleone.stanleysfilm.util.constant.BindingConstant
import com.stenleone.stanleysfilm.util.extencial.throttleFirst
import com.stenleone.stanleysfilm.viewModel.masterDetails.DialogControllerViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks


class UnSupportVersionDialog : DialogFragment() {

    companion object {

        const val TAG = "UnSupportVersionDialog"

        fun show(fragmentManager: FragmentManager) {
            val fragment = UnSupportVersionDialog()
            fragment.show(fragmentManager, TAG)
        }
    }

    lateinit var binding: DialogMessageBinding
    private val masterViewModel: DialogControllerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
        dialog?.setCanceledOnTouchOutside(false)
        binding = DialogMessageBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            title = masterViewModel.title.value
            subTitle = masterViewModel.subTitle.value
            buttonOk = masterViewModel.buttonOkText.value

            buttonOkCard.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    masterViewModel.clickOk.value?.invoke(dialog)
                }
                .launchIn(lifecycleScope)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }
}