package com.inglass.android.presentation.helpers
//
//import android.os.Parcelable
//import com.inglass.android.R
//import com.inglass.android.databinding.ItemHelperBinding
//import com.inglass.android.presentation.helpers.HelpersVM.SingleEvent
//import com.inglass.android.utils.adapter.CommonItemVM
//import java.math.BigDecimal
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.parcelize.Parcelize
//
//data class HelperItemVM(
//    val data: HelperItemData
//) : CommonItemVM<ItemHelperBinding>(R.layout.item_helper) {
//    var fullName = data.fullName
//    var participation = data.participation.toString()
//    var participationTextColor = if (data.isParticipationValid) R.color.silver else R.color.red
//
//    fun onDeleteIconClick() {
//        CoroutineScope(Dispatchers.IO).launch {
//            emitEvent(SingleEvent.OnDeleteIconClick(data))
//        }
//    }
//
//    fun onParticipationRateClick() {
//        CoroutineScope(Dispatchers.IO).launch {
//            emitEvent(SingleEvent.OnParticipationRateClick(data))
//        }
//    }
//}
//
//@Parcelize
//data class HelperItemData(
//    val id: String,
//    val fullName: String,
//    val participation: BigDecimal,
//    val isParticipationValid: Boolean = true
//) : Parcelable
