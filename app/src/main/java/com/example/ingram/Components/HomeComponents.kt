package com.example.ingram.Components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.ingram.R

@Composable
fun IconAttractive(modifier: Modifier = Modifier,
                   @DrawableRes icon:Int,

                   onClick:()->Unit){
    Row (modifier= modifier.clickable{onClick()},
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Bottom

    ){
        Icon(painter = painterResource(id = icon),
            contentDescription = "icon",
            modifier= Modifier
                .size(25.dp)
                .clickable { onClick() },

            )
    }
}

@Composable
fun FeedIcon(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    contentDescription: String,
    color: Color,
    onClick: () -> Unit,

    ) {
    Row (modifier= modifier.clickable{onClick()},
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Bottom

    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = contentDescription,
            modifier = Modifier
                .size(25.dp)
//            .padding(end = spacingLarge)
                .clickable { onClick() },
            colorFilter = ColorFilter.tint(color)
        )
    }
}
@Composable
fun CommentScreen(){
    Column (modifier = Modifier
        .background(color = Color.White)
        .fillMaxSize()){
        Text(text = "Bình luận", )
        Divider(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp)
                .height(1.dp))
        UserComment()
    }
}
@Composable
fun UserComment() {
    val likeIcon = R.drawable.ic_notification
    val likedIcon = R.drawable.ic_liked
    var isLiked by rememberSaveable { mutableStateOf(false) }
    val iconsColor = MaterialTheme.colorScheme.onBackground
    val likedColor = if (isLiked) Color.Red else iconsColor
    Column (modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween ){
        ConstraintLayout {
            val (imgAvatar, imgHeart, username, textTime , textContent, textLike,textreply)= createRefs()

            Image(painter = painterResource(id = R.drawable.anh3), contentDescription = "profile",
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)

                    .constrainAs(imgAvatar) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start, margin = 10.dp)
                    },
                contentScale = ContentScale.Crop)
            Text(
                text = "Khánh Trang ", fontSize = 12.sp, maxLines = 1, modifier = Modifier
                    .width(100.dp)
                    .constrainAs(username) {
                        start.linkTo(imgAvatar.end, margin = 6.dp)
//                     end.linkTo(imgMore.start, margin = 13.dp)
                        top.linkTo(parent.top, margin = 5.dp)
                        width = Dimension.fillToConstraints
                    },
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "2 giờ", fontSize = 12.sp, maxLines = 1,  color = Color.Gray
                ,modifier = Modifier
                    .width(100.dp)
                    .constrainAs(textTime) {
                        start.linkTo(username.end, margin = -10.dp)
                        top.linkTo(parent.top, margin = 5.dp)
                        width = Dimension.fillToConstraints
                    },
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "ifbrbfeifhe hfr h ehuf he gfwgf g  igfwgfowgfo wfuo hfi fgfgw", fontSize = 12.sp,
                maxLines = 3 ,
                modifier = Modifier
                    .width(100.dp)
                    .constrainAs(textContent) {
                        start.linkTo(imgAvatar.end, margin = 6.dp)
                        end.linkTo(imgHeart.start, margin = 46.dp)
                        top.linkTo(username.top, margin = 20.dp)

                        width = Dimension.fillToConstraints
                    },
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(

                )
            )
            FeedIcon(
                icon = if (isLiked) likedIcon else likeIcon,
                contentDescription = "null",
                color = likedColor,
                modifier = Modifier.constrainAs(imgHeart){
                    start.linkTo(textTime.end, margin = 110.dp)
                    top.linkTo(parent.top, margin = 5.dp)
                }
            )
            {
                isLiked = !isLiked
            }
            Text(
                text = "1", fontSize = 12.sp,
                modifier = Modifier
                    .width(100.dp)
                    .constrainAs(textLike) {
                        start.linkTo(textContent.end, margin = 55.dp)
                        top.linkTo(imgHeart.top, margin = 30.dp)
                    },
            )
//        Text(
//            text = "Trả lời", fontSize = 12.sp,
//            modifier = Modifier
//                .width(100.dp)
//                .constrainAs(textreply ) {
//                    start.linkTo(parent.start, margin = 35.dp)
//                    top.linkTo(textContent.top, margin = 30.dp)
//                },
//            fontWeight = FontWeight.Bold,
//            color =  Color.Gray
//            )
        }
        Row {
            Image(painter = painterResource(id = R.drawable.anh3), contentDescription = "profile",
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop)
            TextField(value = "", onValueChange = {},
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.size(500.dp, 12.dp)
            )
        }



    }


}