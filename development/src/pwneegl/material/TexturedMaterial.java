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

/** Defines a textured surface material. */
public class TexturedMaterial extends Material {
  
  private BufferedImage image;
  
  private Texture texture;
  
  /**  
   * Create the textured material using a source image from a file or resource 
   * path. 
   */
  public TexturedMaterial(String path, boolean isResource) {
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
  }
  
  
  public TexturedMaterial(String path) {
    this(path, false);
  }
  
  
  private void initTexture(GL2 gl) {
    texture = AWTTextureIO.newTexture(gl.getGLProfile(), image, false);
  }
  
  
  /** Applies this material to the OpenGL state. */
  public void glMaterial(GL2 gl) {
    if(texture == null) {
      initTexture(gl);
    }
    
    texture.enable(gl);
    texture.bind(gl);
    gl.glMaterialfv(GL_FRONT, GL_AMBIENT, getAmbient(), 0);
    gl.glMaterialfv(GL_FRONT, GL_SPECULAR, getSpecular(), 0);
    gl.glMaterialf(GL_FRONT, GL_SHININESS, getShininess());
    gl.glMaterialfv(GL_FRONT, GL_EMISSION, getEmission(), 0);
    
    
    
  }
}

