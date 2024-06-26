package com.example.mychatapp.ui.settings

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mychatapp.data.db.entity.UserInfo
import com.example.mychatapp.data.db.remote.FirebaseReferenceValueObserver
import com.example.mychatapp.data.db.repository.AuthRepository
import com.example.mychatapp.data.db.repository.DatabaseRepository
import com.example.mychatapp.data.db.repository.StorageRepository
import com.example.mychatapp.ui.DefaultViewModel
import com.example.mychatapp.data.Event
import com.example.mychatapp.data.Result

class SettingsViewModelFactory(private val userID: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        Log.d("Check", "UserID on SettingsViewModelFactory " + userID)
        return SettingsViewModel(userID) as T
    }
}

class SettingsViewModel(private val userID: String) : DefaultViewModel() {

    private val dbRepository: DatabaseRepository = DatabaseRepository()
    private val storageRepository = StorageRepository()
    private val authRepository = AuthRepository()

    private val _userInfo: MutableLiveData<UserInfo> = MutableLiveData()
    val userInfo: LiveData<UserInfo> = _userInfo

    private val _editStatusEvent = MutableLiveData<Event<Unit>>()
    val editStatusEvent: LiveData<Event<Unit>> = _editStatusEvent

    private val _editImageEvent = MutableLiveData<Event<Unit>>()
    val editImageEvent: LiveData<Event<Unit>> = _editImageEvent

    private val _logoutEvent = MutableLiveData<Event<Unit>>()
    val logoutEvent: LiveData<Event<Unit>> = _logoutEvent

    private val firebaseReferenceObserver = FirebaseReferenceValueObserver()

    init {
        loadAndObserveUserInfo()
    }

    override fun onCleared() {
        super.onCleared()
        firebaseReferenceObserver.clear()
    }

    private fun loadAndObserveUserInfo() {
        dbRepository.loadAndObserveUserInfo(userID, firebaseReferenceObserver)
        { result: Result<UserInfo> -> onResult(_userInfo, result) }
    }

    fun changeUserStatus(status: String) {
        dbRepository.updateUserStatus(userID, status)
    }

    fun changeUserImage(byteArray: ByteArray) {
        storageRepository.updateUserProfileImage(userID, byteArray) { result: Result<Uri> ->
            onResult(null, result)
            if (result is Result.Success) {
                dbRepository.updateUserProfileImageUrl(userID, result.data.toString())
            }
        }
    }

    fun changeUserImagePressed() {
        _editImageEvent.value = Event(Unit)
    }

    fun changeUserStatusPressed() {
        _editStatusEvent.value = Event(Unit)
    }

    fun logoutUserPressed() {
        authRepository.logoutUser()
        _logoutEvent.value = Event(Unit)
    }
}

