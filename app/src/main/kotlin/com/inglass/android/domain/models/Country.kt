package com.inglass.android.domain.models

import com.inglass.android.R

enum class Country(
    val countryTag: String,
    val countryCode: String,
    val countryName: Int,
    val rawMask: String,
    val countryNameTranslations: List<String>
) {
    RUSSIA(
        "russia",
        "+ 7",
        R.string.comment_text,
        "(___) ___-__-__",
        listOf("Россия", "Russia")
    ),
    UKRAINE(
        "ukraine",
        "+ 380",
        R.string.comment_text,
        "(___) ___-___",
        listOf("Украина", "Ukraine")
    ),
    BELARUS(
        "belarus",
        "+ 375",
        R.string.comment_text,
        "(__) ___ __-__",
        listOf("Беларусь", "Belarus")
    ),
    KAZAKHSTAN(
        "kazakhstan",
        "+ 7",
        R.string.comment_text,
        "(___) ___-__-__",
        listOf("Казахстан", "Kazakhstan")
    ),
    USA(
        "usa",
        "+ 1",
        R.string.comment_text,
        "(___) ___-____",
        listOf("США", "USA")
    ),
    NO_COUNTRY("",
        "",
        0,
        "",
        listOf("")
    );
}
