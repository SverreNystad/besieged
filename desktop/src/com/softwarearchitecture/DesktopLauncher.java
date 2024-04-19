package com.softwarearchitecture;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main(String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Besieged!");
		config.setWindowedMode(GameApp.SCREEN_WIDTH, GameApp.SCREEN_HEIGHT); // TODO: set responsive window size instead of magic numbers.
		new Lwjgl3Application(new GameApp(), config);
	}
}
