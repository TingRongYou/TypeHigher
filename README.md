# TypeHigher

A 2D typing game built with a strict Model-View-Controller (MVC) architecture, featuring dynamic difficulty scaling and micro-step progression mechanics.

## 🛠️ Technologies & Tools

This project is built using the following core technologies:

* **[Java](https://www.java.com/):** The core programming language used for all game logic and object-oriented architecture.
* **[LibGDX](https://libgdx.com/):** A cross-platform Java game development framework used for rendering 2D graphics, managing the game loop, and handling inputs.
* **[Gradle](https://gradle.org/):** The build automation tool used to manage project dependencies (like the LWJGL3 graphics backend) and compile the application.
* **[IntelliJ IDEA](https://www.jetbrains.com/idea/):** The primary Integrated Development Environment (IDE) used for development.
* **Git:** Version control for tracking code changes and managing project history.

## ⚙️ Installation & Setup

Follow these steps to get the project running on your local machine.

### Prerequisites
1. Ensure you have the **Java Development Kit (JDK)** installed (JDK 8 or higher is recommended for standard LibGDX projects).
2. Ensure you have **Git** installed on your system.

### 1. Clone the Repository
Open your terminal or command prompt and run:
```bash
git clone [https://github.com/your-username/TypeHigher.git](https://github.com/TingRongYou/TypeHigher.git)
cd TypeHigher
```
### 2. Open IntelliJ IDEA
1. Launch IntelliJ IDEA.
2. Click Open and select the TypeHigher root folder.
3. Wait a few moment for IntelliJ to detect the Gradle project
4. If a prompt appears asking to "Load Gradle Project" or a small Elephant icon appears in the top right, click it to sync the dependencies.

### 3. Run the Game (Desktop)
You can launch the game directly through the terminal using the Gradle wrapper.

Open the terminal at the root of the project (or use the built-in Terminal tab at the bottom of IntelliJ) and execute the following command:

**For Windows, Mac and Linux:**
```bash
./run
```
_(Note: If you are on Windows Command Prompt, you can also type `run` without the `./`)_

**Manual Launch / Fallback:**

If the shortcut scripts do not work on your machine, you can always run the game using the standard Gradle wrapper:
* **Windows:** `gradlew lwjgl3:run`
* **Linux/Mac:** `./gradlew lwjgl3:run`

### Architecture Note
This project strictly enforces the MVC (Model-View-Controller) pattern.
* Game rules, timers and string manipulation live purely in the `model` package.
* LibGDX rendering the UI live in the `view` package.
* The `controller` acts as the sole communication bridge between the two.

## Credits & Acknowledgements
**Dictionary Data**

The core dictionary used to generate the typing targets in TypeHigher is sourced from the [dwyl/english-words](https://github.com/dwyl/english-words) repository.

For the purposes of this game's mechanics, the massive dataset was programmatically sanitized at runtime to exclusively use lowercase, purely alphabetic words to ensure smooth gameplay flow.

The original `dwyl` word list is free and unencumbered software released into the public domain via **The Unlicense**.
