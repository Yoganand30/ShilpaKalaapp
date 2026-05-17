package com.example.shilpakalaapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.shilpakalaapp.data.model.Timeline

@Composable
fun TimelineScreen() {

    val stages = listOf(

        Timeline(
            "Stone Selection",
            "Selecting premium soapstone for carving.",
            ""
        ),

        Timeline(
            "Rough Carving",
            "Initial structure is shaped carefully.",
            ""
        ),

        Timeline(
            "Detail Engraving",
            "Intricate Hoysala patterns are added.",
            ""
        ),

        Timeline(
            "Polishing",
            "Final finishing and polishing process.",
            ""
        )
    )

    LazyColumn(

        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F1EB))
            .padding(16.dp)

    ) {

        items(stages.size) { index ->

            val stage = stages[index]

            Card(

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),

                shape = RoundedCornerShape(20.dp)

            ) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Text(

                        text = stage.stage,

                        style = MaterialTheme.typography.titleLarge,

                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(stage.description)
                }
            }
        }
    }
}