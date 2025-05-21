package gioco.personaggi;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import gioco.oggetti.BaseActor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;

public class Compagno extends BaseActor implements Membro, Serializable {

    Animation<TextureRegion> sx;
    Animation<TextureRegion> dx;
    Animation<TextureRegion> su;
    Animation<TextureRegion> giu;

    Animation<TextureRegion> iSx;
    Animation<TextureRegion> iDx;
    Animation<TextureRegion> iSu;
    Animation<TextureRegion> iGiu;

    MoveToAction action = new MoveToAction();

    protected char direzione;
    protected boolean fermo;
    private int contatore;
    protected LinkedList<Float> ultimeX;
    protected LinkedList<Float> ultimeY;

    public Compagno(float x, float y, Stage s) {
        super(x, y, s);


        inizializzaAnimazioni();


        ultimeX =new LinkedList<>();
        ultimeY =new LinkedList<>();

        //numero di frame di latenza tra i compagni
        for (int i=0;i<frameDistanza;i++){
            ultimeX.add(getX());
            ultimeY.add(getY());
        }
    }

    public void segui(float x, float y, char direzione, boolean fermo) {
        this.direzione=direzione;
        this.fermo=fermo;

        if (!fermo){
            setX(x);
            setY(y);
        }
    }

    public void act(float delta) {
        super.act(delta);


        if(!fermo){
            ultimeX.removeFirst();
            ultimeY.removeFirst();

            ultimeX.addLast(getX());
            ultimeY.addLast(getY());
        }


        if(!fermo){
            switch (direzione) {
                case 'l':
                    setAnimation(sx);
                    break;
                case 'r':
                    setAnimation(dx);
                    break;
                case 'u':
                    setAnimation(su);
                    break;
                case 'd':
                    setAnimation(giu);
                    break;

            }
        }else {
            switch (direzione) {
                case 'l':
                    setAnimation(iSx);
                    break;
                case 'r':
                    setAnimation(iDx);
                    break;
                case 'u':
                    setAnimation(iSu);
                    break;
                case 'd':
                    setAnimation(iGiu);
                    break;
            }
        }




        applyPhysics(delta);
        //if ( getSpeed() > 0 )
        //setRotation( getMotionAngle() );

    }

    private void inizializzaAnimazioni(){

        Texture walksheet = new Texture(Gdx.files.internal("images/Unarmed_Idle/Unarmed_Idle_full.png"),true);
        TextureRegion[][] tmp = TextureRegion.split(walksheet, 13, 22);
        //movimento
        iGiu=new Animation<>(0.2f,tmp[0]);
        iGiu.setPlayMode(Animation.PlayMode.LOOP);
        iSx=new Animation<>(0.2f,tmp[1]);
        iSx.setPlayMode(Animation.PlayMode.LOOP);
        iDx=new Animation<>(0.2f,tmp[2]);
        iDx.setPlayMode(Animation.PlayMode.LOOP);
        iSu=new Animation<>(0.25f, Arrays.copyOfRange(tmp[3],0,3));
        iSu.setPlayMode(Animation.PlayMode.LOOP);

        walksheet = new Texture(Gdx.files.internal("images/Unarmed_walk/Unarmed_Walk_full.png"),true);
        tmp = TextureRegion.split(walksheet, 14, 24);
        //movimento
        giu=new Animation<>(0.08f,tmp[0]);
        giu.setPlayMode(Animation.PlayMode.LOOP);
        sx=new Animation<>(0.08f,tmp[1]);
        sx.setPlayMode(Animation.PlayMode.LOOP);
        dx=new Animation<>(0.08f,tmp[2]);
        dx.setPlayMode(Animation.PlayMode.LOOP);
        su=new Animation<>(0.08f,tmp[3]);
        su.setPlayMode(Animation.PlayMode.LOOP);

        setAnimation(iDx);
    }

}
