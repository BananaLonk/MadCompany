package gioco.oggetti.oggettiMappa;

import com.badlogic.gdx.scenes.scene2d.Stage;
import gioco.oggetti.BaseActor;

public class Teletrasporto extends Oggetto {
    private String destinazione;

    public Teletrasporto(float x, float y, float width, float height, String destinazione, Stage stage) {
        super(x,y,width,height,stage);
        this.destinazione=destinazione;
    }

    public String getDestinazione(){
        return destinazione;
    }
}
