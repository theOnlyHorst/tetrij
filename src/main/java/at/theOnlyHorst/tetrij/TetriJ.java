package at.theOnlyHorst.tetrij;

import at.theOnlyHorst.tetrij.engine.GameEngine;
import at.theOnlyHorst.tetrij.engine.ResourceManager;
import at.theOnlyHorst.tetrij.gameObjects.GameSprite;
import at.theOnlyHorst.tetrij.gameObjects.tetrij.Grid;
import at.theOnlyHorst.tetrij.gameObjects.tetrij.Mino;
import at.theOnlyHorst.tetrij.gameObjects.tetrij.TetriMino;
import at.theOnlyHorst.tetrij.gameTasks.FPSCounter;
import at.theOnlyHorst.tetrij.gameTasks.QueueVertRecalc;
import at.theOnlyHorst.tetrij.gameTasks.RenderScreen;
import at.theOnlyHorst.tetrij.gameTasks.UpdateMinoPos;
import at.theOnlyHorst.tetrij.renderer.Renderer;
import at.theOnlyHorst.tetrij.renderer.Screen;
import at.theOnlyHorst.tetrij.util.Bag;
import at.theOnlyHorst.tetrij.util.RNG;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Callback;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class TetriJ {

    private static TetriJ mainGame;
    public static long window;

    public static int WINDOW_WIDTH = 512;
    public static int WINDOW_HEIGHT = 512;

    private static boolean requiredRerender;

    public static Matrix4f projectionMat;

    private boolean running;

    private Thread thread;
    private Screen activeScreen;

    public static final RNG rng = new RNG();

    private final List<Screen> screens = new ArrayList<>();

    private GameEngine engine;
    private static final int MS_PER_UPDATE = 16;

    private static boolean gameOver;

    private Grid grid;



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

    public static boolean isGameOver() {
        return gameOver;
    }

    public void start()
    {
        running = true;
        run();
    }

    private void init()
    {
        engine = GameEngine.initEngine();
        Renderer.createRenderer();
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
        //GameEngine.addRenderTask(new FPSCounter());

        GameEngine.addRenderTask(new RenderScreen());
        GameEngine.addLogicTask(new QueueVertRecalc());



        if(!glfwInit())
        {

            return;
        }
        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);

        Mino.loadMinoTextures();



        window = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, "TetriJ", 0, 0);

        if(window == 0) {
            throw new RuntimeException("Failed to create window");
        }

        glfwMakeContextCurrent(window);
        glfwShowWindow(window);
        glfwSetWindowSizeCallback(window, new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                WINDOW_WIDTH = width;
                WINDOW_HEIGHT = height;
                processResize();
            }
        });


        GL.createCapabilities();

        Callback debugProc = GLUtil.setupDebugMessageCallback(); // may return null if the debug mode is not available

        Screen main = new Screen(0);
        Screen gameOver = new Screen(1);
        getTetriJ().screens.addAll(Arrays.asList(main,gameOver));
        main.addScreenTasks(new UpdateMinoPos());
        grid = new Grid(main);
        Bag.initBags();
        grid.spawnTetriMino(Bag.getCurrentPiece());
        TetriJ.getTetriJ().setActiveScreen(0);

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
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
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

    public void setActiveScreen(int activeScreen) {

        if(this.activeScreen!=null)
            this.activeScreen.clean();
        this.activeScreen = screens.get(activeScreen);
    }

    public List<Screen> getScreens() {
        return screens;
    }

    private void processResize()
    {
        GL30.glViewport(0,0,WINDOW_WIDTH,WINDOW_HEIGHT);
        Renderer.recalculateBounds();
    }

    public Grid getGrid() {
        return grid;
    }

    public static void queueGameOver()
    {
        gameOver =true;
        //getTetriJ().setActiveScreen(1);
    }
}
