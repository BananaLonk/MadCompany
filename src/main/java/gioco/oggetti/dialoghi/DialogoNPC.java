package gioco.oggetti.dialoghi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import gioco.oggetti.BaseActor;

/**
 * classe che gestisce un dialogo di un NPC
 */
public class DialogoNPC extends BaseActor implements DialogoI{
    private int i;
    private Animation texture;
    private String[] testo;
    private String[] indirizzoSprite;
    private Texture sprite;
    float contatore;

    public DialogoNPC(float x, float y, String[] testo, String[] sprite, Stage s) {
        super(x, y, s);

        texture=loadTexture("images/dialogo.png");
        contatore=0;
        i=0;

        this.testo=testo;
        indirizzoSprite=sprite;
        this.sprite=new Texture(indirizzoSprite[0]);
    }

    @Override
    public void act(float dt) {

        super.act(dt);

        if(Gdx.input.isKeyJustPressed(Input.Keys.E)){
            next();
        }

    }
    /**
     * @return true se ci sono dialoghi mai detti dall'NPC
     */
    public boolean hasNext(){
        return i<testo.length-1;
    }

    /**
     * passa al prossimo dialogo
     */
    public void next(){
        if(hasNext()){
            i++;
            contatore=0;
            sprite=new Texture(indirizzoSprite[i]);
        }else{
            this.remove();
        }
    }

    public String getTesto() {
        return testo[i];
    }

    public void setTesto(String testo) {
        this.testo[i] = testo;
    }

    public Texture getSprite() {
        return sprite;
    }

    public void setSprite(Texture sprite) {
        this.sprite = sprite;
    }

    public Animation getTexture() {
        return texture;
    }

    public void setTexture(Animation texture) {
        this.texture = texture;
    }
}
