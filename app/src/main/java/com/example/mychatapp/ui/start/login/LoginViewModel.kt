package com.example.mychatapp.ui.start.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mychatapp.data.model.Login
import com.example.mychatapp.data.db.repository.AuthRepository
import com.example.mychatapp.ui.DefaultViewModel
import com.example.mychatapp.data.Event
import com.example.mychatapp.data.Result
import com.example.mychatapp.util.isEmailValid
import com.example.mychatapp.util.isTextValid
import com.google.firebase.auth.FirebaseUser

class LoginViewModel : DefaultViewModel() {

    private val authRepository = AuthRepository()
    private val _isLoggedInEvent = MutableLiveData<Event<FirebaseUser>>()

    val isLoggedInEvent: LiveData<Event<FirebaseUser>> = _isLoggedInEvent
    val emailText = MutableLiveData<String>() // Two way
    val passwordText = MutableLiveData<String>() // Two way
    val isLoggingIn = MutableLiveData<Boolean>() // Two way

    private fun login() {
        isLoggingIn.value = true
        val login = Login(emailText.value!!, passwordText.value!!)

        authRepository.loginUser(login) { result: Result<FirebaseUser> ->
            onResult(null, result)
            if (result is Result.Success) _isLoggedInEvent.value = Event(result.data!!)
            if (result is Result.Success || result is Result.Error) isLoggingIn.value = false
        }
    }

    fun loginPressed() {
        if (!isEmailValid(emailText.value.toString())) {
            mSnackBarText.value = Event("Invalid email format")
            return
        }
        if (!isTextValid(6, passwordText.value)) {
            mSnackBarText.value = Event("Password is too short")
            return
        }

        login()
    }
}