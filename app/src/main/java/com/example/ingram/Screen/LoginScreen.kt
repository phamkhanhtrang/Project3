package com.example.ingram.Screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.ingram.Components.textHeadComponent
import com.example.ingram.DestinationScreen
import com.example.ingram.LCViewModel
import com.example.ingram.R
import com.example.ingram.navigateTo
import com.example.ingram.ui.theme.Bule1


@Composable
fun LoginScreen(vm: LCViewModel, navController : NavController){
    CheckSignedIn(vm = vm, navController = navController )
    Box(modifier = Modifier.fillMaxSize()){
        Column(modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight()
            .verticalScroll(
                rememberScrollState()
            ),
            horizontalAlignment = Alignment.CenterHorizontally) {
            val emailState = remember {
                mutableStateOf(TextFieldValue())
            }
            val passwordState = remember {
                mutableStateOf(TextFieldValue())
            }
            val focus = LocalFocusManager.current

            textHeadComponent(value = stringResource(id = R.string.instagram ) )
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(
                value =emailState.value ,
                onValueChange = {
                    emailState.value = it
                },
                label = { Text(text = "Email", fontSize = 13.sp)
                },
                modifier = Modifier.width(250.dp)
                    .height(60.dp))

            OutlinedTextField(
                value =passwordState.value ,
                onValueChange = {
                    passwordState.value = it
                },
                label = { Text(text = "Mật khẩu", fontSize = 13.sp) },
                modifier = Modifier.width(250.dp)
                    .height(60.dp))

            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
                vm.loginIn(emailState.value.text,passwordState.value.text)
            },
                modifier = Modifier
                    .width(250.dp)
                    .height(40.dp),
                contentPadding = PaddingValues(),
                colors = ButtonDefaults.buttonColors(Bule1))  {
                Text(text = "Đăng nhập ")
            }
            Spacer(modifier = Modifier.height(22.dp))
            DividerTextComponent()
            Text(text = " Bạn chưa có tài khoản. Đăng ký",
                modifier = Modifier.clickable {
                    navigateTo(navController, DestinationScreen.SignUp.route)
                },
                fontWeight = FontWeight.Bold
            )
        }
    }
    if(vm.inProcess.value){
        CommonProgressBar()
    }
}
