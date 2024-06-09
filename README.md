# Group Project in TDT4240 - Software Architecture

<div align="center">

![GitHub top language](https://img.shields.io/github/languages/top/SverreNystad/progark)
![GitHub language count](https://img.shields.io/github/languages/count/SverreNystad/progark)
[![Project Version](https://img.shields.io/badge/version-1.0.0-blue)](https://img.shields.io/badge/version-1.0.0-blue)

</div>

<div align="center">
  <img src="docs\images\besieged.png" alt="logo"           width="200" height="200" />
</div>

<details>
    <summary><strong> Table of Contents </strong></summary>

- [Group Project in TDT4240 - Software Architecture](#group-project-in-tdt4240---software-architecture)
  - [Introduction](#introduction)
    - [Game play](#game-play)
    - [Trailer](#trailer)
  - [Installation and Setup](#installation-and-setup)
    - [Prerequisites](#prerequisites)
    - [Installing](#installing)
  - [How to Play](#how-to-play)
    - [Desktop](#desktop)
    - [Android](#android)
    - [Generating APK in Android Studio](#generating-apk-in-android-studio)
  - [Running the tests](#running-the-tests)
  - [Architecture](#architecture)
  - [Contributors](#contributors)

</details>

## Introduction

Besieged! is a cooperative, real-time multiplayer tower defense game inspired by Viking and Norse mythology. Supporting both single player and multiplayer. Players collaborate in shared instances to defend their village against waves of mythological creatures. Gameplay involves purchasing and strategically placing combination of "tower-cards"—on the map to build unique towers that attack invading enemies. Each kill grants players money to acquire more cards. Enemies spawn in waves and follow a set path toward your village; if they succeed in destroying it, the game ends. Besieged! innovates on traditional tower defense mechanics, such as those in Bloons Tower Defense 4, by enabling dynamic tower creation through card combinations.

### Game play

<div align="center">
  <div align="center">
    <img src="docs\images\mainmenu.png"alt="main menu"           width="200" height="100">
    <img src="docs\images\tutorial_besieged.png" alt="tutorial"  width="200" height="100">
  </div>
  <div align="center">
    <img src="docs\images\map_choice_menu.png" alt="gameplay"    width="200" height="100">
    <img src="docs\images\gameplay.png" alt="gameplay"           width="200" height="100">
  </div>
</div>

### Trailer

<div align="center">

[![Trailer](https://img.youtube.com/vi/hZOwTa846p4/0.jpg)](https://www.youtube.com/watch?v=hZOwTa846p4)

</div>

## Installation and Setup

### Prerequisites

<ul>
<details> <summary><b> Git </b> </summary>
  Git is a distributed version control system that is used to manage the source code of this project. It is essential for cloning the project from GitHub and collaborating with other developers.

- Git - Version Control System
_ Download and install Git from the official [Git website](https://git-scm.com/downloads).
_ After installation, verify the installation by running `git --version` in your command line or terminal.
</details>
</ul>

<ul>
  <details> <summary><b> Java JDK 17 (Download from Oracle's website) </b></summary>
  This project requires Java JDK to be installed. The project is tested with JDK 17.

- Java JDK 17 - Java Development Kit is essential for compiling and running Java applications.
_ Download and install it from [Oracle's Java JDK Download Page](https://www.oracle.com/java/technologies/downloads/#java17) or adopt an open-source JDK like AdoptOpenJDK.
_ After installation, verify the installation by running `java -version` and `javac -version` in your command line or terminal.
  </details>
</ul>
<ul>
  <details> 
  <summary><b> Gradle 6.7 </b></summary>
  Gradle is used as the build tool for this project. It automates the process of building, testing, and deploying the application.

- Gradle 6.7 - Gradle brings advanced build toolkit to manage dependencies and other aspects of the build process.
_ You can download Gradle from the [Gradle Download Page](https://gradle.org/install/).
_ Alternatively, if you are using a Gradle Wrapper script (gradlew or gradlew.bat), you do not need to manually install Gradle, as the wrapper script will handle the installation for you. \* To confirm that Gradle is properly installed, run `gradlew -v` in your command line or terminal which will display the installed Gradle version.
  </details>
</ul>

<ul>
  <details> 
    <summary><b>Android SDK (if you want to run the game on Android)</b></summary>
    When testing the Android app one can run it either by connecting your Android phone via USB to your computer, or you could use an Android emulator (virtual device). To do this, you need to have the Android SDK installed.
    details> 
    <summary><b> Android SDK (if you want to run the game on Android) </b></summary>
    To set up the Android SDK for running the game on an emulator, you need to create a file called `local.properties` in the root of the project and add the path to your SDK with the following line:
    
    echo sdk.dir=YOUR/ANDROID/SDK/PATH > local.properties

  </details> 
</ul>
<ul>
  <details>
    <summary><b>Firebase Secret Key (for multiplayer features)</b></summary>
    To enable multiplayer features in the game, you need to obtain a Firebase secret key. This key is necessary to authenticate and manage the game's online interactions securely.
    <ul> 
     Obtain your Firebase secret key from the Firebase console.
    </ul>
    <ul>
      Put the key into the asset` folder with the name 
      ``FirebaseSecretKey.json``
    </ul>
  </details>
</ul>

Ensure that both Java and Gradle are properly installed and configured in your system's PATH environment variable to be able to run the game.

### Installing

To install the project, you can use the following commands:

```cmd
git clone https://github.com/SverreNystad/besieged.git
```

## How to Play

The game can be played on both desktop and Android.

### Desktop

To start the game, you can start it on desktop using the following command:

```cmd
gradlew desktop:run
```

### Android

To start the game on Android one needs to run the APK file of the Besieged project. This can either be done by generating this your self or genereting it with tools like Android Studio
When one have the APK file one can then run it on an emulator or on any android device by executing the file.

### Generating APK in Android Studio

Open the root folder as a project in Android Studio and let it configure itself the first time you open it. It should then automatically make an Android run configuration, so all you have to do is press the green run button on the top bar. Please take a look at [Figure](docs/images/android_running_of_project.png):

## Running the tests

To run the tests, you can use the following command:

```cmd
gradlew test
```

## Architecture

- [Requirements](docs/architectural_documents/Requirements.pdf)
- [Architectural Design](docs/architectural_documents/Architectural%20Design.pdf)
- [Implementation](docs/architectural_documents/Implementation%20Report.pdf)

<details>

<summary> General overview of how the game runs when starting either a singleplayer or multiplayer-match
</summary>

<div>
    <li> The GameApp, which is the entry point of the application, creates the GameLauncher. 
    </li> 
    <li> The GameLauncher creates all the different instances of the necessary interfaces that are needed to run the application like drawing, audio and input. It also creates the Data Access Objects (DAOs) for the client and server. It also creates the GameClient itself.
    </li> 
    <li>The GameClient tells the ScreenManager to initialize and enter the Menu state, i.e. the main menu. 
    </li> 
    <li>The Menu-state instantiates the required ECS-systems like the InputSystem, RenderingSystem, AudioSystem.
    </li> 
    <li>The GameApp starts its update-method which updates the GameClient continuously, which in turn updates the ECS-system. Review the next subsection to get a better deeper understand of the update-cycle in the ECS. 
    </li> 
    <li>After each iteration of the ECS update-cycle, the GameClient checks if it has joined a game or not. 
    </li> 
    <li>User navigates through the multiplayer-menu to the "Choose map"-menu and chooses a map. 
    </li> 
    <li>After choosing a map, the server is started on a separate thread. Depending on if the game is a singleplayer or a multiplayer-game, the Server and Client get either a LocalDAO or a FirebaseDAO, respectively. 
    </li> 
    <li>The server continuously polls to check if there is a pending player waiting to join the game. 
    </li> 
    <li>A client notifies the server that it wants to join the game. This happens in the "Join Game"-menu by pressing an available game. It then waits a short period before checking for a response from the server. 
    </li> 
    <li>The server handles the join request and puts the player hosting the game into the game itself, i.e. the InGame-state. This also happens to the joining player. 
    </li> 
    <li>The server then initializes the game itself by creating entities and adding them to the ECS-system, and updating all clients. 
    </li> 
    <li>During each update-cycle in the Server while In-game, the following things happen: The ECS-systems are updated, progressing the game. It then checks if the game has ended or not. Then, it looks for pending actions received from players, and handles them if they are valid, like placing a card on an available tile if the player has enough money to buy it. At the end of each update-cycle the new updated GameState is applied and sent to all clients. 
    </li> 
    <li>Repeat main update-cycle until the game is over, or the host terminates the application or loses connection. 
    </li> 
    <li>Clients continuously pull updates to the GameState from the server and updates their local ECS-systems. 
    </li> 
    <li>Clients send request for doing actions to the server. 
    </li> 
    <li>The game ends in one of the two following ways: Case 1. The server abruptly loses connection. If a client does not receive new updates from the server within 10 seconds, it exits to the "lost connection"-screen. Case 2: The game ends with the players losing all their health, and the "Game Over"-screen is displayed. The server does a teardown-procedure which include updating the global highscore, and removes the game from the list of pollable games.    
   
</div>
</details>

<div align="center">
  <img src="docs\images\tim_image.jpg" alt="logo"      />
</div>

## Contributors

<table>
  <tr>
    <td align="center">
        <a href="https://github.com/Jensern1">
            <img src="https://github.com/Jensern1.png?size=100" width="100px;" alt="Jens Martin Norheim Berget"/><br />
            <sub><b>Jens Martin Norheim Berget</b></sub>
        </a>
    </td>
    <td align="center">
      <a href="https://github.com/mvbryne">
            <img src="https://github.com/mvbryne.png?size=100" width="100px;" alt="Magnus Vesterøy Bryne"/><br />
            <sub><b>Magnus Vesterøy Bryne</b></sub>
        </a>
    </td>
    <td align="center">
        <a href="https://github.com/mattiastofte">
            <img src="https://github.com/mattiastofte.png?size=100" width="100px;" alt="Mattias Tofte"/><br />
            <sub><b>Mattias Tofte</b></sub>
        </a>
    </td>
    <td align="center">
        <a href="https://github.com/SverreNystad">
            <img src="https://github.com/SverreNystad.png?size=100" width="100px;"
            alt="Sverre Nystad"/><br />
            <sub><b>Sverre Nystad</b></sub>
        </a>
    </td>
    <td align="center">
        <a href="https://github.com/Artewald">
            <img src="https://github.com/Artewald.png?size=100" width="100px;" alt="Tim Matras"/><br />
            <sub><b>Tim Matras</b></sub>
        </a>
    </td>
    <td align="center">
        <a href="https://github.com/tobiasfremming">
            <img src="https://github.com/tobiasfremming.png?size=100" width="100px;"/><br />
            <sub><b>Tobias Fremming</b></sub>
        </a>
    </td>
  </tr>
</table>
