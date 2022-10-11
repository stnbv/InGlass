package com.inglass.android.utils.base.simple_dialog

import androidx.lifecycle.MutableLiveData
import com.inglass.android.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SimpleDialogVM @Inject constructor() : BaseViewModel() {

    val phone = MutableLiveData("")

    fun setPhoneNumber(phoneNumber: String) {
        phone.value = phoneNumber
    }
}
