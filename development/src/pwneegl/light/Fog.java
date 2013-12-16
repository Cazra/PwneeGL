package pwneegl.light;

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

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import static javax.media.opengl.GL.*;  // GL constants
import static javax.media.opengl.GL2.*; // GL2 constants

import pwneegl.math.PwneeMath;

/** Defines a fog effect. */
public class Fog {
  
  /** 
   * The rgb color array for the fog.
   */
  private float[] color;
  
  /**
   * The depth-cueing mode for the fog
   */
  private float mode;
  
  /** The density of the fog. */
  private float density;
  
  public Fog(float[] rgb) {
    this(rgb[0], rgb[1], rgb[2]);
    
  }
  
  
  public Fog(float r, float g, float b) {
    init(r, g, b);
  }
  
  
  public Fog(int rgb) {
    float r = ((rgb >> 16) & 0x000000FF)/255f;
    float g = ((rgb >> 8) & 0x000000FF)/255f;
    float b = (rgb & 0x000000FF)/255f;
    init(r, g, b);
  }
  
  
  private void init(float r, float g, float b) {
    this.color = new float[4];
    this.color[0] = r;
    this.color[1] = g;
    this.color[2] = b;
    
    mode = GL_EXP;
    density = 0.5f;
  }
  
  
  //////// color
  
  /** Get the rgb color array for the fog. */
  public float[] getColor() {
    return color;
  }
  
  /** Sets the color of the fog. */
  public void setColor(float[] rgb) {
    this.color[0] = rgb[0];
    this.color[1] = rgb[1];
    this.color[2] = rgb[2];
  }
  
  /** Sets the color of the fog. */
  public void setColor(float r, float g, float b) {
    this.color[0] = r;
    this.color[1] = g;
    this.color[2] = b;
  }
  
  /** Sets the color of the fog. */
  public void setColor(int rgb) {
    this.color[0] = ((rgb >> 16) & 0x000000FF)/255f;
    this.color[1] = ((rgb >> 8) & 0x000000FF)/255f;
    this.color[2] = (rgb & 0x000000FF)/255f;
  }
  
  
  
  //////// Mode
  
  public void setMode(float mode) {
    this.mode = mode;
  }
  
  //////// Density
  
  public void setDensity(float density) {
    this.density = density;
  }
  
  //////// Rendering
  
  /** Applies this material to the OpenGL state. */
  public void glFog(GL2 gl) {
    
    gl.glEnable(GL_FOG);
    gl.glFogf(GL_FOG_MODE, mode);
    gl.glFogf(GL_FOG_DENSITY, density);
    gl.glFogfv(GL_FOG_COLOR, color, 0);
  }
}

