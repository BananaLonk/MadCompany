package gioco.oggetti.dialoghi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;

public interface DialogoI {
    boolean hasNext();
    void next();
    String getTesto();
    void setTesto(String testo);
    Texture getSprite();
    void setSprite(Texture sprite);
    Animation getTexture();
    void setTexture(Animation texture);
    float getX();
    float getY();
    void setX(float x);
    void setY(float y);
}
