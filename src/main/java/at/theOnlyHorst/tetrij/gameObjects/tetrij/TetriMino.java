package at.theOnlyHorst.tetrij.gameObjects.tetrij;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TetriMino {

    private Mino mino1;
    private Mino mino2;
    private Mino mino3;
    private Mino mino4;


    private int relPosMin2;
    private int relPosMin3;
    private int relPosMin4;


    private TetriMinoTemplate template;

    public static final TetriMinoTemplate T = new TetriMinoTemplate(-1,-10,1, Mino.MinoColor.PINK);
    public static final TetriMinoTemplate I = new TetriMinoTemplate(-1,1,2, Mino.MinoColor.CYAN);
    public static final TetriMinoTemplate O = new TetriMinoTemplate(1,-10,-9, Mino.MinoColor.YELLOW);
    public static final TetriMinoTemplate L = new TetriMinoTemplate(-1,1,-9, Mino.MinoColor.ORANGE);
    public static final TetriMinoTemplate J = new TetriMinoTemplate(-1,1,-11, Mino.MinoColor.BLUE);
    public static final TetriMinoTemplate S = new TetriMinoTemplate(-1,-10,-9, Mino.MinoColor.GREEN);
    public static final TetriMinoTemplate Z = new TetriMinoTemplate(1,-10,-11, Mino.MinoColor.RED);


    public static TetriMinoTemplate getTetriminoByNum(int num)
    {
        switch (num)
        {
            case 0:
                return T;
            case 1:
                return I;
            case 2:
                return O;
            case 3:
                return L;
            case 4:
                return J;
            case 5:
                return S;
            case 6:
                return Z;
        }
        throw new IllegalArgumentException("tetrimino num not valid");
    }


    private TetriMino(int gridPosMinC, TetriMinoTemplate template) {
        mino1 = new Mino(gridPosMinC,template.getColor());
        mino2 = new Mino(gridPosMinC+template.getRelPosMin2(),template.getColor());
        mino3 = new Mino(gridPosMinC+template.getRelPosMin3(),template.getColor());
        mino4 = new Mino(gridPosMinC+template.getRelPosMin4(),template.getColor());
        relPosMin2 = template.getRelPosMin2();
        relPosMin3 = template.getRelPosMin3();
        relPosMin4 = template.getRelPosMin4();
        this.template = template;
    }





    public static TetriMino createTetrimino(int gridPosCenter,TetriMinoTemplate template)
    {
        return new TetriMino(gridPosCenter,template);
    }


    public List<Mino> getAllMinos()
    {
        return new ArrayList<>(Arrays.asList(mino1, mino2, mino3, mino4));
    }


    public TetriMinoTemplate getTemplate() {
        return template;
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

    public Mino getMino1() {
        return mino1;
    }

    public Mino getMino2() {
        return mino2;
    }

    public Mino getMino3() {
        return mino3;
    }

    public Mino getMino4() {
        return mino4;
    }
}


