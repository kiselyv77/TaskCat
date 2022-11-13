package com.example.tasksapp.presentation.screens.messenger

import com.example.tasksapp.domain.model.MessageModel
import com.example.tasksapp.domain.model.UserModel

data class MessengerState(
    val my: UserModel = UserModel("", "", "", ""),
    val messagesList: List<MessageModel> = listOf<MessageModel>(),
    val inputMessage: String = "",
    val send: Boolean = false,
    val sendVoice: Boolean = false,
    val voiceFile: String = "",
    val isLoading: Boolean = true,
    val isVoiceRecording: Boolean = false,
    val playingVoiceMessageId: String = "",
    val playingVoiceMessageProgress: Float = 0F,
    val voiceRecordAmplitude:Int = 0,
    val voiceRecordTime:Long = 0,
    val error: String = "",
)
