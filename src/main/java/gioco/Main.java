package gioco;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import gioco.mappe.Launcher;

public class Main {
    public static void main(String[] args) {

        Game game=new Launcher();
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        //titolo finestra
        cfg.setTitle("Persona 6 ReForged");
        //dimensioni effettive della finestra
        cfg.setWindowedMode(1280,720);
        cfg.setPauseWhenLostFocus(true);
        cfg.setForegroundFPS(60);
        //icona della finestra
        cfg.setWindowIcon(Files.FileType.Internal,"images/game-icon.png");
        Lwjgl3Application application=new Lwjgl3Application(game,cfg);
    }
}