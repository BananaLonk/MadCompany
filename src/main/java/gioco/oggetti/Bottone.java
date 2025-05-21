package gioco.oggetti;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * classe che gestisce un bottone del menu
 */
public class Bottone extends BaseActor {
    private Animation texture;

    public Bottone(float x, float y, Stage s,String texture) {
        super(x, y, s);
        this.texture = loadTexture(texture);
    }

    public void act(float delta) {
        super.act(delta);

        applyPhysics(delta);
    }
}