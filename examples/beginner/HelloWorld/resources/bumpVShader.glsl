/** bump map vertex shader with per-fragment Phong lighting. */

attribute vec3 tangental;
attribute vec3 rainbowbarf;

uniform float time; /* Value provided by the application program. */

varying vec3 normal;
varying vec3 l;
varying vec3 e;
varying vec3 tang;


void main() {
  gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
  
  vec4 eyePos = gl_ModelViewMatrix * gl_Vertex;
  vec4 eyeLightPos = gl_LightSource[0].position;
  
  normal = normalize(gl_NormalMatrix*gl_Normal);
  l = normalize(gl_LightSource[0].position.xyz); //vec3(0,0,1)); //eyeLightPos.xyz - eyePos.xyz);
  e = -normalize(eyePos.xyz);
  tang = normalize(gl_NormalMatrix*tangental);
  
  gl_FrontColor = gl_Color;
  gl_FrontColor.rgb = rainbowbarf;
  
  gl_TexCoord[0] = gl_MultiTexCoord0;
}
