/** A vertex shader that morphs vertices into some other set of vertices. */

attribute vec4 vertices2;
uniform float blend;

void main() {
  vec4 t = vec4(mix(gl_Vertex.xyz, vertices2.xyz, blend), 1.0);
  gl_Position = gl_ModelViewProjectionMatrix * t;
  gl_FrontColor = gl_Color;
}
