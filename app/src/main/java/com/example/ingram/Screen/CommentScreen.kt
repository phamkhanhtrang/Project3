package com.example.ingram.Screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberImagePainter
import com.example.ingram.LCViewModel
import com.example.ingram.R
import com.example.ingram.TitleText
import com.example.ingram.data.Comment
import com.example.ingram.ui.theme.Bule1

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CommentScreen(vm: LCViewModel,postID:String) {
    var DataCommment = vm.comment
    var comment by rememberSaveable {
        mutableStateOf("")
    }
    val onComment = {
        vm.onSendComment(postID, comment)
        comment = ""
        
    }
    LaunchedEffect(key1 = Unit) {
        vm.populateComment(postID)
    }
    Scaffold(
        bottomBar = {
            SendComment(
                comment = comment,
                onCommentChange = { comment = it },
                onSendComment = onComment
            )
        },
         content = {
            Column {
                Text(text = "Bình luận",)
                Divider(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .height(1.dp)
                )
                CommentItem(comment = DataCommment.value)
            }
        }
    )
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun CommentItem(comment: List<Comment>,
               ) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        items(comment) {msg->
            ConstraintLayout {
                val painter = rememberImagePainter(data = msg.imageUrl)
                val (imgAvatar, username, textTime, textContent) = createRefs()

                Image(
                    painter = painter,
                    contentDescription = "profile",
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)

                        .constrainAs(imgAvatar) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start, margin = 10.dp)
                        },
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = msg.username ?:"", fontSize = 12.sp, maxLines = 1, modifier = Modifier
                        .width(100.dp)
                        .constrainAs(username) {
                            start.linkTo(imgAvatar.end, margin = 9.dp)
                            top.linkTo(parent.top, margin = 7.dp)
                        },
                    style = TextStyle(
                        fontWeight = FontWeight.Bold
                    )
                )

                Text(
                    text = msg.comment ?: "",
                    fontSize = 12.sp,
                    maxLines = 3,
                    modifier = Modifier
                        .width(200.dp)
                        .constrainAs(textContent) {
                            start.linkTo(parent.start, margin = 26.dp)
                            end.linkTo(parent.end)
                            top.linkTo(username.top, margin = 30.dp)
                        },
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(

                    )
                )
            }
        }
     }
}
@Composable
fun SendComment(comment: String, onCommentChange:(String)->Unit, onSendComment:()->Unit){

    Row {
        Image(
            painter = painterResource(id = R.drawable.anh3), contentDescription = "profile",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        TextField(value = comment,
            onValueChange = onCommentChange)
        Spacer(modifier = Modifier.height(90.dp))
        Icon(Icons.Default.Send, contentDescription = null,
            modifier = Modifier
                .clickable {
                    onSendComment()
                }
                .size(40.dp),
            tint = Bule1)
        }
    }