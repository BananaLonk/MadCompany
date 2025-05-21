package gioco.battaglia;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import gioco.oggetti.BaseActor;

public class BarreStato extends BaseActor {

    protected Texture contorno;
    protected Texture sfondo;
    protected Texture pv;
    protected Texture pm;
    protected BasePGBattaglia player;

    public BarreStato(BasePGBattaglia player) {
        super(player.getX(),player.getY(),player.getStage());

        this.player = player;

        this.contorno = new Texture("images/Battaglia/contorno barra stato.png");
        this.sfondo = new Texture("images/Battaglia/sfondo barra stato.png");
        this.pv = new Texture("images/Battaglia/pv.png");
        this.pm = new Texture("images/Battaglia/pm.png");



    }





    @Override
    public void draw(Batch batch, float parentAlpha){
        super.draw(batch, parentAlpha);

        //sfondo
        batch.draw(sfondo, getX()+10, getY()+20+10, 25*5, 3*5);
        batch.draw(sfondo, getX()+10, getY()-20+10, 25*5, 3*5);

        //PM
        batch.draw(pm, getX()+10, getY()-20+10, 25 * ((float)player.PM/player.PMMax)*5, 3*5);

        //PV
        batch.draw(pv, getX()+10, getY()+20+10, 25 * ((float)player.PV/player.PVMax)*5, 3*5);

        //contorno sopra tutto
        batch.draw(contorno, getX(), getY()+20, 29*5, 7*5);
        batch.draw(contorno, getX(), getY()-20, 29*5, 7*5);
    }
}
