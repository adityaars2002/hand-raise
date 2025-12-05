<div align="center">

# ✋ Hand-Raise Detection App  
**Android | Jetpack Compose | CameraX | ML Kit | MVVM | Text-to-Speech**

---

![Android](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9-purple?logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?logo=jetpackcompose&logoColor=white)
![ML Kit](https://img.shields.io/badge/ML%20Kit-Pose%20Detection-orange?logo=google)
![CameraX](https://img.shields.io/badge/Camera-CameraX-lightblue)
![Architecture](https://img.shields.io/badge/Architecture-MVVM-blueviolet)
![License](https://img.shields.io/badge/License-MIT-green)

---

</div>

## 📌 Overview

This Android application detects when a user raises their hand above shoulder level using **Google ML Kit Pose Detection** and responds using **Text-to-Speech**.

Once a hand is detected, the app speaks:

> **“Hand detected, how can I help you?”**

The UI is built entirely using **Jetpack Compose**, and the project follows a clean **MVVM architecture** using UI state and one-time events.

---

## 🚀 Features

- 📷 Live camera feed with **CameraX**
- 🧠 Hand-raise gesture using **ML Kit Pose Detection**
- ✋ Detects left or right hand above shoulder level
- 🔊 Voice feedback using device Text-to-Speech
- 🧩 MVVM with state + event-driven communication
- 🎨 Pure Jetpack Compose UI (no XML)

---

## 🏛 Architecture

The app follows an MVVM structure:



View (Compose UI) ← observes — ViewModel — exposes → State + One-Time Events


| Layer | Role |
|-------|------|
| **View (CameraScreen)** | Displays camera preview, UI text, and reacts to events (like speaking). |
| **ViewModel** | Processes frames, runs ML logic, detects gestures, and fires `Speak` events. |
| **Model/Data** | Pose detection result and UI state values. |

UI contains **no business logic** — everything is decided in the ViewModel.

---

## 🧠 Detection Logic

A hand is considered raised when:

wrist.position.y < shoulder.position.y


The UI contains zero business logic — it only reacts to changes.

🧠 Gesture Logic

A hand is considered raised if:

```kotlin
wrist.position.y < shoulder.position.y
```


Since ML Kit coordinates treat lower y-values as higher on screen, this comparison accurately detects a raised hand.

Either hand can trigger the response.

## 🧰 Tech Stack

| Category | Technology |
|----------|------------|
| Language | Kotlin |
| UI | Jetpack Compose |
| Camera | CameraX |
| ML | ML Kit Pose Detection (Accurate Model) |
| Architecture | MVVM |
| Speech | Android Text-to-Speech |


Android physical device (recommended)

Android API Level 24+

Camera permission granted at runtime

🚫 Pose detection may not work on emulators.

👤 Author

Aditya Raj
Made with ❤️ and Kotlin.

