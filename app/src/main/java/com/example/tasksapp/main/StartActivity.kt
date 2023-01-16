package com.example.tasksapp.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.tasksapp.data.local.global.Token
import com.example.tasksapp.presentation.theme.TasksAppTheme
import com.example.tasksapp.util.UserStatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: StartViewModel by viewModels()
        setContent {
            TasksAppTheme {
                App(viewModel)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val viewModel: StartViewModel by viewModels()
        if (Token.token.isNotEmpty()){
            viewModel.setUserStatus(UserStatus.ONLINE_STATUS)
        }
    }

    override fun onStop() {
        super.onStop()
        val viewModel: StartViewModel by viewModels()
        if (Token.token.isNotEmpty()){
            viewModel.setUserStatus(UserStatus.OFFLINE_STATUS)
        }
    }
}