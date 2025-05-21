package gioco.battaglia;

import com.badlogic.gdx.scenes.scene2d.Stage;
import gioco.battaglia.mosse.Mossa;
import gioco.oggetti.BaseActor;
import gioco.personaggi.Compagno;
import gioco.personaggi.Joker;

import java.util.ArrayList;
import java.util.Random;

import static gioco.oggetti.BaseGame.setActiveScreen;


public class PlayerBattaglia extends BasePGBattaglia {


    //giocatore a cui è collegato
    protected BaseActor giocatore;


    Random r = new Random();

    public PlayerBattaglia(float x, float y, Stage s,int PV, int PM, int livello, int difesa, int attacco, int agilita, int critico) {
        super(x, y, s);

        this.PVMax=PV;
        this.PMMax=PM;
        this.PV=PV;
        this.PM=PM;
        this.livello=livello;

        this.difesa=difesa;
        this.attacco=attacco;
        this.agilita=agilita;
        this.critico=critico;

        mosse.add(new Mossa("CARTE","Tanto la descrizione non la stampa", 'A',10,1, true,false,false,false,true,() ->{
            boolean Critico=false;
            if(rand.nextInt(10)+this.critico>=100) Critico=true;
            GiocoCarte giocoCarte=new GiocoCarte();
            setActiveScreen(giocoCarte);
            Battaglia.bersagliEffettivi.get(0).presaDanno(110,3,Critico);
        }));



    }

    //costruttore se è compagno
    public PlayerBattaglia(float x, float y, Stage s, Compagno giocatore, int PV, int PM, int livello, int difesa, int attacco, int agilita, int critico){
        this(x,y,s,PV,PM,livello,difesa,attacco,agilita,critico);
        this.giocatore=(BaseActor) giocatore;
    }

    //construttore se è joker
    public PlayerBattaglia(float x, float y, Stage s, Joker giocatore, int PV, int PM, int livello, int difesa, int attacco, int agilita, int critico){
        this(x,y,s,PV,PM,livello,difesa,attacco,agilita,critico);
        this.giocatore=(BaseActor) giocatore;
    }



















}
