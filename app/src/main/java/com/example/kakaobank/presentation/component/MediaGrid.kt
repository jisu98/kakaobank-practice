package com.example.kakaobank.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.kakaobank.R
import com.example.kakaobank.domain.model.MediaItem
import com.example.kakaobank.domain.model.MediaType

@Composable
fun MediaGrid(
    items: List<MediaItem>,
    onBookmarkClick: (MediaItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(4.dp),
        modifier = modifier,
    ) {
        items(items, key = { it.imageUrl }) { item ->
            MediaCard(item = item, onBookmarkClick = onBookmarkClick)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun MediaCard(
    item: MediaItem,
    onBookmarkClick: (MediaItem) -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
    ) {
        Box {
            GlideImage(
                model = item.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentScale = ContentScale.Crop,
            )
            Text(
                text = if (item.type == MediaType.IMAGE) "IMAGE" else "VIDEO",
                fontSize = 10.sp,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(4.dp),
            )
            IconButton(
                onClick = { onBookmarkClick(item) },
                modifier = Modifier.align(Alignment.TopEnd),
            ) {
                Icon(
                    painter = painterResource(
                        if (item.isBookmarked) R.drawable.ic_bookmark_filled else R.drawable.ic_bookmark_outline,
                    ),
                    contentDescription = null,
                    tint = Color.Unspecified,
                )
            }
        }
        Text(
            text = item.datetime.take(10),
            fontSize = 12.sp,
            modifier = Modifier.padding(6.dp),
        )
    }
}
