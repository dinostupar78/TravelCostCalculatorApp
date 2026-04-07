# ⛽ TravelCost Calculator

An Android app for calculating fuel consumption and travel costs.

The user enters distance, fuel, and price to calculate fuel consumption and trip cost.

---

## ▶️ Demo

👉 [YouTube Link](https://youtu.be/WZlBtwWZ7-I)

---

## ✨ Features

- Calculates **Fuel Consumption (L/100 km)**
- Calculates **Total Cost (EUR)**
- Calculates **Cost per 100 km (EUR/100 km)**
- Validates user input (empty fields and invalid values)
- Fuel type selection using a spinner
- Font size control with a slider (Small / Medium / Large)
- Multi-language support (HR / EN / DE) through the language menu
- Modern Material-based UI (header, cards, clean spacing)

---

## 🛠 Tech Stack

- **Kotlin**
- **Android SDK** (`compileSdk 36`, `minSdk 24`)
- **AndroidX**
- **Material Components**
- **Gradle (KTS)**

---

## 📱 App Layout

The app is organized on a single screen:
1. **Header** with logo and title
2. **Input section** (kilometers, liters, fuel price, fuel type)
3. **Appearance control** (font-size slider)
4. **Result card** for calculated output

---

## 🌍 Localization

Supported languages:
- Croatian (`values-hr`)
- English (`values-en`)
- German (`values-de`)

You can change the language from the toolbar overflow menu:
**Language -> Hrvatski / English / Deutsch**

---

## 🚀 Running the Project

### Android Studio
1. Open the `KalkulatorStupar` project in Android Studio.
2. Wait for Gradle sync to finish.
3. Run the app on an emulator or a physical Android device.

### Gradle (CLI)

```powershell
Set-Location "D:\Program Files\Android\Projects\KalkulatorStupar"
.\gradlew.bat :app:assembleDebug --console=plain
```

The debug APK is generated in the standard build output for the `app` module.
