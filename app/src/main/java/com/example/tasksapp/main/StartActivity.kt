package com.example.tasksapp.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.tasksapp.App
import com.example.tasksapp.presentation.theme.TasksAppTheme
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
}