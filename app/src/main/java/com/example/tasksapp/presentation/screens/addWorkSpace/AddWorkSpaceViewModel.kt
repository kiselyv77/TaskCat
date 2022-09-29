package com.example.tasksapp.presentation.screens.addWorkSpace

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddWorkSpaceViewModel @Inject constructor(): ViewModel() {

    private val _state = mutableStateOf(AddWorkSpaceState())
    val state: State<AddWorkSpaceState> = _state

    fun setName(newName:String){
        _state.value = _state.value.copy(name = newName)
    }

    fun setDescription(newDescription:String){
        _state.value = _state.value.copy(description = newDescription)
    }

}