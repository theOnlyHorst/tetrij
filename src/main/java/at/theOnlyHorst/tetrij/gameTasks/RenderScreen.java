package at.theOnlyHorst.tetrij.gameTasks;

import at.theOnlyHorst.tetrij.TetriJ;
import at.theOnlyHorst.tetrij.engine.ResourceManager;
import at.theOnlyHorst.tetrij.renderer.Texture;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTTextureCompressionS3TC;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.EXTTextureCompressionS3TC.GL_COMPRESSED_RGBA_S3TC_DXT1_EXT;
import static org.lwjgl.opengl.GL30.*;

public class RenderScreen extends AbstractRenderTask {


    private Texture tex;

    float[] vertices = {
          -0.25f,0.5f,0,
          -0.25f,-0.5f,0,
          0.25f,-0.5f,0,
          -0.25f,0.5f,0,
          0.25f,0.5f,0,
          0.25f,-0.5f,0
    };
    float[] textureCoords ={
          0,1,
          0,0,
          1,0,
          0,1,
          1,1,
          1,0
    };



    int bao;

    int tbao;

    int texId;

    int texSamplerId;


    @Override
    public void start() {

        tex = ResourceManager.getTexture("bgGrid");

        int blockSize = tex.getGlTXTF() == GL_COMPRESSED_RGBA_S3TC_DXT1_EXT?8:16;

         texId = glGenTextures();

        int width = tex.getWidth();

        int height = tex.getHeight();

        int offset =0;

        glBindTexture(GL_TEXTURE_2D,texId);


        for (int i = 0; i < tex.getMipMapCount(); ++i)
        {
            int size = (Math.max(4, width) / 4) * (Math.max(4, height) / 4) * blockSize;
            ByteBuffer mipMapData = BufferUtils.createByteBuffer(size);
            byte[] mipMapByte = new byte[size];
            tex.getTexData().get(mipMapByte);
            mipMapData.put(mipMapByte);
            mipMapData.rewind();
            glCompressedTexImage2D(GL_TEXTURE_2D, i, tex.getGlTXTF(), width, height, 0, mipMapData);
            width >>= 1;
            height >>= 1;

        }

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAX_LEVEL,tex.getMipMapCount()-1);


        bao = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,bao);
        glBufferData(GL_ARRAY_BUFFER,vertices,GL_STATIC_DRAW);

        tbao = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,tbao);
        glBufferData(GL_ARRAY_BUFFER,textureCoords,GL_STATIC_DRAW);

        texSamplerId = glGetUniformLocation(TetriJ.getTetriJ().getShaderProg(),"myTextureSampler");
    }

    @Override
    public void render(double lagDelta, long deltaTime) {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D,texId);

        glUniform1i(texSamplerId,0);

        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER,bao);
        glVertexAttribPointer(0,3,GL_FLOAT,false,0,0);
        //glDisableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glBindBuffer(GL_ARRAY_BUFFER,tbao);
        glVertexAttribPointer(1,2,GL_FLOAT,false,0,0);
        //glDisableVertexAttribArray(1);
        glDrawArrays(GL_TRIANGLES,0,3*2);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

    }
}
