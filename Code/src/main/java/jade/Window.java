package jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int height, width;
    private String title;
    private long glfwWindow;

    private float r,g,b,a;
    private boolean fadeToBlack;

    // Static window so only one remains at any time
    private static Window window = null;

    private Window(){
        this.height = 1080;
        this.width = 1920;
        this.title = "Mario";
        r = 1;
        b = 1;
        g = 1;
        a = 1;
        fadeToBlack = false;
    }

    public static Window get(){
        if(Window.window == null){
            Window.window = new Window();
        }
        return Window.window;
    }

    public void run(){
        System.out.println("Hello LWJGL "+ Version.getVersion()+"!");

        init();
        loop();

        // Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and free the error
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();

    }

    public void init(){
        // Set up error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialise GLFW
        if(!glfwInit()){
            throw new IllegalStateException("Unable to initialise GLFW.");
        }

        //Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if(glfwWindow == NULL){
            throw new IllegalStateException("Unable to create GLFW Window");
        }

        // Listeners
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallBack);

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);

        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        // This line is critical for LWGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use

        // If this is not present then it may break the project
        GL.createCapabilities();
    }

    public void loop(){
        while(!glfwWindowShouldClose(glfwWindow)){
            // Poll Events
            glfwPollEvents();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            fadeToBlack = KeyListener.isKeyPressed(GLFW_KEY_SPACE);

            if(fadeToBlack){
                r = Math.max(r-0.1f,0);
                g = Math.max(g-0.1f,0);
                b = Math.max(b-0.1f,0);
            }else{
                r = Math.min(r+0.1f,1);
                g = Math.min(g+0.1f,1);
                b = Math.min(b+0.1f,1);
            }

            glfwSwapBuffers(glfwWindow);

        }
    }

}
