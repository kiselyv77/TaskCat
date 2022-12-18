package com.example.tasksapp.presentation.screens.usersList

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasksapp.data.local.global.Token
import com.example.tasksapp.domain.use_cases.DeleteUserFromWorkSpace
import com.example.tasksapp.domain.use_cases.GetUserByToken
import com.example.tasksapp.domain.use_cases.GetUsersFromWorkSpace
import com.example.tasksapp.domain.use_cases.SetUserStatusToWorkSpace
import com.example.tasksapp.util.Resource
import com.example.tasksapp.util.UserTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val getUsersFromWorkSpace: GetUsersFromWorkSpace,
    private val setUserStatusToWorkSpaceUseCase: SetUserStatusToWorkSpace,
    private val deleteUserFromWorkSpaceUseCase: DeleteUserFromWorkSpace,
    private val getUserByToken: GetUserByToken,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(UserListState())
    val state: State<UserListState> = _state

    init {
        getMyLogin()
        getUsers()
    }

    fun onEvent(event: UserListEvent) {
        when (event) {
            is UserListEvent.OnRefresh -> {
                getUsers()
            }
            UserListEvent.SetUserStatusToWorkSpace -> {
                setUserStatusToWorkSpace()
            }
            is UserListEvent.CloseOpenDialog -> {
                _state.value = _state.value.copy(
                    dialogState = UserItemDialogState()
                        .copy(
                            isOpen = !_state.value.dialogState.isOpen,
                            currentStatus = event.currentStatus,
                            userLogin = event.userLogin
                        )
                )
            }
            UserListEvent.DeleteUser -> {
                deleteUser()
            }
        }
    }

    private fun getUsers() {
        viewModelScope.launch {
            val workSpaceId = savedStateHandle.get<String>("id") ?: return@launch
            getUsersFromWorkSpace(Token.token, workSpaceId).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { users ->
                            _state.value = _state.value.copy(
                                usersList = users, error = result.message ?: "", isLoading = false
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.value =
                            _state.value.copy(
                                error = result.message ?: "", isLoading = false
                            )
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = result.isLoading, error = result.message ?: ""
                        )
                    }
                }

            }
        }
    }
    private fun setUserStatusToWorkSpace() {
        viewModelScope.launch {
            val workSpaceId = savedStateHandle.get<String>("id") ?: return@launch
            val userLogin = _state.value.dialogState.userLogin
            val newStatus = when(_state.value.dialogState.currentStatus) {
                UserTypes.MEMBER_TYPE -> UserTypes.ADMIN_TYPE
                UserTypes.ADMIN_TYPE -> UserTypes.MEMBER_TYPE
                else -> UserTypes.MEMBER_TYPE
            }
            setUserStatusToWorkSpaceUseCase(Token.token, userLogin, workSpaceId, newStatus).collect { result ->
                Log.d("asdasdasdadrrreeewe", result.toString())
                when (result) {
                    is Resource.Success -> {
                        result.data?.let {
                            _state.value = _state.value.copy(
                                dialogState = _state.value.dialogState.copy(
                                    isSuccess = true,
                                    isLoading = false,
                                    error = ""
                                )
                            )
                            onEvent(UserListEvent.OnRefresh)
                        }
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            dialogState = _state.value.dialogState.copy(
                                error = result.message ?: "",
                                isLoading = false
                            )
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            dialogState = _state.value.dialogState.copy(
                                isLoading = result.isLoading,
                                error = result.message ?: ""
                            )
                        )
                    }
                }
            }
        }
    }

    private fun getMyLogin() {
        viewModelScope.launch {
            getUserByToken(Token.token).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { userDto ->
                            _state.value = _state.value.copy(
                                myLogin = userDto.login,
                                error = result.message ?: "",
                                isLoading = false
                            )
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

    private fun deleteUser() {
        viewModelScope.launch {
            val workSpaceId = savedStateHandle.get<String>("id") ?: return@launch
            deleteUserFromWorkSpaceUseCase(Token.token, workSpaceId, _state.value.dialogState.userLogin).collect { result ->

                when (result) {

                    is Resource.Success -> {
                        result.data?.let {
                            _state.value = _state.value.copy(
                                dialogState = _state.value.dialogState.copy(
                                    isSuccess = true,
                                    isLoading = false,
                                    error = ""
                                )
                            )
                            onEvent(UserListEvent.OnRefresh)
                        }
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            dialogState = _state.value.dialogState.copy(
                                error = result.message ?: "",
                                isLoading = false
                            )
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            dialogState = _state.value.dialogState.copy(
                                isLoading = result.isLoading,
                                error = result.message ?: ""
                            )
                        )
                    }
                }
            }
        }
    }
}