package at.theOnlyHorst.tetrij.util;

import at.theOnlyHorst.tetrij.engine.ResourceManager;
import at.theOnlyHorst.tetrij.renderer.Texture;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.lwjgl.opengl.EXTTextureCompressionS3TC.*;

public class DDSParser {

    private static final int DDS_VERFICATIONSIGN = 0x20534444;

    protected static final int DDPF_FOURCC = 0x00004;

    public static Texture readDDS(String path)
    {
        try(InputStream is = ResourceManager.class.getClassLoader().getResourceAsStream(path))
        {
            byte[] ddsDefineB = new byte[4];

            is.read(ddsDefineB);
            ByteBuffer ddsDefineBB = newByteBuffer(ddsDefineB);
            int verficationNUM = ddsDefineBB.getInt();

            if(verficationNUM != DDS_VERFICATIONSIGN)
            {
                throw new RuntimeException("Invalid DDS File");
            }

            byte[] ddsHeaderB = new byte[124];
            is.read(ddsHeaderB);

            ByteBuffer ddsHeaderBB = newByteBuffer(ddsHeaderB);

            int dwSize = ddsHeaderBB.getInt();
            int dwFlags = ddsHeaderBB.getInt();
            int dwHeight = ddsHeaderBB.getInt();
            int dwWidth = ddsHeaderBB.getInt();
            int dwPitchOrLinearSize = ddsHeaderBB.getInt();
            int dwDepth = ddsHeaderBB.getInt();
            int dwMipMapCount = ddsHeaderBB.getInt();
            for(int i =0;i<11;i++) ddsHeaderBB.getInt();
            ddsHeaderBB.getInt();
            ddsHeaderBB.getInt();
            int fourCC = ddsHeaderBB.getInt();

            String sfourCC = createFourCCString(fourCC);
            int glDXTF;
            switch (sfourCC) {
                case "DXT1":
                    glDXTF = GL_COMPRESSED_RGBA_S3TC_DXT1_EXT;
                    break;
                case "DXT3":
                    glDXTF = GL_COMPRESSED_RGBA_S3TC_DXT3_EXT;
                    break;
                case "DXT5":
                    glDXTF = GL_COMPRESSED_RGBA_S3TC_DXT5_EXT;
                    break;
                case "DX10":
                    throw new RuntimeException("DX10 File Format not supported");
                default:
                    throw new RuntimeException("fourCC of dds file invalid");

            }

            int bufsize = dwMipMapCount > 1? dwPitchOrLinearSize*2:dwPitchOrLinearSize;

            byte[] ddsDataBytes = new byte[bufsize];

            is.read(ddsDataBytes);
            ByteBuffer ddsDataBB = newByteBuffer(ddsDataBytes);

            System.out.println(is.available());





            return new Texture(dwHeight,dwWidth,dwPitchOrLinearSize,dwMipMapCount,glDXTF, ddsDataBB);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static ByteBuffer newByteBuffer(byte[] data) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(data.length).order(ByteOrder.nativeOrder());
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private static String createFourCCString(int fourCC) {
        byte[] fourCCString = new byte[DDPF_FOURCC];
        for(int i = 0; i < fourCCString.length; i++) fourCCString[i] = (byte) (fourCC >> (i*8));
        return new String(fourCCString);
    }

}
