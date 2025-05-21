package gioco.oggetti;

import com.badlogic.gdx.graphics.Texture;

public class ItemGenerico extends BaseItem {

    //effetto ???? =(
    public int quantita;

    public ItemGenerico(String nome, String descrizione, int valore, Texture texture, int quantita) {
        super(nome,descrizione,valore,texture);
        this.quantita=quantita;
    }
    
}
