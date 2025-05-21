package gioco.personaggi;

public interface Membro {
    int frameDistanza=7;

    void act(float delta);
    void segui(float x, float y, char direzione, boolean fermo);
}
