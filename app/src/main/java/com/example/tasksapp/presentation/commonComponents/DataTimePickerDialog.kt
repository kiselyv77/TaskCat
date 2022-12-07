package com.example.tasksapp.presentation.commonComponents

import androidx.compose.runtime.Composable
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import java.time.LocalDate
import java.time.LocalTime


@Composable
fun DataTimePickerDialog(
    dialogDateState: MaterialDialogState,
    dialogTimeState: MaterialDialogState,
    onDateChange: (date: LocalDate)->Unit,
    onTimeChange: (time: LocalTime)->Unit,
) {
    MaterialDialog(
        dialogState = dialogDateState,
        buttons = {
            positiveButton("Ok", disableDismiss = true){
                dialogTimeState.show()
            }
            negativeButton("Cancel"){

            }
        }
    ) {
        datepicker { date ->
            onDateChange(date)
        }
    }

    MaterialDialog(
        dialogState = dialogTimeState,
        buttons = {
            positiveButton("Ok"){
                dialogDateState.hide()
                dialogTimeState.hide()
            }
            negativeButton("Cancel")
        }
    ) {
        timepicker { time ->
            onTimeChange(time)
        }
    }
}