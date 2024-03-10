package com.criticove

import android.graphics.Paint.Style
import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.criticove.m3.ButtonStyles.IconButton

@Composable
fun Topbar(
    pageTitle: String = "Default",
    onMenuClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(colorResource(id = R.color.blue)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(onClick = onMenuClicked) {
            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.hamburger),
                contentDescription = "menu", tint = colorResource(id = R.color.off_white) )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = pageTitle,
                color = colorResource(id = R.color.off_white),
                fontSize = 30.sp,
                fontFamily = FontFamily(Font(R.font.alegreya_sans_bold))
            )
        }

        TextButton(onClick = { TODO() }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.default_profile),
                contentDescription = "profile", tint = colorResource(id = R.color.off_white)
            )
        }
    }
}
