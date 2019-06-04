package at.theOnlyHorst.tetrij.gameObjects.tetrij;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TetriMinoTemplate
{
    private int relPosMin2;
    private int relPosMin3;
    private int relPosMin4;
    private Mino.MinoColor color;

    protected TetriMinoTemplate(int relPosMin2, int relPosMin3, int relPosMin4, Mino.MinoColor color)
    {

        this.relPosMin2 = relPosMin2;
        this.relPosMin3 = relPosMin3;
        this.relPosMin4 = relPosMin4;
        this.color = color;
    }

    public int getRelPosMin2() {
        return relPosMin2;
    }

    public int getRelPosMin3() {
        return relPosMin3;
    }

    public int getRelPosMin4() {
        return relPosMin4;
    }

    public Mino.MinoColor getColor() {
        return color;
    }

    public List<Integer> getRelPosList()
    {
        return new ArrayList<>(Arrays.asList(0, relPosMin2, relPosMin3, relPosMin4));
    }
}