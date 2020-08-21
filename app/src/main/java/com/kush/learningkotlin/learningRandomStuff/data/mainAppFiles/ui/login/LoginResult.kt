package com.kush.learningkotlin.learningRandomStuff.data.mainAppFiles.ui.login

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: LoggedInUserView? = null,
    val error: Int? = null
)