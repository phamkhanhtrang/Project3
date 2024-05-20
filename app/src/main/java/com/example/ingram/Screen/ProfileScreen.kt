package com.example.ingram.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.ingram.CommonProgressBar
import com.example.ingram.DestinationScreen
import com.example.ingram.LCViewModel
import com.example.ingram.Model.StoryHighlights
import com.example.ingram.Model.TabRowIcons
import com.example.ingram.R
import com.example.ingram.navigateTo


@Composable
fun ProfileScreen( navController: NavController, vm : LCViewModel) {
    val inProcess = vm.inProcess.value
    if (inProcess){
        CommonProgressBar()
    }else {
        val userData = vm.userData.value
        val name by rememberSaveable {
            mutableStateOf(userData?.name ?: "")
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                Row {
                    Text(
                        text = name,
                        modifier = Modifier.padding(start = 20.dp, bottom = 6.dp, top = 20.dp),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                }
            },
            content = { contentPadding ->
                MainContent(
                    modifier = Modifier.padding(contentPadding),
                    vm,
                    navController = navController
                )
            }
        )
    }
}

@Composable
fun MainContent(modifier: Modifier, vm : LCViewModel, navController: NavController) {

    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }
    Column(modifier.fillMaxSize()) {
        ProfileSection(vm = vm, navController = navController)
        Spacer(modifier = Modifier.height(20.dp))
        PostsTabView(onTabSelected = { index ->
            selectedTabIndex = index
        })
        when (selectedTabIndex) {
            0 -> PostsSection()
        }
        when (selectedTabIndex) {
            1 -> PostsSection()
        }
    }
}

@Composable
fun PostsSection() {
    val posts = listOf(
        painterResource(id = R.drawable.image_post_1),
        painterResource(id = R.drawable.image_post_2)

    )
    LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.scale(1.01f), content = {
        items(posts.size) {
            Image(
                painter = posts[it], contentDescription = "", contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(1f)
                    .border(
                        width = 1.dp,
                        color = Color.White
                    )
            )
        }
    })
}

@Composable
fun PostsTabView(
    modifier: Modifier = Modifier,
    onTabSelected: (selectedIndex: Int) -> Unit
) {
    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }
    val tabIcons = listOf(
        TabRowIcons(R.drawable.ic_grid),
        TabRowIcons(R.drawable.instagram_reels_icon),
        TabRowIcons(R.drawable.instagram_tag_icon)
    )
    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier
    ) {
        tabIcons.forEachIndexed { index, tabRowIcons ->
            Tab(
                selected = index == selectedTabIndex,
                onClick = {
                    selectedTabIndex = index
                    onTabSelected(index)
                },
                icon = {
                    Icon(
                        painter = painterResource(id = tabRowIcons.icon), contentDescription = "",
                        modifier.size(20.dp),
                        tint = if (selectedTabIndex == index) Color.Black else Color.Gray
                    )
                },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Gray
            )
        }
    }
}

@Composable
fun ProfileSection(modifier: Modifier = Modifier,  vm: LCViewModel, navController: NavController) {
 val textname = vm.userData.value?.name
    val textphone = vm.userData.value?.number
    val textnote = vm.userData.value?.note
    val imageProfile = vm.userData.value?.imageUrl

    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            ImageBuilder(
                image = imageProfile ,
                 modifier = modifier
                     .size(100.dp)
                     .weight(3f)
            )
        }
        BioSection(
            name = textname.toString(),
            activityLabel = textphone.toString(),
            description = textnote.toString()
        )

        UpdateAndPost(navController = navController, vm )

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun UpdateAndPost(navController: NavController ,vm: LCViewModel) {
     val inProcess = vm.inProcess.value
    if (inProcess) {
        CommonProgressBar()
    } else {
        Row {
            Button(onClick = {
                navigateTo(navController, DestinationScreen.EditProfle.route)
            }) {
                Row {
                    Text(text = "Chỉnh sửa thông tin")
                    Icon(Icons.Default.Person, contentDescription = null)
                }

            }
            Text(text = "Đăng xuất", modifier = Modifier.clickable {
                vm.logout()
                navigateTo(navController,DestinationScreen.Login.route)
            })
        }
    }

}

@Composable
fun BioSection(
    name: String,
    activityLabel: String,
    description: String,

) {
    val letterSpacing = 0.5.sp
    val lineHeight = 20.sp
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {

        Text(
            text = name,

            letterSpacing = letterSpacing,
            lineHeight = lineHeight,
            fontSize = 15.sp
        )
        Text(
            text = activityLabel,
            fontWeight = FontWeight.Medium,
            letterSpacing = letterSpacing,
            lineHeight = lineHeight,
            color = Color.Gray
        )
        Text(
            text = description,
            fontWeight = FontWeight.Medium,
            letterSpacing = letterSpacing,
            lineHeight = lineHeight
        )
    }
}

@Composable
fun ImageBuilder(image: String?, modifier: Modifier) {
val imageProfile = rememberImagePainter(data = image)
    Image(
        painter = imageProfile, contentDescription = "",
        modifier = modifier
            .aspectRatio(1f, matchHeightConstraintsFirst = true)
            .border(width = 2.dp, color = Color.LightGray, shape = CircleShape)
            .clip(CircleShape)
    )
}


