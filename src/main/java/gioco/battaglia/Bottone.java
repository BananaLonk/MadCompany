package gioco.battaglia;

import gioco.oggetti.BaseActor;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Bottone extends BaseActor {
    private Animation texture;

    public Bottone(float x, float y, Stage s) {
        super(x, y, s);
        texture = loadTexture("images/Start.png");
    }

    public void act(float delta) {
        super.act(delta);

        applyPhysics(delta);
    }
}
