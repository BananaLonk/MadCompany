package gioco.battaglia;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import gioco.oggetti.BaseActor;
import gioco.oggetti.BaseScreen;
import gioco.personaggi.Squadra;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import static gioco.mappe.Launcher.*;

public class Battaglia extends BaseScreen {

    protected static BaseActor fondale;
    protected static ArrayList<PlayerBattaglia> squadra;
    protected static ArrayList<NemicoBattaglia> nemici;
    private float fadeAlpha=1f;
    private boolean fade=false;
    private BaseActor culo;
    protected static int turno;
    protected static boolean inizioTurno;
    private String path;

    public static ArrayList<BasePGBattaglia> bersagliEffettivi; //arraylist contenente gli effettifi bersagli

    static Music musicabackground = Gdx.audio.newMusic(Gdx.files.internal("sounds/Miitopia OST - Battle (World 3).mp3"));

    public Battaglia(String path) {
        super();
        this.path = path;
    }

    @Override
    public void initialize() {

        setUltimaMappaAggiuntiva(this);

        fondale= new BaseActor(0,0,mainStage);
        fondale.loadTexture("images/sfondo.png");
        fondale.setSize(mainStage.getViewport().getWorldWidth(),mainStage.getViewport().getWorldHeight());
        fondale.setZIndex(0);

        //riempimento della squadra
        squadra=new ArrayList<>();
        this.caricaSquadra();

        //riempimento dei nemici
        if (path == null) {
            nemici=new ArrayList<>();
            nemici.add(new NemicoBattaglia(0, 0, mainStage, 50, 100, 10, 10, 20, 10, 10));
            nemici.add(new NemicoBattaglia(0, 0, mainStage, 30, 100, 10, 10, 20, 10, 10));
            nemici.add(new NemicoBattaglia(0, 0, mainStage, 45, 100, 10, 10, 20, 10, 10));
            nemici.add(new NemicoBattaglia(0, 0, mainStage, 70, 100, 10, 10, 20, 10, 10));
        }else {
            caricaNemici(path);
        }

        //musica
        musicabackground.play();
        musicabackground.setLooping(true);
        musicabackground.setVolume(0.3f);


        //posizionamento players
        float margineSinistro = fondale.getWidth() * 0.20f; // Margine sinistro (15% della larghezza)
        float spaziamento = fondale.getHeight() / (5.5f); // Distanza tra i giocatori


        for (int i = 0; i < squadra.size(); i++) {
            float x = margineSinistro;
            float y = (i + 1) * spaziamento;
            squadra.get(i).setPosition(x, y + 125);
            squadra.get(i).Selezionato.setPosition(x, y+(90-squadra.get(i).getHeight()));
            squadra.get(i).Attivo.setPosition(x, y+(90-squadra.get(i).getHeight()));
            mainStage.addActor(squadra.get(i).barreStato);
            squadra.get(i).barreStato.setPosition(x-200, y+115);
        }

        //posizionamento nemici
        margineSinistro =fondale.getWidth() * 0.80f;
        for (int i = 0; i < nemici.size(); i++) {
            float x = margineSinistro;
            float y = (i + 1) * spaziamento;
            nemici.get(i).setPosition(x, y+125);
            nemici.get(i).Selezionato.setPosition(x, y+(90-nemici.get(i).getHeight()));
            nemici.get(i).Attivo.setPosition(x, y+(90-nemici.get(i).getHeight()));
            mainStage.addActor(nemici.get(i).barreStato);
            nemici.get(i).barreStato.setPosition(x-200, y+115);
        }

        //cambiare poi in un sistema che lo rileva
        turno=1;

        //fade in
        culo=new BaseActor(0,0,mainStage);
        culo.setScale(10);
        culo.setOpacity(100f);
        culo.loadTexture("images/fadein.png");

        inizioTurno=true;
    }

    @Override
    public void update(float dt) {

        if(inizioTurno){
            this.iniziaTurno();
        }

    }

    @Override
    public void render(float dt)
    {
        mainStage.act(dt);

        //fade in nero
        if(!fade){
            fadeAlpha-=0.6f*dt;
            culo.setOpacity(fadeAlpha);
            if(fadeAlpha<=0){
                fade=true;
                culo.remove();
            }
        }



        update(dt);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mainStage.draw();


    }


    public void iniziaTurno(){
        switch(turno){
            case 1:
            case 2:
            case 3:
            case 4:
                if(!squadra.isEmpty()) { //controllo aggiuntivo per evitare errori
                    if (squadra.get(turno - 1).morto) {
                        finisciTurno();
                        break;
                    } //se il player è morto, salto il turno

                    if(squadra.get(turno - 1).aTerra) squadra.get(turno - 1).aTerra=false; //se è a terra, si rialza

                    new Hud(0, 0, mainStage, squadra.get(turno - 1));
                    System.out.println("Turno " + turno);
                    inizioTurno = false;
                }

                break;
            case 5:
            case 6:
            case 7:
            case 8:
                if(!nemici.isEmpty()) {
                    if (nemici.get(turno - 5).morto) {
                        finisciTurno();
                        break;
                    }
                    if(squadra.get(turno - 5).aTerra) nemici.get(turno - 1).aTerra=false; //se è a terra, si rialza
                    nemici.get(turno - 5).AI();
                    inizioTurno = false;
                    break;
                }
        }
    }


    public static void finisciTurno(){
        Battaglia.turno++; //cambiare poi con dei controlli, sempre per l'ordine
        if(Battaglia.turno==9) Battaglia.turno=1;
        inizioTurno = true;
        bersagliEffettivi.clear();

        if(squadraAllDead()){
            System.out.println("Perso che palle");
            musicabackground.dispose();
            tornaUltimaMappa();
        } else if(nemiciAllDead()){
            System.out.println("Vinto wow");
            musicabackground.dispose();
            tornaUltimaMappa();
            salvaSquadra();
        }
    }

    //metodo che ti dice se la squadra è stata annientata
    private static boolean squadraAllDead(){
        for(PlayerBattaglia p : squadra){
            if(!p.morto) return false;
        }
        return true;
    }

    //metodo che ti dice se i nemici sono tutti morti (evvai!)
    private static boolean nemiciAllDead(){
        for(NemicoBattaglia n : nemici){
            if(!n.morto) return false;
        }
        return true;
    }

    //metodo che carica da file csv la squadra
    public void caricaSquadra() {
        try(BufferedReader reader = new BufferedReader(new FileReader("csvBattaglia/squadra.csv"))){
            String line;
            int i=-1;
            while((line = reader.readLine()) != null) {
                String[] statistiche = line.split(",");
                System.out.println("Si"+statistiche.length);

                int pv = Integer.parseInt(statistiche[0]);
                int pm = Integer.parseInt(statistiche[1]);
                int livello = Integer.parseInt(statistiche[2]);
                int difesa = Integer.parseInt(statistiche[3]);
                int attacco = Integer.parseInt(statistiche[4]);
                int agilita = Integer.parseInt(statistiche[5]);
                int critico = Integer.parseInt(statistiche[6]);

                if(i==-1) squadra.add(new PlayerBattaglia(0, 0, mainStage, Squadra.joker, pv, pm, livello, difesa, attacco, agilita, critico));
                else squadra.add(new PlayerBattaglia(0, 0, mainStage, Squadra.compagni.get(i), pv, pm, livello, difesa, attacco, agilita, critico));
                i++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //metodo che carica un gruppo di nemici casuale
    public void caricaNemici(){
        Random rand = new Random();
        try(PrintWriter writer = new PrintWriter(new FileWriter("csvBattaglia/nemici"+rand.nextInt(10)+".csv"))){

            for (NemicoBattaglia o : nemici) {
                writer.printf("%d,%d,%d,%d,%d,%d,%d\n", o.PV, o.PM, o.livello, o.difesa, o.attacco, o.agilita, o.critico);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void caricaNemici(String path){
        nemici=new ArrayList<>();
        try(PrintWriter writer = new PrintWriter(new FileWriter(path))){

            for (NemicoBattaglia o : nemici) {
                writer.printf("%d,%d,%d,%d,%d,%d,%d\n", o.PV, o.PM, o.livello, o.difesa, o.attacco, o.agilita, o.critico);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    //metodo che salva su file la squadra
    public static void salvaSquadra() {
        try(PrintWriter writer = new PrintWriter(new FileWriter("csvBattaglia/squadra.csv"))){

            for (PlayerBattaglia o : squadra) {
                writer.printf("%d,%d,%d,%d,%d,%d,%d\n", o.PV, o.PM, o.livello, o.difesa, o.attacco, o.agilita, o.critico);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}
