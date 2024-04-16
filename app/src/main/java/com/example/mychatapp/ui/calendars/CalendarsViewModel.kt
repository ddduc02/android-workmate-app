package com.example.mychatapp.ui.calendars

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mychatapp.ui.DefaultViewModel
import com.example.mychatapp.ui.chats.ChatsViewModel


class CalendarsViewModelFactory(private val myUserID: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CalendarsViewModel(myUserID) as T
    }
}

class CalendarsViewModel(val myUserID: String) : DefaultViewModel() {
}