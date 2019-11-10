#version 450 core
layout (location = 0) in vec4 vertex; // pos_x, pos_y, tex_x, tex_y

out vec2 TexCoord;

uniform mat4 model;
uniform mat4 projection;

void main() {
    gl_Position = projection * model * vec4(vertex.xy, 0.0, 1.0);
    TexCoord = vertex.zw;
}