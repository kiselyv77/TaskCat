package com.example.tasksapp.presentation.screens.registration

sealed class RegistrationEvent{
    object Register:RegistrationEvent()

    data class SetName(val newValue:String):RegistrationEvent()
    data class SetLogin(val newValue:String):RegistrationEvent()
    data class SetPassword(val newValue:String):RegistrationEvent()

}
