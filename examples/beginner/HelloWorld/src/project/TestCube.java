package project;

import java.nio.FloatBuffer;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import static javax.media.opengl.GL.*;  // GL constants
import static javax.media.opengl.GL2.*; // GL2 constants

import com.jogamp.common.nio.Buffers;


import pwneegl.geom.Poly3f;
import pwneegl.geom.Shapes;
import pwneegl.geom.Vertex3f;
import pwneegl.material.Material;
import pwneegl.material.MaterialLibrary;
import pwneegl.material.TexturedMaterial;
import pwneegl.shader.ShaderLibrary;
import pwneegl.sprite.Sprite3f;


/** A sprite for a simple test cube. */
public class TestCube extends Sprite3f {
  
  private static Poly3f shape = Shapes.makeSphere(20, 20); // Shapes.makeSphere(20, 20); // Shapes.makeCube(); // Shapes.makeRect(4f, 3f); // Shapes.makeCylinder(20);
  
  public TestCube(float x, float y, float z) {
    super(x, y, z);
    
    if(!MaterialLibrary.contains("testCube")) {
      initMaterial();
    }
  }
  
  private void initMaterial() {
    Material m = new TexturedMaterial("sampleTex.png", true, GL_TEXTURE0);
    m.setShininess(100f);
    m.setSpecular(1f, 1f, 0f, 1f);
    m.setEmission(0f, 0.5f, 0.5f, 1f);
    MaterialLibrary.put("testCube", m);
    
    Material bump = new TexturedMaterial("sampleTexBump.png", true, GL_TEXTURE1);
    MaterialLibrary.put("testCubeBump", bump);
  }
  
  
  @Override
  public void draw(GL2 gl) {
    
    ShaderLibrary.get().setUniformi(gl, "useBump", 1);
    MaterialLibrary.use(gl, "testCube", "texMap");
    MaterialLibrary.use(gl, "testCubeBump", "bumpMap");
    shape.render(gl);
  }
  
  
}
