package com.niriko.voyotest.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.niriko.voyotest.R
import com.niriko.voyotest.ui.theme.VoyoColor

@Composable
fun MainScreen(
    mediaList: List<Pair<String, List<String>>>,
    isNewDataAvailable: Boolean,
    addNextPage: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            LazyColumn {
                items(mediaList) { pair ->
                    val (category, imageUrls) = pair
                    if (imageUrls.isNotEmpty()) {
                        Text(
                            modifier = Modifier.padding(top = 25.dp, bottom = 5.dp),
                            text = category,
                            color = Color.White
                        )
                    }
                    LazyRow {
                        items(imageUrls) { imageUrl ->
                            Image(
                                painter = rememberAsyncImagePainter(model = imageUrl),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(120.dp)
                            )
                        }
                    }
                }
                item {
                    if (isNewDataAvailable) {
                        Button(
                            onClick = { addNextPage() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = VoyoColor
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text(text = stringResource(id = R.string.add_next_page))
                        }
                    }
                }
            }
        }
    }
}