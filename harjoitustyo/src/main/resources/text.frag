#version 330 core
out vec4 FragColor;

in vec2 TexCoord;

uniform sampler2D texture1;
uniform vec4 spriteColor;

void main() {
    FragColor = spriteColor * vec4(1, 1, 1, texture2D(texture1, TexCoord).r);
}