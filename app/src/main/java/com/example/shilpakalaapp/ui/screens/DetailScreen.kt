package com.example.shilpakalaapp.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.shilpakalaapp.data.model.Artwork

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    artwork: Artwork,
    onBack: () -> Unit
) {

    val context = LocalContext.current

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Text("Artwork Details")
                },

                navigationIcon = {

                    IconButton(

                        onClick = {

                            onBack()
                        }

                    ) {

                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }

    ) { padding ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(
                    rememberScrollState()
                )
                .background(
                    Color(0xFFF6F1EB)
                )
        ) {

            AsyncImage(

                model = artwork.imageUrl,

                contentDescription = null,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp),

                contentScale = ContentScale.Crop
            )

            Column(

                modifier = Modifier.padding(20.dp)

            ) {

                Text(

                    text = artwork.title,

                    style = MaterialTheme.typography.headlineMedium,

                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(

                    text = "Artist: ${artwork.artist}",

                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(

                    text = "Category: ${artwork.category}",

                    color = Color.Gray
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Text(

                    text = artwork.description,

                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(
                    modifier = Modifier.height(30.dp)
                )

                Button(

                    onClick = {

                        val message =
                            "Hello, I am interested in this artwork: ${artwork.title}"

                        val intent = Intent(

                            Intent.ACTION_VIEW,

                            Uri.parse(
                                "https://wa.me/?text=$message"
                            )
                        )

                        context.startActivity(intent)
                    },

                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(20.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF25D366)
                    )

                ) {

                    Icon(
                        Icons.Default.Chat,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Text("Enquire on WhatsApp")
                }
            }
        }
    }
}