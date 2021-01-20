package com.stenleone.stanleysfilm.viewModel.masterDetails

import android.app.Dialog
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stenleone.stanleysfilm.ui.dialog.UnSupportVersionDialog

class DialogControllerViewModel : ViewModel() {

    val title = MutableLiveData<String>()
    val subTitle = MutableLiveData<String>()
    val buttonOkText = MutableLiveData<String>()
    val clickOk = MutableLiveData<(dialog: Dialog?) -> Unit>()

    fun startUnSupportDialog(title: String, subTitle: String, buttonOkText: String, clickOk: (dialog: Dialog?) -> Unit, fragmentManager: FragmentManager) {
        this.title.postValue(title)
        this.subTitle.postValue(subTitle)
        this.buttonOkText.postValue(buttonOkText)
        this.clickOk.postValue (clickOk)

        UnSupportVersionDialog.show(fragmentManager)
    }
}