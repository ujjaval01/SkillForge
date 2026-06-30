# Skillforge

Skillforge is a tiny learning app built with Jetpack Compose, Retrofit, and Kotlin Coroutines. It fetches course data from a remote API and displays it across three main screens: Home, Course Detail, and Lesson.

## 📱 Demo & Downloads

<<<<<<< HEAD
### Watch the Demo
![Skillforge Demo](https://github.com/user-attachments/assets/db897304-8d6a-4fca-99e9-86b1822911e4)

*Additional demo highlights:*
- [Feature Walkthrough](https://github.com/user-attachments/assets/9caec903-ddda-4624-beaa-99474ddb3cc7)

### Download the App
- 📥 **[Download Skillforge APK (Google Drive)](https://drive.google.com/file/d/1D8H6zrF-r3SGUa1vTGFi7lomznX9GuB3/view?usp=sharing)**

---

## 🚀 Features
- **Home Screen**: Browse categories with a functional sliding scroll bar and explore popular courses.
- **Course Detail**: Comprehensive overview with hero gradient headers, instructor profiles, and course content.
- **Lesson Screen**: Interactive lesson player with lesson navigation and status tracking.
- **Network Handling**: Built-in loading and error states for a smooth user experience.
- **Responsive UI**: Custom "Cream + Teal" theme matching the exact design specifications.

## 🛠 Tech Stack
=======
https://github.com/user-attachments/assets/db897304-8d6a-4fca-99e9-86b1822911e4



## Tech Stack
>>>>>>> 5d1531ac0d19d07badb85d307f27cca0b7f79cb0
- **Jetpack Compose**: For the modern, declarative UI.
- **Retrofit & Kotlinx Serialization**: For network calls and JSON parsing.
- **Coil**: For efficient image loading and caching.
- **Navigation Compose**: For seamless screen transitions.
- **ViewModel & Flow**: For reactive UI state management.

## 🤖 Working with AI

### AI Tools Used
- **Android Studio AI Assistant (Gemini)**: Used for scaffolding the project structure, generating data models from JSON, and implementing UI components based on design descriptions.
<<<<<<< HEAD
- **Antigravity**: Instrumental in debugging and fixing UI alignment issues on the home screen, implementing the functional sliding bar in the categories section, and resolving image loading issues in the Popular Courses list.
=======
- - **Antigravity**: Used for debugs and fix UI- Alignment problem in home screen, fix bottom sliding bar in categories section, images missing in Popular courses etc...
>>>>>>> 5d1531ac0d19d07badb85d307f27cca0b7f79cb0

### Prompts Sent
1. "Extract the JSON structure from this URL and define Kotlin data models using Kotlin Serialization: https://raw.githubusercontent.com/android-assesment/notes/refs/heads/main/data.json"
2. "Create a Jetpack Compose Home screen with a horizontal Categories row and a vertical Popular Courses list using the provided Skillforge models."
3. "Implement a Retrofit API service and a Repository to fetch data from the github usercontent URL."

### AI Successes and Challenges
- **Success**: The AI was exceptionally good at generating accurate data models from the JSON schema, including nested structures like categories, courses, and instructors. It correctly identified data types and suggested using Kotlin Serialization.
- **Wrong/Fix**: The AI initially missed some material icon dependencies and specific Compose imports (like `alpha` or `TextDark` color). I fixed this by manually updating the `libs.versions.toml` file with `material-icons-extended` and ensuring all necessary UI imports were included in the screen files.

## 🧪 Testing
The project includes unit tests for core logic, such as the `isFree()` check on courses.
Run tests using: `./gradlew test`

https://drive.google.com/file/d/1D8H6zrF-r3SGUa1vTGFi7lomznX9GuB3/view?usp=sharing

https://github.com/user-attachments/assets/9caec903-ddda-4624-beaa-99474ddb3cc7





