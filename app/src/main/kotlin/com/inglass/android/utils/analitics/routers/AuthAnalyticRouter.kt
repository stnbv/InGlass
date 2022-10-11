package com.inglass.android.utils.analitics.routers

import com.inglass.android.utils.analitics.AUTH_BASE_ACTION
import com.inglass.android.utils.analitics.AUTH_CATEGORY
import com.inglass.android.utils.analitics.AnalyticEvent
import com.inglass.android.utils.analitics.AnalyticMessage
import com.inglass.android.utils.analitics.AnalyticMessage.AnalyticsLogInEvent
import com.inglass.android.utils.analitics.AnalyticMessageHandler

class AuthAnalyticRouter : AnalyticMessageHandler {

    override fun handleAnalyticMessage(message: AnalyticMessage): AnalyticEvent? {
        return when (message) {

            is AnalyticsLogInEvent -> AnalyticEvent(
                AUTH_CATEGORY,
                AUTH_BASE_ACTION,
                "Успешная авторизация пользователя"
            )

            //Пример эвента, с мапой
            /*is AnalyticsLogInEvent -> {
                AnalyticEvent(
                    AUTH_CATEGORY,
                    AUTH_BASE_ACTION,
                    mapOf(
                        "id" to message.id,
                        "ClickTime" to message.clickTime
                    )
                )
            }*/

            else -> return null
        }
    }
}
