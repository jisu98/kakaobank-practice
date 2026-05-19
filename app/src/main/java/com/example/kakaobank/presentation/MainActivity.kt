package com.example.kakaobank.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.kakaobank.presentation.navigation.KakaoBankNavGraph
import com.example.kakaobank.presentation.ui.theme.KakaoBankTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KakaoBankTheme {
                KakaoBankNavGraph()
            }
        }
    }
}
