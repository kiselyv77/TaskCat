package com.example.tasksapp.presentation.screens.addWorkSpace

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasksapp.data.local.global.Token
import com.example.tasksapp.domain.use_cases.AddWorkSpace
import com.example.tasksapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddWorkSpaceViewModel @Inject constructor(
    private val addWorkSpaceUseCase: AddWorkSpace
): ViewModel() {

    private val _state = mutableStateOf(AddWorkSpaceState())
    val state: State<AddWorkSpaceState> = _state

    fun onEvent(event: AddWorkSpaceEvent){
        when (event){
            is AddWorkSpaceEvent.SetName -> {
                _state.value = _state.value.copy(name = event.newName)
            }
            is AddWorkSpaceEvent.SetDescription -> {
                _state.value = _state.value.copy(description = event.newDescription)
            }
            is AddWorkSpaceEvent.AddWorkspace -> {
                if(_state.value.name.isNotEmpty()&&_state.value.description.isNotEmpty()){
                    addWorkspace()
                }
                else{
                    _state.value = _state.value.copy(error = "Заполните все поля!")
                }
            }
        }
    }


    private fun addWorkspace(){
        viewModelScope.launch {
            addWorkSpaceUseCase(
                token = Token.token,
                name = _state.value.name,
                description = _state.value.description
            ).collect{ result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let{
                            Log.d("addWorkspace", it.toString())
                            _state.value = _state.value.copy(
                                error = result.message ?: "",
                                isLoading = false,
                                isSuccess = true
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(error = result.message?: "", isLoading = false)
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = result.isLoading,
                            error = result.message ?: ""
                        )
                    }
                }
            }
        }
    }
}