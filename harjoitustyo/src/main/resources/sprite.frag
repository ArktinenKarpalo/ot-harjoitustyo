#version 330 core
out vec4 FragColor;

in vec2 TexCoord;

uniform sampler2D texture1;
uniform vec3 spriteColor;

void main() {
    FragColor = vec4(spriteColor, 1.0) * texture(texture1, TexCoord);
}