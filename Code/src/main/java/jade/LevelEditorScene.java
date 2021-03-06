package jade;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import renderer.Shader;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class LevelEditorScene extends Scene{
//    private String vertexShaderSrc = "#version 330 core\n" +
//            "\n" +
//            "//vecN is N sized vector\n" +
//            "in vec3 aPos;\n" +
//            "in vec4 aColor;\n" +
//            "\n" +
//            "// Output\n" +
//            "out vec4 fColor;\n" +
//            "\n" +
//            "void main(){\n" +
//            "    fColor = aColor;\n" +
//            "    gl_Position = vec4(aPos, 1.0);\n" +
//            "}";
//
//    private String fragmentShaderSrc = "#version 330 core\n" +
//            "\n" +
//            "in vec4 fColor;\n" +
//            "\n" +
//            "out vec4 color;\n" +
//            "\n" +
//            "void main(){\n" +
//            "    color = fColor;\n" +
//            "}";

    private int vertexID, fragmentID, shaderProgram;

    // positions are normalised
    private float[] vertexArray = {
        // position             // color
         200.5f, 100.5f, 0.0f,      1.0f, 0.0f, 0.0f, 1.0f,  // 0: Bottom right
        100.5f,  200.5f, 0.0f,      0.0f, 1.0f, 0.0f, 1.0f,  // 1: Top left
         200.5f,  200.5f, 0.0f,      0.0f, 0.0f, 1.0f, 1.0f,  // 2: Top Right
        100.5f, 100.5f, 0.0f,      1.0f, 1.0f, 0.0f, 1.0f,  // 3: Bottom left
    };

    // IMPORTANT: Must be in counter-clockwise order
    private int [] elementArray = {
            /*
                *1       *2


                *3       *0

             */
            0, 2, 1,  // Top right triangle
            0, 1, 3   // bottom left triangle
    };

    private int vaoID,  // Vertex array object
                vboID,  // Vertex Buffer object
                eboID;  // Element Buffer object

    private Shader defaultShader;



    public LevelEditorScene(){

    }

    @Override
    public void init(){
        this.camera = new Camera(new Vector2f());
        defaultShader = new Shader("./assets/shaders/default.glsl");
        defaultShader.compile();





        // =================================================
        // Generate VAO, VBO and EBO buffer objects, and send to GPU
        // =================================================

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add vertex vertex attribute pointers
        int positionsize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;     // size(float) = 4
        int vertexSizeBytes = (positionsize + colorSize)*floatSizeBytes;

        glVertexAttribPointer(0, positionsize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsize*floatSizeBytes);
        glEnableVertexAttribArray(1);





    }

    @Override
    public void update(float dt) {

        defaultShader.use();
        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        //Bind the VAO that we are using
        glBindVertexArray(vaoID);

        // Enable the vertex attributes
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);   // 0 = nothing. So basically unbinds
        defaultShader.detach();
    }
}
