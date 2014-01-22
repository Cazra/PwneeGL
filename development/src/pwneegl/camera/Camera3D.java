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

import pwneegl.PwneeGLError;
import pwneegl.math.Point3f;
import pwneegl.math.PwneeMath;
import pwneegl.math.Vector3f;


/** 
 * A camera in 3D space to define the view transform for a scene. 
 * Unless otherwise specified, all coordinates in this class are expected to be
 * model coordinates. 
 */
public class Camera3D {
  
  /** X coordinate of the camera's eye. */
  private float x;
  
  /** Y coordinate of the camera's eye. */
  private float y;
  
  /** Z coordinate of the camera's eye. */
  private float z;
  
  
  /** X coordinate the camera is looking at. */
  private float lookX;
  
  /** Y coordinate the camera is looking at. */
  private float lookY;
  
  /** Z coordinate the camera is looking at. */
  private float lookZ;
  
  
  /** The "up" orientation of the camera. */
  private Vector3f up;
  
  /** The base field-of-view angle. */
  private float fovBase;
  
  /** The zoom of the camera. Modifies the field-of-view angle. */
  private float zoom;
  
  /** The near z-clipping plane. */
  private float zNear;
  
  /** The far z-clipping plane. */
  private float zFar;
  
  
  public Camera3D(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
    
    // look at origin.
    lookX = 0f;
    lookY = 0f;
    lookZ = 0f;
    
    // up is in direction of + y axis by default.
    up = new Vector3f(0f, 1f, 0f);
    
    fovBase = 45f;
    zoom = 1.0f;
    zNear = 1f;
    zFar = 1000f;
  }
  
  public Camera3D(Point3f eye) {
    this(eye.getX(), eye.getY(), eye.getZ());
  }
  
  
  /** Updates the GLU perspective with the camera's properties. */
  public void glCamera(GL2 gl, GLU glu, int width, int height) {
    
    // The aspect ratio.
    float aspect = (float) (1.0*width/Math.max(height,1));
    
    // Change to projection matrix.
    gl.glMatrixMode(GL_PROJECTION);
    gl.glLoadIdentity();
    
    // Perspective
    glu.gluPerspective( getFoV(), aspect, zNear, zFar);
                        
    glu.gluLookAt(x,            y,            z,
                  lookX,        lookY,        lookZ,
                  up.getX(),    up.getY(),    up.getZ());
    
    // Change back to model-view matrix.
    gl.glMatrixMode(GL_MODELVIEW);
    gl.glLoadIdentity();
  }
  
  
  /** Returns the location of the camera's eye in model coordinates. */
  public Point3f getEyePoint() {
    return new Point3f(x, y, z);
  }
  
  /** Returns the X coordinate of the camera's eye. */
  public float getEyeX() {
    return x;
  }
  
  /** Returns the Y coordinate of the camera's eye. */
  public float getEyeY() {
    return y;
  }
  
  /** Returns the Z coordinate of the camera's eye. */
  public float getEyeZ() {
    return z;
  }
  

  /** Sets the location of the camera's eye. */
  public void setEyePoint(Point3f p) {
    setEyePoint(p.getX(), p.getY(), p.getZ());
  }
  
  /** Sets the location of the camera's eye. */
  public void setEyePoint(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  /** Sets the X coordinate of the camera's eye. */
  public void setEyeX(float x) {
    setEyePoint(x, y, z);
  }
  
  /** Sets the Y coordinate of the camera's eye. */
  public void setEyeY(float y) {
    setEyePoint(x, y, z);
  }
  
  /** Sets the Z coordinate of the camera's eye. */
  public void setEyeZ(float z) {
    setEyePoint(x, y, z);
  }
  
  
  /** Returns the point in space that the camera is looking towards.*/
  public Point3f getLookPoint() {
    return new Point3f(lookX, lookY, lookZ);
  }
  
  /** Gets the X coordinate of the point the camera is looking towards. */
  public float getLookX() {
    return lookX;
  }
  
  /** Gets the Y coordinate of the point the camera is looking towards. */
  public float getLookY() {
    return lookY;
  }
  
  /** Gets the Z coordinate of the point the camera is looking towards. */
  public float getLookZ() {
    return lookZ;
  }
  
  /** Sets the point the camera is looking towards. */
  public void setLookPoint(Point3f p) {
    setLookPoint(p.getX(), p.getY(), p.getZ());
  }
  
  /** Sets the point the camera is looking towards. */
  public void setLookPoint(float x, float y, float z) {
    lookX = x;
    lookY = y;
    lookZ = z;
  }
  
  /** Sets the X coordinate for the point the camera is looking towards. */
  public void setLookX(float lookX) {
    setLookPoint(lookX, lookY, lookZ);
  }
  
  /** Sets the Y coordinate for the point the camera is looking towards. */
  public void setLookY(float lookY) {
    setLookPoint(lookX, lookY, lookZ);
  }
  
  /** Sets the Z coordinate for the point the camera is looking towards. */
  public void setLookZ(float lookZ) {
    setLookPoint(lookX, lookY, lookZ);
  }
  
  
  /** Returns the camera's current "up" vector. */
  public Vector3f getUpVector() {
    return up;
  }
  
  /** 
   * Sets the "up" vector of the camera. By default, the up vector is in the 
   * direction of the positive y-axis, [0, 1, 0]. 
   */
  public void setUpVector(float x, float y, float z) {
    up = new Vector3f(x, y, z);
  }
  
  /** Returns the vector from the eye to the look-at point of the camera. */
  public Vector3f getLookVector() {
    return new Vector3f(getEyePoint(), getLookPoint());
  }
  
  /** Returns the camera's base field of view, in degrees. */
  public float getBaseFoV() {
    return fovBase;
  }
  
  /** Gets the camera's current field of view, in degrees, modified by its zoom. */
  public float getFoV() {
    return fovBase*zoom;
  }
  
  
  /** Returns the camera's zoom level. */
  public float getZoom() {
    return zoom;
  }
  
  /** 
   * Sets the camera's zoom level. Default zoom level is 1.0f. Values < 1.0f
   * are zoomed in. Values > 1.0f are zoomed out.
   * A PwneeGLError is thrown if zoom is set <= 0.
   */
  public void setZoom(float zoom) {
    if(zoom <= 0f) {
      throw new PwneeGLError("Camera's zoom cannot be less than or equal to 0.");
    }
    this.zoom = zoom;
  }
  
  
  
  /** Returns the distance of the near clipping plane to the camera. */
  public float getClippingPlaneNear() {
    return zNear;
  }
  
  /** Returns the distance of the far clipping plane to the camera. */
  public float getClippingPlaneFar() {
    return zFar;
  }
  
  /** 
   * Sets the distances of the clipping planes from the camera. 
   * A PwneeGLError is thrown if near or far <= 0, or if far <= near.
   */
  public void setClippingPlanes(float near, float far) {
    if(near <= 0) {
      throw new PwneeGLError("Near clipping plane's distance can't be <= 0.");
    }
    if(far <= 0) {
      throw new PwneeGLError("Far clipping plane's distance can't be <= 0.");
    }
    if(far <= near) {
      throw new PwneeGLError("Far clipping plane must be further than near clipping plane.");
    }
    zNear = near;
    zFar = far;
  }
  
  
  /** 
   * Set the location of the camera's eye, relative in radial coordinates 
   * to the current look point with angles given in radians.
   */
  public void setEyeRadialCoords(float dist, float angleX, float angleY) {
    float[] xyz = PwneeMath.toXYZ(dist, angleX, angleY);
  
    x = lookX + xyz[0];
    y = lookY + xyz[1];
    z = lookZ + xyz[2];
  }
  
  
  /** 
   * Makes the camera look in the direction specified by angleX and angleY.
   * Sets the position of the camera's look point, given 
   * the rotation around the X and Y axes in radians. 
   * @param angleX is the camera's direction about the X axis.
   * @param angleY is the camera's direction about the Y axis.
   */
  public void lookInDirection(float angleX, float angleY) {
    float[] xyz = PwneeMath.toXYZ(1, angleX, angleY);
    
    lookX = x + xyz[0];
    lookY = y + xyz[1];
    lookZ = z + xyz[2];
  }
  
  
  
  /** Returns the distance of the camera's eye to its look point. */
  public float getDistance() {
    return getLookVector().length();
  }
  
  /** Sets the camera's eye to be positioned at the specified distance from its look point*/
  public void setDistance(float dist) {
    Vector3f v = getLookVector().negate();
    float[] rxy = PwneeMath.toRadial(v.getX(), v.getY(), v.getZ());
    float thetaX = rxy[1];
    float thetaY = rxy[2];
    setEyeRadialCoords(dist, thetaX, thetaY);
  }
}


