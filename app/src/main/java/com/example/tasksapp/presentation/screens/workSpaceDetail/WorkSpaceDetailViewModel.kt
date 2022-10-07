package com.example.tasksapp.presentation.screens.workSpaceDetail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WorkSpaceDetailViewModel @Inject constructor(

) : ViewModel() {
    private val _state = mutableStateOf(WorkSpaceDetailState())
    val state: State<WorkSpaceDetailState> = _state
}