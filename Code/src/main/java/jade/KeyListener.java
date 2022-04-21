package jade;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class KeyListener {
    private static KeyListener instance;
    private boolean keyPressed[] = new boolean [350];

    private KeyListener(){
        // java automatically initialises all to false
    }

    public static KeyListener get(){
        if(KeyListener.instance == null){
            KeyListener.instance = new KeyListener();
        }
        return KeyListener.instance;
    }

    public static void keyCallBack(long window, int key, int scanCode, int action, int mods){
        get().keyPressed[key] = action == GLFW_PRESS;
    }

    public static boolean isKeyPressed(int keyCode){
        return get().keyPressed[keyCode];
    }
}
