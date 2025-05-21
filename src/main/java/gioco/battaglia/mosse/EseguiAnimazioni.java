package gioco.battaglia.mosse;

import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import gioco.battaglia.BasePGBattaglia;

public class EseguiAnimazioni extends Thread{
    MoveByAction azione;
    BasePGBattaglia pg;

    public EseguiAnimazioni(BasePGBattaglia pg) {
        azione=new MoveByAction();
        this.pg=pg;
    }

    public void run(){
        azione.setAmount(30,0);
        azione.setDuration(0.5f);
        pg.addAction(azione);
        while (!azione.isComplete());
        azione=new MoveByAction();
        azione.setAmount(-60,0);
        azione.setDuration(0.5f);
        pg.addAction(azione);
        while (!azione.isComplete());
        azione=new MoveByAction();
        azione.setAmount(30,0);
        azione.setDuration(0.5f);
        pg.addAction(azione);
    }
}
