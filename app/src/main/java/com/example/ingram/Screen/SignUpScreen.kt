package com.example.ingram.Screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ingram.CheckSignedIn
import com.example.ingram.CommonProgressBar
import com.example.ingram.Components.DividerTextComponent
import com.example.ingram.Components.TextComponent
import com.example.ingram.Components.textHeadComponent
import com.example.ingram.DestinationScreen
import com.example.ingram.LCViewModel
import com.example.ingram.R
import com.example.ingram.navigateTo
import com.example.ingram.ui.theme.Bule1


@Composable
fun SignUpScreen(navController: NavController, vm: LCViewModel) {
    CheckSignedIn(vm = vm, navController = navController )
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val nameState = remember {
                mutableStateOf(TextFieldValue())
            }
            val numberState = remember {
                mutableStateOf(TextFieldValue())
            }
            val emailState = remember {
                mutableStateOf(TextFieldValue())
            }
            val passwordState = remember {
                mutableStateOf(TextFieldValue())
            }
            val focus = LocalFocusManager.current

            textHeadComponent(value = stringResource(id = R.string.instagram))

            TextComponent(value = stringResource(id = R.string.text))
            Spacer(modifier = Modifier.height(4.dp))
//            ButtonComponent(value = stringResource(id = R.string.face))
            Spacer(modifier = Modifier.height(4.dp))


            OutlinedTextField(
                value =nameState.value ,
                onValueChange = {
                    nameState.value = it
                },
                label = { Text(text = "Tên tài khoản", fontSize = 13.sp) },
                modifier = Modifier
                    .width(250.dp)
                    .height(60.dp))
            OutlinedTextField(
                value =numberState.value ,
                onValueChange = {
                    numberState.value = it
                },
                label = { Text(text = "Số điện thoại", fontSize = 13.sp) },
                modifier = Modifier
                    .width(250.dp)
                    .height(60.dp))
            OutlinedTextField(
                value =emailState.value ,
                onValueChange = {
                    emailState.value = it
                },
                label = { Text(text = "Email", fontSize = 13.sp) },
                modifier = Modifier
                    .width(250.dp)
                    .height(60.dp))

            OutlinedTextField(
                value =passwordState.value ,
                onValueChange = {
                    passwordState.value = it
                },
                label = { Text(text = "Mật khẩu", fontSize = 13.sp) },
                modifier = Modifier
                    .width(250.dp)
                    .height(60.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { vm.signUp(
                name =  nameState.value.text,
                number =  numberState.value.text,
                email = emailState.value.text,
                password= passwordState.value.text,
            )},
                modifier = Modifier
                    .width(250.dp)
                    .height(40.dp),
                contentPadding = PaddingValues(),
                colors = ButtonDefaults.buttonColors(Bule1)) {
                Text(text = "Đăng ký ")
            }
            DividerTextComponent()
            Spacer(modifier = Modifier.height(22.dp))

            Text(text = " Bạn đã có tài khoản? Đăng nhập  ",
                modifier = Modifier.clickable {
                    navigateTo(navController, DestinationScreen.Login.route)
                },
                fontWeight = FontWeight.Bold
            )
        }
        Text(text = "",)
    }
    if(vm.inProcess.value){
        CommonProgressBar()
    }
}
