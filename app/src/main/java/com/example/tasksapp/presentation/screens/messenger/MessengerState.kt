package com.example.tasksapp.presentation.screens.messenger

import com.example.tasksapp.domain.model.MessageModel
import com.example.tasksapp.domain.model.UserModel

data class MessengerState(
    val my: UserModel = UserModel("", "", "", ""),
    val messagesList: List<MessageModel> = listOf<MessageModel>(),
    val inputMessage: String = "",
    val isLoading: Boolean = true,
    val isVoiceRecording: Boolean = false,
    val voiceMessagesState:VoiceMessagesState = VoiceMessagesState(),
    val playingVoiceMessageProgress: Float = 0F,
    val voiceRecordAmplitude: Float = 0F,
    val voiceRecordTime: Long = 0,
    val recordError: String = "",
    val error: String = ""
)

data class VoiceMessagesState(
    val currentMessageId: String = "",
    val playing: Boolean = false
)
