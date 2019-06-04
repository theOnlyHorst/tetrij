package at.theOnlyHorst.tetrij.gameTasks;

import at.theOnlyHorst.tetrij.TetriJ;
import at.theOnlyHorst.tetrij.util.Bag;

public class UpdateMinoPos extends AbstractLogicTask {

    int count;

    @Override
    public void start() {

    }

    @Override
    public void update() {
        if(count == 2)
        {
            if(TetriJ.getTetriJ().getGrid().tetriMinoHasHitGround())
            {
                TetriJ.getTetriJ().getGrid().lockTetriMinoOnGrid();
                Bag.advanceBag();
                TetriJ.getTetriJ().getGrid().spawnTetriMino(Bag.getCurrentPiece());
            }else
            TetriJ.getTetriJ().getGrid().moveTetriMinoRel(10);
            count = 0;
        }else
        count++;
    }
}
