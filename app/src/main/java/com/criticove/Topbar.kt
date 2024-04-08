package com.criticove

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun Topbar(
    pageTitle: String = "Default",
    onMenuClicked: () -> Unit,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(colorResource(id = R.color.blue))
    ) {
        TextButton(onClick = onMenuClicked) {
            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.hamburger),
                contentDescription = "menu", tint = colorResource(id = R.color.off_white) )
        }

        Row(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
            ) {
            Text(
                text = pageTitle,
                color = colorResource(id = R.color.off_white),
                fontSize = 30.sp,
                fontFamily = FontFamily(Font(R.font.alegreya_sans_bold)),
                textAlign = TextAlign.Center,
            )
        }
    }
}
