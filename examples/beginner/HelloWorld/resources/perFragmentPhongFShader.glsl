/** scaleVShader with per-fragment Phong lighting. */

uniform float time; /* Value provided by the application program. */
uniform sampler2D texMap;
uniform sampler2D bumpMap;

varying vec3 normal;
varying vec3 l;
varying vec3 e;
varying vec3 t;

void main() {
  // The binormal vector orthogonal to normal and t. Used for doing bump mapping with tangent space.
  vec3 u = normalize(cross(normal, t));
  
  // compute the transform from object (bump) space to tangent (normal) space.
  mat3 tMat = mat3(t, u, normal);
  tMat = transpose(tMat);

  // Convert bump from [0,1] to [-1,1] range.
  vec3 n = normalize(2.0*texture2D(bumpMap, gl_TexCoord[0].st).rgb - 1.0);
  
  // Compute our perturbed normal by transforming the bump vector to tangent space.
  //normal = bump; // normalize(tMat * bump);
  
  vec3 h = normalize(e + l);
  
  float f = 1.0;
  
  float kd = max(dot(l, normal), 0.0);
  float ks = pow(max(dot(normal, h), 0.0), gl_FrontMaterial.shininess);
  
  if(dot(l, normal) < 0.0) f = 0.0;
  
  vec4 diffuse = kd*gl_Color*gl_LightSource[0].diffuse;
  vec4 ambient = gl_LightSource[0].ambient;
  vec4 specular = f*ks*gl_LightSource[0].specular;
  
  vec4 color = ambient + diffuse + specular;
  vec4 texColor = texture2D(texMap, gl_TexCoord[0].st);
  
  
  //gl_FragColor = vec4(color.r, color.g, color.b, gl_Color.a)*texColor;
  gl_FragColor = vec4(specular, 1);
}
