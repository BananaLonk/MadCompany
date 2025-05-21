package gioco.battaglia;
import java.util.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.viewport.Viewport;
import gioco.oggetti.BaseActor;
import gioco.oggetti.BaseScreen;
import gioco.oggetti.GiocoGenerico;

public class GiocoCarte extends BaseScreen {
    private BaseActor fondale;
    private boolean win;
    private OrthographicCamera camera;
    private int carteSelezionate=0;
    protected Viewport viewport;
    protected BitmapFont font;
    protected ArrayList<Carta> carte,listaCarteSelezionate;
    protected ArrayList<Carta> mazzo;
    protected Bottone start,discard;
    protected int valore,valoreElemento,scarti=0;
    protected String listaCarte[][]= {
            {"images/Ghiaccio/AssoGhiaccio.png","images/Ghiaccio/2Ghiaccio.png","images/Ghiaccio/3Ghiaccio.png","images/Ghiaccio/4Ghiaccio.png","images/Ghiaccio/5Ghiaccio.png","images/Ghiaccio/6Ghiaccio.png","images/Ghiaccio/7Ghiaccio.png","images/Ghiaccio/8Ghiaccio.png","images/Ghiaccio/9Ghiaccio.png"}, //ghiaccio
            {"images/Fuoco/AssoFuoco.png","images/Fuoco/2Fuoco.png","images/Fuoco/3Fuoco.png","images/Fuoco/4Fuoco.png","images/Fuoco/5Fuoco.png","images/Fuoco/6Fuoco.png","images/Fuoco/7Fuoco.png","images/Fuoco/8Fuoco.png","images/Fuoco/9Fuoco.png"}, //fuoco
            {"images/Elettro/AssoElettro.png","images/Elettro/2Elettro.png","images/Elettro/3Elettro.png","images/Elettro/4Elettro.png","images/Elettro/5Elettro.png","images/Elettro/6Elettro.png","images/Elettro/7Elettro.png","images/Elettro/8Elettro.png","images/Elettro/9Elettro.png"}, //elettro
            {"images/Ghiaccio/AssoGhiaccio.png","images/Ghiaccio/2Ghiaccio.png","images/Ghiaccio/3Ghiaccio.png","images/Ghiaccio/4Ghiaccio.png","images/Ghiaccio/5Ghiaccio.png","images/Ghiaccio/6Ghiaccio.png","images/Ghiaccio/7Ghiaccio.png","images/Ghiaccio/8Ghiaccio.png","images/Ghiaccio/9Ghiaccio.png"}  //aria
    };

    @Override
    public void initialize() {


        fondale=new BaseActor(0,0,mainStage);
        fondale.loadTexture("images/balatro.png");


        mazzo=new ArrayList<>();
        //riempimento mazzo iniziale
        for(int j=0;j<4;j++) {
            for(int i=0;i<9;i++){
                mazzo.add(new Carta(-200,-200,mainStage,listaCarte[j][i],i+1,getElemento(j)));
            }
        }


        font= new BitmapFont();
        carte=new ArrayList<>();
        start=new Bottone(Gdx.graphics.getWidth()/3,Gdx.graphics.getHeight()/2,mainStage);
        start.setScale(3f);
        discard=new Bottone(Gdx.graphics.getWidth()/50,Gdx.graphics.getHeight()/2,mainStage);
        discard.setScale(3f);
        creaMano(carte);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                // Converti le coordinate dello schermo in coordinate del mondo di gioco
                float worldX = screenX;
                float worldY = Gdx.graphics.getHeight() - screenY;

                // Verifica se il clic è avvenuto sull'oggetto
                for(int i=0;i<6;i++){
                    if (carte.get(i).getBoundaryPolygon().contains(worldX, worldY)) {
                        if(carteSelezionate<5 && carte.get(i).selezionato==false){
                            carte.get(i).selezionato = true;
                            carteSelezionate++;
                        }else if(carte.get(i).selezionato==true){
                            carte.get(i).selezionato = false;
                            carteSelezionate--;
                        }

                    }
                }
                if(start.getBoundaryPolygon().contains(worldX, worldY) && carteSelezionate>0) {
                    listaCarteSelezionate=new ArrayList<Carta>(5);
                    for(int i=0;i<6;i++){
                        if(carte.get(i).selezionato==true)
                            listaCarteSelezionate.add(carte.get(i));
                    }
                    System.out.println(Carta.controlloMano(listaCarteSelezionate));

                }

                if(discard.getBoundaryPolygon().contains(worldX, worldY) && carteSelezionate>0 && carteSelezionate<4 && scarti<2) {
                    modificaMano(carte);
                }

                return true;
            }
        });
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(float dt)
    {

        mainStage.act(dt);
        update(dt);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mainStage.draw();
    }

    public ArrayList creaMano(ArrayList<Carta> carte){
        float y=(Gdx.graphics.getHeight()/20);
        float x=0;
        //valore=numeroRandom();
        //valoreElemento=numeroElementoRandom();
        valore=(int)(Math.random() * mazzo.size());
        carte.add(mazzo.get(valore));
        mazzo.remove(valore);
        for(int i=1;i<6;i++){
            valore=(int)(Math.random() * mazzo.size());
            carte.add(mazzo.get(valore));
            mazzo.remove(valore);
        }
        ordinaCarte(carte);
        x=Gdx.graphics.getWidth()/25;
        for(int i=0;i<6;i++) {
            carte.get(i).setX(x);
            carte.get(i).setY(y);
            x+=200;
        }
        return carte;
    }

    public ArrayList modificaMano(ArrayList<Carta> carte){
        float x=(Gdx.graphics.getWidth()/25),y=(Gdx.graphics.getHeight()/20);
        for(int i=0;i<6;i++){
            if(carte.get(i).selezionato){
                valore=(int)(Math.random() * mazzo.size());


                carte.get(i).setY(-2000);
                carte.set(i,mazzo.get(valore));
                mazzo.remove(valore);
            }
            x+=200;
        }
        carteSelezionate=0;
        ordinaCarte(carte);
        x=Gdx.graphics.getWidth()/25;
        for(int i=0;i<6;i++) {
            carte.get(i).setX(x);
            carte.get(i).setY(y);
            x+=200;
        }
        return carte;
    }

    public String getElemento(int valore){
        switch (valore){
            case 0:
                return "GHIACCIO";

            case 1:
                return "FUOCO";

            case 2:
                return "ELETTRO";

            case 3:
                return "ARIA";
        }
        return "";
    }

    public static ArrayList ordinaCarte(ArrayList<Carta> carte) {
        for(int i=carte.size()-1;i>0;i--) {
            for(int j=0;j<i;j++) {
                if(carte.get(j).getValore()>carte.get(j+1).getValore()) {
                    Carta temp=carte.get(j+1);
                    carte.set(j+1, carte.get(j));
                    carte.set(j, temp);
                }
            }
        }
        return carte;
    }
}
