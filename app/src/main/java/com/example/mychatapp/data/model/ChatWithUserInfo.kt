package com.example.mychatapp.data.model

import com.example.mychatapp.data.db.entity.Chat
import com.example.mychatapp.data.db.entity.UserInfo

data class ChatWithUserInfo(
    var mChat: Chat,
    var mUserInfo: UserInfo
)
