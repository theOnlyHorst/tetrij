package at.theOnlyHorst.tetrij.gameTasks;

public interface RenderTask {

    void run(double lagDelta,long deltaTime);
    void start();
    void render(double lagDelta,long deltaTime);
}
