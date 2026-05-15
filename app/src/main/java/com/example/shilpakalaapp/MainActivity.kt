package com.example.shilpakalaapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.net.URLEncoder

data class Artwork(
    val id: String = "",
    val title: String = "",
    val imageUrl: String = "",
    val timeline: List<String> = emptyList()
)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp() {

    var screen by remember { mutableStateOf("gallery") }
    var selected by remember { mutableStateOf<Artwork?>(null) }
    var dark by remember { mutableStateOf(true) }

    MaterialTheme(
        colorScheme = if (dark) darkColorScheme() else lightColorScheme()
    ) {

        Scaffold(

            topBar = {

                TopAppBar(
                    title = {
                        Text("ShilpaKala")
                    },

                    actions = {

                        IconButton(
                            onClick = {
                                dark = !dark
                            }
                        ) {
                            Icon(Icons.Default.Brightness4, null)
                        }
                    }
                )
            },

            bottomBar = {

                NavigationBar {

                    NavigationBarItem(
                        selected = screen == "gallery",
                        onClick = {
                            screen = "gallery"
                        },
                        icon = {
                            Icon(Icons.Default.Image, null)
                        },
                        label = {
                            Text("Gallery")
                        }
                    )

                    NavigationBarItem(
                        selected = screen == "heritage",
                        onClick = {
                            screen = "heritage"
                        },
                        icon = {
                            Icon(Icons.Default.AccountBalance, null)
                        },
                        label = {
                            Text("Heritage")
                        }
                    )

                    NavigationBarItem(
                        selected = screen == "profile",
                        onClick = {
                            screen = "profile"
                        },
                        icon = {
                            Icon(Icons.Default.Person, null)
                        },
                        label = {
                            Text("Profile")
                        }
                    )
                }
            }

        ) { padding ->

            Box(
                Modifier.padding(padding)
            ) {

                when {

                    selected != null -> {

                        DetailScreen(
                            art = selected!!,
                            onBack = {
                                selected = null
                            }
                        )
                    }

                    screen == "gallery" -> {

                        GalleryScreen(
                            onSelect = {
                                selected = it
                            },
                            onUpload = {
                                screen = "upload"
                            }
                        )
                    }

                    screen == "upload" -> {

                        UploadScreen(
                            onDone = {
                                screen = "gallery"
                            }
                        )
                    }

                    screen == "heritage" -> {
                        HeritageScreen()
                    }

                    screen == "profile" -> {
                        ProfileScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun GalleryScreen(
    onSelect: (Artwork) -> Unit,
    onUpload: () -> Unit
) {

    val db = FirebaseFirestore.getInstance()

    var list by remember {
        mutableStateOf(listOf<Artwork>())
    }

    var loading by remember {
        mutableStateOf(true)
    }

    var search by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {

        db.collection("artworks")
            .addSnapshotListener { value, _ ->

                list = value?.documents?.map {

                    Artwork(
                        id = it.id,
                        title = it.getString("title") ?: "",
                        imageUrl = it.getString("imageUrl") ?: "",
                        timeline = it.get("timeline") as? List<String>
                            ?: emptyList()
                    )

                } ?: emptyList()

                loading = false
            }
    }

    Column(
        Modifier.fillMaxSize()
    ) {

        Button(
            onClick = onUpload,
            modifier = Modifier.padding(10.dp)
        ) {
            Text("Upload Artwork")
        }

        OutlinedTextField(
            value = search,
            onValueChange = {
                search = it
            },
            label = {
                Text("Search")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )

        if (loading) {

            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

        } else {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2)
            ) {

                items(
                    list.filter {
                        it.title.contains(search, true)
                    }
                ) { art ->

                    Card(
                        Modifier
                            .padding(8.dp)
                            .clickable {
                                onSelect(art)
                            }
                    ) {

                        Column {

                            AsyncImage(
                                model = art.imageUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp)
                            )

                            Text(
                                art.title,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UploadScreen(onDone: () -> Unit) {

    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    var title by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var loading by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        imageUri = it
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Button(onClick = onDone) {
            Text("← Back")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                launcher.launch("image/*")
            }
        ) {
            Text("Select Image")
        }

        Spacer(modifier = Modifier.height(16.dp))

        imageUri?.let {

            AsyncImage(
                model = it,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
            },
            label = {
                Text("Artwork Title")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                if (imageUri == null || title.isEmpty()) {

                    Toast.makeText(
                        context,
                        "Select image and enter title",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@Button
                }

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

                        val client = OkHttpClient()

                        val response =
                            client.newCall(request).execute()

                        val responseData =
                            response.body?.string()

                        val imageUrl =
                            JSONObject(responseData!!)
                                .getString("secure_url")

                        val data = hashMapOf(

                            "title" to title,

                            "imageUrl" to imageUrl,

                            "timeline" to listOf(
                                imageUrl,
                                imageUrl,
                                imageUrl
                            )
                        )

                        db.collection("artworks")
                            .add(data)

                            .addOnSuccessListener {

                                loading = false

                                Toast.makeText(
                                    context,
                                    "Upload Successful",
                                    Toast.LENGTH_SHORT
                                ).show()

                                onDone()
                            }

                    } catch (e: Exception) {

                        loading = false

                        Toast.makeText(
                            context,
                            e.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        ) {

            if (loading) {

                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp)
                )

            } else {

                Text("Upload Artwork")
            }
        }
    }
}

@Composable
fun DetailScreen(
    art: Artwork,
    onBack: () -> Unit
) {

    val context = LocalContext.current

    var scale by remember {
        mutableStateOf(1f)
    }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Button(onClick = onBack) {
            Text("Back")
        }

        Spacer(Modifier.height(10.dp))

        Box(
            Modifier
                .fillMaxWidth()
                .height(250.dp)
                .pointerInput(Unit) {

                    detectTransformGestures { _, _, zoom, _ ->
                        scale = (scale * zoom)
                            .coerceIn(1f, 5f)
                    }
                }
        ) {

            AsyncImage(
                model = art.imageUrl,
                contentDescription = null,
                modifier = Modifier.graphicsLayer(
                    scaleX = scale,
                    scaleY = scale
                )
            )
        }

        Spacer(Modifier.height(10.dp))

        Text(
            art.title,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(10.dp))

        Button(
            onClick = {

                val msg = URLEncoder.encode(
                    "Interested in ${art.title}",
                    "UTF-8"
                )

                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        "https://wa.me/917676833092?text=$msg"
                    )
                )

                context.startActivity(intent)
            }
        ) {
            Text("Enquire on WhatsApp")
        }

        Spacer(Modifier.height(20.dp))

        Text(
            "Timeline",
            style = MaterialTheme.typography.titleMedium
        )

        art.timeline.forEach {

            AsyncImage(
                model = it,
                contentDescription = null,
                modifier = Modifier.height(120.dp)
            )

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun HeritageScreen() {

    Column(
        Modifier.padding(16.dp)
    ) {

        Text(
            "Heritage - Hoysala Art",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(8.dp))

        Text(
            "Ancient temple carvings from Karnataka."
        )
    }
}

@Composable
fun ProfileScreen() {

    Column(
        Modifier.padding(16.dp)
    ) {

        Text(
            "Artist Profile",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(8.dp))

        Text("Name: Shilpi")

        Text("Role: Admin/User")
    }
}