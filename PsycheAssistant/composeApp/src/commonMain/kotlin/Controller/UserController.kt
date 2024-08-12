package org.psyche.assistant.Controller

import org.psyche.assistant.Model.User.User
import org.psyche.assistant.Service.UserService


class UserController {
    private val userService = UserService()
    suspend fun getUserProfile(authToken: String): User? {
        return userService.getUserDetails(authToken)
    }

    suspend fun registerNewUser(email: String, password: String): String {
        return userService.registerUser(email, password)
    }

    suspend fun authenticateUser(email: String, password: String): String {
        return userService.loginUser(email, password)
    }

    suspend fun signOutUser() {
        userService.logoutUser()
    }
}
