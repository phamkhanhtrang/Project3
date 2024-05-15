package com.example.ingram

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ingram.Screen.ChatListScreen
import com.example.ingram.Screen.EditProfile
import com.example.ingram.Screen.HomeScreen
import com.example.ingram.Screen.LoginScreen
import com.example.ingram.Screen.PostScreen
import com.example.ingram.Screen.ProfileScreen
import com.example.ingram.Screen.SearchScreen
import com.example.ingram.Screen.SignUpScreen
import com.example.ingram.Screen.SingleChatScreen
import com.example.ingram.ui.theme.IngramTheme
import dagger.hilt.android.AndroidEntryPoint

sealed class DestinationScreen(var route : String){
    object SignUp: DestinationScreen("signup")
    object Login: DestinationScreen("login")
    object Profile: DestinationScreen("profile")
    object ChatList: DestinationScreen("chatlist")
    object HomeScreen: DestinationScreen("home")
    object Search: DestinationScreen("search")
    object SingleChat: DestinationScreen("singleChat/{chatID}"){
        fun createRoute(id: String ) = "singlechat/$id"
    }
    object EditProfle: DestinationScreen("edit ")
    object PostContent: DestinationScreen("post ")
//    object fr : DestinationScreen("editprofle/{userID}"){
//        fun createRoute(userID: String? = null ) = "edit/$userID"
//    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IngramTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    IngramAppNavigation()
                }
            }
        }
    }
    @Composable
    fun IngramAppNavigation(){
        val navControler = rememberNavController()
        var vm = hiltViewModel<LCViewModel>()
        val selected = remember {
            mutableStateOf(Icons.Default.Home)
        }
        var showBottomBar by remember { mutableStateOf(true) }


        Scaffold (
            bottomBar = {
                if (showBottomBar) {
                    BottomAppBar {
                        IconButton(
                            onClick = {
                                selected.value = Icons.Default.Home
                                navControler.navigate(DestinationScreen.HomeScreen.route) {
                                    popUpTo(0)
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Default.Home,
                                contentDescription = null,
                                modifier = Modifier.size(26.dp),
                                tint = if (selected.value == Icons.Default.Home) Color.White else Color.Gray
                            )
                        }

                        IconButton(
                            onClick = {
                                selected.value = Icons.Default.Search
                                navControler.navigate(DestinationScreen.Search.route) {
                                    popUpTo(0)
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                modifier = Modifier.size(26.dp),
                                tint = if (selected.value == Icons.Default.Search) Color.White else Color.Gray
                            )
                        }

                        IconButton(
                            onClick = {
                                selected.value = Icons.Default.MailOutline
                                navControler.navigate(DestinationScreen.ChatList.route) {
                                    popUpTo(0)
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Default.MailOutline,
                                contentDescription = null,
                                modifier = Modifier.size(26.dp),
                                tint = if (selected.value == Icons.Default.MailOutline) Color.White else Color.Gray
                            )
                        }
                        IconButton(
                            onClick = {
                                selected.value = Icons.Default.Person
                                navControler.navigate(DestinationScreen.Profile.route) {
                                    popUpTo(0)
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(26.dp),
                                tint = if (selected.value == Icons.Default.Person) Color.White else Color.Gray
                            )
                        }
                    }
                }
            }
        ){  paddingValues ->
            NavHost(navController = navControler,
                startDestination = DestinationScreen.SignUp.route,
                modifier = Modifier.padding(paddingValues)) {
                composable(DestinationScreen.SignUp.route){
                    showBottomBar = false
                    SignUpScreen(navControler,vm) }
                composable(DestinationScreen.Login.route){
                    showBottomBar = false
                    LoginScreen( navController =navControler , vm = vm)
                }
                composable(DestinationScreen.HomeScreen.route){
                    showBottomBar = true
                   HomeScreen(navController = navControler, vm =vm ) }
                composable(DestinationScreen.ChatList.route){
                    showBottomBar = true
                    ChatListScreen(navController = navControler, vm =vm ) }
                composable(DestinationScreen.SingleChat.route){
                    val chatID = it.arguments?.getString("chatID")
                    chatID?.let {
                        showBottomBar = false
                        SingleChatScreen(navController = navControler, vm = vm, chatId = chatID)
                    }
                }
                composable(DestinationScreen.Search.route){
                    showBottomBar = true
                    SearchScreen( navController =navControler , vm = vm)
                }
                composable(DestinationScreen.Profile.route){
                    showBottomBar = true
                    ProfileScreen( navController =navControler , vm = vm)
                }
                composable(DestinationScreen.EditProfle.route){
                    showBottomBar = false
                    EditProfile( navController =navControler , vm = vm)
                }
                composable(DestinationScreen.PostContent.route){
                    showBottomBar = false
                    PostScreen( navController =navControler , vm = vm)
                }
            }
        }
    }
}

