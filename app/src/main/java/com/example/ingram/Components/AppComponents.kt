package com.example.ingram.Components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ingram.R
import com.example.ingram.ui.theme.Purple40

@Composable
fun textHeadComponent(value:String,modifier: Modifier = Modifier){
    var pacificoFamily = FontFamily(
        Font(R.font.pacifico, FontWeight.Bold)
    )
    Text(
        text = value,
        fontSize = 40.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = pacificoFamily
    )
}
@Composable
fun TextComponent(value: String){
    Text(text = value, fontSize = 13.sp, color = Purple40
    )
}
@Composable
fun DividerTextComponent(){
    Row(modifier = Modifier.width(290.dp),
        verticalAlignment = Alignment.CenterVertically){
        Divider(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            ,color = Color.Gray,
            thickness = 1.dp)
        Text(text = " Hoặc ", fontSize = 13.sp, color = Color.Gray,
            modifier = Modifier.padding(8.dp))
        Divider(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            ,color = Color.Gray,
            thickness = 1.dp)
    }
}