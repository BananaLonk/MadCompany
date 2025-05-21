package gioco.battaglia;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import gioco.battaglia.mosse.Mossa;
import gioco.oggetti.BaseActor;
import gioco.oggetti.Inventario;
import gioco.personaggi.Joker;

import java.util.ArrayList;

import static gioco.mappe.Launcher.apriInventario;
import static gioco.oggetti.BaseGame.setActiveScreen;


public class Hud extends BaseActor {

    //pulsanti per hud
    public PulsanteBattaglia[][] pulsantiMain;
    public PulsanteBattaglia[] pulsantiMosse;

    protected int selezione; //1=main, 2=attacco, ...

    public int sceltaX;
    public int sceltaY;
    protected PlayerBattaglia player;
    private static ArrayList<BasePGBattaglia> bersagli; //arraylist per la selezione dei bersagli

    private Mossa mossa; //mossa di sostegno per aiutarmi nella codifica

    BitmapFont font=new BitmapFont();
    private boolean disegnaTesto;

    public Hud(float x, float y, Stage s, PlayerBattaglia player) {
        super(x, y, s);

        pulsantiMain = new PulsanteBattaglia[2][2];
        pulsantiMosse = new PulsanteBattaglia[player.mosse.size()];

        sceltaX=0;
        sceltaY=0;
        this.player = player;

        //sfondo dell'hud
        this.loadTexture("images/Battaglia/sfondo hud.png");
        this.setScale(5);
        this.setPosition(s.getWidth()/2-80, 80);

        selezione=1; //primo menu è quello di base

        this.bersagli = new ArrayList<>();
        Battaglia.bersagliEffettivi = new ArrayList<>();

        //pulsanti di base
        pulsantiMain[0][0] = new PulsanteBattaglia(x, y+50, s,"images/Battaglia/pulsante attacco.png","images/Battaglia/pulsante attacco selezionato.png","images/Battaglia/pulsante attacco attivo.png");
        pulsantiMain[0][0].centerAtActor(this);
        pulsantiMain[0][0].setPosition(pulsantiMain[0][0].getX()-25*5, pulsantiMain[0][0].getY()+7*5);
        pulsantiMain[0][0].setNormale();
        pulsantiMain[0][0].setScale(5);

        pulsantiMain[0][1] = new PulsanteBattaglia(x, y+50, s,"images/Battaglia/pulsante speciale.png","images/Battaglia/pulsante speciale selezionato.png","images/Battaglia/pulsante speciale attivo.png");
        pulsantiMain[0][1].centerAtActor(this);
        pulsantiMain[0][1].setPosition(pulsantiMain[0][1].getX()+25*5, pulsantiMain[0][1].getY()+7*5);
        pulsantiMain[0][1].setNormale();
        pulsantiMain[0][1].setScale(5);

        pulsantiMain[1][0] = new PulsanteBattaglia(x, y+50, s,"images/Battaglia/pulsante inventario.png","images/Battaglia/pulsante inventario selezionato.png","images/Battaglia/pulsante inventario attivo.png");
        pulsantiMain[1][0].centerAtActor(this);
        pulsantiMain[1][0].setPosition(pulsantiMain[1][0].getX()-25*5, pulsantiMain[1][0].getY()-7*5);
        pulsantiMain[1][0].setNormale();
        pulsantiMain[1][0].setScale(5);

        pulsantiMain[1][1] = new PulsanteBattaglia(x, y+50, s,"images/Battaglia/pulsante check.png","images/Battaglia/pulsante check selezionato.png","images/Battaglia/pulsante check attivo.png");
        pulsantiMain[1][1].centerAtActor(this);
        pulsantiMain[1][1].setPosition(pulsantiMain[1][1].getX()+25*5, pulsantiMain[1][1].getY()-7*5);
        pulsantiMain[1][1].setNormale();
        pulsantiMain[1][1].setScale(5);

        for(int i=0;i<player.mosse.size();i++){
            pulsantiMosse[i]=new PulsanteBattaglia(x,y+50,s,"images/Battaglia/pulsante vuoto.png","images/Battaglia/pulsante vuoto selezionato.png", "images/Battaglia/pulsante vuoto attivo.png");
            pulsantiMosse[i].centerAtActor(this);
            pulsantiMosse[i].setPosition(pulsantiMosse[i].getX(), pulsantiMosse[i].getY()+120+(i*60));
            pulsantiMosse[i].setNormale();
            pulsantiMosse[i].setScale(5);
            pulsantiMosse[i].setVisible(false);
        }
        this.disegnaTesto=false;
    }


    public void act(float delta) {
        super.act(delta);


        if (selezione == 1) {
            sceltaMain();
        } else if (selezione == 2) {
            sceltaBersagli();
        } else if (selezione == 3) {
            selezione = 1;
        } else if (selezione == 4) {
            sceltaMossa();
        }



    }

    //scelta tra i pulsanti principali
    protected void sceltaMain() {
        pulsantiMain[sceltaX][sceltaY].setSelezionato();
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){ //quando premo invio, guardo che bottone è selezionato e entro
            pulsantiMain[sceltaX][sceltaY].setAttivo();

            if(sceltaX==0 && sceltaY==0){ //attacco normale
                this.mossa=this.player.mosse.get(0);
                this.preparaBersagli(this.mossa.nemici,this.mossa.aTerra,this.mossa.morto,this.mossa.aTerraPeffo,this.mossa.mortoPeffo);
                selezione=2;
            } else if(sceltaX==1 && sceltaY==0)selezione=3; //inventario
            else if (sceltaX==0 && sceltaY==1) {
                for(int i=0;i<player.mosse.size();i++){ //se scelgo la mossa, visualizzo i pulsanti delle mosse
                    pulsantiMosse[i].setVisible(true);
                }
                pulsantiMosse[0].setSelezionato();
                disegnaTesto=true;
                selezione=4;//scelta mossa
            }

        } else if(Gdx.input.isKeyJustPressed(Input.Keys.S) && sceltaX!=1){
            pulsantiMain[sceltaX][sceltaY].setNormale();
            sceltaX++;
            pulsantiMain[sceltaX][sceltaY].setSelezionato();

        } else if(Gdx.input.isKeyJustPressed(Input.Keys.W) && sceltaX!=0){
            pulsantiMain[sceltaX][sceltaY].setNormale();
            sceltaX--;
            pulsantiMain[sceltaX][sceltaY].setSelezionato();

        } else if(Gdx.input.isKeyJustPressed(Input.Keys.D) && sceltaY!=1){
            pulsantiMain[sceltaX][sceltaY].setNormale();
            sceltaY++;
            pulsantiMain[sceltaX][sceltaY].setSelezionato();

        } else if(Gdx.input.isKeyJustPressed(Input.Keys.A) && sceltaY!=0){
            pulsantiMain[sceltaX][sceltaY].setNormale();
            sceltaY--;
            pulsantiMain[sceltaX][sceltaY].setSelezionato();

        }
    }



    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        // disegno le scritte *sullo stesso batch* in cui Stage sta disegnando
        if(disegnaTesto){
            for (int i = 0; i < player.mosse.size(); i++) {
                pulsantiMosse[i].setZIndex(1);
                font.draw(player.getStage().getBatch(), player.mosse.get(i).nome, pulsantiMosse[i].getX()-50, pulsantiMosse[i].getY()+10);
            }
        }

    }

    /*
     Scelta dei bersagli base
    */
    protected void sceltaBersagli(){

        if(bersagli.isEmpty())return; //per sicurezza, se non si è ancora inizializzato l'arraylist, non faccio partire scelta bersagli


        bersagli.get(sceltaX).setSelezionato(true);
        //scelta del tizio da colpire
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){

            this.bersagli.get(sceltaX).setAttivo(true);
            if (Battaglia.bersagliEffettivi.size() < this.mossa.bersagli) {
                Battaglia.bersagliEffettivi.add(this.bersagli.get(sceltaX));
            }
            if (Battaglia.bersagliEffettivi.size() == this.mossa.bersagli) {
                this.player.usaMossa(this.mossa,this);
            }


        } else if(Gdx.input.isKeyJustPressed(Input.Keys.S) && sceltaX!=0){
            bersagli.get(sceltaX).setSelezionato(false);
            sceltaX--;
            bersagli.get(sceltaX).setSelezionato(true);

        } else if(Gdx.input.isKeyJustPressed(Input.Keys.W) && sceltaX!=bersagli.size()-1){
            bersagli.get(sceltaX).setSelezionato(false);
            sceltaX++;
            bersagli.get(sceltaX).setSelezionato(true);

        } else if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            selezione=1; //quando premo esc, torno al menu iniziale
            sceltaX=0;
            resetSelezione();
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)){
            if(Battaglia.bersagliEffettivi.contains(this.bersagli.get(sceltaX))){ //se bersagliEffettivi ha già il bersaglio, lo tolgo

                Battaglia.bersagliEffettivi.remove(this.bersagli.get(sceltaX));
                this.bersagli.get(sceltaX).setAttivo(false);
                bersagli.get(sceltaX).setSelezionato(true);

            }
        }

    }

    public void resetSelezione(){
        sceltaX=0;
        while(!bersagli.isEmpty()){
            bersagli.get(0).setSelezionato(false);
            bersagli.get(0).setAttivo(false);
            bersagli.remove(0);
        }
        Battaglia.bersagliEffettivi.clear();
    }


    //scelta con bottoni della mossa da fare, non il target della suddetta mossa
    public void sceltaMossa(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){

            //controllo copiato da nemicobattaglia per vedere se posso eseguire quella mossa
            if(player.mosse.get(sceltaX).costo<=player.PM){

                ArrayList<PlayerBattaglia> alleatiMorti= new ArrayList<>(); //Arraylist di alleati morti
                ArrayList<PlayerBattaglia> alleatiFeriti= new ArrayList<>(); //Arraylist di alleati feriti
                for (PlayerBattaglia o : Battaglia.squadra) { //riempio gli arraylist
                    if (o.morto)   alleatiMorti.add(o);
                    else if (o.PV < o.PVMax*0.5) alleatiFeriti.add(o);
                }

                if(player.mosse.get(sceltaX).tag.equals("R") && !alleatiMorti.isEmpty()){
                    this.mossa=player.mosse.get(sceltaX);
                    preparaBersagli(this.mossa.nemici,this.mossa.aTerra,this.mossa.morto,this.mossa.aTerraPeffo,this.mossa.mortoPeffo);
                    sceltaX=0;
                    selezione=2;
                } else if(player.mosse.get(sceltaX).tag.equals("G") && !alleatiFeriti.isEmpty()){
                    this.mossa=player.mosse.get(sceltaX);
                    preparaBersagli(this.mossa.nemici,this.mossa.aTerra,this.mossa.morto,this.mossa.aTerraPeffo,this.mossa.mortoPeffo);
                    sceltaX=0;
                    selezione=2;
                } else if(player.mosse.get(sceltaX).tag.equals("A")){
                    this.mossa=player.mosse.get(sceltaX);
                    preparaBersagli(this.mossa.nemici,this.mossa.aTerra,this.mossa.morto,this.mossa.aTerraPeffo,this.mossa.mortoPeffo);
                    sceltaX=0;
                    selezione=2;
                }

            }

        } else if(Gdx.input.isKeyJustPressed(Input.Keys.S) && sceltaX!=0){
            pulsantiMosse[sceltaX].setNormale();
            sceltaX--;
            pulsantiMosse[sceltaX].setSelezionato();

        } else if(Gdx.input.isKeyJustPressed(Input.Keys.W) && sceltaX!=pulsantiMosse.length-1){
            pulsantiMosse[sceltaX].setNormale();
            sceltaX++;
            pulsantiMosse[sceltaX].setSelezionato();

        } else if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            for(int i=0;i<player.mosse.size();i++){ //quando esco, li nasconde di nuovo
                pulsantiMosse[i].setVisible(false);
            }
            disegnaTesto=false;
            sceltaX=0;
            selezione=1;
        }
    }


    //metodo che prepara un arraylist di bersagli che si possono colpire, presi dagli arraylist di battaglia
    public void preparaBersagli(boolean nemici, boolean aTerra, boolean morto, boolean aTerraPeffo, boolean mortoPeffo){ //i primi boolean sono i parametri, i secondi sono se i parametri sono obbligatori o no
        boolean buono;
        if(nemici){
            for(BasePGBattaglia i: Battaglia.nemici){
                buono=true;
                if(aTerraPeffo && i.aTerra!=aTerra)buono=false;
                if(mortoPeffo && i.morto!=morto)buono=false;

                if(buono)bersagli.add(i);
            }
        } else{
            for(BasePGBattaglia i: Battaglia.squadra){
                buono=true;
                if(aTerraPeffo && i.aTerra!=aTerra)buono=false;
                if(mortoPeffo && i.morto!=morto)buono=false;

                if(buono)bersagli.add(i);
            }
        }
    }

    public void rimuovi(){
        this.pulsantiMain[0][0].remove();
        this.pulsantiMain[0][1].remove();
        this.pulsantiMain[1][0].remove();
        this.pulsantiMain[1][1].remove();
        for(int i=0;i<player.mosse.size();i++){
            this.pulsantiMosse[i].remove();
        }
        this.remove();
    }

}
