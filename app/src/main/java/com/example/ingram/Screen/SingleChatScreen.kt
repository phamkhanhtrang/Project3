package com.example.ingram.Screen

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ingram.CommonDivier
import com.example.ingram.CommonImage
import com.example.ingram.LCViewModel
import com.example.ingram.data.Message
import com.example.ingram.ui.theme.Bule1
import com.example.ingram.ui.theme.Gray


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SingleChatScreen (navController: NavController, vm: LCViewModel, chatId:String){
    var reply by rememberSaveable {
        mutableStateOf("")
    }
    val onSendReply ={
        vm.onSendReply(chatId,reply)
        reply=""
    }
    var chatMessage = vm.chatMessage
    val myUser = vm.userData.value
    var currentChat = vm.chats.value.first {
        it.chatID==chatId
    }
    val chatUser = if(myUser?.userId==currentChat.user1.userId)currentChat.user2 else currentChat.user1
    LaunchedEffect(key1 = Unit) {
        vm.populateMessages(chatId)
    }
    BackHandler {
        vm.depopulateMessage()
    }
//    Column (modifier = Modifier.fillMaxSize(),verticalArrangement = Arrangement.SpaceBetween){

            Scaffold  (
           bottomBar = {

               ReplyBox(
                   reply = reply,
                   onReplyChange = { reply = it },
                   onSendReply = onSendReply
               )

           } ,
           content = {
               Column(
                   modifier = Modifier
                       .fillMaxWidth()
                       .wrapContentHeight()
               ) {
                   ChatHeard(
                       name = chatUser.name ?: "",
                       imageUrl = chatUser.imageUrl ?: ""
                   ) {
                       navController.popBackStack()
                       vm.depopulateMessage()
                   }
                       MessageBox(
                       modifier = Modifier.weight(1f),
                       chatMessage = chatMessage.value,
                       currentUserId = myUser?.userId ?: ""
                   )
               }
           }
       )

//    }

}
@SuppressLint("SuspiciousIndentation")
@Composable
fun MessageBox(modifier: Modifier, chatMessage: List<Message>, currentUserId: String){
        LazyColumn(modifier = Modifier) {

            items(chatMessage) { msg ->
                val alignment = if (msg.sendBy == currentUserId) Alignment.End else Alignment.Start
                val color =
                    if (msg.sendBy == currentUserId) Color(0xFF63B4E9) else Color(0xD78D9094)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp), horizontalAlignment = alignment
                ) {
                    Text(
                        text = msg.message ?: "",
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(color)
                            .padding(12.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
}
@Composable
fun ChatHeard(name: String, imageUrl: String, onBackClicked :()->Unit){
    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically){
        Icon(
            Icons.Rounded.ArrowBack, contentDescription = null,
            modifier = Modifier
                .clickable {
                    onBackClicked.invoke()
                }
                .padding(8.dp))
        CommonImage(data = imageUrl, modifier = Modifier
            .padding(8.dp)
            .size(50.dp)
            .clip(CircleShape))
        Text(text = name, fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp))
    }
}
@Composable
fun ReplyBox(reply: String, onReplyChange:(String)->Unit, onSendReply:()->Unit){

        Column(
            modifier = Modifier.fillMaxWidth()
                .background(color = Color.White)
        ) {
            CommonDivier()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,

                ) {
                TextField(
                    value = reply, onValueChange = onReplyChange, maxLines = 3,
                    modifier = Modifier.height(50.dp)
                )
                Button(
                    onClick = onSendReply,
                    modifier = Modifier.height(55.dp)
                        .width(192.dp)
                ) {
                    Text(
                        text = "Gửi",
                        fontSize = 9.sp,
                    )
                }
            }
        }
}