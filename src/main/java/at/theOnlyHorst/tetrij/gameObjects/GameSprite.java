package at.theOnlyHorst.tetrij.gameObjects;

import at.theOnlyHorst.tetrij.TetriJ;
import at.theOnlyHorst.tetrij.renderer.IDrawable;
import at.theOnlyHorst.tetrij.renderer.Texture;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.EXTTextureCompressionS3TC.GL_COMPRESSED_RGBA_S3TC_DXT1_EXT;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_MAX_LEVEL;
import static org.lwjgl.opengl.GL13.glCompressedTexImage2D;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;

public class GameSprite extends GameObject2D implements IDrawable {


    private float scaleX;
    private float scaleY;

    int bao;

    int Tbao;

    private boolean initialized;



    int texId;

    float parentPrevPosX;
    float parentPrevPosY;

    private int texSamplerId;

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


    public GameSprite(float x, float y,GameObject2D parent,float scaleX,float scaleY, Texture tex)
    {
        super(x,y,parent);
        this.tex = tex;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        if(parent!=null) {
            this.parentPrevPosX = parent.getAbsX();
            this.parentPrevPosY = parent.getAbsY();
        }
        calcVerts();
    }
    public GameSprite(float x, float y, float scaleX, float scaleY, Texture tex)
    {
        this(x,y,null,scaleX,scaleY,tex);
    }



    private void calcVerts()
    {
        float x = super.getAbsX();
        float y = super.getAbsY();

        float upperX = ((float)tex.getWidth()/(float) TetriJ.WINDOW_WIDTH*scaleX )/2;
        float upperY = ((float) tex.getHeight()/ (float) TetriJ.WINDOW_HEIGHT*scaleY)/2;

        float[] verts = {
                x-upperX,y+upperY,0,
                x-upperX,y-upperY,0,
                x+upperX,y-upperY,0,
                x-upperX,y+upperY,0,
                x+upperX,y+upperY,0,
                x+upperX,y-upperY,0
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

        ByteBuffer texBuffer = BufferUtils.createByteBuffer(tex.getTexData().length);
        texBuffer.put(tex.getTexData());
        texBuffer.flip();


        for (int i = 0; i < tex.getMipMapCount(); ++i)
        {
            int size = (Math.max(4, width) / 4) * (Math.max(4, height) / 4) * blockSize;
            ByteBuffer mipMapData = BufferUtils.createByteBuffer(size);
            byte[] mipMapByte = new byte[size];
            texBuffer.get(mipMapByte);
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

        texSamplerId = glGetUniformLocation(TetriJ.getTetriJ().getShaderProg(),"myTextureSampler");

        bao = glGenBuffers();
        Tbao = glGenBuffers();


        loadBufferData();
        glBindBuffer(GL_ARRAY_BUFFER,Tbao);
        glBufferData(GL_ARRAY_BUFFER,texCoords,GL_STATIC_DRAW);
        initialized = true;
    }

    @Override
    public void draw()
    {


        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D,texId);
        glUniform1i(texSamplerId,0);

        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER,bao);
        glVertexAttribPointer(0,3,GL_FLOAT,false,0,0);
        //glDisableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glBindBuffer(GL_ARRAY_BUFFER,Tbao);
        glVertexAttribPointer(1,2,GL_FLOAT,false,0,0);
        //glDisableVertexAttribArray(1);
        glDrawArrays(GL_TRIANGLES,0,3*2);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

    }
    private void loadBufferData()
    {
        glBindBuffer(GL_ARRAY_BUFFER,bao);
        glBufferData(GL_ARRAY_BUFFER,vertices,GL_STATIC_DRAW);
    }


    public float[] getVertices() {
        return vertices;
    }

    public float[] getTexCoords() {
        return texCoords;
    }

    public void checkParentsChanged()
    {
        if(getParent()!=null &&(parentPrevPosY!=getParent().getAbsY()||getParent().getAbsX()!=parentPrevPosX))
        {
            onRecalculate();
        }
    }


    @Override
    public void onRecalculate()
    {
        calcVerts();
        loadBufferData();
    }

    public boolean isInitialized() {
        return initialized;
    }
}
