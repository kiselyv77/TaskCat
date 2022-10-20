package com.example.tasksapp.main

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasksapp.data.local.global.Token
import com.example.tasksapp.domain.use_cases.GetToken
import com.example.tasksapp.domain.use_cases.SetUserStatus
import com.example.tasksapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val getTokenUseCase: GetToken,
    private val setUserStatusUseCase: SetUserStatus
) : ViewModel() {
    private val _state = mutableStateOf(StartState())
    val state: State<StartState> = _state

    init {
        checkToken()
    }

    fun setUserStatus(status: String) {
        viewModelScope.launch {
            setUserStatusUseCase(Token.token, status).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        Log.d("StartViewModel", result.data.toString())
                    }
                    is Resource.Error -> {
                        Log.d("StartViewModel", result.data.toString())
                    }
                    is Resource.Loading -> {
                        Log.d("StartViewModel", result.data.toString())
                    }
                }
            }
        }
    }

    private fun checkToken() {
        viewModelScope.launch {
            getTokenUseCase().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val token = result.data
                        val isTokenValid = token?.isNotEmpty() ?: false
                        if (isTokenValid) {
                            Token.token = token!!
                        }
                        Log.d("GettokenFromRoom", isTokenValid.toString())
                        _state.value = _state.value.copy(
                            isTokenValid = isTokenValid,
                            isLoading = false,
                            error = result.message ?: ""
                        )
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            isTokenValid = false,
                            isLoading = false,
                            error = result.message ?: ""
                        )
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