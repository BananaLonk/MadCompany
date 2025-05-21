package gioco.mappe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl3.audio.Mp3;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import gioco.battaglia.Battaglia;
import gioco.oggetti.*;
import gioco.oggetti.dialoghi.Dialogo;
import gioco.oggetti.dialoghi.DialogoNPC;
import gioco.oggetti.oggettiMappa.*;
import gioco.personaggi.*;

import javax.sound.sampled.Port;
import java.io.FileNotFoundException;
import java.io.Serializable;

/**
 * mappa del primo "labirinto" del gioco
 */
public class Dungeon extends BaseScreen{
    public OrthographicCamera camera;
    protected Viewport viewport;
    protected Texture sfondoMenu;
    protected ShapeRenderer shapeRenderer;
    private TilemapActor tma;
    private BitmapFont font12;
    private float contatore=0;
    private TiledMapTileLayer invisibile;
    private TiledMapTileLayer trasparente;
    private GestoreDialoghi gestoreDialoghi;
    private Dialogo dialogo;
    private boolean inDialogo;
    private BloccoMovibile blocco;

    //TODO: rimuovi
    private DialogoNPC dialogoNpc;


    private float fadeAlpha=0f;
    private boolean fade=false;
    private BaseActor culo;


    @Override
    public void initialize() {
        //telecamera con dimensioni di rendering
        camera = new OrthographicCamera(640,360);

        //viewport che integra la telecamera
        viewport = new FitViewport(320,180,camera);
        mainStage = new Stage(viewport);


        //sfondo
        sfondoMenu=new Texture("images/Lebron James Hennesy.png");

        gestoreDialoghi=new GestoreDialoghi();

        //carico la mappa e gli oggetti di tutti i tipi
        tma = new TilemapActor("assets/dungeon.tmx", mainStage, camera);
        tma.setScale(4);
        //carico le collisioni
        for (MapObject obj : tma.getRectangleList("solido") )
        {
            MapProperties props = obj.getProperties();
            new Solido( (float)props.get("x"), (float)props.get("y"),
                    (float)props.get("width"), (float)props.get("height"),
                    mainStage );
        }
        //carico i teletrasporti
        for (MapObject obj : tma.getRectangleList("teletrasporto") )
        {
            MapProperties props = obj.getProperties();
            new Teletrasporto( (float)props.get("x"), (float)props.get("y"),
                    (float)props.get("width"), (float)props.get("height"),(String)props.get("mappa destinazione"),
                    mainStage );
        }
        //carico gli oggetti "invisibili"
        for (MapObject obj : tma.getRectangleList("rimuovi livello") )
        {
            MapProperties props = obj.getProperties();
            new RimuoviLivello( (float)props.get("x"), (float)props.get("y"),
                    (float)props.get("width"), (float)props.get("height"),(String)props.get("mappa destinazione"),
                    mainStage );
        }
        //carico gli NPC
        for (MapObject obj : tma.getRectangleList("NPC") )
        {
            MapProperties props = obj.getProperties();
            try {
                new Npc((float) props.get("x"),
                        (float) props.get("y"),mainStage,
                        (String) props.get("nome"),gestoreDialoghi,
                        (int) props.get("maxDialoghi"));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        //carico i nemici
        for (MapObject obj : tma.getRectangleList("nemico") )
        {
            MapProperties props = obj.getProperties();
            new Nemico((float) props.get("x"), (float) props.get("y"),mainStage, (String) props.get("percorso")).setScale(2);
        }
        //carico limiti blocco
        for (MapObject obj : tma.getRectangleList("solido blocco") )
        {
            MapProperties props = obj.getProperties();
            new SolidoBlocco( (float)props.get("x"), (float)props.get("y"),
                    (float)props.get("width"), (float)props.get("height"),
                    mainStage );
        }
        //carico l'arrivo del blocco
        for (MapObject obj : tma.getRectangleList("arrivo") )
        {
            MapProperties props = obj.getProperties();
            new ArrivoBlocco( (float)props.get("x"), (float)props.get("y"),
                    (float)props.get("width"), (float)props.get("height"),
                    mainStage);
        }
        //carico la porta
        for (MapObject obj : tma.getRectangleList("porta") )
        {
            MapProperties props = obj.getProperties();
            new Porta( (float)props.get("x"), (float)props.get("y"),
                    (float)props.get("width"), (float)props.get("height"),
                    mainStage);
        }

        invisibile = (TiledMapTileLayer) tma.getTiledMap().getLayers().get("Invisibile");
        trasparente = (TiledMapTileLayer) tma.getTiledMap().getLayers().get("Trasparente");

        //carico la posizione iniziale del protagonista
        MapObject startPoint = tma.getRectangleList("start").get(0);
        MapProperties startProps = startPoint.getProperties();

        //carico il blocco
        startPoint = tma.getRectangleList("start blocco").get(0);
        MapProperties startBlocco= startPoint.getProperties();
        blocco=new BloccoMovibile((float) startBlocco.get("x"),(float) startBlocco.get("y"), mainStage);

        if(Squadra.isInizializzata()){
            Squadra.aggiungiAStage(mainStage, (float) startProps.get("x"), (float) startProps.get("y"));
        }else{
            Squadra.inizializzaSquadra(mainStage, (float) startProps.get("x"), (float) startProps.get("y"));
        }

        //rendered delle hitbox
        shapeRenderer=new ShapeRenderer();

        //creo il font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("assets/OpenSans.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 10; // font size
        font12 = generator.generateFont(parameter);
        font12.setUseIntegerPositions(false);
        generator.dispose();

        //roba di Nava
        culo=new BaseActor(0,0,mainStage);
        culo.setScale(10);
        culo.setOpacity(0f);
        culo.loadTexture("images/fadein.png");

        //inizializzo i dialoghi
        try {
            dialogo= (Dialogo) gestoreDialoghi.caricaDialogo("assets/dialoghi/dialoghi.csv",Squadra.joker.getX(),Squadra.joker.getY()+50,mainStage,-1);
            dialogo.setScale(0.7f);
            dialogo.setVisible(false);
            dialogoNpc= (DialogoNPC) gestoreDialoghi.caricaDialogo("assets/dialoghi/Mario.csv",Squadra.joker.getX(),Squadra.joker.getY()+50,mainStage,1);
            dialogoNpc.setVisible(false);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(float dt) {

        controllaDialoghi(dt);

        if(Gdx.input.isKeyJustPressed(Input.Keys.E)){
            for (BaseActor actor : BaseActor.getList(mainStage, Npc.class.getName())) {
                Npc n = (Npc) actor;

                if (n.parla(Squadra.joker.getX(),Squadra.joker.getY())) {
                    try {
                        dialogoNpc= (DialogoNPC) n.getDialogo();
                        inDialogo=true;
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        //Puzzle
        if(blocco.overlaps(Squadra.joker)){
            Vector2 offset = blocco.preventOverlap(Squadra.joker);
            if (offset != null) {
                // collisione nella direzione x
                if (Math.abs(offset.x) > Math.abs(offset.y)) {
                    blocco.velocityVec.x = Squadra.joker.velocityVec.x*2;
                } else // collided in Y direction
                {
                    blocco.velocityVec.y = Squadra.joker.velocityVec.y*2;
                }

            }
        }

        controllaOggetti();

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            Launcher.setPausa();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.I))
            Launcher.apriInventario(this);

        if(fade){
            culo.act(dt);
            fadeAlpha+=dt;
            culo.setOpacity(fadeAlpha);
            if(fadeAlpha>=1){
                fade=false;
                culo.remove();
                Launcher.setUltimaMappa(this);
                Battaglia battaglia = new Battaglia("csvBattaglia/boss.csv");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Launcher.cambiaMappa(battaglia);
            }
        }

        Squadra.segui(dt);

        //mostra/nascondi hitbox
        if(Gdx.input.isKeyJustPressed(Input.Keys.H))
            Launcher.setHitbox();

    }

    @Override
    public void resize(int width, int height) {
        // the game area will be resized as per the screen size of the device
        mainStage.getViewport().update(width, height, true);
    }

    public void menuPausa(){
        mainStage.getBatch().begin();
        mainStage.getBatch().draw(sfondoMenu, Squadra.joker.getX()-(float)sfondoMenu.getWidth() /2,  Squadra.joker.getY()-(float)sfondoMenu.getHeight() /2);
        mainStage.getBatch().end();

    }

    public void renderHitbox() {
        // Inizia il rendering con ShapeRenderer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED); // Colore della hitbox
        shapeRenderer.setProjectionMatrix(camera.combined);

        for(BaseActor baseActor: BaseActor.getList(mainStage,BaseActor.class.getName())) {
            Polygon boundaryPolygon = baseActor.getBoundaryPolygon();
            if (boundaryPolygon == null) return; // Se non c'è un poligono, esci

            float[] vertices = boundaryPolygon.getVertices();

            // Ottieni la matrice di trasformazione dell'actor
            Matrix4 transform = new Matrix4();
            transform.setToTranslationAndScaling(baseActor.getX(), baseActor.getY(), 0, baseActor.getScaleX(), baseActor.getScaleY(), 1);
            transform.rotate(0, 0, 1, baseActor.getRotation());


            // Applica la trasformazione
            shapeRenderer.setTransformMatrix(transform);

            // Disegna il poligono
            for (int i = 0; i < vertices.length - 1; i += 2) {
                float x1 = vertices[i];
                float y1 = vertices[i + 1];
                float x2 = vertices[(i + 2) % vertices.length];
                float y2 = vertices[(i + 3) % vertices.length];
                shapeRenderer.line(x1, y1, x2, y2);
            }
        }

        shapeRenderer.end();
        shapeRenderer.identity(); // Ripristina le impostazioni
    }

    //TODO: sistema in un unico for per tutti gli oggetti
    private void controllaOggetti(){
        //calcolo collisioni
        for (BaseActor actor : BaseActor.getList(mainStage, Oggetto.class.getName()))
        {
            if(actor instanceof Solido) {
                Solido solido = (Solido) actor;
                if (Squadra.joker.overlaps(solido) && solido.isEnable()) {
                    Vector2 offset = Squadra.joker.preventOverlap(solido);
                    if (offset != null) {
                        // collisione nella direzione x
                        if (Math.abs(offset.x) > Math.abs(offset.y)) {
                            Squadra.joker.velocityVec.x = 0;
                            Squadra.joker.setX(Squadra.joker.getUltimeX().getLast());
                        } else // collided in Y direction
                        {
                            Squadra.joker.velocityVec.y = 0;
                            Squadra.joker.setY(Squadra.joker.getUltimeY().getLast());
                        }

                    }
                }
            }
            if(actor instanceof RimuoviLivello){
                if((Squadra.joker.overlaps(actor)) && ((RimuoviLivello) actor).isEnable()){
                    invisibile.setVisible(false);
                    blocco.setVisible(true);
                }
            }
            if(actor instanceof Teletrasporto){
                if ( Squadra.joker.overlaps(actor))
                {
                    if(((Teletrasporto) actor).getDestinazione().equalsIgnoreCase("battaglia")){
                        fade=true;
                    }
                    if(((Teletrasporto) actor).getDestinazione().equalsIgnoreCase("inizio")){
                        Launcher.openWorld=new OpenWorld();
                        Launcher.cambiaMappa(Launcher.openWorld);
                    }
                }
            }
            if(actor instanceof SolidoBlocco) {
                SolidoBlocco solido = (SolidoBlocco) actor;
                if (blocco.overlaps(solido) && solido.isEnable()) {
                    Vector2 offset = blocco.preventOverlap(solido);
                    Vector2 offset2 = Squadra.joker.preventOverlap(blocco);
                    if (offset != null) {
                        // collisione nella direzione x
                        if (Math.abs(offset.x) > Math.abs(offset.y)) {
                            blocco.velocityVec.x = 0;
                        } else // collided in Y direction
                        {
                            blocco.velocityVec.y = 0;
                        }
                    }

                    if (offset2 != null) {
                        // collisione nella direzione x
                        if (Math.abs(offset2.x) > Math.abs(offset2.y)) {
                            Squadra.joker.velocityVec.x = 0;
                            Squadra.joker.setX(Squadra.joker.getUltimeX().getLast());
                        } else // collided in Y direction
                        {
                            Squadra.joker.velocityVec.y = 0;
                            Squadra.joker.setY(Squadra.joker.getUltimeY().getLast());
                        }
                    }
                }
            }
            if(actor instanceof ArrivoBlocco){
                ArrivoBlocco arrivo = (ArrivoBlocco) actor;
                if (blocco.overlaps(arrivo) && arrivo.isEnable()){
                    ((Porta) BaseActor.getList(mainStage, Porta.class.getName()).get(0)).setEnable(false);
                    arrivo.setEnable(false);
                    Gdx.audio.newSound(Gdx.files.internal("sounds/portaAperta.mp3")).play();
                }
            }
        }

        //cambio mappa con nemico
        for(BaseActor nemico: BaseActor.getList(mainStage,Nemico.class.getName())) {
            if (Squadra.joker.overlaps(nemico)) {
                fade=true;
            }
        }
    }


    private void disegnaTrasparente(){
        tma.tiledMapRenderer.getSpriteCache().begin();
        //tma.tiledMapRenderer.getSpriteCache().setColor(0,0,0,0.5f);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        tma.tiledMapRenderer.getSpriteCache().draw(2);
        tma.tiledMapRenderer.getSpriteCache().end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private boolean compagniOvrelap(BaseActor oggetto){
        for(Compagno membro : Squadra.compagni){
            if(membro.overlaps(oggetto))
                return true;
        }
        return false;
    }

    /**
     * renderizza tutti i dialoghi
     */
    private void renderizzaDialoghi(){
        mainStage.getBatch().begin();

        for(BaseActor actor : BaseActor.getList(mainStage,Dialogo.class.getName())) {
            if(actor.isVisible()) {
                dialogo = (Dialogo) actor;
                mainStage.getBatch().draw(dialogo.getSprite(), dialogo.getX() + 40, dialogo.getY() + 20);
                font12.draw(mainStage.getBatch(), dialogo.getTesto(), dialogo.getX() + 75, dialogo.getY() + 50);
            }
        }
        for(BaseActor actor : BaseActor.getList(mainStage,DialogoNPC.class.getName())){
            if(actor.isVisible()) {
                dialogoNpc = (DialogoNPC) actor;
                mainStage.getBatch().draw(dialogoNpc.getSprite(), dialogoNpc.getX()+5, dialogoNpc.getY() + 20);
                font12.draw(mainStage.getBatch(), dialogoNpc.getTesto(), dialogoNpc.getX() + 40, dialogoNpc.getY() + 50);
            }
        }

        mainStage.getBatch().end();
    }

    /**
     * gestisce i dialoghi
     * @param dt delta time
     */
    private void controllaDialoghi(float dt){
        if (BaseActor.getList(mainStage,Dialogo.class.getName()).isEmpty()) {
            contatore+=dt;
            if(contatore>15){
                contatore=0;
                try {
                    dialogo= (Dialogo) gestoreDialoghi.caricaDialogo("assets/dialoghi/dialoghi.csv",Squadra.joker.getX()+100,Squadra.joker.getY()+110,mainStage,-1);
                    dialogo.setScale(0.7f);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if(BaseActor.getList(mainStage,DialogoNPC.class.getName()).isEmpty()){
            inDialogo=false;
        }
    }

    @Override
    public void render(float dt)
    {
        if(!Launcher.isPausa() && !fade && !inDialogo) {
            mainStage.act(dt);
        }else{
            dialogoNpc.act(dt);
        }

        if(!inDialogo){
            update(dt);
        }
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //sposto la telecamera
        mainStage.getViewport().getCamera().position.x=Squadra.joker.getX();
        mainStage.getViewport().getCamera().position.y=Squadra.joker.getY();
        //sposto i dialoghi se ci sono
        dialogo.setX(Squadra.joker.getX() - 50);
        dialogo.setY(Squadra.joker.getY() + 34);
        dialogoNpc.setX(Squadra.joker.getX() - dialogoNpc.getWidth()/2);
        dialogoNpc.setY(Squadra.joker.getY() - 80);



        mainStage.draw();
        renderizzaDialoghi();

        if(!invisibile.isVisible())
            disegnaTrasparente();

        invisibile.setVisible(true);
        blocco.setVisible(false);

        if(Launcher.isHitbox())
            renderHitbox();

        controllaDialoghi(dt);

        if(Launcher.isPausa())
            menuPausa();
    }
}
