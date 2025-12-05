package com.example.handraise

import android.speech.tts.TextToSpeech
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import java.util.*
import java.util.concurrent.Executors

@Composable
fun CameraScreen() {

    val context = LocalContext.current
    val viewModel: CameraViewModel = viewModel()
    val lifecycleOwner = LocalLifecycleOwner.current

    val handRaised by viewModel.handRaised.observeAsState(false)

    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var lastSpeakTime by remember { mutableStateOf(0L) }

    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                val previewView = androidx.camera.view.PreviewView(ctx).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }

                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()

                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val analysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build().apply {
                            setAnalyzer(Executors.newSingleThreadExecutor()) { img ->
                                viewModel.processImage(img)
                            }
                        }

                    val selector = CameraSelector.DEFAULT_FRONT_CAMERA

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            selector,
                            preview,
                            analysis
                        )
                    } catch (_: Exception) {}
                }, ContextCompat.getMainExecutor(ctx))

                previewView
            }
        )

        Text(
            text = if (handRaised) "✋ Hand Detected!" else "Raise your hand...",
            modifier = Modifier
                .padding(20.dp)
                .background(Color.Gray.copy(alpha = 0.6f))
                .padding(10.dp),
            color = Color.White
        )
    }

    LaunchedEffect(handRaised) {
        if (handRaised) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastSpeakTime > 3000) {
                lastSpeakTime = currentTime
                delay(300)
                tts?.speak("Hand detected, how can I help you?", TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }
    }
}
