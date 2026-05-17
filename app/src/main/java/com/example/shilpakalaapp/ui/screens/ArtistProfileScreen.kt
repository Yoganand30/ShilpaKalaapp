package com.example.shilpakalaapp.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.shilpakalaapp.data.model.Artist

@Composable
fun ArtistProfileScreen() {

    val artist = Artist(

        name = "Ravi Shilpi",

        village = "Shivapatna, Karnataka",

        experience = "25 Years",

        speciality = "Hoysala Stone Carving",

        imageUrl =
            "https://images.unsplash.com/photo-1500648767791-00dcc994a43e"
    )

    Column(

        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F1EB))
            .padding(20.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AsyncImage(

            model = artist.imageUrl,

            contentDescription = null,

            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape),

            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(

            text = artist.name,

            style = MaterialTheme.typography.headlineMedium,

            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(

            shape = RoundedCornerShape(20.dp)

        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text("Village: ${artist.village}")

                Spacer(modifier = Modifier.height(10.dp))

                Text("Experience: ${artist.experience}")

                Spacer(modifier = Modifier.height(10.dp))

                Text("Speciality: ${artist.speciality}")
            }
        }
    }
}