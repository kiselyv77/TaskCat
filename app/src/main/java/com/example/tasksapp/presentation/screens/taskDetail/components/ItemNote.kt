package com.example.tasksapp.presentation.screens.taskDetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasksapp.domain.model.NoteModel
import com.example.tasksapp.util.getTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ItemNote(
    clicable: () -> Unit,
    downloadFile: () -> Unit,
    openFile:()->Unit,
    note: NoteModel
) {
    val parsedDataTime = LocalDateTime.parse(note.dateTime, DateTimeFormatter.ISO_DATE_TIME)
    val date = parsedDataTime.toLocalDate()
    val time = getTime(parsedDataTime)
    Card(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(3))
            .clickable { clicable() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${note.userName} добавил обновление:",
                fontSize = 20.sp
            )
            if (note.attachmentFile.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(30.dp))
                        .background(Color.Gray)
                        .clickable {
                            when(note.downloadState){
                                NoteModel.Companion.DownLoadState.SAVED -> openFile()
                                NoteModel.Companion.DownLoadState.LOAD -> {}
                                NoteModel.Companion.DownLoadState.NOTSAVED -> downloadFile()
                                NoteModel.Companion.DownLoadState.ERROR -> downloadFile()
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if(note.downloadState == NoteModel.Companion.DownLoadState.LOAD){
                        CircularProgressIndicator(
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    else{
                        val icon = when(note.downloadState){
                            NoteModel.Companion.DownLoadState.SAVED -> Icons.Default.FileOpen
                            NoteModel.Companion.DownLoadState.LOAD -> Icons.Default.FileOpen
                            NoteModel.Companion.DownLoadState.NOTSAVED -> Icons.Default.Download
                            NoteModel.Companion.DownLoadState.ERROR -> Icons.Default.ErrorOutline
                        }
                        Icon(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(30.dp))
                                .clickable { },
                            imageVector = icon,
                            contentDescription = "clear text",
                            tint = if(note.downloadState == NoteModel.Companion.DownLoadState.ERROR) Color.Red else Color.Unspecified
                        )
                    }

                    Text(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        maxLines = 1,
                        text = note.attachmentFile,
                        fontSize = 20.sp,
                        overflow = TextOverflow.Ellipsis
                    )
                }

            }
            if (note.info.isNotEmpty()) {
                Text(
                    text = note.info,
                    fontSize = 23.sp
                )
            }
            Text(
                text = "$date в $time",
                fontSize = 20.sp
            )
        }
    }
}

