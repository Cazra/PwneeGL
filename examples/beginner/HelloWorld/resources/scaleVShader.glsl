/** Vertex shader that moves vertex locations sinusoidally. */

uniform float time; /* Value provided by the application program. */

const vec4 cyan = vec4(0.0, 1.0, 1.0, 1.0);

void main() {
  float s;
  s = abs(sin(time));
  gl_Position = gl_ModelViewProjectionMatrix*(vec4(s, s, s, 1.0)*gl_Vertex);
  gl_FrontColor = gl_Color;
}
