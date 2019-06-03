package at.theOnlyHorst.tetrij.gameTasks;

import at.theOnlyHorst.tetrij.TetriJ;
import at.theOnlyHorst.tetrij.renderer.Renderer;
import at.theOnlyHorst.tetrij.renderer.Texture;

import static org.lwjgl.opengl.GL30.*;

public class RenderScreen extends AbstractRenderTask {


    private Texture tex;













    @Override
    public void start() {
        Renderer.getInstance().setBao(glGenBuffers());
        glBindBuffer(GL_ARRAY_BUFFER,Renderer.getInstance().getBao());


        Renderer.getInstance().setTbao(glGenBuffers());
        glBindBuffer(GL_ARRAY_BUFFER,Renderer.getInstance().getTbao());


        Renderer.getInstance().setTexSamplerId(glGetUniformLocation(TetriJ.getTetriJ().getShaderProg(),"myTextureSampler"));
        TetriJ.getTetriJ().getActiveScreen().draw();
    }

    @Override
    public void render(double lagDelta, long deltaTime) {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        Renderer.getInstance().render();


    }
}
