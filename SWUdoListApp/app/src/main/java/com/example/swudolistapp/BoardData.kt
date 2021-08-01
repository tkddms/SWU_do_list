package com.example.swudolistapp

data class SubjectListData(
    val subjectName: String,
    val subjectCode: String,
)

data class ToDoListData(
    val context: String,
    val checked: Boolean,
)