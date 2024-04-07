package com.softwarearchitecture;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.softwarearchitecture.GameApp;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main(String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Besieged!");
		config.setWindowedMode(1800, 1000); // TODO: set responsive window size instead of magic numbers.
		new Lwjgl3Application(new GameApp(), config);
	}
}
