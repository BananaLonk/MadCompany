package gioco.oggetti;

import com.badlogic.gdx.scenes.scene2d.Stage;
import gioco.oggetti.dialoghi.Dialogo;
import gioco.oggetti.dialoghi.DialogoI;
import gioco.oggetti.dialoghi.DialogoNPC;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * classe che permette di caricare dialoghi da file csv
 */
public class GestoreDialoghi {
    Scanner in;
    ArrayList<String[][]> dialoghi;
    String[] informazioni;
    String[] testi;
    String[] sprites;
    String path;
    Random rand;
    public GestoreDialoghi() {
        dialoghi = new ArrayList<>();
        informazioni = new String[3];
        rand= new Random();
    }

    /**
     * metodo che carica un insieme di dialoghi da un file csv
     * @param path percorso del file da cui caricare i dialoghi
     * @param x posizione x da assegnare al dialogo
     * @param y posizione x da assegnare al dialogo
     * @param stage lo stage a cui assegnare l'attore
     * @param i posizione del dialogo da selezionare, se -1 viene scelto casualmente
     * @return il dialogo scelto
     * @throws FileNotFoundException
     */
    public DialogoI caricaDialogo(String path, float x, float y, Stage stage, int i) throws FileNotFoundException {

        if(dialoghi.isEmpty() || !this.path.equals(path)) {
            dialoghi.clear();
            this.path = path;
            in = new Scanner(new File(path));
            while (in.hasNextLine()) {
                informazioni=(in.nextLine().split(";"));

                testi=new String[Integer.parseInt(informazioni[0])];
                sprites=new String[Integer.parseInt(informazioni[0])];

                testi[0]=informazioni[1].replace("§","\n");
                sprites[0]=informazioni[2];

                for(int j=1; j<Integer.parseInt(informazioni[0]); j++) {
                    informazioni=(in.nextLine().split(";"));
                    testi[j]=informazioni[1].replace("§","\n");
                    sprites[j]=informazioni[2];
                }

                dialoghi.add(new String[][]{testi,sprites});
            }
            in.close();
        }
        if(i==-1){
            i = rand.nextInt(dialoghi.size());
            return new Dialogo(x,y,dialoghi.get(i)[0],dialoghi.get(i)[1],stage);
        }else{
            return new DialogoNPC(x,y,dialoghi.get(i)[0],dialoghi.get(i)[1],stage);
        }


    }
}
