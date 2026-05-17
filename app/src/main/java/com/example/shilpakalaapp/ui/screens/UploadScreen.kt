package com.example.shilpakalaapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File

@Composable
fun UploadScreen() {

    val context = LocalContext.current

    val db = FirebaseFirestore.getInstance()

    var title by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    var category by remember {
        mutableStateOf("")
    }

    var artist by remember {
        mutableStateOf("")
    }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var loading by remember {
        mutableStateOf(false)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        imageUri = it
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F1EB))
            .padding(16.dp)
    ) {

        Button(
            onClick = {
                launcher.launch("image/*")
            }
        ) {
            Text("Select Image")
        }

        Spacer(modifier = Modifier.height(10.dp))

        imageUri?.let {

            AsyncImage(
                model = it,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
            },
            label = {
                Text("Title")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = description,
            onValueChange = {
                description = it
            },
            label = {
                Text("Description")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = category,
            onValueChange = {
                category = it
            },
            label = {
                Text("Category")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = artist,
            onValueChange = {
                artist = it
            },
            label = {
                Text("Artist")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(

            onClick = {

                if (imageUri == null) return@Button

                loading = true

                CoroutineScope(Dispatchers.IO).launch {

                    try {

                        val inputStream =
                            context.contentResolver
                                .openInputStream(imageUri!!)

                        val file = File.createTempFile(
                            "upload",
                            ".jpg",
                            context.cacheDir
                        )

                        file.outputStream().use {
                            inputStream?.copyTo(it)
                        }

                        val requestBody = MultipartBody.Builder()
                            .setType(MultipartBody.FORM)

                            .addFormDataPart(
                                "file",
                                file.name,
                                file.asRequestBody(
                                    "image/*".toMediaTypeOrNull()
                                )
                            )

                            .addFormDataPart(
                                "upload_preset",
                                "shilpakala_upload"
                            )

                            .build()

                        val request = Request.Builder()

                            .url(
                                "https://api.cloudinary.com/v1_1/dnkjotaus/image/upload"
                            )

                            .post(requestBody)

                            .build()

                        val response =
                            OkHttpClient()
                                .newCall(request)
                                .execute()

                        val body =
                            response.body?.string()

                        val imageUrl =
                            JSONObject(body!!)
                                .getString("secure_url")

                        val data = hashMapOf(

                            "title" to title,

                            "description" to description,

                            "category" to category,

                            "artist" to artist,

                            "imageUrl" to imageUrl,

                            "timeline" to listOf(imageUrl)
                        )

                        db.collection("artworks")
                            .add(data)

                        withContext(Dispatchers.Main) {

                            loading = false

                            Toast.makeText(
                                context,
                                "Upload Successful",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    } catch (e: Exception) {

                        withContext(Dispatchers.Main) {

                            loading = false

                            Toast.makeText(
                                context,
                                e.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }

        ) {

            if (loading) {

                CircularProgressIndicator()

            } else {

                Text("Upload Artwork")
            }
        }
    }
}