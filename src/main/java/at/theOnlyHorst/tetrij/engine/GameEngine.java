package at.theOnlyHorst.tetrij.engine;

import at.theOnlyHorst.tetrij.TetriJ;
import at.theOnlyHorst.tetrij.gameTasks.AbstractLogicTask;
import at.theOnlyHorst.tetrij.gameTasks.AbstractRenderTask;
import at.theOnlyHorst.tetrij.gameTasks.LogicTask;
import at.theOnlyHorst.tetrij.gameTasks.RenderTask;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;

public class GameEngine {

    private  List<AbstractLogicTask> logicTasks;

    private  List<AbstractRenderTask> renderTasks;



    private static GameEngine instance;

    private GameEngine()
    {
        logicTasks = new ArrayList<>();
        renderTasks = new ArrayList<>();
    }

    public static GameEngine initEngine()
    {
        if(instance!=null)
            throw new RuntimeException("Attempted to initialize GameEngine for a second time");
        instance = new GameEngine();
        return instance;
    }

    public static void addLogicTask(AbstractLogicTask task)
    {
        instance.logicTasks.add(task);
    }

    public static void addRenderTask(AbstractRenderTask task)
    {
        instance.renderTasks.add(task);
    }

    public void update()
    {
        glfwPollEvents();
        logicTasks.forEach(AbstractLogicTask::run);
    }


    public void render(double lagDelta,long deltaTime)
    {
        renderTasks.forEach(t-> t.run(lagDelta,deltaTime));
        glfwSwapBuffers(TetriJ.window);

    }





}
