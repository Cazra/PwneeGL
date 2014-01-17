/** bump map fragment shader with per-fragment Phong lighting. */

uniform float time; /* Value provided by the application program. */
uniform sampler2D texMap;
uniform sampler2D bumpMap;
uniform int useBump;

varying vec3 normal;
varying vec3 l;
varying vec3 e;
varying vec3 tang;

void main() {
  
  // The binormal vector orthogonal to normal and tang. 
  // This is used for doing bump mapping with tangent space.
  vec3 binormal = cross(normal, tang);
  
  // compute the transform from object (bump) space to tangent (normal) space.
  mat3 tMat = mat3(tang, binormal, normal);

  // Convert bump from [0,1] to [-1,1] range.
  vec3 n = normal;
  if(useBump != 0) {
    n = tMat * normalize(2.0*texture2D(bumpMap, gl_TexCoord[0].st).rgb - 1.0);
  }
  
  
  // compute lighting.
  vec3 h = normalize(e + l);
  
  float kd = max(dot(l, n), 0.0);
  float ks = pow(max(dot(n, h), 0.0), gl_FrontMaterial.shininess);
  
  if(dot(l, n) < 0.0) ks = 0.0;
  
  // light scalars
  vec4 diffuse = vec4(kd*gl_LightSource[0].diffuse.rgb, gl_LightSource[0].diffuse.a);
  vec4 ambient = gl_LightSource[0].ambient;
  vec4 specular = vec4(ks*gl_LightSource[0].specular.rgb, gl_LightSource[0].specular.a)*0.1;
  
  // material color
  vec4 color = texture2D(texMap, gl_TexCoord[0].st) + gl_Color;
  
  //gl_FragColor = color;
  gl_FragColor = specular + diffuse*color + ambient; // vec4((n+1)/2,1); // diffuse*color + ambient;
}
