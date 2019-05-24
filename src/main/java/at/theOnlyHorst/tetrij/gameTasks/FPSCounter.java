package at.theOnlyHorst.tetrij.gameTasks;

public class FPSCounter extends AbstractRenderTask {

    int frames;
    long timestamp;

    @Override
    public void start() {

    }

    @Override
    public void render(double lagDelta,long deltaTime) {
        frames++;
        if(timestamp>=1000)
        {
            System.out.println(frames);
            frames = 0;
            timestamp = 0;
        }
        else
        {
            timestamp+=deltaTime;
        }
    }
}
