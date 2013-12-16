package pwneegl.material;

/*======================================================================
 * 
 * PwneeGL : A JOGL 2.* game engine for Java. 
 * 
 * Copyright (c) 2013 by Stephen Lindberg (sllindberg21@students.tntech.edu)
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: 
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
======================================================================*/

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import com.jogamp.opengl.util.texture.Texture;

import static javax.media.opengl.GL.*;  // GL constants
import static javax.media.opengl.GL2.*; // GL2 constants

import pwneegl.math.PwneeMath;
import pwneegl.shader.ShaderLibrary;

/** 
 * Defines a textured surface material. 
 * In your vertex and fragment shaders, this class expects there to be a uniform 
 * attribute named "texMap" used to access the sampler2D for the texture. 
 * There should also be  
 */
public class TexturedMaterial extends Material {
  
  /** The source image for the texture. */
  private BufferedImage image;
  
  /** The OpenGL texture stored in graphics memory. */
  private Texture texture;
  
  /** The active texture location this texture is bound to. */
  private int activeTexNum;
  
  /**  
   * Create the textured material using a source image from a file or resource 
   * path. The texture will be loaded into graphics memory using the specified 
   * active texture number (i.e.: GL_TEXTUREi).
   */
  public TexturedMaterial(String path, boolean isResource, int activeTexNum) {
    super(0x000000);
    try {
      if(isResource) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(path);
        image = ImageIO.read(is);
      }
      else {
        File file = new File(path);
        image = ImageIO.read(file);
      }
    }
    catch(IOException e) {
      e.printStackTrace();
    }
    texture = null;
    this.activeTexNum = activeTexNum;
  }
  
  /**  
   * Create the textured material using a source image from a file or resource 
   * path. 
   */
  public TexturedMaterial(String path, boolean isResource) {
    this(path, isResource, GL_TEXTURE0);
  }
  
  /** Creates the textured material using a source image from a file path. */
  public TexturedMaterial(String path) {
    this(path, false, GL_TEXTURE0);
  }
  
  
  /** Loads the texture into graphics memory. */
  private void initTexture(GL2 gl) {
    texture = AWTTextureIO.newTexture(gl.getGLProfile(), image, false);
  }
  
  
  /** Removes the texture from graphics memory. */
  @Override
  public void clean(GL2 gl) {
    if(texture != null) {
      texture.destroy(gl);
    }
  }
  
  
  
  //////// Geometry
  
  /** Returns the width of the image used for the texture. */
  public int getWidth() {
    return image.getWidth();
  }
  
  /** Returns the height of the image used for the texture. */
  public int getHeight() {
    return image.getHeight();
  }
  
  /** Returns the width of the allocated OpenGL texture in pixels. */
  public int getGLWidth() {
    return texture.getWidth();
  }
  
  /** Returns the height of the allocated OpenGL texture in pixels. */
  public int getGLHeight() {
    return texture.getHeight();
  }
  
  
  
  
  //////// Rendering
  
  
  /** 
   * Attempts to bind the texture and its properties to the specified uniform 
   * attribute in the OpenGL state and current shader program. 
   */
  public void glMaterial(GL2 gl, String uniName) {
    if(texture == null) {
      initTexture(gl);
    }
    
    gl.glActiveTexture(activeTexNum);
    texture.bind(gl);
    int texLocation = ShaderLibrary.get().getUniform(gl, uniName);
    gl.glUniform1i(texLocation, activeTexNum - GL_TEXTURE0);
    
    gl.glMaterialfv(GL_FRONT, GL_AMBIENT, getAmbient(), 0);
    gl.glMaterialfv(GL_FRONT, GL_DIFFUSE, getDiffuse(), 0);
    gl.glMaterialfv(GL_FRONT, GL_SPECULAR, getSpecular(), 0);
    gl.glMaterialf(GL_FRONT, GL_SHININESS, getShininess());
    gl.glMaterialfv(GL_FRONT, GL_EMISSION, getEmission(), 0);
  }
}

