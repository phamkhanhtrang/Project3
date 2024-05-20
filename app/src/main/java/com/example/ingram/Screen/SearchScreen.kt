package com.example.ingram.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.ingram.Components.SearchComponets
import com.example.ingram.DestinationScreen
import com.example.ingram.LCViewModel
import com.example.ingram.R
import com.example.ingram.navigateTo
import com.example.ingram.ui.theme.Bule1
import com.example.ingram.ui.theme.Gray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController, vm : LCViewModel) {
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    val searchHistory = listOf("Android", "Kotlin", "Compose", "Material Design", "GPT-4")

    DockedSearchBar(
        modifier = Modifier.background(color = Color.White),
//        modifier = Modifier.size(width = 300.dp, height = 50.dp),
        colors = SearchBarDefaults.colors(Gray),
        shape = RoundedCornerShape(12.dp, 12.dp, 12.dp, 12.dp),
        query = query,
        onQueryChange = { query = it },
        onSearch = { newQuery ->
            println("Performing search on query: $newQuery")
        },
        active = active,
        onActiveChange = { active = it },
        placeholder = {
            Text(text = "Search")
        },
        leadingIcon = {
            Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
        },
        trailingIcon = if (active) {
            {
                IconButton(onClick = { if (query.isNotEmpty()) query = "" else active = false }) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
                }
            }
        } else null
    ) {
        ConstraintLayout(modifier = Modifier.background(color = Color.White)) {
            var (text1, text2) = createRefs()
            Text(text = "Gần đây", fontSize = 20.sp,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                modifier = Modifier.constrainAs(text1) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start, margin = 5.dp)
                }
            )
            Text(text = "Xem tất cả", fontSize = 18.sp,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = Bule1
                ),
                modifier = Modifier.constrainAs(text2) {
                    top.linkTo(parent.top)
                    start.linkTo(text1.end, margin = 164.dp)
                }
            )
        }

//        searchHistory.takeLast(5).forEach{item->
//            ListItem(
//                modifier = Modifier.clickable { query = item },
//                headlineContent = { Text(text = item) },
//                leadingContent = {
//                    SearchComponets(modifier = Modifier.fillMaxSize())
//                }
//            )
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)) {

            items(2) { item ->
                SearchComponets(modifier = Modifier.fillMaxSize())
            }

        }

    }

}
//
//@Composable
//fun DiscoverScreen(){
//    val posts = listOf(
//        painterResource(id = R.drawable.image_post_1),
//        painterResource(id = R.drawable.image_post_2),
//        painterResource(id = R.drawable.image_post_2),
//        painterResource(id = R.drawable.anh1),
//        painterResource(id = R.drawable.anh3)
//
//    )
//    LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.scale(1.01f), content = {
//        items(posts.size) {
//            Image(
//                painter = posts[it], contentDescription = "", contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .aspectRatio(1f)
//                    .border(
//                        width = 1.dp,
//                        color = Color.White
//                    )
//            )
//        }
//    })
//}
