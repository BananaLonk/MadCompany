package gioco.battaglia;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import gioco.oggetti.BaseActor;

import java.util.ArrayList;
import java.util.Arrays;

public class Carta extends BaseActor {

    private Animation texture;
    private int valore;
    private String tipo;
    public boolean selezionato;
    private boolean eraSelezionato;
    public static double molt=0;

    public Carta(float x, float y, Stage s,String images, int valore,String tipo) {
        super(x, y, s);
        this.valore=valore;
        texture = loadTexture(images);
        this.tipo=tipo;
    }

    public void act(float delta) {
        super.act(delta);

        if(selezionato && !eraSelezionato){
            eraSelezionato = true;
            setY(getY() + 51);
        }else if(!selezionato && eraSelezionato){
            eraSelezionato = false;
            setY(getY() - 51);

        }


        applyPhysics(delta);
    }

    public static String controlloMano(ArrayList<Carta> carte) {
        ordinaCarte(carte);
        if (isFlush(carte) && isStraight(carte)) return "Scala colore";
        if (isFlush(carte)) return "Colore";
        if (isFourOfAKind(carte)) return "Poker";
        if (isFullHouse(carte)) return "Full";
        if (isStraight(carte)) return "Scala";
        if (isThreeOfAKind(carte)) return "Tris";
        if (isTwoPair(carte)) return "Doppia Coppia";
        if (isOnePair(carte)) return "Coppia";
        return "Carta Alta";
    }

    private static boolean isFlush(ArrayList<Carta> carte) {
        String tipo = carte.get(0).tipo;
        if(carte.size()!=5)
            return false;
        for (Carta carta : carte) {
            if (!carta.tipo.equals(tipo)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isStraight(ArrayList<Carta> carte) {
        int[] valori = new int[carte.size()];
        if(carte.size()!=5)
            return false;
        for (int i = 0; i < carte.size(); i++) {
            valori[i] = carte.get(i).valore;
        }
        Arrays.sort(valori);
        for (int i = 0; i < valori.length - 1; i++) {
            if (valori[i] + 1 != valori[i + 1]) {
                return false;
            }
        }
        return true;
    }

    private static boolean isFourOfAKind(ArrayList<Carta> carte) {
        return hasSameRankCount(carte, 4);
    }

    private static boolean isThreeOfAKind(ArrayList<Carta> carte) {
        return hasSameRankCount(carte, 3);
    }

    private static boolean isFullHouse(ArrayList<Carta> carte) {
        return hasSameRankCount(carte, 3) && hasSameRankCount(carte, 2);
    }

    private static boolean isTwoPair(ArrayList<Carta> carte) {
        int pairs = 0;
        int valore=-1;
        for (int i=0;i<carte.size();i++) {
            if(valore==-1 || valore!=carte.get(i).valore)
                if(countRank(carte,carte.get(i).valore)==2) {
                    valore=carte.get(i).valore;
                    pairs++;
                }

        }
        return (pairs==2);
    }

    private static boolean isOnePair(ArrayList<Carta> carte) {
        return hasSameRankCount(carte, 2);
    }

    public static ArrayList ordinaCarte(ArrayList<Carta> carte) {
        for(int i=carte.size()-1;i>0;i--) {
            for(int j=0;j<i;j++) {
                if(carte.get(j).valore>carte.get(j+1).valore) {
                    Carta temp=carte.get(j+1);
                    carte.set(j+1, carte.get(j));
                    carte.set(j, temp);
                }
            }
        }
        return carte;
    }

    private static boolean hasSameRankCount(ArrayList<Carta> carte, int count) {
        for (int i=0;i<carte.size();i++) {
            if(countRank(carte,carte.get(i).valore)==count)
                return true;
        }
        return false;
    }

    private static int countRank(ArrayList<Carta> carte, int rank) {
        int count = 0;
        for (int i=0;i<carte.size();i++){
            if (carte.get(i).valore==rank) {
                count++;
            }
        }
        return count;
    }

    public Animation getTexture() {
        return texture;
    }

    public void setTexture(Animation texture) {
        this.texture = texture;
    }

    public int getValore() {
        return valore;
    }

    public void setValore(int valore) {
        this.valore = valore;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isSelezionato() {
        return selezionato;
    }

    public void setSelezionato(boolean selezionato) {
        this.selezionato = selezionato;
    }

    public boolean isEraSelezionato() {
        return eraSelezionato;
    }

    public void setEraSelezionato(boolean eraSelezionato) {
        this.eraSelezionato = eraSelezionato;
    }

    public static double getMolt() {
        return molt;
    }

    public static void setMolt(double molt) {
        Carta.molt = molt;
    }
}
