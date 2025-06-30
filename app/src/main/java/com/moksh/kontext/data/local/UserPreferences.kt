package com.moksh.kontext.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.moksh.kontext.domain.model.UserDto
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        PREFS_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveUser(user: UserDto) {
        sharedPreferences.edit {
            putString(USER_ID_KEY, user.id)
            putString(USER_EMAIL_KEY, user.email)
            putString(USER_FIRST_NAME_KEY, user.firstName)
            putString(USER_LAST_NAME_KEY, user.lastName)
            putString(USER_NICKNAME_KEY, user.nickname)
            putString(USER_AUTH_PROVIDER_KEY, user.authProvider)
            putBoolean(USER_EMAIL_VERIFIED_KEY, user.isEmailVerified)
            putString(USER_ROLE_KEY, user.role)
            putBoolean(USER_IS_ACTIVE_KEY, user.isActive)
            putString(USER_PROFILE_PICTURE_URL_KEY, user.profilePictureUrl)
        }
    }

    fun getUser(): UserDto? {
        val id = sharedPreferences.getString(USER_ID_KEY, null) ?: return null
        val email = sharedPreferences.getString(USER_EMAIL_KEY, null) ?: return null
        val firstName = sharedPreferences.getString(USER_FIRST_NAME_KEY, null) ?: return null
        val lastName = sharedPreferences.getString(USER_LAST_NAME_KEY, null) ?: return null
        val nickname = sharedPreferences.getString(USER_NICKNAME_KEY, null) ?: return null
        val authProvider = sharedPreferences.getString(USER_AUTH_PROVIDER_KEY, null) ?: return null
        val role = sharedPreferences.getString(USER_ROLE_KEY, null) ?: return null
        val profilePicture =
            sharedPreferences.getString(USER_PROFILE_PICTURE_URL_KEY, null) ?: return null

        return UserDto(
            id = id,
            email = email,
            firstName = firstName,
            lastName = lastName,
            nickname = nickname,
            authProvider = authProvider,
            isEmailVerified = sharedPreferences.getBoolean(USER_EMAIL_VERIFIED_KEY, false),
            role = role,
            isActive = sharedPreferences.getBoolean(USER_IS_ACTIVE_KEY, true),
            profilePictureUrl = profilePicture
        )
    }

    fun clearUser() {
        sharedPreferences.edit {
            remove(USER_ID_KEY)
            remove(USER_EMAIL_KEY)
            remove(USER_FIRST_NAME_KEY)
            remove(USER_LAST_NAME_KEY)
            remove(USER_NICKNAME_KEY)
            remove(USER_AUTH_PROVIDER_KEY)
            remove(USER_EMAIL_VERIFIED_KEY)
            remove(USER_ROLE_KEY)
            remove(USER_IS_ACTIVE_KEY)
            remove(USER_PROFILE_PICTURE_URL_KEY)
        }
    }

    fun hasUser(): Boolean {
        return !sharedPreferences.getString(USER_ID_KEY, null).isNullOrEmpty()
    }

    companion object {
        private const val PREFS_NAME = "kontext_user_prefs"
        private const val USER_ID_KEY = "user_id"
        private const val USER_EMAIL_KEY = "user_email"
        private const val USER_FIRST_NAME_KEY = "user_first_name"
        private const val USER_LAST_NAME_KEY = "user_last_name"
        private const val USER_NICKNAME_KEY = "user_nickname"
        private const val USER_AUTH_PROVIDER_KEY = "user_auth_provider"
        private const val USER_EMAIL_VERIFIED_KEY = "user_email_verified"
        private const val USER_ROLE_KEY = "user_role"
        private const val USER_IS_ACTIVE_KEY = "user_is_active"
        private const val USER_PROFILE_PICTURE_URL_KEY = "user_profile_picture_url"
    }
}