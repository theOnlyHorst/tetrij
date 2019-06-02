package at.theOnlyHorst.tetrij.renderer;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;

public class Texture {

    private int height;
    private int width;
    private int linearSize;
    private int mipMapCount;
    private int glTXTF;

    private ByteBuffer texData;


    public Texture(int height, int width, int linearSize, int mipMapCount, int glTXTF, ByteBuffer texData) {
        this.height = height;
        this.width = width;
        this.linearSize = linearSize;
        this.mipMapCount = mipMapCount;
        this.glTXTF = glTXTF;
        this.texData = texData;

    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getLinearSize() {
        return linearSize;
    }

    public int getMipMapCount() {
        return mipMapCount;
    }

    public int getGlTXTF() {
        return glTXTF;
    }

    public ByteBuffer getTexData() {
        return texData;
    }
}
