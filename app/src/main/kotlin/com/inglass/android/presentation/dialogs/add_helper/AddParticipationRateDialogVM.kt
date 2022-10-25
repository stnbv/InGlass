package com.inglass.android.presentation.dialogs.add_helper

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.inglass.android.presentation.helpers.HelperItemData
import com.inglass.android.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow

private val DIVIDER = BigDecimal("0.05")

@HiltViewModel
class AddParticipationRateDialogVM @Inject constructor(
    stateHandle: SavedStateHandle
) : BaseViewModel() {

    var navArgs = AddParticipationRateDialogFragmentArgs.fromSavedStateHandle(stateHandle)

    val participation = MutableStateFlow("")
    val isSaveButtonEnable = MutableStateFlow(false)
    val fullHelperInfo = MutableLiveData<HelperItemData>()

    fun checkFields() {
        if (participation.value == "") return
        val currentParticipation = BigDecimal(participation.value)
        val isParticipationFilledCorrect = currentParticipation.compareTo(ZERO) != 0 &&
                currentParticipation.remainder(DIVIDER).compareTo(ZERO) == 0

        isSaveButtonEnable.value = isParticipationFilledCorrect
    }

    fun saveParticipationRate() {
        fullHelperInfo.postValue(
            HelperItemData(
                navArgs.employee.id,
                navArgs.employee.fullName,
                BigDecimal(participation.value)
            )
        )
    }

    fun selectedChipText(text: String) {
        participation.tryEmit(text)
        checkFields()
    }
}
