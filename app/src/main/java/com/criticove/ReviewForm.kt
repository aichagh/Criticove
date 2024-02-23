package com.criticove


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview

class ReviewForm : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(colorResource(id = R.color.off_white))
            ) {
                Column() {
                    ReviewHeader()
                    Selection()
                }
            }
        }
    }
}

@Composable

fun ReviewHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(colorResource(id = R.color.blue)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "New Review",
            color = colorResource(id = R.color.white),
            fontSize = 20.sp

        )
    }
}
@Composable
fun Selection() {
    var selectedType by remember { mutableStateOf("Book") }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .padding(horizontal = 10.dp)
                .background(colorResource(id = R.color.off_white)),
        ) {
                RadioButton(
                    selected = selectedType == "Book",
                    onClick = { selectedType = "Book" }
                )
                Text(
                    text = "Book",
                    modifier = Modifier.padding(start = 4.dp)
                )
            RadioButton(
                selected = selectedType == "TV Show",
                onClick = { selectedType = "TV Show" }

            )
            Text(
                text = "TV Show"
            )

            RadioButton(
                selected = selectedType == "Movie",
                onClick = { selectedType = "Movie" }

            )
            Text(
                text = "Movie"

            )
        }
    Form(selectedType)
}

@Composable
fun Form(type: String) {
    if (type == "Book") {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.off_white))
                .padding(10.dp)
        ) {
            Column() {
                var text by remember { mutableStateOf(TextFieldValue()) }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column() {
                        Text(
                            text = "Book Title"
                        )
                        OutlinedTextField(
                            value = text,
                            onValueChange = { text = it },
                            label = {
                                Text(
                                    "Book Title",
                                    color = colorResource(id = R.color.blue)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column() {
                        Text(
                            text = "Author"
                        )
                        OutlinedTextField(
                            value = text,
                            onValueChange = { text = it },
                            label = {
                                Text(
                                    "Author",
                                    color = colorResource(id = R.color.blue)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column() {
                        Text(
                            text = "Date Published"
                        )
                        OutlinedTextField(
                            value = text,
                            onValueChange = { text = it },
                            label = {
                                Text(
                                    "Date Published",
                                    color = colorResource(id = R.color.blue)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column() {
                        Text(
                            text = "Genre"
                        )
                        OutlinedTextField(
                            value = text,
                            onValueChange = { text = it },
                            label = {
                                Text(
                                    "Genre",
                                    color = colorResource(id = R.color.blue)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column() {
                        Text(
                            text = "Book Type"
                        )
                        OutlinedTextField(
                            value = text,
                            onValueChange = { text = it },
                            label = {
                                Text(
                                    "Book Type",
                                    color = colorResource(id = R.color.blue)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }

        }

    } else if (type == "TV Show") {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.off_white))
                .padding(10.dp)
        ) {
            Column() {
                var text by remember { mutableStateOf(TextFieldValue()) }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column() {
                        Text(
                            text = "TV Show Title"
                        )
                        OutlinedTextField(
                            value = text,
                            onValueChange = { text = it },
                            label = {
                                Text(
                                    "TV show Title",
                                    color = colorResource(id = R.color.blue)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column() {
                        Text(
                            text = "Publication Company"
                        )
                        OutlinedTextField(
                            value = text,
                            onValueChange = { text = it },
                            label = {
                                Text(
                                    "Publication Company",
                                    color = colorResource(id = R.color.blue)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column() {
                        Text(
                            text = "Date Released"
                        )
                        OutlinedTextField(
                            value = text,
                            onValueChange = { text = it },
                            label = {
                                Text(
                                    "Date Released",
                                    color = colorResource(id = R.color.blue)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column() {
                        Text(
                            text = "Genre"
                        )
                        OutlinedTextField(
                            value = text,
                            onValueChange = { text = it },
                            label = {
                                Text(
                                    "Genre",
                                    color = colorResource(id = R.color.blue)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column() {
                        Text(
                            text = "Book Type"
                        )
                        OutlinedTextField(
                            value = text,
                            onValueChange = { text = it },
                            label = {
                                Text(
                                    "Book Type",
                                    color = colorResource(id = R.color.blue)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }

        }

    } else if (type == "Movie") {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.off_white))
                .padding(10.dp)
        ) {
            Column() {
                var text by remember { mutableStateOf(TextFieldValue()) }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column() {
                        Text(
                            text = "Movie Title"
                        )
                        OutlinedTextField(
                            value = text,
                            onValueChange = { text = it },
                            label = {
                                Text(
                                    "Movie Title",
                                    color = colorResource(id = R.color.blue)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column() {
                        Text(
                            text = "Publication Company"
                        )
                        OutlinedTextField(
                            value = text,
                            onValueChange = { text = it },
                            label = {
                                Text(
                                    "Publication Company",
                                    color = colorResource(id = R.color.blue)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column() {
                        Text(
                            text = "Year Released"
                        )
                        OutlinedTextField(
                            value = text,
                            onValueChange = { text = it },
                            label = {
                                Text(
                                    "Year Released",
                                    color = colorResource(id = R.color.blue)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column() {
                        Text(
                            text = "Genre"
                        )
                        OutlinedTextField(
                            value = text,
                            onValueChange = { text = it },
                            label = {
                                Text(
                                    "Genre",
                                    color = colorResource(id = R.color.blue)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column() {
                        Text(
                            text = "Book Type"
                        )
                        OutlinedTextField(
                            value = text,
                            onValueChange = { text = it },
                            label = {
                                Text(
                                    "Book Type",
                                    color = colorResource(id = R.color.blue)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }

        }

    }
}

@Preview
@Composable
fun PreviewRadio() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(colorResource(id = R.color.off_white))
    ) {
        Column() {
            ReviewHeader()
            Selection()
        }
    }
}

