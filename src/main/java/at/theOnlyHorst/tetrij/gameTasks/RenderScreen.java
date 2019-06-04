package at.theOnlyHorst.tetrij.gameTasks;

import at.theOnlyHorst.tetrij.TetriJ;
import at.theOnlyHorst.tetrij.renderer.Renderer;

import static org.lwjgl.opengl.GL30.*;

public class RenderScreen extends AbstractRenderTask {




    @Override
    public void start() {
        TetriJ.getTetriJ().getActiveScreen().redraw();
    }

    @Override
    public void render(double lagDelta, long deltaTime) {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        Renderer.getInstance().render();
    }
}
