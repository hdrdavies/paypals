package com.example.paypals.data

data class Contact(
    val name: String,
    val number: String,
    val avatarUrl: String?
) {
    companion object {
        fun generateRandomNumber() = "+34${(100000000..999999999).random()}"
    }
}