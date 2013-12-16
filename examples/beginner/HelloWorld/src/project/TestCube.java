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
import pwneegl.material.TexturedMaterial;
import pwneegl.sprite.Sprite3f;


/** A sprite for a simple test cube. */
public class TestCube extends Sprite3f {
  
  private static Poly3f shape = Shapes.makeCube(0xFF0000); // Shapes.makeSphere(20, 20, 0xFFFF0000); // Shapes.makeCube(0xFF0000); // Shapes.makeRect(4f, 3f, 0xFF0000); // Shapes.makeCylinder(20, 0xFF0000);
  
  private static Material material = null;
  
  public TestCube(float x, float y, float z) {
    super(x, y, z);
    
    if(material == null) {
      initMaterial();
    }
    shape.material = material;
  }
  
  private void initMaterial() {
    material = new TexturedMaterial("sampleTex.png", true);
    material.setShininess(100f);
    material.setSpecular(1f, 1f, 0f, 1f);
    material.setEmission(0f, 0.5f, 0.5f, 1f);
  }
  
  
  @Override
  public void draw(GL2 gl) {
    
  //  material.glMaterial(gl);
    gl.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    shape.renderVBO(gl);
  }
  
  
}
