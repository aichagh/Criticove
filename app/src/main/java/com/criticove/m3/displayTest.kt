package com.criticove.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.criticove.m3.ButtonStyles
import androidx.compose.ui.tooling.preview.Preview
import com.criticove.m3.M3Icons
import androidx.compose.material3.Icon


@Composable
fun DisplayAllButtons() {
    Column {
        ButtonStyles.PrimaryButton(onClick = {}, text = "Primary")
        Spacer(modifier = Modifier.height(8.dp))
        ButtonStyles.SecondaryButton(onClick = {}, text = "Secondary")
        Spacer(modifier = Modifier.height(8.dp))
        ButtonStyles.TertiaryButton(onClick = {}, text = "Tertiary")
        Spacer(modifier = Modifier.height(8.dp))
        ButtonStyles.EditButton(onClick = { /*TODO*/ }, text = "Edit")
        Spacer(modifier = Modifier.height(8.dp))
        ButtonStyles.IconButton(onClick = { /*TODO*/ }, icon = M3Icons.LiveTv, description = "Movie" )
        Spacer(modifier = Modifier.height(8.dp))
        Icon(M3Icons.BookmarkUnchecked, contentDescription = "Bookmark")
        Spacer(modifier = Modifier.height(8.dp))
        ButtonStyles.QuaternaryButton(onClick = {}, text = "Elevated")
        Spacer(modifier = Modifier.height(8.dp))
        ButtonStyles.QuinaryButton(onClick = {}, text = "Text")
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewAllButtons() {
    DisplayAllButtons()
}
