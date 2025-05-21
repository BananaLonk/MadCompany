package gioco.battaglia;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import gioco.oggetti.BaseActor;

public class PulsanteBattaglia extends BaseActor {

    private Animation<TextureRegion> normale;
    private Animation<TextureRegion> selezionato;
    private Animation<TextureRegion> attivo;

    public PulsanteBattaglia(float x, float y, Stage s,String normale, String selezionato, String attivo) {
        super(x,y,s);
        this.normale=loadAnimationFromSheet(normale,1,1,1f,false);
        this.selezionato=loadAnimationFromSheet(selezionato,1,1,1f,false);
        this.attivo=loadAnimationFromSheet(attivo,1,1,1f,false);

    }


    public void setNormale() {
        this.setAnimation(normale);
    }

    public void setSelezionato() {
        this.setAnimation(selezionato);
    }

    public void setAttivo() {
        this.setAnimation(attivo);
    }


}
