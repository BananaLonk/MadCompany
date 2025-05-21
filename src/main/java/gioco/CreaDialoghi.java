package gioco;

import java.io.*;

/**
 * classe grezza, non accessibile dal programma principale, per scrivere i file csv dei dialoghi
 */
public class CreaDialoghi {
    public static void main(String[] args) {
        FileWriter fw = null;
        try {
            fw = new FileWriter("assets/dialoghi/Ugo.csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);

        pw.println("2"+";"+"Attento Max, per§proseguire dovrai sconfiggere§tutti i nemici nell'area"+";"+"images/game-icon.png");
        pw.println("2"+";"+"Solo allora la porta§sopra di me si aprirà"+";"+"images/game-icon.png");



        pw.close();
        try {
            bw.close();
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
