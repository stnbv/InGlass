package com.inglass.android.domain.repository

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.inglass.android.BuildConfig
import com.inglass.android.domain.models.PersonalInformationModel
import com.inglass.android.domain.repository.interfaces.IPreferencesRepository
import com.inglass.android.utils.helpers.fromJson
import com.inglass.android.utils.helpers.toJson

private const val PREFS = "in_glass_prefs"
private const val AUTH_DATA_KEY = "auth_data"
private const val IS_ONBOARD_KEY = "is_onboard"
private const val USER_KEY = "user_key"
private const val BASE_URL_KEY = "base_url_key"
private const val LAST_RECEIVED_DATA_KEY = "last_received_data_key"

class PreferencesRepository(context: Context) : IPreferencesRepository {

    private val settings = EncryptedSharedPreferences.create(
        context,
        PREFS,
        MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override var baseUrl: String
        get() = settings.getString(BASE_URL_KEY, null) ?: BuildConfig.BASE_URL
        set(value) = settings.edit().putString(BASE_URL_KEY, value).apply()

    override var token: String?
        get() = settings.getString(AUTH_DATA_KEY, null)
        set(value) = settings.edit().putString(AUTH_DATA_KEY, value).apply()

    override var isOnboardNotPassed: Boolean
        get() = settings.getBoolean(IS_ONBOARD_KEY, true)
        set(value) = settings.edit().putBoolean(IS_ONBOARD_KEY, value).apply()

    override var user: PersonalInformationModel?
        get() = settings.getString(USER_KEY, null).fromJson()
        set(value) = settings.edit().putString(USER_KEY, value.toJson()).apply()

    override var lastReceivedData: Long
        //300000 = 5 минут в милисекундах
        get() = settings.getLong(LAST_RECEIVED_DATA_KEY, System.currentTimeMillis() - 300000)
        set(value) = settings.edit().putLong(LAST_RECEIVED_DATA_KEY, value).apply()

    @Synchronized
    override suspend fun clear() {
        settings.edit {
            remove(AUTH_DATA_KEY)
            remove(IS_ONBOARD_KEY)
            remove(USER_KEY)
        }
    }
}
