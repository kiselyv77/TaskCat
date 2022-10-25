package com.example.tasksapp.presentation.screens.login

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasksapp.domain.use_cases.LoginUser
import com.example.tasksapp.domain.use_cases.SaveToken
import com.example.tasksapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserUseCase: LoginUser,
    private val saveTokenUseCase: SaveToken
) : ViewModel() {

    private val _state = mutableStateOf(LoginState())
    val state: State<LoginState> = _state

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.SetLogin -> {
                _state.value = _state.value.copy(login = event.newValue)
            }
            is LoginEvent.SetPassword -> {
                _state.value = _state.value.copy(password = event.newValue)
            }
            is LoginEvent.Login -> {
                if (_state.value.login.isNotEmpty()
                    && _state.value.password.isNotEmpty()
                ) {
                    loginUser()
                } else {
                    _state.value = _state.value.copy(error = "Заполните все поля!")
                }
            }
        }

    }

    private fun loginUser() {
        viewModelScope.launch {
            loginUserUseCase(_state.value.login, _state.value.password).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { token ->
                            _state.value = _state.value.copy(
                                isLoading = false
                            )
                            saveTokenUseCase(token).collect {
                                _state.value = _state.value.copy(
                                    error = result.message ?: "",
                                    isLoading = false,
                                    token = token
                                )
                            }
                        }
                    }
                    is Resource.Error -> {
                        _state.value =
                            _state.value.copy(error = result.message ?: "", isLoading = false)
                        Log.d("Resource.Error", result.message.toString())
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