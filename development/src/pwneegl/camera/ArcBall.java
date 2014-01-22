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

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

import javax.swing.JFrame;

import com.jogamp.opengl.math.FloatUtil;
import com.jogamp.opengl.util.AnimatorBase;
import com.jogamp.opengl.util.FPSAnimator;

import static javax.media.opengl.GL.*;  // GL constants
import static javax.media.opengl.GL2.*; // GL2 constants

import pwneegl.input.Mouse;
import pwneegl.math.Point3f;
import pwneegl.math.PwneeMath;
import pwneegl.math.Vector3f;


/** 
 * A unit sphere used for camera manipulation. 
 * The arc ball is centered in its parent viewport and has a diameter equal 
 * to the viewport's height.
 * Unlike a normal camera, the user doesn't specify an eyepoint for the 
 * arcball camera. Instead, the arcball camera rotates around its look point at
 * some specified distance away. The arcball rotation can be controlled by 
 * click-and-drag with the mouse, or it can be controlled programatically.
 */
public class ArcBall extends Camera3D {
  
  public Component viewport;
  
  private float lastThetaX = 0f;
  private float lastThetaY = 0f;
  
  /** Distance from the eye point to the look point. */
  private float distance;
  
  /** Rotation of the eye around the X axis relative to the look point. */
  private float thetaX;
  
  /** Rotation of the eye around the Y axis relative to the look point. */
  private float thetaY;
  
  public ArcBall(Point3f eye, Point3f look, Component viewport) {
    super(eye);
    setLookPoint(look);
    calibratePolarCoords();
    this.viewport = viewport;
  }
  
  public ArcBall(Point3f look, float distance, float thetaX, float thetaY, Component viewport) {
    super(0, 0, 0);
    setLookPoint(look);
    setEyeRadialCoords(distance, thetaX, thetaY);
    calibratePolarCoords();
    this.viewport = viewport;
  }
  
  
  /** 
   * Recalculates the stored polar coordinates for the eye relative to the 
   * look point, based on their current relative xyz coordinates.
   */
  private void calibratePolarCoords() {
    Vector3f v = getLookVector().negate();
    float[] rxy = PwneeMath.toRadial(v.getX(), v.getY(), v.getZ());
    distance = rxy[0];
    thetaX = rxy[1];
    thetaY = rxy[2];
  }
  
  
  
  /** Controls the arcball camera with mouse input. */
  public void doMouseControl(Mouse mouse) {
    if(isGestureStart(mouse)) {
      beginDrag(mouse.x, mouse.y);
    }
    else if(isGestureDrag(mouse)) {
      drag(mouse.x, mouse.y);
    }
  }
  
  
  /** 
   * Returns true if the mouse input is performing some gesture to begin 
   * dragging the arcball. By default, this returns true when the user 
   * begins holding the left mouse button. Override this if you want some 
   * other mouse gesture.
   */
  public boolean isGestureStart(Mouse mouse) {
    return mouse.justLeftPressed;
  }
  
  /** 
   * Returns true if the mouse input is performing some gesture to continue 
   * dragging the arball. By default, this returns true when the user 
   * holds the left mouse button. Override this if you want some 
   * other mouse gesture.
   */
  public boolean isGestureDrag(Mouse mouse) {
    return mouse.isLeftPressed;
  }
  
  
  //////// Dragging
  
  /** 
   * Converts view coords into radial coords on the surface of the arc ball. 
   * This is returned as the array [angleX, angleY].
   */
  private float[] mouse2SphereCoords(int x, int y) {
    int r = viewport.getHeight()/2;
    int cx = viewport.getWidth()/2;
    int cy = viewport.getHeight()/2;
    
    // Get the vector from the input view point to the center view point.
    float dx = x-cx;
    float dy = y-cy;
    Vector3f v = new Vector3f(dx, dy, 0);
    
    // Collapse the vector to fit in a unit circle.
    v = v.scale((float) (1f/r));
    if(v.length() > 1) {
      v = v.normalize();
    }
    
    // compute the radial coordinates on the arc ball's virtual unit sphere.
    float angleX = (float) Math.asin(PwneeMath.clamp(v.getY(), -1, 1));
    float angleY = 0f;
    float u = (float) Math.cos(angleX);
    if(u != 0) {
      angleY = (float) Math.acos(PwneeMath.clamp(v.getX()/u, -1, 1));
    }
    
    return new float[] {angleX, angleY};
  }
  
  /** Begin dragging the arc ball from the specified coordinates.*/
  private void beginDrag(int mouseX, int mouseY) {
    float[] angles = mouse2SphereCoords(mouseX, mouseY);
    lastThetaX = angles[0];
    lastThetaY = angles[1];
  }
  
  /** Drag the arc ball and return the change in the x and y angles. */
  private void drag(int mouseX, int mouseY) {
    float[] arcAngles = mouse2SphereCoords(mouseX, mouseY);
    float dx = arcAngles[0] - lastThetaX;
    float dy = arcAngles[1] - lastThetaY;
    
    lastThetaX = arcAngles[0];
    lastThetaY = arcAngles[1];
    
    thetaX += dx;
    thetaY += dy;
    
    thetaX = PwneeMath.clamp(thetaX, -PwneeMath.TAU/4 + 0.01f, PwneeMath.TAU/4 - 0.01f);
    thetaY = PwneeMath.wrap(thetaY, 0, PwneeMath.TAU);
    setEyeRadialCoords(distance, thetaX, thetaY);
  }
  
  
  @Override
  public void setEyePoint(float x, float y, float z) {
    super.setEyePoint(x, y, z);
    calibratePolarCoords();
  }
  
  @Override
  public void setLookPoint(float x, float y, float z) {
    super.setLookPoint(x, y, z);
    calibratePolarCoords();
  }
  
  
  /** 
   * Moves the eye point so that it is looking towards the look point in the 
   * direction specified by the angles.
   */
  @Override
  public void lookInDirection(float angleX, float angleY) {
    setEyeRadialCoords(distance, angleX, angleY);
    thetaX = angleX;
    thetaY = angleY;
  }
  
  
  @Override
  public void setDistance(float dist) {
    super.setDistance(dist);
    distance = dist;
  }
  
}