package pwneegl.sprite;

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

/** 
 * Represents an object in 3D space. This is usually a renderable,  
 * animated model composed of several Poly3f's.
 */
public abstract class Sprite3f {
  
  //////// Coordinates
  
  /** The sprite's pivotal x coordinate in model space. */
  public float x;
  
  /** The sprite's pivotal y coordinate in model space. */
  public float y;
  
  /** The sprite's pivotal z coordinate in model space. */
  public float z;
  
  
  //////// Transform properties
  
  /** The scale coefficient of this sprite on its X axis. */
  public float scaleX;
  
  /** The scale coefficient of this sprite on its Y axis. */
  public float scaleY;
  
  /** The scale coefficient of this sprite on its Z axis. */
  public float scaleZ;
  
  /** The uniform scale coefficient of this sprite. */
  public float scaleUni;
  
  
  /** The rotation of the sprite in radians about its X axis. (Pitch) */
  public float angleX;
  
  /** The rotation of the sprite in radians about its Y axis. (Yaw) */
  public float angleY;
  
  /** The rotation of the sprite in radians about its Z axis. (Roll) */
  public float angleZ;
  
  
  //////// Rendering properties
  
  /** Whether or not to render the sprite. */
  public boolean isVisible;
  
  /** 
   * Controls how opaque or transparent this Sprite is. The value for this 
   * should be in the range [0.0, 1.0], where 0.0 is completely transparent 
   * and 1.0 is completely opaque. 
   */
   public float opacity = 1.0f;
  
  
  ////////
  
  /** Whether or not this sprite has been destroyed. */
  public boolean isDestroyed;
  
  
  //////// Constructors
  
  /** Constructs a sprite given its (x, y, z) model coordinates. */
  public Sprite3f(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
    
    scaleX = 1f;
    scaleY = 1f;
    scaleZ = 1f;
    scaleUni = 1f;
    
    angleX = 0f;
    angleY = 0f;
    angleZ = 0f;
    
    isVisible = true;
    isDestroyed = false;
  }
  
  
  
  
  
  //////// Properties
  
  /** Set the sprite's opacity. */
  public void setOpacity(float o) {
    this.opacity = PwneeMath.clamp(o);
  }
  
  
  //////// Rendering
  
  /** Renders the sprite using a naiive approach to OpenGL. */
  public void render(GL gl) {
    if(!isVisible) {
      return;
    }
    
    GL2 gl2 = gl.getGL2();
    gl2.glPushMatrix();
    
    // Apply the sprite's transforms before drawing it. 
    gl2.glTranslatef(x, y, z);
    applyTransforms(gl2);
    
    draw(gl2);
    gl2.glPopMatrix();
  }
  
  
  public void applyTransforms(GL2 gl) {
    gl.glRotatef(angleX/PwneeMath.TAU*360, 1f, 0f, 0f);
    gl.glRotatef(angleY/PwneeMath.TAU*360, 0f, 1f, 0f);
    gl.glRotatef(angleZ/PwneeMath.TAU*360, 0f, 0f, 1f);
    
    gl.glScalef(scaleX*scaleUni, scaleY*scaleUni, scaleZ*scaleUni);
  }
  
  
  /** Draw the sprite, assuming we are already drawing in GL_TRIANGLES mode. */
  public abstract void draw(GL2 gl);
  
  
}
