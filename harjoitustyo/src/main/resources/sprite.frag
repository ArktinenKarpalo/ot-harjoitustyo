#version 330 core
out vec4 FragColor;

in vec2 TexCoord;

uniform sampler2D texture1;
uniform vec4 spriteColor;

void main() {
    FragColor = spriteColor * texture(texture1, TexCoord);
}