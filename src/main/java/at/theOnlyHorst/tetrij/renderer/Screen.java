package at.theOnlyHorst.tetrij.renderer;

import at.theOnlyHorst.tetrij.engine.GameEngine;
import at.theOnlyHorst.tetrij.gameObjects.GameSprite;
import at.theOnlyHorst.tetrij.gameTasks.AbstractLogicTask;
import at.theOnlyHorst.tetrij.gameTasks.LogicTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Screen
{
    private int id;

    private List<GameSprite> spriteList;

    private List<AbstractLogicTask> screenTasks;

    public Screen(int id)
    {

        spriteList = new ArrayList<>();
        screenTasks = new ArrayList<>();
    }

    public List<GameSprite> getSpriteList() {
        return spriteList;
    }

    public void addScreenTasks(AbstractLogicTask... tasks){
        screenTasks.addAll(Arrays.asList(tasks));
        GameEngine.addLogicTasks(tasks);
    }

    public void clean()
    {
        GameEngine.removeLogicTasks(screenTasks.toArray(new AbstractLogicTask[screenTasks.size()]));
        Renderer.getInstance().clearScreen();
    }


    public void redraw() {spriteList.forEach(sprite -> {Renderer.getInstance().queueSprite(sprite);});}

}
