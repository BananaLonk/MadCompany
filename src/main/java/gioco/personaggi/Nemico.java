package gioco.personaggi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import gioco.oggetti.BaseActor;

import java.util.Arrays;
import java.util.Scanner;

public class Nemico extends BaseActor {

    private Animation<TextureRegion> sx;
    private Animation<TextureRegion> dx;
    private Animation<TextureRegion> su;
    private Animation<TextureRegion> giu;

    private Animation<TextureRegion> iSx;
    private Animation<TextureRegion> iDx;
    private Animation<TextureRegion> iSu;
    private Animation<TextureRegion> iGiu;

    Scanner in;

    private String[] percorso;
    private int i;
    private int contatore;
    private boolean percorsoFinito;
    private int passi;
    private String direzione;
    private boolean sconfitto;


    short ultimaDirezione;

    public Nemico(float x, float y, Stage s, String percorso) {
        super(x, y, s);

        inizializzaAnimazioni();
        this.percorso=percorso.split(" ");
        i=0;
        contatore=0;
        percorsoFinito=false;

        setAcceleration(5000);
        setMaxSpeed(150);
        setDeceleration(5000);
    }

    public boolean isSconfitto() {
        return sconfitto;
    }

    public void setSconfitto(boolean sconfitto) {
        this.sconfitto = sconfitto;
    }

    /*
        Si controllano i tasti freccia vengono premuti e in caso di pressione il sub accelererà nella direzione corrispondente.
        Per aggiornare effettivamente la posizione del sub, deve essere chiamato il metodo applyPhysics.
        L'animazione si metterà in pausa se il sub è fermo e viene applicata una rotazione che segue il verso di avanzamento del sub
         */
    public void act(float delta) {
        super.act(delta);

        if(sconfitto){
            this.remove();
        }

        if(percorso.length>1){
            if (percorsoFinito) {
                if (i == percorso.length - 2) {
                    i = 0;
                    percorsoFinito = false;
                } else {
                    i += 2;
                    percorsoFinito = false;
                }
            }


            direzione = percorso[i];
            passi = Integer.parseInt(percorso[i + 1]);


            if (contatore >= passi) {
                percorsoFinito = true;
                contatore = 0;
            } else {

                if (direzione.contains("U")) {
                    accelerateAtAngle(90);
                    setAnimation(su);
                }
                if (direzione.contains("D")) {
                    accelerateAtAngle(270);
                    setAnimation(giu);
                }
                if (direzione.contains("R")) {
                    accelerateAtAngle(0);
                    setAnimation(dx);
                }
                if (direzione.contains("L")) {
                    accelerateAtAngle(180);
                    setAnimation(sx);
                }
                contatore += 1;
            }
        }

        applyPhysics(delta);
    }

    /**
     * legge da file e carica tutte le animazioni del personaggio
     */
    private void inizializzaAnimazioni(){
        Texture walksheet = new Texture(Gdx.files.internal("images/Unarmed_walk/Unarmed_Walk_Nemico.png"),true);
        TextureRegion[][] tmp = TextureRegion.split(walksheet, 14, 24);
        //movimento
        giu=new Animation<>(0.08f,tmp[0]);
        giu.setPlayMode(Animation.PlayMode.LOOP);
        sx=new Animation<>(0.08f,tmp[1]);
        sx.setPlayMode(Animation.PlayMode.LOOP);
        dx=new Animation<>(0.08f,tmp[2]);
        dx.setPlayMode(Animation.PlayMode.LOOP);
        su=new Animation<>(0.08f,tmp[3]);
        su.setPlayMode(Animation.PlayMode.LOOP);

        walksheet = new Texture(Gdx.files.internal("images/Unarmed_Idle/Unarmed_Idle_full.png"),true);
        tmp = TextureRegion.split(walksheet, 13, 22);
        //idle
        iGiu=new Animation<>(0.1f,tmp[0]);
        iGiu.setPlayMode(Animation.PlayMode.LOOP);
        iSx=new Animation<>(0.1f,tmp[1]);
        iSx.setPlayMode(Animation.PlayMode.LOOP);
        iDx=new Animation<>(0.1f,tmp[2]);
        iDx.setPlayMode(Animation.PlayMode.LOOP);
        iSu=new Animation<>(0.25f, Arrays.copyOfRange(tmp[3],0,3));
        iSu.setPlayMode(Animation.PlayMode.LOOP);

        setAnimation(iGiu);
    }


}
