package com.stenleone.stanleysfilm.ui.dialog

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.databinding.DialogMessageBinding
import com.stenleone.stanleysfilm.util.constant.BindingConstant
import com.stenleone.stanleysfilm.util.extencial.throttleFirst
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class UnSupportVersionDialog : DialogFragment() {

    companion object {
        private const val TAG = "UnSupportVersionDialog"

        fun show(fragmentManager: FragmentManager) {
            val fragment = UnSupportVersionDialog()
            fragment.show(fragmentManager, TAG)
        }
    }

    lateinit var binding: DialogMessageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
        dialog?.setCanceledOnTouchOutside(false)
        isCancelable = false
        binding = DialogMessageBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            title = getString(R.string.version_app_title)
            subTitle = getString(R.string.version_app_sub_title)
            buttonOk = getString(R.string.ok)
            buttonCancel = getString(R.string.version_update)

            buttonOkCard.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    requireActivity().finish()
                }
                .launchIn(lifecycleScope)

            buttonCancelCard.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.app_telegram_link))).also {
                        startActivity(it)
                    }
                }
                .launchIn(lifecycleScope)
        }
    }
}