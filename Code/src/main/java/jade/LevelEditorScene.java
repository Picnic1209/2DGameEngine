package jade;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene{

    private boolean changingScene = false;
    private float timeTOChangeScene = 2.0F;

    public LevelEditorScene(){
        System.out.println("Inside Level Editor Scene");
    }

    @Override
    public void update(float dt) {

        System.out.println("FPS: " + (1.0f/dt) );

        if(KeyListener.isKeyPressed(KeyEvent.VK_SPACE)){
            changingScene = true;
        }

        if(changingScene && timeTOChangeScene>0){
            timeTOChangeScene -= dt;
            Window.get().r -= dt*5.0f;
            Window.get().g -= dt*5.0f;
            Window.get().b -= dt*5.0f;
        }
        else if(changingScene){
            Window.changeScene(1);
        }
    }
}
