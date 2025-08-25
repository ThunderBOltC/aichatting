# 🤖 AI Chatting App (Android + Kotlin)

This is a simple **AI-powered chat app** built with **Android Studio (Kotlin)**.  
It uses the **OpenRouter API** (supports GPT, Claude, Mistral, etc.) to simulate a real chat experience like **Messenger/WhatsApp**.

---

## 📱 Features
- Real-time **chat interface** with RecyclerView (messages aligned left/right).  
- Uses **Retrofit** + **Gson** for API requests & responses.  
- API key stored securely in `local.properties` (not hardcoded).  
- Works with **OpenRouter API** and supports GPT-like models.  
- Simple, clean, and beginner-friendly project structure.

---

## 🛠️ Tech Stack
- **Language:** Kotlin  
- **UI:** RecyclerView + ChatAdapter  
- **Networking:** Retrofit + OkHttp Interceptor + Gson  
- **API Provider:** [OpenRouter](https://openrouter.ai)  
- **Build System:** Gradle (KTS)

---


## 🔑 Setup Instructions

### 1. Clone the Repo 

```bash
git clone https://github.com/your-username/aichatting.git
cd aichatting
```
### 2. Get an API key
Go to OpenRouter.

Copy your API key.

### 3. Add API Key to local.properties
Open your local.properties file (same folder as sdk.dir=). Add:
```bash

OPENROUTER_API_KEY=sk-or-v1-xxxxxxxxxxxxxxxxxxxx

```
⚠️ Never commit your API key to GitHub.

### 4. Sync Gradle
In app/build.gradle.kts we load it like this:
```bash
val localProps = project.rootProject.file("local.properties")
val properties = java.util.Properties()
properties.load(localProps.inputStream())
val apiKey: String = properties.getProperty("OPENROUTER_API_KEY") ?: ""
buildConfigField("String", "OPENROUTER_API_KEY", "\"$apiKey\"")

```

### 5. Run the App 🚀
Open Android Studio

Run on Emulator or Device

Start chatting with the AI 🎉



