package at.theOnlyHorst.tetrij.renderer;

import at.theOnlyHorst.tetrij.TetriJ;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.EXTTextureCompressionS3TC.GL_COMPRESSED_RGBA_S3TC_DXT1_EXT;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_MAX_LEVEL;
import static org.lwjgl.opengl.GL13.glCompressedTexImage2D;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;

public class GameSprite implements IDrawable {

    private int x;
    private int y;

    int texId;

    private float[] vertices;
    private float[] texCoords = {
            0,1,
            0,0,
            1,0,
            0,1,
            1,1,
            1,0
    };


    private Texture tex;


    public GameSprite(int x, int y, Texture tex)
    {
        this.x =x;
        this.y = y;
        this.tex = tex;
        calcVerts();
    }

    private void calcVerts()
    {
        float upperX = TetriJ.WINDOW_WIDTH / tex.getWidth() + x;
        float upperY = TetriJ.WINDOW_HEIGHT / tex.getHeight() + y;

        float[] verts = {
                -upperX,upperY,0,
                -upperX,-upperY,0,
                upperX,-upperY,0,
                -upperX,upperY,0,
                upperX,upperY,0,
                upperX,-upperY,0
        };
        vertices = verts;
    }

    @Override
    public void init() {
        int blockSize = tex.getGlTXTF() == GL_COMPRESSED_RGBA_S3TC_DXT1_EXT?8:16;

        texId = glGenTextures();

        int width = tex.getWidth();

        int height = tex.getHeight();

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



    }

    @Override
    public void draw()
    {
        glBindBuffer(GL_ARRAY_BUFFER,Renderer.getInstance().getBao());
        glBufferData(GL_ARRAY_BUFFER,vertices,GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER,Renderer.getInstance().getTbao());
        glBufferData(GL_ARRAY_BUFFER,texCoords,GL_STATIC_DRAW);
    }


    public float[] getVertices() {
        return vertices;
    }

    public float[] getTexCoords() {
        return texCoords;
    }
}
