package com.ayushi.patientreg

data class Patient(
    val patientId: String = "",
    val name: String = "",
    val age: String = "",
    val gender: String = "",
    val phone: String = "",
    val address: String = "",
    val disease: String = "",
    val timestamp: Long = 0L
)