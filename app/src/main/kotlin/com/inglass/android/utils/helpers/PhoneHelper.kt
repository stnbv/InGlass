package com.inglass.android.utils.helpers

import com.inglass.android.domain.models.Country.BELARUS
import com.inglass.android.domain.models.Country.KAZAKHSTAN
import com.inglass.android.domain.models.Country.RUSSIA
import com.inglass.android.domain.models.Country.UKRAINE
import com.inglass.android.domain.models.Country.USA

fun getPhone(code: String?, phone: String?): Long {
    if (code.isNullOrBlank() || phone.isNullOrBlank()) return 0
    val codeNumber = code.filter { it.isDigit() }
    val phoneNumber = phone.filter { it.isDigit() }
    return (codeNumber + phoneNumber).toLong()
}

fun getCountryByName(countryName: String?) = when (countryName) {
    RUSSIA.countryTag -> RUSSIA
    UKRAINE.countryTag -> UKRAINE
    BELARUS.countryTag -> BELARUS
    KAZAKHSTAN.countryTag -> KAZAKHSTAN
    USA.countryTag -> USA
    else -> null
}
