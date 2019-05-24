package at.theOnlyHorst.tetrij.gameTasks;

public abstract class AbstractLogicTask implements LogicTask {

    private boolean firstTick;

    public void run()
    {
        if(!firstTick) {
            start();
            firstTick = true;
        }
        update();
    }


}
