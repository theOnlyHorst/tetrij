package at.theOnlyHorst.tetrij.renderer;

import at.theOnlyHorst.tetrij.gameObjects.GameSprite;

import java.util.ArrayList;
import java.util.List;

public class Renderer {

    private static Renderer instance;

    private List<GameSprite> activeSprites;

    private List<GameSprite> queuedSprites;




    private Renderer()
    {
        activeSprites = new ArrayList<>();
        queuedSprites = new ArrayList<>();
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
        if(activeSprites.contains(sprite)||queuedSprites.contains(sprite))
        {
            return;
        }
        queuedSprites.add(sprite);
    }

    public void clearScreen()
    {
        activeSprites.clear();
        queuedSprites.clear();
    }



    public void render()
    {
        queuedSprites.forEach(gameSprite ->
        {
            gameSprite.init();
            activeSprites.add(gameSprite);
        });
        queuedSprites.clear();
        activeSprites.forEach(GameSprite::draw);
    }

    public static void recalculateBounds()
    {
        instance.activeSprites.forEach(GameSprite::onRecalculate);
    }


}
