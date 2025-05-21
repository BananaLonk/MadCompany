package gioco.battaglia;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.utils.compression.lzma.Base;
import gioco.battaglia.mosse.Mossa;
import gioco.mappe.Launcher;
import gioco.oggetti.BaseActor;
import gioco.oggetti.BaseScreen;

import java.io.Serializable;
import java.util.*;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static gioco.mappe.Launcher.cambiaMappa;
import static gioco.mappe.Launcher.setUltimaMappaAggiuntiva;
import static gioco.oggetti.BaseGame.setActiveScreen;


/**
 * Classe di base da cui ereditano sia giocatori che nemici
 */
public abstract class BasePGBattaglia extends BaseActor implements Serializable {

    //statistiche generali
    public int PV;
    public int PVMax;
    public int PM;
    public int PMMax;
    public int livello;

    //statistiche specifiche
    public int difesa;
    public int attacco;
    public int agilita;
    public int critico;

    //mosse
    public static ArrayList<Mossa> mosse;

    //resistenze/debolezze (-1=debole, 0=normale, 1=resistente)
    public int fisico;
    public int fuoco;
    public int ghiaccio;
    public int elettro;
    public int vento;


    //effetti di stato
    protected boolean morto;
    protected boolean aTerra;

    private boolean selezionato;
    private boolean attivo;

    //equipaggiamento
    public EquipaggiamentoBattaglia arma;
    public EquipaggiamentoBattaglia armatura;
    public EquipaggiamentoBattaglia accessorio;


    protected BaseActor Selezionato;
    protected BaseActor Attivo;

    private Animation<TextureRegion> idle;
    private Animation<TextureRegion> Attacco;

    protected BarreStato barreStato;


    Random rand=new Random();

    public BasePGBattaglia(float x, float y, Stage s) {
        super(x,y,s);
        this.morto=false;
        this.aTerra=false;

        this.selezionato=false;
        this.attivo=false;

        this.barreStato=new BarreStato(this);

        this.setScale(5);

        this.mosse=new ArrayList<>();

        this.Selezionato=new BaseActor(x,y-(this.getY()/2),s);
        this.Attivo=new BaseActor(x,y-(this.getY()/2),s);
        this.Selezionato.loadTexture("images/Battaglia/selezionato.png");
        this.Attivo.loadTexture("images/Battaglia/attivo.png");
        this.Selezionato.setVisible(false);
        this.Attivo.setVisible(false);
        this.Selezionato.setScale(5);
        this.Attivo.setScale(5);


        mosse.add(new Mossa("Attacco","Attacco fisico normale",'A',0,1,true,true,false,false,true,() ->{
            int danno;
            danno=0;
            danno+=this.attacco; //aggiungo al danno l'attacco di base del player
            boolean Critico=false;
            if(rand.nextInt(10)+this.critico>=100) Critico=true;
            System.out.println("critico: "+Critico);
            Battaglia.bersagliEffettivi.get(0).presaDanno(danno,1,Critico);
        }));

        mosse.add(new Mossa("Palla di fuoco doppia","Attacco che infierisce dei danni di fuoco 2 volte", 'A',30,2, true,false,false,false,true,() ->{
            boolean Critico=false;
            if(rand.nextInt(10)+this.critico>=100) Critico=true;
            Battaglia.bersagliEffettivi.get(0).presaDanno(100,2,Critico);
            if(rand.nextInt(10)+this.critico>=100) Critico=true;
            Battaglia.bersagliEffettivi.get(1).presaDanno(100,2,Critico);
        }));

        mosse.add(new Mossa("Cura doppia","Cura 2 bersagli a propria scelta di un discreto ammontare", 'G',30,2, false,false,false,false,true,() ->{
            Battaglia.bersagliEffettivi.get(0).PV+=30;
            if(Battaglia.bersagliEffettivi.get(0).PV>Battaglia.bersagliEffettivi.get(0).PVMax)Battaglia.bersagliEffettivi.get(0).PV=Battaglia.bersagliEffettivi.get(0).PVMax;
            Battaglia.bersagliEffettivi.get(1).PV+=30;
            if(Battaglia.bersagliEffettivi.get(1).PV>Battaglia.bersagliEffettivi.get(1).PVMax)Battaglia.bersagliEffettivi.get(1).PV=Battaglia.bersagliEffettivi.get(1).PVMax;
        }));

        mosse.add(new Mossa("Cura","Cura 1 bersaglio di un buon ammontare", 'G',25,1, false,false,false,false,true,() ->{
            Battaglia.bersagliEffettivi.get(0).PV+=60;
            if(Battaglia.bersagliEffettivi.get(0).PV>Battaglia.bersagliEffettivi.get(0).PVMax)Battaglia.bersagliEffettivi.get(0).PV=Battaglia.bersagliEffettivi.get(0).PVMax;
        }));

        mosse.add(new Mossa("Rinascita","Fa rinascere un bersaglio", 'R',50,1, false,false,true,false,true,() ->{
            Battaglia.bersagliEffettivi.get(0).morto=false;
            Battaglia.bersagliEffettivi.get(0).PV=Battaglia.bersagliEffettivi.get(0).PVMax/2;
        }));

        mosse.add(new Mossa("Mossa sorrisetto","Fresh!", 'A',30,1, true,false,false,false,true,() ->{
            boolean Critico=false;
            if(rand.nextInt(10)+this.critico>=100) Critico=true;
            Battaglia.bersagliEffettivi.get(0).presaDanno(110,3,Critico);
        }));

        this.caricaAnimazioni();

    }


    /*
    Funzioni per la selezione
     */
    public void setSelezionato(boolean selezionato) {
        if(selezionato && !attivo){
            this.Selezionato.setVisible(true);
            this.selezionato=true;
        }else{
            this.Selezionato.setVisible(false);
            this.selezionato=false;
        }
    }

    public void setAttivo(boolean attivo){
        if(attivo){
            this.Attivo.setVisible(true);
            this.attivo=true;
        }else{
            this.Attivo.setVisible(false);
            this.attivo=false;
        }
    }

    public void caricaAnimazioni(){
        //animazioni
        Texture walksheet = new Texture(Gdx.files.internal("images/Unarmed_walk/Unarmed_Walk_full.png"),true);
        TextureRegion[][] tmp = TextureRegion.split(walksheet, 14, 24);

        idle=new Animation<>(0.5f,tmp[0]);
        Attacco=new Animation<>(0.3f,tmp[1]);
        idle.setPlayMode(Animation.PlayMode.LOOP);
        Attacco.setPlayMode(Animation.PlayMode.LOOP);
        this.setAnimation(idle);
    }


    //funzioni della difesa
    protected void presaDanno(int danno,int tipo,boolean critico){

        if(rand.nextInt(10)+agilita>=100)System.out.println("Danno schivato"); //se ho abbastanza agilità, schivo il danno

        else{

            danno-=difesa;//un po del danno lo prende la difesa (già calcolata con l'equipaggiamento)
            if(danno<0) danno=0; //se il danno viene completamente negato, lo riporto a 0

            //controllo delle resistenze
            int temp=0;
            switch(tipo){
                case 1:
                    temp=this.fisico;
                    break;
                case 2:
                    temp=this.fuoco;
                    break;
                case 3:
                    temp=this.ghiaccio;
                    break;
                case 4:
                    temp=this.elettro;
                    break;
                case 5:
                    temp=this.vento;
                    break;
            }
            if(temp==-1){ //se è debole
                danno=danno*2;

                //se è debole cade a terra
                this.aTerra=true;
                System.out.println("A terra =(");
            }else if(temp==1){ //se è resistente
                danno=danno/2;
            }

            if(critico)danno= (int) (danno*1.3); // se il colpo è critico, fa il doppio dei danni
            if(this.aTerra)danno=(int)(danno*1.5);//se il pg è a terra, fa più danni


            this.PV-=danno;
            System.out.println("PV: "+PV);

            //se è morto, tolgo gli altri stati e applico morte
            if(this.PV<=0) {
                this.PV=0;
                this.aTerra=false;
                this.morto=true;
                System.out.println("Morto");
            }
        }
    }


    //equipaggiamento oggetti
    public void equipaggiaOggetto(EquipaggiamentoBattaglia oggetto){


        switch(oggetto.tipo){
            case 1:
                if(this.arma!=null)this.disequipaggiaOggetto(1);
                this.arma.eguaglia(oggetto);
                break;
            case 2:
                if(this.armatura!=null)this.disequipaggiaOggetto(2);
                this.armatura.eguaglia(oggetto);
                break;
            case 3:
                if(this.accessorio!=null)this.disequipaggiaOggetto(3);
                this.accessorio.eguaglia(oggetto);
                break;
        }
        this.attacco+=oggetto.attacco;
        this.difesa+=oggetto.difesa;
        this.agilita+=oggetto.agilita;
        this.critico+=oggetto.critico;

    }

    public void disequipaggiaOggetto(int index){
        switch(index){
            case 1:
                //copia su inventario
                Launcher.inventario.aggiuntaItem(this.arma);

                //rimozione attributi delle armi
                this.difesa-=this.arma.difesa;
                this.attacco-=this.arma.attacco;
                this.agilita-=this.arma.agilita;
                this.critico-=this.arma.critico;

                this.arma=null;
                break;
            case 2:
                //copia su inventario
                Launcher.inventario.aggiuntaItem(this.armatura);

                this.difesa-=this.armatura.difesa;
                this.attacco-=this.armatura.attacco;
                this.agilita-=this.armatura.agilita;
                this.critico-=this.armatura.critico;

                this.armatura=null;
                break;
            case 3:
                //copia su inventario
                Launcher.inventario.aggiuntaItem(this.accessorio);

                this.difesa-=this.accessorio.difesa;
                this.attacco-=this.accessorio.attacco;
                this.agilita-=this.accessorio.agilita;
                this.critico-=this.accessorio.critico;

                this.accessorio=null;
                break;
        }
    }


    protected void usaMossa(Mossa m, Hud hud){

        clearActions();

        addAction(sequence(
                run(() -> {
                    setAnimation(Attacco);
                    System.out.println("Animazione Attack");
                }),
                delay(Attacco.getAnimationDuration()),
                run(() -> {
                    m.utilizza();
                    System.out.println("Mossa utilizzata");
                    this.PM-=m.costo;
                }),
                run(() -> {
                    setAnimation(idle);
                }),
                run(() -> {
                    hud.resetSelezione(); //resetto per il prossimo turno
                    hud.rimuovi();
                    Battaglia.finisciTurno();
                })
        ));

    }

    protected void usaMossa(Mossa m){

        clearActions();

        addAction(sequence(
                run(() -> {
                    setAnimation(Attacco);
                    System.out.println("Animazione Attack");
                }),
                delay(Attacco.getAnimationDuration()),
                run(() -> {
                    m.utilizza();
                    System.out.println("Mossa utilizzata");
                    this.PM-=m.costo;
                }),
                run(() -> {
                    setAnimation(idle);
                }),
                run(() -> {
                    Battaglia.finisciTurno();
                })
        ));

    }

}
