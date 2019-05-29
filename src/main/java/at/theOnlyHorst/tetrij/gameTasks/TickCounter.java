package at.theOnlyHorst.tetrij.gameTasks;

public class TickCounter extends AbstractLogicTask {

    private int ticks;
    private long timeStamp;


    @Override
    public void start() {
        timeStamp = System.currentTimeMillis();
    }

    @Override
    public void update() {
        if(System.currentTimeMillis()-timeStamp>=1000)
        {
            System.out.println(ticks);
            ticks = 0;
            timeStamp = System.currentTimeMillis();
        }
        else
        ticks++;


    }
}