/** scaleVShader with per-fragment Phong lighting. */

uniform float time; /* Value provided by the application program. */

varying vec3 n;
varying vec3 l;
varying vec3 e;

void main() {
  float s;
  s = abs(sin(time));
  gl_Position = gl_ModelViewProjectionMatrix*(vec4(s, s, s, 1.0)*gl_Vertex);
  
  vec4 eyePos = gl_ModelViewMatrix * gl_Vertex;
  vec4 eyeLightPos = gl_LightSource[0].position;
  
  n = normalize(gl_NormalMatrix*gl_Normal);
  l = normalize(eyeLightPos.xyz - eyePos.xyz);
  e = -normalize(eyePos.xyz);
  
  gl_FrontColor = gl_Color;
  
  gl_TexCoord[0] = gl_MultiTexCoord0;
}
