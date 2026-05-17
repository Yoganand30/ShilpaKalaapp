package com.example.shilpakalaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.shilpakalaapp.data.model.Artwork
import com.example.shilpakalaapp.ui.screens.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {

            ShilpaKalaTheme {

                MainApp()
            }
        }
    }
}

@Composable
fun ShilpaKalaTheme(
    content: @Composable () -> Unit
) {

    val colors = lightColorScheme(

        primary = Color(0xFF6A4CAF),

        secondary = Color(0xFFB89B5E),

        background = Color(0xFFF6F1EB),

        surface = Color.White
    )

    MaterialTheme(

        colorScheme = colors,

        typography = Typography(),

        content = content
    )
}

@Composable
fun MainApp() {

    var loggedIn by remember {

        mutableStateOf(true)
    }

    var selectedArtwork by remember {

        mutableStateOf<Artwork?>(null)
    }

    var screen by remember {

        mutableStateOf("gallery")
    }

    if (!loggedIn) {

        AuthScreen {

            loggedIn = true
        }

    } else {

        Scaffold(

            containerColor =
                MaterialTheme.colorScheme.background,

            bottomBar = {

                NavigationBar(

                    containerColor = Color(0xFF10243F)

                ) {

                    NavigationBarItem(

                        selected = screen == "gallery",

                        onClick = {

                            screen = "gallery"
                        },

                        icon = {

                            Icon(
                                Icons.Default.AccountBalance,
                                contentDescription = null
                            )
                        },

                        label = {

                            Text("Gallery")
                        },

                        colors = NavigationBarItemDefaults.colors(

                            selectedIconColor = Color.White,

                            selectedTextColor = Color.White,

                            indicatorColor = Color(0xFF2F4B7C),

                            unselectedIconColor = Color.LightGray,

                            unselectedTextColor = Color.LightGray
                        )
                    )

                    NavigationBarItem(

                        selected = screen == "upload",

                        onClick = {

                            screen = "upload"
                        },

                        icon = {

                            Icon(
                                Icons.Default.CloudUpload,
                                contentDescription = null
                            )
                        },

                        label = {

                            Text("Upload")
                        },

                        colors = NavigationBarItemDefaults.colors(

                            selectedIconColor = Color.White,

                            selectedTextColor = Color.White,

                            indicatorColor = Color(0xFF2F4B7C),

                            unselectedIconColor = Color.LightGray,

                            unselectedTextColor = Color.LightGray
                        )
                    )

                    NavigationBarItem(

                        selected = screen == "heritage",

                        onClick = {

                            screen = "heritage"
                        },

                        icon = {

                            Icon(
                                Icons.Default.Timeline,
                                contentDescription = null
                            )
                        },

                        label = {

                            Text("Heritage")
                        },

                        colors = NavigationBarItemDefaults.colors(

                            selectedIconColor = Color.White,

                            selectedTextColor = Color.White,

                            indicatorColor = Color(0xFF2F4B7C),

                            unselectedIconColor = Color.LightGray,

                            unselectedTextColor = Color.LightGray
                        )
                    )

                    NavigationBarItem(

                        selected = screen == "artist",

                        onClick = {

                            screen = "artist"
                        },

                        icon = {

                            Icon(
                                Icons.Default.Person,
                                contentDescription = null
                            )
                        },

                        label = {

                            Text("Artist")
                        },

                        colors = NavigationBarItemDefaults.colors(

                            selectedIconColor = Color.White,

                            selectedTextColor = Color.White,

                            indicatorColor = Color(0xFF2F4B7C),

                            unselectedIconColor = Color.LightGray,

                            unselectedTextColor = Color.LightGray
                        )
                    )
                }
            }

        ) { padding ->

            Surface(
                modifier = Modifier.padding(padding)
            ) {

                AnimatedContent(

                    targetState = selectedArtwork,

                    label = ""

                ) { artwork ->

                    if (artwork != null) {

                        DetailScreen(

                            artwork = artwork,

                            onBack = {

                                selectedArtwork = null
                            }
                        )

                    } else {

                        when (screen) {

                            "gallery" -> {

                                GalleryScreen {

                                    selectedArtwork = it
                                }
                            }

                            "upload" -> {

                                UploadScreen()
                            }

                            "heritage" -> {

                                HeritageScreen()
                            }

                            "artist" -> {

                                ArtistProfileScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}