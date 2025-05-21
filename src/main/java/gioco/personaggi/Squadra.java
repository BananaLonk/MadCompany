package gioco.personaggi;

import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;

public class Squadra {
    public static Compagno leBron;
    public static Compagno cleopatra;
    public static Compagno leonardo;
    public static Joker joker;

    private static boolean inizializzata=false;
    //TODO: LeBron James Hennesy

    public static ArrayList<Compagno> compagni= new ArrayList<>();

    public static void inizializzaSquadra(Stage s, float x, float y){
        joker =new Joker(x,y,s);
        leBron=new Compagno(x,y,s);
        cleopatra=new Compagno(x,y,s);
        leonardo=new Compagno(x,y,s);
        compagni.add(leonardo);
        compagni.add(cleopatra);
        compagni.add(leBron);

        inizializzata=true;
    }

    public static void aggiungiAStage(Stage s, float x, float y){
        s.addActor(joker);
        joker.setPosition(x, y);
        for(Compagno membro: compagni){
            s.addActor(membro);
        }
    }

    public static boolean isInizializzata() {
        return inizializzata;
    }

    public static void setInizializzata(boolean inizializzata) {
        Squadra.inizializzata = inizializzata;
    }

    public static void segui(float delta){
        compagni.get(0).segui(joker.ultimeX.getFirst(),joker.ultimeY.getFirst(), joker.ultimaDirezione, joker.isFermo());
        for(int i=1;i<compagni.size();i++){
            //i compagni seguono il precedente
            compagni.get(i).segui(compagni.get(i-1).ultimeX.getFirst(),compagni.get(i-1).ultimeY.getFirst(),
                    compagni.get(i-1).direzione, compagni.get(i-1).fermo);
        }
    }

}
