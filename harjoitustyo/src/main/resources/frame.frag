#version 330 core
out vec4 FragColor;

in vec2 TexCoord;

uniform sampler2D texture1;// Y
uniform sampler2D texture2;// U
uniform sampler2D texture3;// V
uniform vec4 spriteColor;

void main() {
    float nx, ny, r, g, b, y, u, v;
    nx=TexCoord.x;
    ny=TexCoord.y;
    y=texture2D(texture1, TexCoord).r;
    u=texture2D(texture2, TexCoord).r;
    v=texture2D(texture3, TexCoord).r;

    y=1.1643*(y-0.0625);
    u=u-0.5;
    v=v-0.5;

    r=y+1.5958*v;
    g=y-0.39173*u-0.81290*v;
    b=y+2.017*u;

    gl_FragColor = vec4(r, g, b, 1.0);
}