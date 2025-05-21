package gioco.oggetti.oggettiMappa;

import com.badlogic.gdx.scenes.scene2d.Stage;
import gioco.oggetti.BaseActor;

/**
 * classe che gestisce un oggetto generico caricato dalla mappa tiled
 */
public abstract class Oggetto extends BaseActor {
    private boolean enable;

    public Oggetto(float x, float y, float width, float height, Stage stage) {
        super(x,y,stage);
        setSize(width,height);
        setBoundaryRectangle();
        enable = true;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
    public boolean isEnable() {
        return enable;
    }
}
