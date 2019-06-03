package at.theOnlyHorst.tetrij.renderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Renderer {

    private static Renderer instance;

    private List<GameSprite> activeSprites;

    private int bao;

    private int tbao;

    private int texSamplerId;

    private Renderer()
    {
        activeSprites = new ArrayList<>();
    }

    public static void createRenderer()
    {
        if(instance == null)
        {
            instance = new Renderer();
        }
    }

    public static Renderer getInstance()
    {
        return instance;
    }

    public void queueSprite(GameSprite sprite)
    {
        activeSprites.add(sprite);
    }

    public void clearScreen()
    {
        activeSprites.clear();
    }

    public int getBao() {
        return bao;
    }

    public void setBao(int bao) {
        this.bao = bao;
    }

    public int getTbao() {
        return tbao;
    }

    public void setTbao(int tbao) {
        this.tbao = tbao;
    }

    public int getSpriteAmount()
    {
        return activeSprites.size();
    }

    public int getTexSamplerId() {
        return texSamplerId;
    }

    public void setTexSamplerId(int texSamplerId) {
        this.texSamplerId = texSamplerId;
    }
    public void render()
    {
        activeSprites.forEach(GameSprite::draw);
    }
}
