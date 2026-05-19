package com.example.mygamecolor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.mygamecolor.navigation.AppNavigation
import com.example.mygamecolor.ui.theme.MyGameColorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyGameColorTheme {
                Surface {
                    AppNavigation()
                }
            }
        }
    }
}