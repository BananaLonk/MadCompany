package gioco.oggetti;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class GiocoGenerico extends Game {
    protected Stage stage;


    public void create () {
        stage = new Stage();
        initialize();
    }

    public abstract void initialize();

    public void render()
    {
        float dt = Gdx.graphics.getDeltaTime();
        stage.act(dt);
        update(dt);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }
    public abstract void update (float dt);

}
