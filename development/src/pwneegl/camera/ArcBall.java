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

import pwneegl.math.Vector3f;

import static javax.media.opengl.GL.*;  // GL constants
import static javax.media.opengl.GL2.*; // GL2 constants

/** 
 * A unit sphere used for camera manipulation. 
 * The arc ball is centered in its parent viewport and has a diameter equal 
 * to the viewport's height.
 */
public class ArcBall {
  
  public Component viewport;
  
  private float startAngleX = 0f;
  private float startAngleY = 0f;
  
  public ArcBall(Component viewport) {
    this.viewport = viewport;
  }
  
  /** 
   * Converts view coords into radial coords on the surface of the arc ball. 
   * This is returned as the array [angleX, angleY].
   */
  public float[] view2RadCoords(int x, int y) {
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
    float angleX = (float) Math.asin(v.getY());
    float angleY = (float) Math.acos(v.getX()/Math.cos(angleX));
    float[] result = new float[2]; 
    result[0] = angleX;
    result[1] = angleY;
    
    return result;
  }
  
  /** Begin dragging the arc ball from the specified coordinates.*/
  public void beginDrag(int x, int y) {
    float[] angles = view2RadCoords(x, y);
    startAngleX = angles[0];
    startAngleY = angles[1];
  }
  
  /** Drag the arc ball and return the change in the x and y angles. */
  public float[] drag(int x, int y) {
    float[] angles = view2RadCoords(x, y);
    float[] result = new float[2]; 
    result[0] = angles[0] - startAngleX;
    result[1] = angles[1] - startAngleY;
    
    startAngleX = angles[0];
    startAngleY = angles[1];
    
    return result;
  }
}