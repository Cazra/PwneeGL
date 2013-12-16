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

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import static javax.media.opengl.GL.*;  // GL constants
import static javax.media.opengl.GL2.*; // GL2 constants

import pwneegl.math.Point3f;

/** A spotlight. */
public class LightSpot extends Light {
  
  public float x;
  public float y;
  public float z;
  
  public float dx;
  public float dy;
  public float dz;
  
  public float cutoffAngle;
  public float spotExponent;
  
  /** Creates the spotlight given its position, direction, cutoff angle, and color. */
  public LightSpot(float x, float y, float z, float dx, float dy, float dz, float cutoffAngle, int color) {
    super();
    setDiffuse(color);
    
    this.x = x;
    this.y = y;
    this.z = z;
    
    this.dx = dx;
    this.dy = dy;
    this.dz = dz;
    
    this.cutoffAngle = cutoffAngle;
    spotExponent = 0f;
  }
  
  /** Creates a white light source at some point. */
  public LightSpot(float x, float y, float z, float dx, float dy, float dz, float angle) {
    this(x, y, z, dx, dy, dz, angle, 0xFFFFFF);
  }
  
  
  
  public void setSpotExponent(float exp) {
    spotExponent = exp;
  }
  
  
  //////// Position
  
  /** Returns a length 4 array of the light's position coordinates (for multiplying by a 4x4 transformation matrix) */
  public float[] getCoords() {
    float[] result = new float[4];
    result[0] = x;
    result[1] = y;
    result[2] = z;
    result[3] = 1f;
    return result;
  }
  
  
  //////// Direction
  
  /** Returns a length 3 array of the light's direction vector coordinates. */
  public float[] getDirection() {
    float[] result = new float[3];
    result[0] = dx;
    result[1] = dy;
    result[2] = dz;
    return result;
  }
  
  //////// Rendering
  
  /** 
   * The light applies itself to the scene using the OpenGL lighting api. 
   * (This only supports 8 lights though, so it would be a better idea to 
   * implement the lights in a shader program. TODO)
   */
  public void glLight(GL2 gl, int index) {
    gl.glEnable(GL_LIGHTING);
    
    // Get the GL constant for our light index.
    int glLight = GL_LIGHT0;
    if(index == 1) {
      glLight = GL_LIGHT1;
    }
    if(index == 2) {
      glLight = GL_LIGHT2;
    }
    if(index == 3) {
      glLight = GL_LIGHT3;
    }
    if(index == 4) {
      glLight = GL_LIGHT4;
    }
    if(index == 5) {
      glLight = GL_LIGHT5;
    }
    if(index == 6) {
      glLight = GL_LIGHT6;
    }
    if(index == 7) {
      glLight = GL_LIGHT7;
    }
    
    // set up the light.
    gl.glEnable(glLight);
    
    gl.glLightfv(glLight, GL_POSITION, getCoords(), 0);
    gl.glLightfv(glLight, GL_SPOT_DIRECTION, getDirection(), 0);
    gl.glLightfv(glLight, GL_AMBIENT, getAmbient(), 0);
    gl.glLightfv(glLight, GL_DIFFUSE, getDiffuse(), 0);
    gl.glLightfv(glLight, GL_SPECULAR, getSpecular(), 0);
    
    float[] atten = getAttenuation();
    gl.glLightf(glLight, GL_CONSTANT_ATTENUATION, atten[0]);
    gl.glLightf(glLight, GL_LINEAR_ATTENUATION, atten[1]);
    gl.glLightf(glLight, GL_QUADRATIC_ATTENUATION, atten[2]);
    
    gl.glLightf(glLight, GL_SPOT_EXPONENT, spotExponent);
    gl.glLightf(glLight, GL_SPOT_CUTOFF, cutoffAngle);
  }
}

