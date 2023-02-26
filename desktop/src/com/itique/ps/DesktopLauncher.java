package com.itique.ps;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Police Simulator");
		config.setWindowedMode(640, 480);
		config.setWindowSizeLimits(640, 480, 1920, 1080);
		new Lwjgl3Application(new PoliceSimulator(), config);
	}
}
