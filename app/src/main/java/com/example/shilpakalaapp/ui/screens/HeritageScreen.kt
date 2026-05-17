package com.example.shilpakalaapp.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun HeritageScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F1EB))
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Text(
            "Heritage of Karnataka Sculpture",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            """
The Hoysala temples of Karnataka represent one of the finest achievements in Indian stone carving.

Master sculptors created intricate carvings using soapstone, depicting mythology, dance, nature, and spirituality.

ShilpaKala preserves this ancient art digitally and connects modern audiences with traditional artists.

Features:
• High-resolution sculpture showcase
• Artist portfolio system
• Work-in-progress storytelling
• WhatsApp enquiry integration
• Digital heritage preservation

This platform empowers rural artisans with global visibility.
            """.trimIndent(),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}