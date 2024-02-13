# Group Project in TDT4240 - Software Architecture
<div align="center">

![GitHub Workflow Status (with event)](https://img.shields.io/github/actions/workflow/status/SverreNystad/progark/ci.yml)
![GitHub top language](https://img.shields.io/github/languages/top/SverreNystad/progark)
![GitHub language count](https://img.shields.io/github/languages/count/SverreNystad/progark)
[![Project Version](https://img.shields.io/badge/version-0.0.1-blue)](https://img.shields.io/badge/version-0.0.1-blue)


</div>

<details>
    <summary><strong> Table of Contents </strong></summary>

- [Group Project in TDT4240 - Software Architecture](#group-project-in-tdt4240---software-architecture)
  - [Introduction](#introduction)
  - [Installation and Setup](#installation-and-setup)
    - [Prerequisites](#prerequisites)
    - [Installing](#installing)
  - [How to Play](#how-to-play)
  - [Running the tests](#running-the-tests)
  - [Contributors](#contributors)

</details>

## Introduction


## Installation and Setup


### Prerequisites
<ul>
<details> <summary><b> Git </b> </summary>
  Git is a distributed version control system that is used to manage the source code of this project. It is essential for cloning the project from GitHub and collaborating with other developers.

  * Git - Version Control System
    * Download and install Git from the official [Git website](https://git-scm.com/downloads).
    * After installation, verify the installation by running ```git --version``` in your command line or terminal.
</details>
</ul>

<ul>
  <details> <summary><b> Java JDK 17 (Download from Oracle's website) </b></summary>
  This project requires Java JDK to be installed. The project is tested with JDK 17.

  * Java JDK 17 - Java Development Kit is essential for compiling and running Java applications.
    * Download and install it from [Oracle's Java JDK Download Page](https://www.oracle.com/java/technologies/downloads/#java17) or adopt an open-source JDK like AdoptOpenJDK.
    * After installation, verify the installation by running ```java -version``` and ```javac -version``` in your command line or terminal.
  </details>
</ul>
<ul>
  <details> 
  <summary><b> Gradle 6.7 </b></summary>
  Gradle is used as the build tool for this project. It automates the process of building, testing, and deploying the application.

  * Gradle 6.7 - Gradle brings advanced build toolkit to manage dependencies and other aspects of the build process.
    * You can download Gradle from the [Gradle Download Page](https://gradle.org/install/).
    * Alternatively, if you are using a Gradle Wrapper script (gradlew or gradlew.bat), you do not need to manually install Gradle, as the wrapper script will handle the installation for you.
    * To confirm that Gradle is properly installed, run ```gradlew -v``` in your command line or terminal which will display the installed Gradle version.
  </details>
</ul>
Ensure that both Java and Gradle are properly installed and configured in your system's PATH environment variable for seamless execution of this project.

* Android SDK (if you want to run the game on Android)

### Installing
To install the project, you can use the following commands:
```cmd
git clone https://github.com/SverreNystad/progark.git
```


## How to Play
The game can be played on both desktop and Android.

To start the game, you can start it on desktop using the following command:
```cmd
gradlew desktop:run
```

To start the game on Android, you can use the following command:
```cmd
gradlew android:run
```

## Running the tests
To run the tests, you can use the following command:
```cmd
./gradlew test
```

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
