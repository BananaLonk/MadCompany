package gioco.oggetti;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * blocco che può essere spinto dal giocatore collidendoci
 */
public class BloccoMovibile extends BaseActor{
    public BloccoMovibile(float x, float y, Stage s) {
        super(x, y, s);
        setAnimation(loadTexture("images/blocco.png"));
        setAcceleration(1000);
        setMaxSpeed(200);
        setDeceleration(0);
    }

    @Override
    public void act(float dt) {
        super.act(dt);

        applyPhysics(dt);
    }
}
