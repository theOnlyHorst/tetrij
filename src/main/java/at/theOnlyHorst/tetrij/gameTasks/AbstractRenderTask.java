package at.theOnlyHorst.tetrij.gameTasks;

public abstract class AbstractRenderTask implements RenderTask {

    boolean firstFrame;

    public void run(double lagDelta,long deltaTime)
    {
        if(!firstFrame)
        {
            start();
            firstFrame = true;
        }
        render(lagDelta,deltaTime);
    }




}
