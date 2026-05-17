package com.example.shilpakalaapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.shilpakalaapp.data.model.Artwork
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun GalleryScreen(
    onSelect: (Artwork) -> Unit
) {

    val db = FirebaseFirestore.getInstance()

    var artworks by remember {
        mutableStateOf(listOf<Artwork>())
    }

    var search by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {

        db.collection("artworks")
            .addSnapshotListener { value, _ ->

                artworks = value?.documents?.map {

                    Artwork(
                        id = it.id,
                        title = it.getString("title") ?: "",
                        description = it.getString("description") ?: "",
                        category = it.getString("category") ?: "",
                        imageUrl = it.getString("imageUrl") ?: "",
                        artist = it.getString("artist") ?: ""
                    )
                } ?: emptyList()
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFFF6F1EB)
            )
    ) {

        Text(
            text = "ShilpaKala",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(
                start = 20.dp,
                top = 20.dp
            )
        )

        Text(
            text = "Ancient Art • Modern Showcase",
            color = Color.Gray,
            modifier = Modifier.padding(
                start = 20.dp,
                bottom = 10.dp
            )
        )

        OutlinedTextField(
            value = search,
            onValueChange = {
                search = it
            },
            placeholder = {
                Text("Search sculptures")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(12.dp)
        ) {

            items(
                artworks.filter {
                    it.title.contains(search, true)
                }
            ) { art ->

                PremiumArtworkCard(
                    artwork = art,
                    onClick = {
                        onSelect(art)
                    }
                )
            }
        }
    }
}

@Composable
fun PremiumArtworkCard(
    artwork: Artwork,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(300.dp)
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(28.dp),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Box {

            AsyncImage(
                model = artwork.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),

                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {

                Text(
                    artwork.title,
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    artwork.category,
                    color = Color.LightGray
                )

                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(
                            Color(0xFFD4AF37)
                        )
                        .padding(
                            horizontal = 12.dp,
                            vertical = 4.dp
                        )
                ) {

                    Text(
                        artwork.artist,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}