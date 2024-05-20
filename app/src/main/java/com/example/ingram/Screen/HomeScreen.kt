package com.example.ingram.Screen

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.ingram.Components.FeedIcon
import com.example.ingram.Components.IconAttractive
import com.example.ingram.DestinationScreen
import com.example.ingram.LCViewModel
import com.example.ingram.Model.Stories
import com.example.ingram.R
import com.example.ingram.data.PostData
import com.example.ingram.navigateTo
import com.example.ingram.ui.theme.Bule1
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")

@Composable
fun HomeScreen(navController: NavController, vm : LCViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
    ) {
        Heard(
            onMessClick={
                navigateTo(navController,DestinationScreen.ChatList.route)
            })
        StorySection(vm = vm, navController =navController )

        Divider(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp)
                .height(2.dp)
        )
        PostSection(vm=vm)

    }
}

@Composable
fun Heard(modifier: Modifier = Modifier,
          onMessClick:()->Unit
){
    val pacificoFamily = FontFamily(
        Font(R.font.pacifico, FontWeight.Bold)
    )
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .size(450.dp, 50.dp)
            .background(color = Color.White),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        ConstraintLayout (modifier=modifier.fillMaxWidth()) {
            val (nameapp, iconNoti, iconMess )=createRefs()
            Text(text = stringResource(id = R.string.instagram),
                fontSize = 20.sp,
                fontFamily = pacificoFamily,
                modifier= Modifier.constrainAs(nameapp){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start, margin = (15.dp))
                }
            )
            IconAttractive(icon = R.drawable.messenger ,
                modifier=modifier.constrainAs(iconMess){
                    top.linkTo(parent.top, margin = 10.dp)
                    end.linkTo(parent.end, margin = 15.dp)
                }) {
                onMessClick()
            }
        }
    }
}





@Composable
fun PostSection(vm: LCViewModel,) {
    val postdata = vm.Post.value
    var currentPostID by rememberSaveable { mutableStateOf<String?>(null) }
    val content= LocalContext.current
    if (postdata.isEmpty()) {
        Text(text = "Không có bài đăng")
    } else {
        currentPostID?.let { postID ->
            Modalbotom(vm = vm, postID = postID) {
                currentPostID = null // Reset postID after closing ModalBottom
            }
        }
        LazyColumn {
            items(postdata) { post ->
                PostItem(
                    postID = post.postID,
                    imageProfile = post.user.imageUrl,
                    name = post.user.name,
                    content = post.content,
                    vm = vm,
                    imagePostUrl = post.imageUrl,
                    onCommentClick = { postID -> currentPostID = postID },
                    onShareClick = {
                        val shareText=""""
                            Thông Tin bài viết:${post.content}
                            
                            Được viết bởi:${post.content}
                        """.trimIndent()
                        vm.shareOrder(content,"chia sẻ" ,post.imageUrl,shareText ) }
                )
            }
        }
    }

}

@OptIn( ExperimentalFoundationApi::class)
@Composable
fun PostItem(modifier: Modifier = Modifier,
             imagePostUrl:List<String>?,
             imageProfile: String?,
             name:String?,
             postID: String?,
             content: String?,
             vm:LCViewModel,
             onCommentClick: (String) -> Unit,
             onShareClick: (String) -> Unit

){
      val painterProfile = rememberImagePainter(data = imageProfile)
    Column (modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 10.dp)){
        val pagerState = rememberPagerState(pageCount = { imagePostUrl?.size ?: 0 })
        Spacer(modifier = Modifier.height(3.dp))
        ConstraintLayout (modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)){
            val (imgAvatar, imgMore, username )= createRefs()
            Image(painter = painterProfile, contentDescription = "profile",
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)

                    .constrainAs(imgAvatar) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    },
                contentScale = ContentScale.Crop)

            Text(text =name ?:"", fontSize = 12.sp, maxLines = 1,modifier = Modifier
                .width(100.dp)
                .constrainAs(username) {
                    start.linkTo(imgAvatar.end, margin = 6.dp)
                    end.linkTo(imgMore.start, margin = 13.dp)
                    top.linkTo(parent.top, margin = 10.dp)
                    width = Dimension.fillToConstraints
                },
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    fontWeight = FontWeight.Bold
                )
            )

            Icon(imageVector = Icons.Default.MoreVert , contentDescription ="more",
                modifier= Modifier
                    .size(24.dp)
                    .padding(end = 10.dp)
                    .constrainAs(imgMore) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
            )
        }
        Spacer(modifier = Modifier.height(7.dp))
        PostCarousel( data = imagePostUrl, pagerState = pagerState)

        sideBarview(
            onLikeClick = { /*TODO*/ },
            onCommentClick = { onCommentClick(postID.toString()) },
            onShareClick = { onShareClick(postID.toString())  }
        ){

        }
        val annotatedString = buildAnnotatedString {
            withStyle(style = SpanStyle(Color.Black, fontWeight = FontWeight.Bold)){
                append("${name} ")
            }
            append(content)
        }
        Text(
            text = annotatedString,
            fontSize = 12.sp,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
    }
}

@OptIn( ExperimentalFoundationApi::class)
@Composable
fun PostCarousel(data: List<String>?,pagerState: PagerState) {
    Box(modifier = Modifier.fillMaxWidth()) {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxWidth()) { page ->
            Image(
                painter = rememberImagePainter(data = data?.getOrNull(page)),
                contentDescription = "post image",
                modifier = Modifier.aspectRatio(16f / 16f),
                contentScale = ContentScale.Crop
            )
        }
    }

    Row(
        Modifier
            .fillMaxWidth()
            .offset(y = 21.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration) Bule1 else Color.LightGray
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(8.dp)
            )
        }

    }

}

@SuppressLint("SuspiciousIndentation")
@Composable
fun sideBarview(
    modifier: Modifier = Modifier,
    onLikeClick:() ->Unit,
    onCommentClick:() -> Unit,
    onShareClick:()->Unit,
    onSaveClick:()->Unit
){
    val likeIcon = R.drawable.ic_notification
    val likedIcon = R.drawable.ic_liked
    var isLiked by rememberSaveable { mutableStateOf(false) }
    val iconsColor = MaterialTheme.colorScheme.onBackground
    val likedColor = if (isLiked) Color.Red else iconsColor
    ConstraintLayout(modifier=modifier.fillMaxWidth()){
        val (iconLike, iconComment, iconShare, iconSave, iconImage) = createRefs()
        FeedIcon(
            icon = if (isLiked) likedIcon else likeIcon,
            contentDescription = "null",
            color = likedColor,
            modifier = Modifier.constrainAs(iconLike){
                top.linkTo(parent.top)
                start.linkTo(parent.start, margin = 20.dp)
            }
        )
        {
            isLiked = !isLiked
        }
        IconAttractive(icon = R.drawable.save,
            modifier = modifier.constrainAs(iconSave){
                top.linkTo(parent.top)
                end.linkTo(parent.end, margin = 15.dp)
            }) {

            onSaveClick()
        }

        IconAttractive(icon = R.drawable.comment,
            modifier = modifier.constrainAs(iconComment){
                start.linkTo(iconLike.end, margin = 20.dp)

            }) {
            onCommentClick()
        }

        IconAttractive(icon = R.drawable.send,
            modifier = modifier.constrainAs(iconShare){
                start.linkTo(iconComment.end, margin = 20.dp)
                top.linkTo(parent.top)
            } ) {
            onShareClick( )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Modalbotom(vm: LCViewModel, postID: String, onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by rememberSaveable { mutableStateOf(true) }

    if (isSheetOpen) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                isSheetOpen = false
                onDismiss()
            },
            modifier = Modifier.fillMaxSize()
        ) {
            CommentScreen(vm = vm, postID = postID)
        }
    }
}