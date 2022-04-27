//Vertex shader

#type vertex
#version 330 core

//vecN is N sized vector
in vec3 aPos;
in vec4 aColor;

uniform mat4 uProjection;
uniform mat4 uView;

// Output
out vec4 fColor;

void main(){
    fColor = aColor;
    gl_Position = uProjection * uView * vec4(aPos, 1.0);
}


//    Fragment shader
#type fragment
#version 330 core

in vec4 fColor;

out vec4 color;

void main(){
    color = fColor;
}
