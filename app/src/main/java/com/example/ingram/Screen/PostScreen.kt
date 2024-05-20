package com.example.ingram.Screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import com.example.ingram.CommonProgressBar
import com.example.ingram.DestinationScreen
import com.example.ingram.LCViewModel
import com.example.ingram.navigateTo
import com.example.ingram.ui.theme.Bule1

@Composable
fun PostScreen(vm: LCViewModel, navController: NavController) {
    val inProcess = vm.inProcess.value
    if (inProcess) {
        CommonProgressBar()
    } else {
        val context = LocalContext.current
        var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
        val multipleImage = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia(),
            onResult = { imageUris = it }
        )
        Column {
            Icon(
                Icons.Rounded.ArrowBack, contentDescription = null,
                modifier = Modifier
                    .clickable {
                        navigateTo(navController, DestinationScreen.HomeScreen.route)
                    }
                    .padding(10.dp)
                    .size(30.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyRow {
                    items(imageUris) { uri ->
                        AsyncImage(
                            model = uri,
                            contentDescription = null,
                            modifier = Modifier.size(200.dp)
                        )
                    }
                }

                Button(onClick = {
                    multipleImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                    colors = ButtonDefaults.buttonColors(Bule1)) {
                    Text(text = "Chọn ảnh")
                }


                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val contentState = remember { mutableStateOf(TextFieldValue()) }
                    OutlinedTextField(
                        value = contentState.value,
                        onValueChange = { contentState.value = it },
                        label = { Text(text = "Nhập nội dung") }
                    )
                    Row {
                        Button(onClick = {
                            val uploadedImageUrls = mutableListOf<String>()
                            vm.inProcess.value = true
                            imageUris.forEach { uri ->
                                vm.upLoadImage(
                                    uri = uri,
                                    context,
                                    type = "image"
                                ) { imageUrl ->
                                    uploadedImageUrls.add(imageUrl)
                                    if (uploadedImageUrls.size == imageUris.size) {
                                        vm.createPost(uploadedImageUrls, contentState.value.text)
                                        vm.inProcess.value = false
                                        navigateTo(navController, DestinationScreen.Profile.route)
                                    }
                                }
                            }
                        },
                            colors = ButtonDefaults.buttonColors(Bule1)) {
                            Text(text = "Đăng bài viết")
                        }
                        Button(onClick = {

                            imageUris.forEach{
                                uri->
                                uri.let {
                                    vm.uploadStory(uri,context)
                                }
                                vm .inProcess.value=true
                                vm.inProcess.value = false
                                navigateTo(navController, DestinationScreen.HomeScreen.route)

                            }

                        },
                            colors = ButtonDefaults.buttonColors(Bule1)) {
                            Text(text = "Đăng tin")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

