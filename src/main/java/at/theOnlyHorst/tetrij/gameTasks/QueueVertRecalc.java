package at.theOnlyHorst.tetrij.gameTasks;

import at.theOnlyHorst.tetrij.TetriJ;
import at.theOnlyHorst.tetrij.gameObjects.GameSprite;

public class QueueVertRecalc extends AbstractLogicTask {
    @Override
    public void start() {

    }

    @Override
    public void update() {
        TetriJ.getTetriJ().getActiveScreen().getSpriteList().forEach(GameSprite::checkParentsChanged);
    }
}
