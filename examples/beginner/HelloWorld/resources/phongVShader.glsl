/** scaleVShader with Phong lighting. */

uniform float time; /* Value provided by the application program. */

const vec4 cyan = vec4(0.0, 1.0, 1.0, 1.0);

void main() {
  float s;
  s = abs(sin(time));
  gl_Position = gl_ModelViewProjectionMatrix*(vec4(s, s, s, 1.0)*gl_Vertex);
  // gl_FrontColor = gl_Color;
  
  vec3 n = normalize(gl_NormalMatrix*gl_Normal);
  vec4 eyePos = gl_ModelViewMatrix * gl_Vertex;
  
  vec4 eyeLightPos = gl_LightSource[0].position;
  vec3 l = normalize(eyeLightPos.xyz - eyePos.xyz);
  vec3 e = -normalize(eyePos.xyz);
  vec3 h = normalize(l + e);
  
  float f = 1.0;
  float kd = max(dot(l, n), 0.0);
  float ks = pow(max(dot(n, h), 0.0), gl_FrontMaterial.shininess);
  
  if(dot(l, n) < 0.0) f = 0.0;
  
  vec4 diffuse = kd*gl_Color*gl_LightSource[0].diffuse;
  vec4 ambient = gl_LightSource[0].ambient;
  vec4 specular = f*ks*gl_LightSource[0].specular;
  
  vec4 color = ambient + diffuse + specular;
  
  gl_FrontColor = vec4(color.r, color.g, color.b, 1.0);
}
