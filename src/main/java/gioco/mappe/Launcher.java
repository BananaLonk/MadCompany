package gioco.mappe;

import com.badlogic.gdx.scenes.scene2d.Stage;
import gioco.battaglia.Battaglia;
import gioco.oggetti.BaseGame;
import gioco.oggetti.BaseScreen;
import gioco.oggetti.Inventario;
import gioco.personaggi.Squadra;

/**
 * classe che avvia e gestisce tutte le altre mappe
 */
public class Launcher extends BaseGame {
    protected Stage stage;
    public static OpenWorld openWorld;
    public static Dungeon dungeon;
    public static Menu menu;
    public static BaseScreen ultimaMappa;
    public static BaseScreen ultimaMappaAggiuntiva;
    public static Youtopia youtopia;
    public static ShopYoutopia shopYoutopia;
    private static boolean pausa;
    private static boolean hitbox;
    private static boolean nuovaPartita;

    //parte nava long cock
    public static Inventario inventario;
    public static Battaglia battaglia;

    public static void setPausa(){
        pausa= !pausa;
    }

    public static boolean isPausa() {
        return pausa;
    }

    public static boolean isHitbox() {
        return hitbox;
    }

    public static void setHitbox() {
        Launcher.hitbox = !hitbox;
    }

    public static boolean isNuovaPartita() {
        return nuovaPartita;
    }

    public static void setNuovaPartita(boolean nuovaPartita) {
        Launcher.nuovaPartita = nuovaPartita;
    }

    @Override
    public void create() {
        super.create();
        menu=new Menu();
        setActiveScreen(menu);
    }

    public static void cambiaMappa(BaseScreen mappa){
        setActiveScreen(mappa);
    }
    public static void cambiaMappaOpenWorld(){setActiveScreen(openWorld);};
    public static void apriInventario(BaseScreen mappa){
        ultimaMappa=mappa;
        setActiveScreen(inventario);
    }
    public static void setBattaglia(Battaglia Battle){battaglia=Battle;}
    public static void tornaBattaglia(){setActiveScreen(battaglia);}

    public static void setUltimaMappa(BaseScreen ultimaMappa) {
        Launcher.ultimaMappa = ultimaMappa;
    }
    public static void setUltimaMappaAggiuntiva(BaseScreen ultimaMappa) {
        Launcher.ultimaMappaAggiuntiva = ultimaMappa;
    }

    public static void tornaUltimaMappa(){
        setActiveScreen(ultimaMappa);
    }

    public static void inizializza(){
        if(nuovaPartita){
            inventario=new Inventario();
            openWorld=new OpenWorld();
            setActiveScreen(openWorld);
        }
    }
}
