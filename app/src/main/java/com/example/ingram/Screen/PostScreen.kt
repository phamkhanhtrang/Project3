package com.example.ingram.Screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ingram.DestinationScreen
import com.example.ingram.LCViewModel
import com.example.ingram.navigateTo

@Composable
fun PostScreen(vm: LCViewModel, navController: NavController,){


val context = LocalContext.current
    var imageUris by remember {
        mutableStateOf<List<Uri>>(emptyList())
    }
    val multipleImage = rememberLauncherForActivityResult(
        contract =ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = {
            imageUris= it
        }
    )
    var uploadedImageCount = 0 // Biến đếm số lượng ảnh đã tải lên thành công
    val totalImageCount = imageUris.size // Tổng số lượng ảnh được chọn

    Column (modifier = Modifier
        .fillMaxSize()
        .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = "Xong", modifier = Modifier.clickable {
            navigateTo(navController, DestinationScreen.HomeScreen.route)
        })

        Column {
            LazyRow {

                items(imageUris){uri ->
                    AsyncImage(model = uri, contentDescription = null, modifier = Modifier.size(200.dp))
                }}

            Button(onClick = { multipleImage.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)

            ) }) {
                Text(text = " Chọn ảnh ")
            }

        }

        Column (horizontalAlignment = Alignment.CenterHorizontally){
            val contentState = remember {
                mutableStateOf(TextFieldValue())
            }
            OutlinedTextField(value = contentState.value, onValueChange = {
                contentState.value  =it
            }, label = { Text(text = "Nhập nội dung")})
            Button(onClick = {
                uploadedImageCount = 0 // Đặt lại số lượng ảnh đã tải lên thành công về 0
                imageUris.forEach { uri ->
                    uri.let {
                        vm.upLoadImage(uri = it, context = context, type = "image") { imageUrl ->
                            uploadedImageCount++
                            if (uploadedImageCount == totalImageCount) {
                                // Nếu đã tải lên tất cả ảnh, gọi hàm createPost
                                vm.createPost(imageUris.mapNotNull { it.toString() }, contentState.value.text, context)
                            }
                        }
                    }
                }
            })
             {
                Text(text = "Đăng bài viết ")
            }

        }
    }
}

