package com.example.ingram.Model


data class Post(
    val profile: Int,
    val userName: String,
    val postImageList: List<Int>,
    val description:String,
    val likeBy: List<User>
)
data class User(
    val profile: Int,
    val userName: String,
    val storyCountt: Int = 0,
    val stories: List<Int> = listOf()
)
