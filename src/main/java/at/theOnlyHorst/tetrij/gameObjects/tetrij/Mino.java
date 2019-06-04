package at.theOnlyHorst.tetrij.gameObjects.tetrij;

import at.theOnlyHorst.tetrij.TetriJ;
import at.theOnlyHorst.tetrij.engine.ResourceManager;
import at.theOnlyHorst.tetrij.gameObjects.GameObject2D;
import at.theOnlyHorst.tetrij.gameObjects.GameSprite;
import at.theOnlyHorst.tetrij.renderer.Texture;

public class Mino extends GameSprite {

    private static final float minoScale = 1.09f;

    public Mino(int gridPos, GameObject2D tetriMino, MinoColor color) {
        super(TetriJ.getTetriJ().getGrid().gridPosToLocX(gridPos), TetriJ.getTetriJ().getGrid().gridPosToLocY(gridPos), tetriMino, minoScale,minoScale, getMinoTexByColor(color));
    }

    public Mino(int gridPos, MinoColor color)
    {
        this(gridPos, TetriJ.getTetriJ().getGrid(),color);
    }


    public static Texture minoPinkTex, minoRedTex, minoBlueTex, minoCyanTex, minoGreenTex, minoOrangeTex, minoYellowTex, minoGreyTex;


    public static void loadMinoTextures()
    {
        minoPinkTex = ResourceManager.getTexture("minoPinkBasic");
        minoRedTex = ResourceManager.getTexture("minoRedBasic");
        minoBlueTex = ResourceManager.getTexture("minoBlueBasic");
        minoCyanTex = ResourceManager.getTexture("minoCyanBasic");
        minoGreenTex = ResourceManager.getTexture("minoGreenBasic");
        minoOrangeTex = ResourceManager.getTexture("minoOrangeBasic");
        minoYellowTex = ResourceManager.getTexture("minoYellowBasic");
    }
    public enum MinoColor
    {
        PINK,
        RED,
        GREEN,
        BLUE,
        ORANGE,
        CYAN,
        YELLOW,
        GREY

    }

    public static Texture getMinoTexByColor(MinoColor color)
    {
        Texture tex;
        switch (color)
        {
            case PINK:
                tex = minoPinkTex;
                break;
            case RED:
                tex = minoRedTex;
                break;
            case BLUE:
                tex = minoBlueTex;
                break;
            case CYAN:
                tex = minoCyanTex;
                break;
            case GREEN:
                tex = minoGreenTex;
                break;
            case ORANGE:
                tex = minoOrangeTex;
                break;
            case YELLOW:
                tex = minoYellowTex;
                break;

            default:
                tex = minoGreyTex;
        }
        return tex;
    }



}


