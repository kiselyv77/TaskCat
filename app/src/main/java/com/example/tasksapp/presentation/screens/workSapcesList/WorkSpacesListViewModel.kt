package com.example.tasksapp.presentation.screens.workSapcesList

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasksapp.data.local.global.Token
import com.example.tasksapp.domain.use_cases.GetWorkSpaces
import com.example.tasksapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkSpacesListViewModel @Inject constructor(
    val getWorkSpacesUseCase: GetWorkSpaces,


) : ViewModel() {
    private val _state = mutableStateOf(WorkSpacesListState())
    val state: State<WorkSpacesListState> = _state

    init {
        getWorkSpaces()
    }

    fun onEvent(event: WorkSpacesListEvent) {
        when (event) {
            is WorkSpacesListEvent.OnRefresh -> getWorkSpaces()
        }
    }

    private fun getWorkSpaces() {
        viewModelScope.launch {
            getWorkSpacesUseCase(Token.token).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { workSpacesList ->
                            _state.value = _state.value.copy(
                                workSpacesList = workSpacesList,
                                error = "",
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


}