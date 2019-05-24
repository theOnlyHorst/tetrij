package at.theOnlyHorst.tetrij;

import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;

public class TetriJ implements Runnable {

    private static TetriJ mainGame;
    private long window;

    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 500;

    private boolean running;

    private Thread thread;

    public static TetriJ getTetriJ()
    {
        return mainGame;
    }


    public static void main(String[] args)
    {
        //SharedLibraryLoader.load();
        mainGame = new TetriJ();
        mainGame.start();
    }

    public TetriJ()
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
        if(!glfwInit())
        {

            return;
        }
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        window = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, "TetriJ", 0, 0);
        if(window == 0) {
            throw new RuntimeException("Failed to create window");
        }

        glfwMakeContextCurrent(window);
        glfwShowWindow(window);

    }

    @Override
    public void run() {
        init();
        while (running)
        {
            update();
            render();

            if(glfwWindowShouldClose(window))
            {
                running = false;
            }
            sleep();
        }

    }


    private void update()
    {
        glfwPollEvents();
    }

    private void render()
    {
        glfwSwapBuffers(window);
    }

    private void sleep()
    {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
