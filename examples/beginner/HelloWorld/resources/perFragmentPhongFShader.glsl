/** scaleVShader with per-fragment Phong lighting. */

uniform float time; /* Value provided by the application program. */
uniform sampler2D texMap;

varying vec3 n;
varying vec3 l;
varying vec3 e;

void main() {
  vec3 h = normalize(e + l);
  
  float f = 1.0;
  
  float kd = max(dot(l, n), 0.0);
  float ks = pow(max(dot(n, h), 0.0), gl_FrontMaterial.shininess);
  
  if(dot(l, n) < 0.0) f = 0.0;
  
  vec4 diffuse = kd*gl_Color*gl_LightSource[0].diffuse;
  vec4 ambient = gl_LightSource[0].ambient;
  vec4 specular = f*ks*gl_LightSource[0].specular;
  
  vec4 color = ambient + diffuse + specular;
  
  gl_FragColor = vec4(color.r, color.g, color.b, 1.0);
}
