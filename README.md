# Skillforge

Skillforge is a tiny learning app built with Jetpack Compose, Retrofit, and Kotlin Coroutines. It fetches course data from a remote API and displays it across three main screens: Home, Course Detail, and Lesson.

## Features
- **Home Screen**: Browse categories and popular courses.
- **Course Detail**: View course overview, instructor information, and lesson list.
- **Lesson Screen**: Interactive lesson player with lesson navigation.
- **Network Handling**: Built-in loading and error states for a smooth user experience.
- **Responsive UI**: "Cream + Teal" theme matching the provided design specifications.

## Tech Stack
- **Jetpack Compose**: For the modern, declarative UI.
- **Retrofit & Kotlinx Serialization**: For network calls and JSON parsing.
- **Coil**: For efficient image loading and caching.
- **Navigation Compose**: For seamless screen transitions.
- **ViewModel & Flow**: For reactive UI state management.

## Working with AI

### AI Tools Used
- **Android Studio AI Assistant (Gemini)**: Used for scaffolding the project structure, generating data models from JSON, and implementing UI components based on design descriptions.

### Prompts Sent
1. "Extract the JSON structure from this URL and define Kotlin data models using Kotlin Serialization: https://raw.githubusercontent.com/android-assesment/notes/refs/heads/main/data.json"
2. "Create a Jetpack Compose Home screen with a horizontal Categories row and a vertical Popular Courses list using the provided Skillforge models."
3. "Implement a Retrofit API service and a Repository to fetch data from the github usercontent URL."

### AI Successes and Challenges
- **Success**: The AI was exceptionally good at generating accurate data models from the JSON schema, including nested structures like categories, courses, and instructors. It correctly identified data types and suggested using Kotlin Serialization.
- **Wrong/Fix**: The AI initially missed some material icon dependencies and specific Compose imports (like `alpha` or `TextDark` color). I fixed this by manually updating the `libs.versions.toml` file with `material-icons-extended` and ensuring all necessary UI imports were included in the screen files.

## Testing
The project includes unit tests for core logic, such as the `isFree()` check on courses.
Run tests using: `./gradlew test`
