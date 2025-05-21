package gioco.mappe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.compression.lzma.Base;
import gioco.oggetti.BaseActor;
import gioco.oggetti.BaseScreen;
import gioco.oggetti.Bottone;

import static gioco.oggetti.BaseGame.setActiveScreen;

/**
 * classe che permette di avviare il gioco tramite click del mouse
 */
public class Menu extends BaseScreen {
    protected Texture sfondoMenu;
    protected BaseActor logo;
    protected Bottone newGameButton,quitButton,continueButton;
    protected ShapeRenderer shapeRenderer;

    @Override
    public void initialize() {
        sfondoMenu=new Texture("images/sfondo.png");
        newGameButton=new Bottone(500,400,mainStage,"images/NewGameButton.png");
        continueButton=new Bottone(500,300,mainStage,"images/ContinueButton.png");
        quitButton=new Bottone(500,200,mainStage,"images/QuitButton.png");

        shapeRenderer=new ShapeRenderer();

        logo=new BaseActor(Gdx.graphics.getWidth()/2-50,Gdx.graphics.getHeight()/2+200,mainStage);
        logo.setAnimation(logo.loadTexture("images/logo.png"));
        logo.setScale(5);


    }

    @Override
    public void update(float dt) {

    }

    /**
     * crea il processore degli input utilizzato nella mappa
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                // Converti le coordinate dello schermo in coordinate del mondo di gioco
                float worldX = screenX;
                float worldY = Gdx.graphics.getHeight() - screenY;
                if(button==0){
                    if (newGameButton.getBoundaryPolygon().contains(worldX, worldY)) {
                        Launcher.setNuovaPartita(true);
                        Launcher.inizializza();
                    }
                    if (continueButton.getBoundaryPolygon().contains(worldX, worldY)) {
                    }
                    if (quitButton.getBoundaryPolygon().contains(worldX, worldY)) {
                        System.exit(0);
                    }
                }

                return true;
            }
        });
    }
}
