package com.example.ingram.Components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.ingram.R
import com.example.ingram.ui.theme.Bule1

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

//@Preview
//@Composable
//fun display(){
//    CommentScreen()
//}