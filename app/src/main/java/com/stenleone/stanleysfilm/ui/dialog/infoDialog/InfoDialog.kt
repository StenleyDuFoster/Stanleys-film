package com.stenleone.stanleysfilm.ui.dialog.infoDialog

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

class InfoDialog : DialogFragment() {

    companion object {

        const val UPDATE_DIALOG_ACTION = 2

        private const val TAG = "UnSupportVersionDialog"
        private const val SAVE_TITLE = "save_title"
        private const val SAVE_SUB_TITLE = "save_sub_title"
        private const val SAVE_BUTTON_OK_TEXT = "save_button_ok_text"
        private const val SAVE_BUTTON_CANCEL_TEXT = "save_button_cancel_text"
        private const val INFO_DIALOG_ACTION = "save_action"

        fun show(fragmentManager: FragmentManager, title: String?, subTitle: String?, buttonOkName: String, buttonCancelName: String? = null, action: Int = 0) {
            val fragment = InfoDialog()
            val bundle = Bundle()
            bundle.putString(SAVE_TITLE, title)
            bundle.putString(SAVE_SUB_TITLE, subTitle)
            bundle.putString(SAVE_BUTTON_OK_TEXT, buttonOkName)
            bundle.putString(SAVE_BUTTON_CANCEL_TEXT, buttonCancelName)
            bundle.putInt(INFO_DIALOG_ACTION, action)
            fragment.arguments = bundle
            fragment.show(fragmentManager, TAG)
        }
    }

    lateinit var binding: DialogMessageBinding
    var title: String? = null
    private var subTitle: String? = null
    private var okButtonText: String? = null
    private var cancelButtonText: String? = null
    private var action: Int = 0

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

        arguments?.let {
            title = it.getString(SAVE_TITLE)
            subTitle = it.getString(SAVE_SUB_TITLE)
            okButtonText = it.getString(SAVE_BUTTON_OK_TEXT)
            cancelButtonText = it.getString(SAVE_BUTTON_CANCEL_TEXT)
            action = it.getInt(INFO_DIALOG_ACTION) ?: 0
        }

        binding.apply {
            title = this@InfoDialog.title
            subTitle = this@InfoDialog.subTitle
            buttonOk = this@InfoDialog.okButtonText
            buttonCancel = this@InfoDialog.cancelButtonText

            buttonOkCard.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    if (parentFragment is InfoDialogCallBack) {
                        (parentFragment as? InfoDialogCallBack?)?.infoDialogOkClick(action)
                    } else if (requireActivity() is InfoDialogCallBack) {
                        (requireActivity() as? InfoDialogCallBack?)?.infoDialogOkClick(action)
                    }
                    dismiss()
                }
                .launchIn(lifecycleScope)

            buttonCancelCard.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    if (parentFragment is InfoDialogCallBack) {
                        (parentFragment as? InfoDialogCallBack?)?.infoDialogCancelClick(action)
                    } else if (requireActivity() is InfoDialogCallBack) {
                        (requireActivity() as? InfoDialogCallBack?)?.infoDialogCancelClick(action)
                    }
                    dismiss()
                }
                .launchIn(lifecycleScope)
        }
    }
}