package com.example.kakaobank.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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

private const val LOAD_MORE_THRESHOLD = 4

@Composable
fun MediaGrid(
    items: List<MediaItem>,
    onItemClick: (MediaItem) -> Unit,
    onBookmarkClick: (MediaItem) -> Unit,
    modifier: Modifier = Modifier,
    isPaginating: Boolean = false,
    onLoadMore: () -> Unit = {},
) {
    val gridState = rememberLazyGridState()
    val shouldLoadMore by remember {
        derivedStateOf {
            val totalItems = gridState.layoutInfo.totalItemsCount
            if (totalItems == 0) return@derivedStateOf false
            val lastVisibleIndex = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                ?: return@derivedStateOf false
            lastVisibleIndex >= totalItems - LOAD_MORE_THRESHOLD
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) onLoadMore()
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = gridState,
        contentPadding = PaddingValues(4.dp),
        modifier = modifier,
    ) {
        items(items, key = { it.thumbnailUrl }) { item ->
            MediaCard(
                item = item,
                onItemClick = onItemClick,
                onBookmarkClick = onBookmarkClick,
            )
        }
        if (isPaginating) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun MediaCard(
    item: MediaItem,
    onItemClick: (MediaItem) -> Unit,
    onBookmarkClick: (MediaItem) -> Unit,
) {
    Card(
        onClick = { onItemClick(item) },
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
    ) {
        Box {
            GlideImage(
                model = item.thumbnailUrl,
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
