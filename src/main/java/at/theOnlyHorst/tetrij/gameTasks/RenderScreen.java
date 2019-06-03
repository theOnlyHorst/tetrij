package at.theOnlyHorst.tetrij.gameTasks;

import at.theOnlyHorst.tetrij.TetriJ;
import at.theOnlyHorst.tetrij.renderer.Renderer;
import at.theOnlyHorst.tetrij.renderer.Texture;

import static org.lwjgl.opengl.GL30.*;

public class RenderScreen extends AbstractRenderTask {


    private Texture tex;






    int texSamplerId;






    @Override
    public void start() {
        Renderer.getInstance().setBao(glGenBuffers());
        glBindBuffer(GL_ARRAY_BUFFER,Renderer.getInstance().getBao());


        Renderer.getInstance().setTbao(glGenBuffers());
        glBindBuffer(GL_ARRAY_BUFFER,Renderer.getInstance().getBao());


        texSamplerId = glGetUniformLocation(TetriJ.getTetriJ().getShaderProg(),"myTextureSampler");
    }

    @Override
    public void render(double lagDelta, long deltaTime) {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glActiveTexture(GL_TEXTURE0);
        //glBindTexture(GL_TEXTURE_2D,texId);
        //TODO implement texture atlases
        glUniform1i(texSamplerId,0);

        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER,Renderer.getInstance().getBao());
        glVertexAttribPointer(0,3,GL_FLOAT,false,0,0);
        //glDisableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glBindBuffer(GL_ARRAY_BUFFER,Renderer.getInstance().getBao());
        glVertexAttribPointer(1,2,GL_FLOAT,false,0,0);
        //glDisableVertexAttribArray(1);
        glDrawArrays(GL_TRIANGLES,0,3*2*Renderer.getInstance().getSpriteAmount());
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);


    }
}
