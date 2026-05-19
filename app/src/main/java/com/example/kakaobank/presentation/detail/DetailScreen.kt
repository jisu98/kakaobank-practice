package com.example.kakaobank.presentation.detail

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.kakaobank.domain.model.MediaItem
import com.example.kakaobank.domain.model.MediaType
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer as YTPlayer
import androidx.core.net.toUri

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailScreen(
    item: MediaItem,
    onBack: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        if (item.type == MediaType.IMAGE) {
            GlideImage(
                model = item.imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit,
            )
        } else {
            YouTubePlayer(
                videoUrl = item.videoUrl,
                modifier = Modifier.fillMaxSize(),
            )
        }
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(8.dp),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "뒤로가기",
                tint = Color.White,
            )
        }
    }
}

@Composable
private fun YouTubePlayer(
    videoUrl: String,
    modifier: Modifier = Modifier,
) {
    val videoId = extractYoutubeVideoId(videoUrl) ?: return
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val playerView = remember(context) {
        YouTubePlayerView(context).apply {
            lifecycleOwner.lifecycle.addObserver(this)
            addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YTPlayer) {
                    youTubePlayer.loadVideo(videoId, 0f)
                }
            })
        }
    }

    DisposableEffect(lifecycleOwner) {
        onDispose { playerView.release() }
    }

    AndroidView(
        factory = { playerView },
        modifier = modifier,
    )
}

private fun extractYoutubeVideoId(url: String): String? {
    if (url.contains("youtu.be/")) {
        return url.substringAfter("youtu.be/").substringBefore("?")
    }
    if (url.contains("youtube.com/watch")) {
        return url.toUri().getQueryParameter("v")
    }
    return null
}
