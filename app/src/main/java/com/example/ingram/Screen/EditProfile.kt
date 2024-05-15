package com.example.ingram.Screen

import android.net.Uri
import android.service.autofill.UserData
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ingram.CommonDivier
import com.example.ingram.CommonImage
import com.example.ingram.CommonProgressBar
import com.example.ingram.DestinationScreen
import com.example.ingram.LCViewModel
import com.example.ingram.navigateTo
import com.example.ingram.ui.theme.Purple80

@Composable
fun EditProfile(vm:LCViewModel, navController: NavController ){
    val inProcess = vm.inProcess.value
    val imageUrl= vm.userData.value?.imageUrl
    if(inProcess){
        CommonProgressBar()
    }else {
        val userData = vm.userData.value
        var name by rememberSaveable {
            mutableStateOf(userData?.name ?: "")
        }
        var number by rememberSaveable {
            mutableStateOf(userData?.number ?: "")
        }
        var note by rememberSaveable {
            mutableStateOf(userData?.note ?: "")
        }
        Column {
           ProfileImage(imageUrl = imageUrl, vm =vm )
            ProfileContent(
                onBack = {
                    navigateTo(navController, DestinationScreen.Profile.route)
                         },
                onSave = {
                    vm.createOrUpdateProfile(
                    name=name, number= number,note = note
                ) },
                name = name,
                number = number,
                note = note,
                onNameChange = {name=it},
                onNumberChange = {number=it},
                onNoteChange = {note= it}
            )
        }
    }
}
@Composable
fun ProfileImage(imageUrl: String? ,vm: LCViewModel){
    val context = LocalContext.current
    var uri by remember{
        mutableStateOf<Uri?>(null)
    }

    val productPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            uri = it
        }
    )
    Box (modifier = Modifier.height(intrinsicSize = IntrinsicSize.Min)){
     Column(modifier = Modifier
         .padding(8.dp)
         .fillMaxWidth(),
         horizontalAlignment = Alignment.CenterHorizontally) {
         AsyncImage(model = uri, contentDescription = null, modifier = Modifier
             .size(150.dp)

             .border(width = 2.dp, color = Color.LightGray, shape = CircleShape)
             .padding(5.dp)
             .clip(CircleShape)
            )
         Spacer(modifier = Modifier.height(20.dp))
         Button(onClick = {
             productPicker.launch(
                 PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
             )
         }, colors = ButtonDefaults.buttonColors(Purple80),
             modifier = Modifier
                 .width(260.dp)
                 .height(40.dp)) {
             Text("Thay đổi ảnh đại diện ",
                 fontSize = 17.sp
                 ,color = Color.Black,
                 modifier = Modifier.background(color = Purple80)
             )
         }
         Text(text = "Lưu ảnh", modifier = Modifier.clickable { uri?.let {
             vm.upLoadImage(uri = it, context = context, type = "image") { imageUrl ->
                 vm.createOrUpdateProfile(
                     imageUrl = imageUrl
                 )
             }
         }

         })
     }
    }
}
@Composable
fun ProfileContent( onBack:() ->Unit,
                    onSave:() ->Unit,
                    name: String,
                    number:String,
                    note: String,
                    onNameChange: (String) ->Unit,
                    onNumberChange: (String) ->Unit,
                    onNoteChange: (String) ->Unit,
){
    Column {
        CommonDivier()
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Tên đăng nhập: ", modifier = Modifier.width(120.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp)
            TextField(value = name, onValueChange =onNameChange,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                    ),
                )
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Số điện thoại: ", modifier = Modifier.width(120.dp),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,)
            TextField(value = number, onValueChange =onNumberChange,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                ),

            )
        }
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            Text(text = "Ghi chú: ", modifier = Modifier.width(120.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
                )
            TextField(value = note, onValueChange =onNoteChange,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            )
        }
        CommonDivier()
        Spacer(modifier = Modifier.height(50.dp))
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween){
            Text(text = "Quay lại", modifier = Modifier.clickable {
                onBack.invoke()
            },
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
            Text(text = "Lưu ", modifier = Modifier.clickable {
                onSave.invoke()
            },
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp)
        }
    }
}