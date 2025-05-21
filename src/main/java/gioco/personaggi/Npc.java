package gioco.personaggi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import gioco.oggetti.BaseActor;
import gioco.oggetti.GestoreDialoghi;
import gioco.oggetti.dialoghi.DialogoI;
import gioco.oggetti.dialoghi.DialogoNPC;

import java.io.FileNotFoundException;

public class Npc extends BaseActor {
    private DialogoNPC dialogo;
    private String pathDialogo;
    private int progressoDialogo;
    private int maxProgressoDialogo;
    private GestoreDialoghi GD;

    private Animation<TextureRegion> iGiu;

    public Npc(float x, float y, Stage s, String nome, GestoreDialoghi GD, int maxDialoghi) throws FileNotFoundException {
        super(x, y, s);

        this.pathDialogo = "assets/dialoghi/"+nome+".csv";
        this.maxProgressoDialogo=maxDialoghi-1;

        this.GD = GD;
        this.progressoDialogo=-1;
        inizializzaAnimazioni();

    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public boolean parla(float x, float y) {
        if(Math.abs(x-getX())<20 && Math.abs(y-getY())<20){
            if(progressoDialogo<maxProgressoDialogo){
                progressoDialogo++;
            }
            return true;
        }else
            return false;
    }

    public DialogoI getDialogo() throws FileNotFoundException {
        dialogo = creaDialogo(GD, getStage());
        return dialogo;
    }

    public void setDialogo(DialogoI dialogo) {
        this.dialogo = (DialogoNPC) dialogo;
    }

    public String getPathDialogo() {
        return pathDialogo;
    }

    public void setPathDialogo(String pathDialogo) {
        this.pathDialogo = pathDialogo;
    }

    public int getProgressoDialogo() {
        return progressoDialogo;
    }

    public void setProgressoDialogo(int progressoDialogo) {
        this.progressoDialogo = progressoDialogo;
    }

    public int getMaxProgressoDialogo() {
        return maxProgressoDialogo;
    }

    public void setMaxProgressoDialogo(int maxProgressoDialogo) {
        this.maxProgressoDialogo = maxProgressoDialogo;
    }

    private DialogoNPC creaDialogo(GestoreDialoghi GD, Stage s) throws FileNotFoundException {
        return (DialogoNPC) GD.caricaDialogo(pathDialogo,100,100,s,progressoDialogo);
    }

    private void inizializzaAnimazioni() {
        Texture walksheet = new Texture(Gdx.files.internal("images/Unarmed_Idle/Unarmed_Idle_full.png"),true);
        TextureRegion[][] tmp = TextureRegion.split(walksheet, 13, 22);
        //idle
        iGiu=new Animation<>(0.2f,tmp[0]);
        iGiu.setPlayMode(Animation.PlayMode.LOOP);

        setAnimation(iGiu);
    }


}
