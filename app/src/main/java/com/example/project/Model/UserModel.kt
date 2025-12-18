package com.example.project.Model

data class UserModel(
    val id : String = "",
    val firstName : String = "",
    val lastName : String = "",
    val gender : String = "",
    val dob : String = "",
    val email : String = "",
){
    fun toMap(): MutableMap<String, Any> {
        return mutableMapOf(
            "id" to id,
            "firstName" to firstName,
            "lastName" to lastName,
            "gender" to gender,
            "dob" to dob,
            "email" to email
        )
    }
}



