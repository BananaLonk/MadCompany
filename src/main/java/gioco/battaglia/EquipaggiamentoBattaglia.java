package gioco.battaglia;

import com.badlogic.gdx.graphics.Texture;
import gioco.oggetti.BaseItem;

public class EquipaggiamentoBattaglia extends BaseItem {

    //modificatori statistiche
    public int difesa;
    public int attacco;
    public int agilita;
    public int critico;

    //tipo di equipaggiamento (1=arma,2=armatura,3=accessorio)
    public int tipo;

    public EquipaggiamentoBattaglia(String nome, String descrizione, int valore, Texture texture,int difesa, int attacco, int agilita, int critico, int tipo) {
        super(nome,descrizione,valore,texture);
        this.difesa = difesa;
        this.attacco = attacco;
        this.agilita = agilita;
        this.critico = critico;
        this.tipo = tipo;
    }

    public void eguaglia(EquipaggiamentoBattaglia e) {
        this.difesa = e.difesa;
        this.attacco = e.attacco;
        this.agilita = e.agilita;
        this.critico = e.critico;
        this.tipo = e.tipo;
    }

}
