package at.theOnlyHorst.tetrij.util;

import at.theOnlyHorst.tetrij.TetriJ;
import at.theOnlyHorst.tetrij.gameObjects.tetrij.TetriMino;
import at.theOnlyHorst.tetrij.gameObjects.tetrij.TetriMinoTemplate;

public class Bag {

    int[] pieceOrder;

    private static Bag currentBag;
    private  static Bag nextBag;

    private int currentPieceInd;

    private Bag(int[] pieceOrder)
    {
        this.pieceOrder = pieceOrder;
    }

    private boolean nextPiece()
    {
        currentPieceInd++;
        return currentPieceInd<7;
    }

    public static TetriMinoTemplate getCurrentPiece()
    {
        return TetriMino.getTetriminoByNum(currentBag.pieceOrder[currentBag.currentPieceInd]);
    }

    public static TetriMinoTemplate getPreview(int piecesAhead)
    {
        int x = currentBag.currentPieceInd+piecesAhead;

        if(x>=7)
        {
            if(x>=14)
            {
                throw new IllegalArgumentException("preview too far");
            }
            return TetriMino.getTetriminoByNum(nextBag.pieceOrder[x-7]);
        }
        return TetriMino.getTetriminoByNum(currentBag.pieceOrder[x]);
    }

    public static Bag createNewBag(int[] nums)
    {
        return new Bag(nums);
    }

    public static void advanceBag()
    {
        if(!currentBag.nextPiece())
        {
            currentBag = nextBag;
            nextBag = TetriJ.rng.get7Bag();
        }
    }

    public static void initBags()
    {
        currentBag = TetriJ.rng.get7Bag();
        nextBag = TetriJ.rng.get7Bag();
    }

}
