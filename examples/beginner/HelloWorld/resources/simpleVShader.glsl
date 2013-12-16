/** A very simple vertex shader. */

const vec4 cyan = vec4(0.0, 1.0, 1.0, 1.0);

void main(void) {
  gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
  gl_FrontColor = gl_Color;
}
