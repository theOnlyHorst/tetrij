package at.theOnlyHorst.tetrij.gameTasks;

import at.theOnlyHorst.tetrij.TetriJ;
import org.lwjgl.opengl.ARBVertexBufferObject;

import static org.lwjgl.opengl.GL30.*;

public class RenderScreen extends AbstractRenderTask {


    float[] vertices = {
          0,0,0,
          0,1,0,
          1,1,0,
          1,0,0
    };

    float[] colors = {
        1,0,0,1
    };

    @Override
    public void start() {

    }

    @Override
    public void render(double lagDelta, long deltaTime) {
        if(TetriJ.requireRerender()) {

            glClear(GL_COLOR_BUFFER_BIT);
            int vao = glGenVertexArrays();
            glBindVertexArray(vao);
            int bao = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER,bao);
            glBufferData(GL_ARRAY_BUFFER,vertices,GL_STATIC_DRAW);
            //glVertexPointer(2,GL_FLOAT,0,0);
            glEnableVertexAttribArray(0);
            glBindBuffer(GL_ARRAY_BUFFER,bao);
            glVertexAttribPointer(0,3,GL_FLOAT,false,0,0);
            glDrawArrays(GL_QUADS,0,4);
            glDisableVertexAttribArray(0);


        }
    }
}
