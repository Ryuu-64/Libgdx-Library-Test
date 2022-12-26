attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;
attribute vec2 a_resolution;
uniform mat4 u_projTrans;
varying vec4 v_color;
varying vec2 v_texCoords;
varying vec2 v_resolution;

void main() {
    v_color = a_color;
    v_texCoords = a_texCoord0;
    v_resolution = a_resolution;
    gl_Position = u_projTrans * a_position;
}

#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
varying vec2 v_resolution;
uniform sampler2D u_texture;

float normpdf(in float x, in float sigma) {
    return 0.39894 * exp(-0.5 * x * x / (sigma * sigma)) / sigma;
}

vec3 gaussianBlur(sampler2D texture) {
    const int mSize = 256;// TODO core size
    const int kSize = (mSize-1)/2;
    float kernel[mSize];
    vec3 finalColor = vec3(0.0);

    // create the 1-D kernel
    float sigma = 7.0;
    float Z = 0.0;
    for (int i = 0; i <= kSize; i++)
    {
        kernel[kSize + i] = kernel[kSize - i] = normpdf(float(i), sigma);
    }

    // get the normalization factor (as the gaussian has been clamped)
    for (int i = 0; i < mSize; i++)
    {
        Z += kernel[i];
    }

    // read out the texels
    for (int i = -kSize; i <= kSize; i++)
    {
        for (int j = -kSize; j <= kSize; j++)
        {
            finalColor += kernel[kSize + i] * kernel[kSize + j] * texture2D(u_texture, v_texCoords.xy + vec2(float(i), float(j)) / v_resolution).rgb;
        }
    }

    return finalColor / (Z * Z);
}

void main() {
    gl_FragColor = vec4(gaussianBlur(u_texture), 1) * v_color;
}