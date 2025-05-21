package gioco.oggetti;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import gioco.oggetti.BaseScreen;

import java.util.ArrayList;

import static gioco.mappe.Launcher.tornaUltimaMappa;
import static gioco.oggetti.BaseGame.setActiveScreen;

public class Inventario extends BaseScreen {

    public ArrayList<ArrayList<BaseItem>> inventario;
    private Texture sfondo;
    private Texture quadrato;
    private Texture quadratoAttivo;
    BitmapFont descrizione = new BitmapFont();

    protected int sceltaX;
    protected int sceltaY;
    protected int selezione;


    @Override
    public void initialize() {


        this.inventario = new ArrayList<>();
        this.inventario.add(new ArrayList<>());

        this.sfondo=new Texture("images/sfondoInventario.png");
        this.quadrato = new Texture("images/quadratoInv.png");
        this.quadratoAttivo = new Texture("images/quadratoInv selezionato.png");

        //aggiunta a caso poi togliere
        this.aggiuntaItem(new ItemGenerico("Oggetto AntiRottura","Oggetto chiave, impedisce \nal gioco di crushare quando \nl'inventario è vuoto, tenere \nsempre a portata di mano",30,new Texture("images/MainCharacterFermo.png"),1));
        this.aggiuntaItem(new ItemGenerico("Chiave (yeah)","Una chiave, che sicuramente\napre qualcosa ",2000,new Texture("images/oggetti/chiave.png"),1));

        this.sceltaX=0;
        this.sceltaY=0;
        this.selezione=1;
    }

    @Override
    public void update(float dt) {

        if(selezione==1){
            navigazione();
        }else if(selezione==2){
            opzioni();
        }


    }

    @Override
    public void render(float dt) {
        mainStage.act(dt);

        update(dt);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mainStage.getBatch().begin();

        //disegno sfondo e quadrati
        mainStage.getBatch().draw(sfondo,0,0,sfondo.getWidth()*5,sfondo.getHeight()*5);
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                if(sceltaX==i && sceltaY==j){
                    mainStage.getBatch().draw(quadratoAttivo,335+(230*i),25+(170*j),quadrato.getWidth() * 5, quadrato.getHeight() * 5);
                    this.descrizione.draw(mainStage.getBatch(), "NOME: "+this.inventario.get(i).get(j).nome+"\n"+this.inventario.get(i).get(j).descrizione+"\nQUANTITA: "+((ItemGenerico)this.inventario.get(i).get(j)).quantita,30,500);
                }
                else mainStage.getBatch().draw(quadrato,335+(230*i),25+(170*j),quadrato.getWidth() * 5, quadrato.getHeight() * 5);
            }
        }

        //disegno texture
        for(int i=0;i<inventario.size();i++){
            for(int j=0;j<inventario.get(i).size();j++){
                mainStage.getBatch().draw(inventario.get(i).get(j).texture,360+(230*i),50+(170*j),inventario.get(i).get(j).texture.getWidth() * 5, inventario.get(i).get(j).texture.getHeight() * 5);
            }
        }

        mainStage.getBatch().end();


        mainStage.draw();
    }



    /*
    Navigazione principale tra gli item
     */
    public void navigazione(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            this.selezione=2;

        } else if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && sceltaY!=0){
            sceltaY--;
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.UP) && sceltaY<inventario.get(sceltaX).size()-1){ //se la casella successiva è una posizione maggiore rispetto alla grandezza della colonna, non faccio aumentare la scelta
            sceltaY++;
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && inventario.size()!=sceltaX+1 && inventario.get(sceltaX+1).size()>sceltaY){ //se la casella successiva è una posizione maggiore rispetto al numero di colonne,
            sceltaX++;
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && sceltaX!=0){
            sceltaX--;
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            tornaUltimaMappa();
        }

    }

    /*
    Scelta delle opzioni su quell'item
     */
    public void opzioni(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){


        } else if(Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)){
            rimuoviItem(inventario.get(sceltaX).get(sceltaY),1);
            selezione=1;
        }
    }

    /*
    Aggiunta di un item
     */
    public void aggiuntaItem(BaseItem item){
        int i=0;
        boolean aggiunta=true;
        if(item instanceof ItemGenerico){ //solo se un oggetto può essere accumulato
            for (ArrayList<BaseItem> colonna : inventario) { //doppio ciclo for-each che controlla se un item è già presente nell'inventario
                for (BaseItem Oggetto : colonna) {
                    if (Oggetto.equals(item)) { //se scopro che un item è già presente, aumento la sua quantità (Senza un limite perchè è troppo complicato)
                        ((ItemGenerico)Oggetto).quantita+=((ItemGenerico)item).quantita;
                        aggiunta=false;
                    }
                }
            }
        }

        if(aggiunta){
            while(inventario.get(i).size()==4){ //controllo tutte le colonne dalla prima, appena ce nè una libera, uso quella
                i++;
                if(inventario.size()==i ){ //se non ho più colonne, ne aggiungo una
                    inventario.add(new ArrayList<>());
                }
            }

            inventario.get(i).add(item);
        }


    }

    public void rimuoviItem(BaseItem item,int quantita){
        if(quantita==0){ //caso equipaggiamento battaglia

            inventario.get(sceltaX).remove(sceltaY);

            if(inventario.get(inventario.size()-1).isEmpty())inventario.remove(inventario.size()-1); //se l'ultima colonna si svuota, la tolgo

        } else{ //caso item generico

            ((ItemGenerico) item).quantita-=quantita;
            if(((ItemGenerico) item).quantita<=0){
                inventario.get(sceltaX).remove(sceltaY);
                if(inventario.get(inventario.size()-1).isEmpty())inventario.remove(inventario.size()-1); //se l'ultima colonna si svuota, la tolgo
            }
        }

        if(sceltaX==inventario.size())sceltaX--;
        if(sceltaY==inventario.get(sceltaX).size())sceltaY--;
    }



}
