/** scaleVShader with per-fragment Phong lighting. */

attribute vec3 tangental;

uniform float time; /* Value provided by the application program. */

varying vec3 normal;
varying vec3 l;
varying vec3 e;
varying vec3 tang;


void main() {
  float s;
  s = abs(sin(time));
  gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex; //gl_ModelViewProjectionMatrix*(vec4(s, s, s, 1.0)*gl_Vertex);
  
  vec4 eyePos = gl_ModelViewMatrix * gl_Vertex;
  vec4 eyeLightPos = gl_LightSource[0].position;
  
  normal = normalize(gl_NormalMatrix*gl_Normal);
  l = normalize(vec3(0,0,1)); //eyeLightPos.xyz - eyePos.xyz);
  e = -normalize(eyePos.xyz);
  tang = normalize(gl_NormalMatrix*tangental);
  
  gl_FrontColor = gl_Color;
  
  gl_TexCoord[0] = gl_MultiTexCoord0;
}
