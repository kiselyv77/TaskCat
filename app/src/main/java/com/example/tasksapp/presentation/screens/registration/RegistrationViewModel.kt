package com.example.tasksapp.presentation.screens.registration

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasksapp.domain.use_cases.RegisterNewUser
import com.example.tasksapp.domain.use_cases.SaveToken
import com.example.tasksapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registerNewUserUseCase: RegisterNewUser,
    private val saveTokenUseCase: SaveToken
) : ViewModel() {
    private val _state = mutableStateOf(RegistrationState())
    val state: State<RegistrationState> = _state

    fun onEvent(event: RegistrationEvent) {
        when (event) {
            is RegistrationEvent.SetName -> {
                _state.value = _state.value.copy(name = event.newValue)
            }
            is RegistrationEvent.SetLogin -> {
                _state.value = _state.value.copy(login = event.newValue)
            }
            is RegistrationEvent.SetPassword -> {
                _state.value = _state.value.copy(password = event.newValue)
            }
            is RegistrationEvent.Register -> {
                if (_state.value.name.isNotEmpty()
                    && _state.value.login.isNotEmpty()
                    && _state.value.password.isNotEmpty()
                ) {
                    registerNewUser()
                }
                else{
                    _state.value = _state.value.copy(error = "Заполните все поля!")
                }

            }
        }
    }

    private fun registerNewUser() {
        viewModelScope.launch {
            registerNewUserUseCase(
                _state.value.name,
                _state.value.login,
                _state.value.password
            ).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { token ->
                            _state.value = _state.value.copy(
                                isLoading = false,
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