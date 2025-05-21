package gioco.battaglia.mosse;

import com.badlogic.gdx.scenes.scene2d.Stage;
import gioco.battaglia.BasePGBattaglia;
import gioco.battaglia.NemicoBattaglia;
import gioco.oggetti.*;

import java.util.ArrayList;

public class Mossa {

    public String nome;
    public String descrizione;
    public int costo;
    public int bersagli;
    public Effetto effetto;
    public String tag;  // Cura, Revive, Attacco, Malus, Bonus
    public boolean nemici;
    public boolean aTerra;
    public boolean morto;
    public boolean aTerraPeffo;
    public boolean mortoPeffo;



    /*
    Tag mosse:
    Attacco
    Guarigione
    Rinascita
     */


    public Mossa(String nome,String descrizione, char tag, int costo, int bersagli,boolean nemici, boolean aTerra, boolean morto, boolean aTerraPeffo, boolean mortoPeffo, Effetto effetto) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.costo = costo;
        this.bersagli = bersagli;
        this.nemici = nemici;
        this.aTerra = aTerra;
        this.morto = morto;
        this.aTerraPeffo = aTerraPeffo;
        this.mortoPeffo = mortoPeffo;
        this.tag= String.valueOf(tag);
        this.effetto=effetto;
    }

    public void utilizza() {
        effetto.esegui();
    }


}
