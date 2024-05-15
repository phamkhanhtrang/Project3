package com.example.ingram.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
fun SearchComponets(modifier: Modifier = Modifier){
    Column (modifier = Modifier
        .fillMaxWidth()
//        .background(color = Color.White)
        .padding(bottom = 1.dp)){
        Spacer(modifier = Modifier.height(3.dp))
        ConstraintLayout (modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)){
            val (imgAvatar, username )= createRefs()
            Image(painter = painterResource(  R.drawable.avata3), contentDescription = "profile",
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)

                    .constrainAs(imgAvatar) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    },
                contentScale = ContentScale.Crop)

            Text(text = "Changg", fontSize = 12.sp, maxLines = 1,modifier = Modifier
                .width(100.dp)
                .constrainAs(username) {
                    start.linkTo(imgAvatar.end, margin = 6.dp)
                    top.linkTo(parent.top, margin = 20.dp)
                    width = Dimension.fillToConstraints
                },
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
        }
    }
}