package com.example.tasksapp.presentation.screens.taskDetail

import android.app.Application
import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.tasksapp.data.local.global.Token
import com.example.tasksapp.data.mappers.toNoteModel
import com.example.tasksapp.data.remote.Spec
import com.example.tasksapp.data.remote.dto.NoteDTO
import com.example.tasksapp.domain.model.NoteModel
import com.example.tasksapp.domain.use_cases.*
import com.example.tasksapp.presentation.commonComponents.CustomAlertDialogState
import com.example.tasksapp.presentation.commonComponents.SetTaskStatusDialogState
import com.example.tasksapp.util.Resource
import com.example.tasksapp.util.generateRandomUUID
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject


@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val myapplication: Application,
    private val getUserByToken: GetUserByToken,
    private val getTaskByIdUseCase: GetTaskById,
    private val getNotesFromTask: GetNotesFromTask,
    private val setTaskStatusUseCase: SetTaskStatus,
    private val setTaskDeadLineUseCase: SetTaskDeadLine,
    private val getUsersFromTaskUseCase: GetUsersFromTask,
    private val getUsersFromWorkSpace: GetUsersFromWorkSpace,
    private val addUserToTaskUseCase: AddUserToTask,
    private val deleteUserFromTaskUseCase: DeleteUserFromTask,
    private val deleteTaskUseCase: DeleteTask,
    private val uploadNoteAttachmentFile: UploadNoteAttachmentFile,
    private val downloadNoteAttachmentFile: DownloadNoteAttachmentFile,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(myapplication) {
    private val _state = mutableStateOf(TaskDetailState())
    val state: State<TaskDetailState> = _state

    private var client: HttpClient = HttpClient {
        install(WebSockets) {
            contentConverter = GsonWebsocketContentConverter()
        }
    }
    private val _notesFlow = MutableSharedFlow<SendNote>()
    private var offset =
        -10 // Этот офсет будет увеличиватся на 10 при каждом новом запросе или на 1 приприеме сообщения

    init {
        getMyLogin()
        getTask()
        getUsers()
        viewModelScope.launch(Dispatchers.IO) {
            val taskId = savedStateHandle.get<String>("id") ?: return@launch
            try {
                client.ws("ws://${Spec.BASE_URL}/notes/${Token.token}/$taskId") {
                    val messageOutputRoutine = launch {
                        while (true) {
                            val note = receiveDeserialized<NoteDTO>()
                            if (!_state.value.notesList.any { it.id == note.id }) {
                                //Пришло чужое сообщение
                                offset++
                                val messageList = _state.value.notesList.toMutableList()
                                messageList.add(0, note.toNoteModel())
                                _state.value = _state.value.copy(notesList = messageList)
                            } else {
                                //Вернулось мое сообщение
                                offset++
                                val messageList = _state.value.notesList.toMutableList()
                                val messageModel = note.toNoteModel()
                                val index =
                                    messageList.indexOf(messageModel.copy(isArrived = false))
                                messageList[index] = messageModel.copy(isArrived = true)
                                _state.value = _state.value.copy(notesList = messageList)
                            }
                        }
                    }
                    val userInputRoutine = launch {
                        _notesFlow.collectLatest { sendNote ->
                            val noteId = sendNote.noteId
                            val attachmentFileName = sendNote.attachmentFileName
                            val noteDTO = NoteDTO(
                                id = noteId,
                                info = sendNote.info,
                                loginUser = _state.value.my.login,
                                userName = _state.value.my.name,
                                taskId = taskId,
                                attachmentFile = attachmentFileName,
                                timeStamp = System.currentTimeMillis().toString()
                            )
                            val messageList = _state.value.notesList.toMutableList()
                            messageList.add(
                                0,
                                noteDTO.toNoteModel().copy(isArrived = false)
                            )
                            _state.value = _state.value.copy(notesList = messageList)

                            sendSerialized(
                                noteDTO
                            )
                        }
                    }
                    userInputRoutine.join() // Wait for completion; either "exit" or error
                    messageOutputRoutine.cancelAndJoin()
                }
            } catch (e: ClosedReceiveChannelException) {
                Log.w("WebSocketSession", "Failure: ${e.message}")
            } catch (e: NoTransformationFoundException) {
                //Возникает если слишком часто открывать и закрывать чат
                // причина 429 Too Many Requests
                Log.w("WebSocketSession", "Failure: ${e.message}")
            } catch (e: Exception) {
                Log.w("WebSocketSession", "Failure: ${e.message}")
            }
        }
    }

    fun onEvent(event: TaskDetailEvent) {
        when (event) {
            is TaskDetailEvent.SendNote -> {
                val noteId = generateRandomUUID()
                val attachmentFile = _state.value.attachmentFileInfo.attachmentFile
                val fileName = _state.value.attachmentFileInfo.originalFileName
                viewModelScope.launch(Dispatchers.IO) {
                    _notesFlow.emit(
                        SendNote(
                            info = _state.value.inputText,
                            noteId = noteId,
                            attachmentFileName = fileName
                        )
                    )
                    if (attachmentFile != null) {
                        sendAttachmentFile(
                            attachmentFile = attachmentFile,
                            attachmentFileName = noteId+fileName
                        )
                    }

                    _state.value = _state.value.copy(inputText = "")
                    _state.value = _state.value.copy(attachmentFileInfo = AttachmentFileInfo())
                }
            }
            is TaskDetailEvent.OnAllRefresh -> {
                getTask()
            }
            is TaskDetailEvent.SetInputText -> {
                _state.value = _state.value.copy(inputText = event.newText)
            }
            is TaskDetailEvent.ShowMore -> {
                getMyLogin()
            }
            is TaskDetailEvent.OpenCloseSetTaskStatusDialog -> {
                _state.value = _state.value.copy(
                    setTaskStatusDialogState = SetTaskStatusDialogState().copy(
                        selectedStatus = _state.value.task.taskStatus,
                        taskId = _state.value.task.id,
                        isOpen = !_state.value.setTaskStatusDialogState.isOpen
                    )
                )
            }
            is TaskDetailEvent.SetTaskStatus -> {
                setTaskStatus()
            }
            is TaskDetailEvent.SetTaskStatusDialog -> {
                _state.value = _state.value.copy(
                    setTaskStatusDialogState = _state.value.setTaskStatusDialogState.copy(
                        selectedStatus = event.newStatus
                    )
                )
            }
            is TaskDetailEvent.SetTaskDeadLine -> {
                setTaskDeadLine(event.newDeadLine.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            }

            is TaskDetailEvent.OnUsersRefresh -> {
                getUsers()
            }
            is TaskDetailEvent.OpenCloseAddUserToTaskDialog -> {
                if (_state.value.addUserDialogState.isOpen) {
                    _state.value = _state.value.copy(
                        addUserDialogState = AddUserToTaskDialogState(),
                    )
                } else {
                    getUsersFromWorkSpace()
                    _state.value = _state.value.copy(
                        addUserDialogState = AddUserToTaskDialogState(isOpen = true)
                    )
                }
            }
            is TaskDetailEvent.AddUserToTask -> {
                addUser(userLogin = event.userLogin)
            }
            is TaskDetailEvent.DeleteTask -> {
                deleteTask()
            }
            is TaskDetailEvent.OpenCloseDeleteTaskDialog -> {
                if (_state.value.deleteTaskDialogState.isOpen) {
                    _state.value = _state.value.copy(
                        deleteTaskDialogState = CustomAlertDialogState(),
                    )
                } else {
                    getUsersFromWorkSpace()
                    _state.value = _state.value.copy(
                        deleteTaskDialogState = CustomAlertDialogState(isOpen = true)
                    )
                }
            }
            is TaskDetailEvent.OpenCloseLeaveFromTaskDialog -> {
                if (_state.value.leaveFromTaskDialogState.isOpen) {
                    _state.value = _state.value.copy(
                        leaveFromTaskDialogState = CustomAlertDialogState(),
                    )
                } else {
                    getUsersFromWorkSpace()
                    _state.value = _state.value.copy(
                        leaveFromTaskDialogState = CustomAlertDialogState(isOpen = true)
                    )
                }
            }
            is TaskDetailEvent.LeaveFromTask -> {
                leaveFromTask()
            }
            is TaskDetailEvent.OpenCloseUserItemDialog -> {
                if (_state.value.userItemDialogState.isOpen) {
                    _state.value = _state.value.copy(
                        userItemDialogState = UserItemDialogState2(),
                    )
                } else {
                    _state.value = _state.value.copy(
                        userItemDialogState = UserItemDialogState2(
                            isOpen = true,
                            userModel = event.userModel
                        )
                    )
                }
            }
            is TaskDetailEvent.DeleteUser -> {
                deleteUserFromTask()
            }
            is TaskDetailEvent.AttachFile -> {
                _state.value = _state.value.copy(
                    attachmentFileInfo = AttachmentFileInfo(
                        attachmentFile = event.inputStream,
                        originalFileName = event.originalFileName
                    )
                )
            }
            is TaskDetailEvent.DetachFile -> {
                _state.value = _state.value.copy(
                    attachmentFileInfo = AttachmentFileInfo(
                        attachmentFile = null,
                        originalFileName = ""
                    )
                )
            }
            is TaskDetailEvent.DownloadFile -> {
                downloadFile(event.fileName)
            }
        }
    }

    private fun downloadFile(fileName:String){
        viewModelScope.launch(Dispatchers.IO) {
            downloadNoteAttachmentFile(fileName).collect{ result ->
                Log.d("dsfvsedfsrvsdfsv", result.toString())
                when (result) {
                    is Resource.Success -> {
                        val isSuccessful = saveFile(result.data, fileName.drop(36))
                        if(isSuccessful){
                            setNoteDownloadState(fileName.substring(0, 36), NoteModel.Companion.DownLoadState.SAVED)
                        }
                        else{
                            setNoteDownloadState(fileName.substring(0, 36), NoteModel.Companion.DownLoadState.ERROR)
                        }
                    }
                    is Resource.Error -> {
                        setNoteDownloadState(fileName.substring(0, 36), NoteModel.Companion.DownLoadState.ERROR)
                    }
                    is Resource.Loading -> {
                        setNoteDownloadState(fileName.substring(0, 36), NoteModel.Companion.DownLoadState.LOAD)
                    }
                }
            }
        }
    }

    private suspend fun saveFile(body: ResponseBody?, fileName: String) : Boolean{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try{
                if (body == null) return false
                val resolver: ContentResolver = myapplication.contentResolver
                val contentValues = ContentValues()
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/Task-cat")
                val uri: Uri? = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                Log.d("urirui", uri.toString())
                val fos = Objects.requireNonNull(uri)?.let { resolver.openOutputStream(it) }
                fos.use { output ->
                    val buffer = ByteArray(4 * 1024) // or other buffer size
                    var read: Int
                    while (body.byteStream().read(buffer).also { read = it } != -1) {
                        output?.write(buffer, 0, read)
                    }
                    output?.flush()
                }
                return true
            }catch (e: Exception){
                Log.e("saveFile", e.toString())
                return false
            }
        }
        else{
            val root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
            val directory = File("$root/Taskcat")

            if (!directory.exists()) {
                directory.mkdir()
            }
            val file = File(directory, fileName)
            if (body==null) return false
            var input: InputStream? = null
            try {
                input = body.byteStream()

                val fos = withContext(Dispatchers.IO) {
                    FileOutputStream(file.absolutePath)
                }
                fos.use { output ->
                    val buffer = ByteArray(4 * 1024) // or other buffer size
                    var read: Int
                    while (input.read(buffer).also { read = it } != -1) {
                        output.write(buffer, 0, read)
                    }
                    output.flush()
                }
                return true
            }catch (e: Exception){
                e.printStackTrace()
                Log.e("saveFile", e.toString())
                return false
            }
            finally {
                withContext(Dispatchers.IO) {
                    input?.close()
                }
            }
        }
    }

    private fun isFileExist(fileName: String) :Boolean {
        val root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        val directory = File("$root/Task-cat")
        val file = File(directory, fileName)
        return file.exists()
    }

    private fun sendAttachmentFile(attachmentFile: InputStream, attachmentFileName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            uploadNoteAttachmentFile(Token.token, attachmentFile, attachmentFileName).collect { result ->
                Log.d("dsfvsedfsrvsdfsv", result.data.toString())
                when (result) {
                    is Resource.Success -> {
                        _state.value = _state.value.copy(error = "", isLoading = false)
                        result.data?.let {}
                    }
                    is Resource.Error -> {
                        _state.value =
                            _state.value.copy(error = result.message.toString(), isLoading = false)
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(error = "")
                    }
                }
            }
        }
    }

    private fun deleteUserFromTask() {
        viewModelScope.launch {
            val taskId = savedStateHandle.get<String>("id") ?: return@launch
            deleteUserFromTaskUseCase(
                Token.token,
                taskId,
                _state.value.userItemDialogState.userModel.login
            ).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let {
                            _state.value = _state.value.copy(
                                userItemDialogState = _state.value.userItemDialogState.copy(
                                    isSuccess = true,
                                    isLoading = false,
                                    error = ""
                                )
                            )
                            onEvent(TaskDetailEvent.OnUsersRefresh)
                        }
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            userItemDialogState = _state.value.userItemDialogState.copy(
                                error = result.message ?: "",
                                isLoading = false
                            )
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            userItemDialogState = _state.value.userItemDialogState.copy(
                                isLoading = result.isLoading,
                                error = result.message ?: ""
                            )
                        )
                    }
                }
            }
        }
    }

    private fun leaveFromTask() {
        viewModelScope.launch {
            val taskId = savedStateHandle.get<String>("id") ?: return@launch
            deleteUserFromTaskUseCase(
                Token.token,
                taskId,
                _state.value.my.login
            ).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let {
                            _state.value = _state.value.copy(
                                leaveFromTaskDialogState = _state.value.leaveFromTaskDialogState.copy(
                                    isSuccess = true,
                                    isLoading = false,
                                    error = ""
                                )
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            leaveFromTaskDialogState = _state.value.leaveFromTaskDialogState.copy(
                                error = result.message ?: "",
                                isLoading = false
                            )
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            leaveFromTaskDialogState = _state.value.leaveFromTaskDialogState.copy(
                                isLoading = result.isLoading,
                                error = result.message ?: ""
                            )
                        )
                    }
                }
            }
        }
    }

    private fun deleteTask() {
        viewModelScope.launch {
            deleteTaskUseCase(Token.token, _state.value.task.id).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let {
                            _state.value = _state.value.copy(
                                deleteTaskDialogState = _state.value.deleteTaskDialogState.copy(
                                    isSuccess = true,
                                    isLoading = false,
                                    error = ""
                                )
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            deleteTaskDialogState = _state.value.deleteTaskDialogState.copy(
                                error = result.message ?: "",
                                isLoading = false
                            )
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            deleteTaskDialogState = _state.value.deleteTaskDialogState.copy(
                                isLoading = result.isLoading,
                                error = result.message ?: ""
                            )
                        )
                    }
                }
            }
        }
    }

    private fun getTask() {
        viewModelScope.launch {
            val id = savedStateHandle.get<String>("id") ?: return@launch
            getTaskByIdUseCase(Token.token, id).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { task ->
                            _state.value = _state.value.copy(
                                task = task,
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

    private fun getMyLogin() {
        viewModelScope.launch() {
            getUserByToken(Token.token).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.value = _state.value.copy(error = "", isLoading = false)
                        result.data?.let { userDto ->
                            _state.value = _state.value.copy(
                                my = userDto,
                            )
                            getNotes()
                        }
                    }
                    is Resource.Error -> {
                        _state.value =
                            _state.value.copy(error = result.message.toString(), isLoading = false)
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(error = "", isLoading = true)
                    }
                }
            }
        }
    }

    private fun getNotes() {
        offset += 10
        viewModelScope.launch {
            val workSpaceId = savedStateHandle.get<String>("id") ?: return@launch
            getNotesFromTask(Token.token, workSpaceId, offset).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val notesList = _state.value.notesList.toMutableList()
                        result.data?.let { notes ->
                            notesList.addAll(notes)
                            _state.value = _state.value.copy(
                                notesList = notesList,
                                error = result.message ?: "",
                                isLoading = false
                            )
                            notes.forEach() { note->
                                if(note.attachmentFile.isNotEmpty()){
                                    if(isFileExist(note.attachmentFile)){
                                        setNoteDownloadState(note.id, NoteModel.Companion.DownLoadState.SAVED)
                                    }
                                    else{
                                        setNoteDownloadState(note.id, NoteModel.Companion.DownLoadState.NOTSAVED)
                                    }
                                }
                            }
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
                            isLoading = result.isLoading,
                            error = result.message ?: ""
                        )
                    }
                }
            }
        }
    }

    private fun setTaskStatus() {
        viewModelScope.launch {
            val taskId = _state.value.setTaskStatusDialogState.taskId
            val newStatus = _state.value.setTaskStatusDialogState.selectedStatus
            setTaskStatusUseCase(Token.token, taskId, newStatus).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let {
                            _state.value = _state.value.copy(
                                setTaskStatusDialogState = _state.value.setTaskStatusDialogState.copy(
                                    isSuccess = true,
                                    isLoading = false,
                                    error = ""
                                )
                            )
                            onEvent(TaskDetailEvent.OnAllRefresh)
                        }
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            setTaskStatusDialogState = _state.value.setTaskStatusDialogState.copy(
                                error = result.message ?: "",
                                isLoading = false
                            )
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            setTaskStatusDialogState = _state.value.setTaskStatusDialogState.copy(
                                isLoading = result.isLoading,
                                error = result.message ?: ""
                            )
                        )
                    }
                }
            }
        }
    }

    private fun setTaskDeadLine(newDeadLine: String) {
        viewModelScope.launch {
            val taskId = _state.value.task.id
            setTaskDeadLineUseCase(Token.token, taskId, newDeadLine).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let {
                            _state.value = _state.value.copy(
                                setTaskStatusDialogState = _state.value.setTaskStatusDialogState.copy(
                                    isSuccess = true,
                                    isLoading = false,
                                    error = ""
                                )
                            )
                            onEvent(TaskDetailEvent.OnAllRefresh)
                        }
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            setTaskStatusDialogState = _state.value.setTaskStatusDialogState.copy(
                                error = result.message ?: "",
                                isLoading = false
                            )
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            setTaskStatusDialogState = _state.value.setTaskStatusDialogState.copy(
                                isLoading = result.isLoading,
                                error = result.message ?: ""
                            )
                        )
                    }
                }
            }
        }
    }

    private fun getUsers() {
        viewModelScope.launch {
            val workSpaceId = savedStateHandle.get<String>("id") ?: return@launch
            getUsersFromTaskUseCase(Token.token, workSpaceId).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { users ->
                            _state.value = _state.value.copy(
                                usersState = _state.value.usersState.copy(
                                    users = users,
                                    error = "",
                                    isLoading = false
                                )
                            )

                        }
                    }
                    is Resource.Error -> {
                        _state.value =
                            _state.value.copy(
                                usersState = _state.value.usersState.copy(
                                    error = result.message ?: "", isLoading = false
                                )
                            )
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            usersState = _state.value.usersState.copy(
                                isLoading = result.isLoading,
                                error = result.message ?: ""
                            )
                        )
                    }
                }

            }
        }
    }

    private fun getUsersFromWorkSpace() {
        viewModelScope.launch {
            val workSpaceId = savedStateHandle.get<String>("workSpaceId") ?: return@launch
            getUsersFromWorkSpace(Token.token, workSpaceId).collect { result ->
                Log.d("asdasdadssddfdlg,s", result.toString())
                when (result) {

                    is Resource.Success -> {
                        result.data?.let { users ->
                            Log.d("asdasdasdsdasadsdasda", users.toString())
                            _state.value = _state.value.copy(
                                addUserDialogState = _state.value.addUserDialogState.copy(
                                    isSuccess = true,
                                    isLoading = false,
                                    displayedList =
                                    users.filter { it.login !in _state.value.usersState.users.map { item -> item.login } },
                                    error = ""
                                )
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            addUserDialogState = _state.value.addUserDialogState.copy(
                                error = result.message ?: "", isLoading = false
                            )
                        )
                    }
                    is Resource.Loading -> {

                        _state.value = _state.value.copy(
                            addUserDialogState = _state.value.addUserDialogState.copy(
                                isLoading = result.isLoading,
                                error = result.message ?: ""
                            )
                        )
                    }
                }

            }
        }
    }

    private fun addUser(userLogin: String) {
        viewModelScope.launch {
            val taskId = _state.value.task.id
            addUserToTaskUseCase(
                Token.token,
                userLogin,
                taskId
            ).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { it ->
                            Log.d("addWorkspace", it.toString())
                            _state.value = _state.value.copy(
                                addUserDialogState = _state.value.addUserDialogState.copy(
                                    displayedList = _state.value.addUserDialogState.displayedList.filter { it.login != userLogin }
                                )
                            )
                            onEvent(TaskDetailEvent.OnUsersRefresh)
                        }
                    }
                    is Resource.Error -> {
                        Log.d("addUser2321323", result.message ?: "")
                        _state.value = _state.value.copy(

                        )
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(

                        )
                    }
                }
            }
        }
    }

    private fun setNoteDownloadState(noteId:String, noteStatus: NoteModel.Companion.DownLoadState){
        val notesList = _state.value.notesList.toMutableList()
        val noteModel = notesList.last{it.id == noteId}
        val index = notesList.indexOf(noteModel)
        notesList[index] = noteModel.copy(downloadState = noteStatus)
        _state.value = _state.value.copy(notesList = notesList)
    }
}