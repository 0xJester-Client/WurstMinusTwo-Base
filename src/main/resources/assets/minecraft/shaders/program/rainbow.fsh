#version 120

uniform sampler2D DiffuseSampler;

varying vec2 texCoord;
varying vec2 oneTexel;

uniform vec2 InSize;

uniform float Time;

vec3 hue(float h)
{
    float r = abs(h * 6.0 - 3.0) - 1.0;
    float g = 2 - abs(h * 6.0 - 2.0);
    float b = 2 - abs(h * 6.0 - 4.0);
    return clamp(vec3(r,g,b), 0.0, 1.0);
}

vec3 HSVtoRGB(vec3 hsv) {
    return ((hue(hsv.x) - 1.0) * hsv.y + 1.0) * hsv.z;
}

vec3 RGBtoHSV(vec3 rgb) {
    vec3 hsv = vec3(0.0);
    hsv.z = max(rgb.r, max(rgb.g, rgb.b));
    float min = min(rgb.r, min(rgb.g, rgb.b));
    float c = hsv.z - min;

    if (c != 0)
    {
        hsv.y = c / hsv.z;
        vec3 delta = (hsv.z - rgb) / c;
        delta.rgb -= delta.brg;
        delta.rg += vec2(2.0, 4.0);
        if (rgb.r >= hsv.z) {
            hsv.x = delta.b;
        } else if (rgb.g >= hsv.z) {
            hsv.x = delta.r;
        } else {
            hsv.x = delta.g;
        }
        hsv.x = fract(hsv.x / 6.0);
    }
    return hsv;
}

void main() {
    vec4 rgb = texture2D(DiffuseSampler, texCoord);
    vec3 hsv = RGBtoHSV(rgb.rgb);
    hsv.x = fract(hsv.x + Time);
    gl_FragColor = vec4(HSVtoRGB(hsv), 1.0);
}
