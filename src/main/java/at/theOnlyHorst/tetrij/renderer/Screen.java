package at.theOnlyHorst.tetrij.renderer;

import java.util.ArrayList;
import java.util.List;

public class Screen
{
    private int id;

    private List<GameSprite> spriteList;

    public Screen(int id)
    {

        spriteList = new ArrayList<>();
    }

    public List<GameSprite> getSpriteList() {
        return spriteList;
    }


    public void draw() {spriteList.forEach(sprite -> {Renderer.getInstance().queueSprite(sprite);sprite.init();});}
}
