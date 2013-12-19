package pwneegl.math;

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

import com.jogamp.opengl.math.FloatUtil;

/** Provides some convenient constants and static math functions. */
public class PwneeMath {
  
  /** 
   * Convenience constant for angular operations. 
   * This can be used in any angular function that uses radians.
   * Tau = 2*PI
   */
  public static final float TAU = 2 * FloatUtil.PI;
  
  
  //////// XYZ <-> Radial
  
  /** 
   * Convert XYZ coordinates to radial coordinates of the form 
   * (radius, thetaX, thetaY), relative to the origin.  thetaX and thetaY are
   * given in radians.
   */
  public static float[] toRadial(float x, float y, float z) {
    
    Vector3f v = new Vector3f(x, y, z);
    float length = v.length();
    
    // Easier to convert using a unit vector.
    v = v.normalize();
    
    float thetaX = (float) Math.asin(v.getY());
    float thetaY = 0f;
    
    float u = (float) Math.cos(thetaX);
    if(u != 0) {
      thetaY = (float) Math.acos(v.getX()/u);
    }
    
    return new float[] {length, thetaX, thetaY};
  }
  
  /** 
   * Convert 3D radial coordinates to XYZ coordinates, relative to the origin. 
   * thetaX and thetaY are expected to be in radians.
   */
  public static float[] toXYZ(float radius, float thetaX, float thetaY) {
    float x = radius * FloatUtil.cos(thetaY)*FloatUtil.cos(thetaX);
    float y = radius * FloatUtil.sin(thetaX);
    float z = 0 - radius * FloatUtil.sin(thetaY)*FloatUtil.cos(thetaX);
    
    return new float[] {x, y, z};
  }
  
  
  //////// Range binding
  
  /** Restricts some floating point value to be within the range [0, 1]. */
  public static float clamp(float coeff) {
    return (float) Math.min(Math.max(coeff, 0), 1);
  }
  
  /** Restricts some floating point value to be within the given [min, max] range. */
  public static float clamp(float coeff, float min, float max) {
    return (float) Math.min(Math.max(coeff, min), max);
  }
  
  
  //////// Normalize
  
  /** 
   * Normalizes a vector of any size. 
   * The original array is unchanged. A copy of the normalized vector array is
   * returned.
   */
  public static float[] normalize(float[] src) {
    int size = src.length;
    float[] result = new float[size];
    float length = 0;
    for(int i = 0; i < size; i++) {
      length += src[i]*src[i];
    }
    length = (float) Math.sqrt(length);
    
    for(int i = 0; i < size; i++) {
      result[i] = src[i]/length;
    }
    
    return result;
  }
  
}

