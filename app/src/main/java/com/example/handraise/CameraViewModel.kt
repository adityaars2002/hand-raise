package com.example.handraise

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import com.google.mlkit.vision.pose.PoseLandmark

class CameraViewModel : ViewModel() {

    private val _handRaised = MutableLiveData(false)
    val handRaised: LiveData<Boolean> get() = _handRaised

    private val poseDetector by lazy {
        val options = AccuratePoseDetectorOptions.Builder()
            .setDetectorMode(AccuratePoseDetectorOptions.STREAM_MODE)
            .build()
        PoseDetection.getClient(options)
    }

    @OptIn(ExperimentalGetImage::class)
    fun processImage(image: ImageProxy) {
        val mediaImage = image.image ?: run {
            image.close()
            return
        }

        val inputImage = InputImage.fromMediaImage(mediaImage, image.imageInfo.rotationDegrees)

        poseDetector.process(inputImage)
            .addOnSuccessListener { pose ->
                _handRaised.postValue(isHandUp(pose))
            }
            .addOnCompleteListener { image.close() }
    }

    private fun isHandUp(pose: Pose): Boolean {
        val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
        val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
        val rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)
        val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)

        val leftUp = leftWrist != null && leftShoulder != null &&
                leftWrist.position.y < leftShoulder.position.y

        val rightUp = rightWrist != null && rightShoulder != null &&
                rightWrist.position.y < rightShoulder.position.y

        return leftUp || rightUp
    }

    override fun onCleared() {
        super.onCleared()
        poseDetector.close()
    }
}
