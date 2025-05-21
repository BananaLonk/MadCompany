package gioco.oggetti;

import com.badlogic.gdx.graphics.Texture;

public abstract class BaseItem{

    public String nome;
    public String descrizione;
    public int valore;
    public Texture texture;
    public boolean usable;

    public BaseItem(String nome, String descrizione, int valore, Texture texture) {
        this.nome=nome;
        this.descrizione=descrizione;
        this.valore=valore;
        this.texture=texture;
        usable=true;
    }

    public BaseItem(String nome, String descrizione, int valore, Texture texture, boolean usable) {
        this.nome=nome;
        this.descrizione=descrizione;
        this.valore=valore;
        this.texture=texture;
        this.usable=usable;
    }


    public boolean equals(BaseItem item){
        if(item.nome.equals(this.nome) && item.descrizione.equals(this.descrizione) && item.valore==this.valore)return true;
        else return false;
    }
}
