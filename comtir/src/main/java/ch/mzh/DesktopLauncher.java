package ch.mzh;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import ch.mzh.game.ArtilleryGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Artillery Strategy Game");
        config.setWindowedMode(1200, 800);
        config.setResizable(true);
        new Lwjgl3Application(new ArtilleryGame(), config);
        // new Lwjgl3Application(new OrthographicCameraExample(), config);
    }
}