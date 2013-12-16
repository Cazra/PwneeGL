package pwneegl.camera;

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
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

import javax.swing.JFrame;

import com.jogamp.opengl.math.FloatUtil;
import com.jogamp.opengl.util.AnimatorBase;
import com.jogamp.opengl.util.FPSAnimator;

import static javax.media.opengl.GL.*;  // GL constants
import static javax.media.opengl.GL2.*; // GL2 constants

import pwneegl.math.Point3f;
import pwneegl.math.PwneeMath;
import pwneegl.math.Vector3f;


/** A camera to define the view transform for a scene. */
public class Camera {
  
  /** X coordinate of the camera's eye. */
  public float x;
  
  /** Y coordinate of the camera's eye. */
  public float y;
  
  /** Z coordinate of the camera's eye. */
  public float z;
  
  
  /** X coordinate the camera is looking at. */
  public float atX;
  
  /** Y coordinate the camera is looking at. */
  public float atY;
  
  /** Z coordinate the camera is looking at. */
  public float atZ;
  
  
  /** The "up" orientation of the camera. */
  public Vector3f up;
  
  /** The base field-of-view angle. */
  public double fovyBase;
  
  /** The zoom of the camera. Modifies the field-of-view angle. */
  public double zoom;
  
  /** The near z-clipping plane. */
  public double zNear;
  
  /** The far z-clipping plane. */
  public double zFar;
  
  
  public Camera(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
    
    // look at origin.
    atX = 0f;
    atY = 0f;
    atZ = 0f;
    
    // up is in direction of + y axis by default.
    up = new Vector3f(0f, 1f, 0f);
    
    fovyBase = 45;
    zoom = 1.0;
    zNear = 1;
    zFar = 1000;
  }
  
  
  /** Updates the GLU perspective with the camera's properties. */
  public void updatePerspective(GL2 gl, GLU glu, int width, int height) {
    
    // The aspect ratio.
    float aspect = (float) (1.0*width/Math.max(height,1));
    
    // Change to projection matrix.
    gl.glMatrixMode(GL_PROJECTION);
    gl.glLoadIdentity();
    
    // Perspective
    glu.gluPerspective( fovyBase*zoom, aspect, zNear, zFar);
                        
    glu.gluLookAt(x,            y,            z,
                  atX,          atY,          atZ,
                  up.getX(),    up.getY(),    up.getZ());
    
    // Change back to model-view matrix.
    gl.glMatrixMode(GL_MODELVIEW);
    gl.glLoadIdentity();
  }
  
  
  
  public Point3f getEyePoint() {
    return new Point3f(x, y, z);
  }
  
  public Point3f getAtPoint() {
    return new Point3f(atX, atY, atZ);
  }
  
  
  public void setEyePoint(Point3f p) {
    x = p.getX();
    y = p.getY();
    z = p.getZ();
  }
  
  
  public void setAtPoint(Point3f p) {
    atX = p.getX();
    atY = p.getY();
    atZ = p.getZ();
  }
  
  
  /** 
   * Sets the position of the camera's eye based on radial coordinates 
   * with the origin at the "look at" point. 
   */
  public void setEyeRadialCoords(float dist, float angleX, float angleY) {
    float[] xyz = PwneeMath.toXYZ(dist, angleX, angleY);
  
    x = atX + xyz[0];
    y = atY + xyz[1];
    z = atZ + xyz[2];
  }
  
  
  /** 
   * Sets the position of the camera's "look at" relative to the eye, given 
   * the rotation around the X and Y axes. 
   */
  public void setLookAngles(float angleX, float angleY) {
    float[] xyz = PwneeMath.toXYZ(1, angleX, angleY);
    
    atX = x + xyz[0];
    atY = y + xyz[1];
    atZ = z + xyz[2];
  }
}


