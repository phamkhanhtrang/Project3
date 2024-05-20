package com.example.ingram.Screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
import com.example.ingram.CommonProgressBar
import com.example.ingram.CommonRow
import com.example.ingram.DestinationScreen
import com.example.ingram.ImageStory
import com.example.ingram.LCViewModel
import com.example.ingram.Model.Stories
import com.example.ingram.navigateTo


@Composable
fun StorySection(  vm:LCViewModel,navController : NavController) {


        val storyes = vm.storys.value
        val userData = vm.userData.value
        val myStory = storyes.filter {
            it.user.userId == userData?.userId
        }
        val otherStory = storyes.filter {
            it.user.userId != userData?.userId
        }

    if (storyes.isEmpty()) {
            Text(text = "Không có bảng tin")
        } else {
            Row(modifier = Modifier) {
                if (myStory.isNotEmpty()) {
                    ImageStory(
                        imageUrl = myStory[0].user.imageUrl,
                        name = myStory[0].user.name
                    ) {
                        navigateTo(
                            navController = navController,
                            DestinationScreen.SingleStory.createRoute(myStory[0].user.userId!!)
                        )
                    }
                    val uniqueUser = otherStory.map { it.user }.toSet().toList()
                    LazyRow(modifier = Modifier.weight(1f)) {
                        items(uniqueUser) { user ->
                            ImageStory(imageUrl = user.imageUrl, name = user.name) {
                                navigateTo(
                                    navController = navController,
                                    DestinationScreen.SingleStory.createRoute(user.userId!!)
                                )
                            }
                        }

                    }
                }
            }
        }
    }

