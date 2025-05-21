package gioco.personaggi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import gioco.mappe.Launcher;
import gioco.oggetti.BaseActor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;

public class Joker extends BaseActor implements Serializable {

    private Animation<TextureRegion> sx;
    private Animation<TextureRegion> dx;
    private Animation<TextureRegion> su;
    private Animation<TextureRegion> giu;

    private Animation<TextureRegion> iSx;
    private Animation<TextureRegion> iDx;
    private Animation<TextureRegion> iSu;
    private Animation<TextureRegion> iGiu;

    protected char ultimaDirezione;
    protected LinkedList<Float> ultimeX;
    protected LinkedList<Float> ultimeY;

    private static final int frameDistanza=7;

    public Joker(float x, float y, Stage s) {
        super(x, y, s);

        inizializzaAnimazioni();

        setAcceleration(1000000000);
        setMaxSpeed(150);
        setDeceleration(1000000000);
        ultimeX =new LinkedList<>();
        ultimeY =new LinkedList<>();


        //numero di frame di latenza tra i compagni
        for (int i=0;i<frameDistanza;i++){
            ultimeX.add(getX());
            ultimeY.add(getY());
        }
    }


    public void act(float delta) {
        super.act(delta);

        aggiornaCoordinate();

        if (Gdx.input.isKeyPressed(Keys.W) & !(Gdx.input.isKeyPressed(Keys.S) & ultimaDirezione =='d')) {
            accelerateAtAngle(90);
            setAnimation(su);
            ultimaDirezione ='u';
        }
        else if (Gdx.input.isKeyPressed(Keys.S)) {
            accelerateAtAngle(270);
            setAnimation(giu);
            ultimaDirezione ='d';
        }
        if (Gdx.input.isKeyPressed(Keys.A) & !(Gdx.input.isKeyPressed(Keys.D) & ultimaDirezione =='r')){
            accelerateAtAngle(180);
            setAnimation(sx);
            ultimaDirezione ='l';
        }
        else if (Gdx.input.isKeyPressed(Keys.D)) {
            accelerateAtAngle(0);
            setAnimation(dx);
            ultimaDirezione ='r';
        }

        //corsa
        if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)){
            setMaxSpeed(200);
        }else
            setMaxSpeed(150);

        //setAnimationPaused( !isMoving() );
        if(!isMoving()){
            switch (ultimaDirezione){
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
    }

    public boolean isFermo(){
        return(getX()==ultimeX.getLast() && getY()==ultimeY.getLast());
    }

    public LinkedList<Float> getUltimeX() {
        return ultimeX;
    }

    public LinkedList<Float> getUltimeY() {
        return ultimeY;
    }

    public int getFrameDistanza(){
        return frameDistanza;
    }

    private void inizializzaAnimazioni(){
        Texture walksheet = new Texture(Gdx.files.internal("images/Unarmed_walk/Unarmed_Walk_full.png"),true);
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
        iGiu=new Animation<>(0.2f,tmp[0]);
        iGiu.setPlayMode(Animation.PlayMode.LOOP);
        iSx=new Animation<>(0.2f,tmp[1]);
        iSx.setPlayMode(Animation.PlayMode.LOOP);
        iDx=new Animation<>(0.2f,tmp[2]);
        iDx.setPlayMode(Animation.PlayMode.LOOP);
        iSu=new Animation<>(0.25f, Arrays.copyOfRange(tmp[3],0,3));
        iSu.setPlayMode(Animation.PlayMode.LOOP);

        setAnimation(iDx);
    }

    public void aggiornaCoordinate(){
        ultimeX.removeFirst();
        ultimeY.removeFirst();

        ultimeX.addLast(getX());
        ultimeY.addLast(getY());
    }
}
