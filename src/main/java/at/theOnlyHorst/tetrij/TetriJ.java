package at.theOnlyHorst.tetrij;

import at.theOnlyHorst.tetrij.engine.GameEngine;
import at.theOnlyHorst.tetrij.engine.ResourceManager;
import at.theOnlyHorst.tetrij.gameTasks.FPSCounter;
import at.theOnlyHorst.tetrij.gameTasks.RenderScreen;
import at.theOnlyHorst.tetrij.renderer.Screen;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Callback;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL40.GL_TRUE;

public class TetriJ implements Runnable {

    private static TetriJ mainGame;
    public static long window;

    public static final int WINDOW_WIDTH = 512;
    public static final int WINDOW_HEIGHT = 512;

    private static boolean requiredRerender;

    public static Matrix4f projectionMat;

    private boolean running;

    private Thread thread;
    private Screen activeScreen;

    private final List<Screen> screens = new ArrayList<>();

    private GameEngine engine;
    private static final int MS_PER_UPDATE = 16;



    private int shaderProg;

    public static TetriJ getTetriJ()
    {
        return mainGame;
    }


    public static void main(String[] args)
    {
        mainGame = new TetriJ();
        mainGame.start();
    }

    private TetriJ()
    {

    }

    public void start()
    {
        running = true;
        thread = new Thread(this,"Game");
        thread.start();
    }

    private void init()
    {
        engine = GameEngine.initEngine();
        try {
            ResourceManager.initResManager();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }




        //GameEngine.addLogicTask(new TickCounter());
        GameEngine.addRenderTask(new FPSCounter());
        GameEngine.addRenderTask(new RenderScreen());


        if(!glfwInit())
        {

            return;
        }
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);


        window = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, "TetriJ", 0, 0);

        if(window == 0) {
            throw new RuntimeException("Failed to create window");
        }

        glfwMakeContextCurrent(window);
        glfwShowWindow(window);
        GL.createCapabilities();

        Callback debugProc = GLUtil.setupDebugMessageCallback(); // may return null if the debug mode is not available

// cleanup
        if ( debugProc == null ) {
            throw new RuntimeException("Debug callback couldn't be created");

        }

        //glEnable(GL_TEXTURE_2D);
        //glDisable(GL_DEPTH_TEST);


        int vao = glGenVertexArrays();
        glBindVertexArray(vao);


        shaderProg = loadShaders();

        glUseProgram(shaderProg);





        //glMatrixMode(GL_PROJECTION);
        //lLoadIdentity();
        //glOrtho(0, TetriJ.WINDOW_WIDTH, TetriJ.WINDOW_HEIGHT, 0, -1, 1);
        //glMatrixMode(GL_MODELVIEW);

        requiredRerender = true;

    }

    private int loadShaders()
    {

        int vShader = glCreateShader(GL_VERTEX_SHADER);
        int fShader =  glCreateShader(GL_FRAGMENT_SHADER);

        String vShaderCode = ResourceManager.getShader("DefaultVShader");
        String fShaderCode = ResourceManager.getShader("DefaultFShader");

        glShaderSource(vShader,vShaderCode);
        glCompileShader(vShader);

        String vShaderInfo = glGetShaderInfoLog(vShader);

        System.out.println(vShaderInfo);


        glShaderSource(fShader,fShaderCode);
        glCompileShader(fShader);

        String fShaderInfo = glGetShaderInfoLog(fShader);

        System.out.println(fShaderInfo);

        int progId = glCreateProgram();
        glAttachShader(progId,vShader);

        glAttachShader(progId,fShader);

        glLinkProgram(progId);

        glDetachShader(progId,vShader);
        glDetachShader(progId,fShader);

        glDeleteShader(vShader);
        glDeleteShader(fShader);



        return progId;
    }



    @Override
    public void run() {
        init();

        long prevTime = System.currentTimeMillis();
        long lag = 0;
        while (running)
        {
            long currentTime = System.currentTimeMillis();
            long elapsed = currentTime - prevTime;
            lag += elapsed;
            prevTime = currentTime;
            processInputs();
            while (lag>=MS_PER_UPDATE) {
                update();
                lag -= MS_PER_UPDATE;
            }
            render(lag/MS_PER_UPDATE,elapsed);

            if(glfwWindowShouldClose(window))
            {
                running = false;
            }
        }

    }

    private void processInputs() {
    }


    private void update()
    {
        engine.update();
    }

    private void render(double lagDelta,long deltaTime)
    {
        engine.render(lagDelta,deltaTime);
        requiredRerender=false;
    }

    public static boolean requireRerender()
    {
        return requiredRerender;
    }

    public int getShaderProg() {
        return shaderProg;
    }

    public Screen getActiveScreen() {
        return activeScreen;
    }

    public void setActiveScreen(Screen activeScreen) {
        this.activeScreen = activeScreen;
    }

    public List<Screen> getScreens() {
        return screens;
    }
}
