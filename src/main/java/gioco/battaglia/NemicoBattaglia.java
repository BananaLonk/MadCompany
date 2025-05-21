package gioco.battaglia;

import com.badlogic.gdx.scenes.scene2d.Stage;
import gioco.battaglia.mosse.Mossa;

import java.util.ArrayList;
import java.util.Random;

public class NemicoBattaglia extends BasePGBattaglia {


    public NemicoBattaglia(float x, float y, Stage s,int PV, int PM, int livello, int difesa, int attacco, int agilita, int critico) {
        super(x, y, s);



        this.PVMax=PV;
        this.PMMax=PM;
        this.PV=PV;
        this.PM=PM;
        this.livello=livello;

        this.difesa=difesa;
        this.attacco=attacco;
        this.agilita=agilita;
        this.critico=critico;

    }




    Random rand = new Random();
    //Scelta del nemico su che cosa fare
    public void AI(){

        ArrayList<NemicoBattaglia> alleatiMorti= new ArrayList<>(); //Arraylist di alleati morti
        ArrayList<NemicoBattaglia> alleatiFeriti= new ArrayList<>(); //Arraylist di alleati feriti

        for (NemicoBattaglia o : Battaglia.nemici) { //riempio gli arraylist
            if (o.morto)   alleatiMorti.add(o);
            else if (o.PV < o.PVMax*0.5) alleatiFeriti.add(o);
        }


        //Creo due arraylist con le mosse di cura e di revivamento e li riempio
        ArrayList<Mossa> mosseRevive = new ArrayList<>();
        ArrayList<Mossa> mosseCura   = new ArrayList<>();
        ArrayList<Mossa> mosseAttacco = new ArrayList<>();

        for (Mossa o : this.mosse) {
            if (o.tag.equals("R") && o.costo < this.PM && !alleatiMorti.isEmpty())
                mosseRevive.add(o);
            else if (o.tag.equals("G") && o.costo < this.PM && !alleatiFeriti.isEmpty())
                mosseCura.add(o);
            else if (o.tag.equals("A") && o.costo < this.PM)
                mosseAttacco.add(o);
        }

        //Se ci sono alleati da resuscitare e ho delle mosse, sceglie mossa + bersaglio
        if (!alleatiMorti.isEmpty() && !mosseRevive.isEmpty()) {

            Mossa m =mosseRevive.get(rand.nextInt(mosseRevive.size()));

            Battaglia.bersagliEffettivi.add(alleatiMorti.get(rand.nextInt(alleatiMorti.size())));
            this.usaMossa(m);


        }

        //Se no se ci sono alleati feriti provo a curarli
        else if (!alleatiFeriti.isEmpty() && !mosseCura.isEmpty()) {

            Mossa m = mosseCura.get(rand.nextInt(mosseCura.size()));

            while(Battaglia.bersagliEffettivi.size()<m.bersagli) {
                int n=rand.nextInt(alleatiFeriti.size());
                Battaglia.bersagliEffettivi.add(alleatiFeriti.get(n));
            }
            this.usaMossa(m);

        }

        else { //Se no attacco

            ArrayList<PlayerBattaglia> nemiciATerra= new ArrayList<>(); //Arraylist di nemici a terra
            ArrayList<PlayerBattaglia> nemici= new ArrayList<>(); //Arraylist di nemici
            for (PlayerBattaglia o : Battaglia.squadra) { //riempio l'arrayist
                if (o.aTerra)   nemiciATerra.add(o);
                else if(!o.morto)   nemici.add(o);
            }

            Mossa m = mosseAttacco.get(rand.nextInt(mosseAttacco.size()));

            if (!nemiciATerra.isEmpty()) { //Se ci sono nemici a terra, attacco loro

                while(Battaglia.bersagliEffettivi.size()<m.bersagli) {
                    Battaglia.bersagliEffettivi.add(nemiciATerra.get(rand.nextInt(nemiciATerra.size())));
                }
                this.usaMossa(m);


            } else{ //se non ci sono, attacco chiunque

                while(Battaglia.bersagliEffettivi.size()<m.bersagli) {
                    Battaglia.bersagliEffettivi.add(nemici.get(rand.nextInt(nemici.size())));
                }
                this.usaMossa(m);

            }

        }



    }

}
