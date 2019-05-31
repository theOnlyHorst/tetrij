package at.theOnlyHorst.tetrij.gameTasks;

import static org.lwjgl.opengl.GL30.*;

public class RenderScreen extends AbstractRenderTask {


    float[] vertices = {
          0,1,
          0,0,
          1,0,
          0,1,
          1,1,
          1,0
    };
    float[] textureCoords ={

    };



    int bao;


    @Override
    public void start() {
        bao = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,bao);
        glBufferData(GL_ARRAY_BUFFER,vertices,GL_STATIC_DRAW);

    }

    @Override
    public void render(double lagDelta, long deltaTime) {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER,bao);
        glVertexAttribPointer(0,2,GL_FLOAT,false,0,0);
        glDrawArrays(GL_TRIANGLES,0,3*2);
        glDisableVertexAttribArray(0);

    }
}
