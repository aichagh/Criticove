package com.criticove

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun Navbar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(colorResource(id = R.color.blue)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(onClick = { TODO() }) {
            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.write),
                contentDescription = "new review", tint = colorResource(id = R.color.off_white) )
        }

        TextButton(onClick = { TODO() }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.list),
                contentDescription = "all reviews", tint = colorResource(id = R.color.off_white)
            )
        }

        TextButton(onClick = { TODO() }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.people_white),
                contentDescription = "friends", tint = colorResource(id = R.color.off_white)
            )
        }
    }
}