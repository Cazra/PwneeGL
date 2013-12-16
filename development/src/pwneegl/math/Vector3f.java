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

import com.jogamp.opengl.math.Quaternion;

/** An immutable 3-dimensional vector. */
public class Vector3f {
  
  /** The XYZ scalar components array. */
  private float[] coords;
  
  
  //////// Constructors
  
  /** Create the vector with the specified scalar components. */
  public Vector3f(float x, float y, float z) {
    coords = new float[4];
    coords[0] = x;
    coords[1] = y;
    coords[2] = z;
    coords[3] = 0f; // 4x4 translation matrices don't affect vectors.
  }
  
  /** Create the vector from p1 to p2. */
  public Vector3f(Point3f p1, Point3f p2) {
    this( p2.getX() - p1.getX(), 
          p2.getY() - p1.getY(), 
          p2.getZ() - p1.getZ());
  }
  
  /** Returns the unit vector in the direction of the positive X-axis. */
  public static Vector3f unitX() {
    return new Vector3f(1f, 0f, 0f);
  }
  
  /** Returns the unit vector in the direction of the positive Y-axis. */
  public static Vector3f unitY() {
    return new Vector3f(0f, 1f, 0f);
  }
  
  /** Returns the unit vector in the direction of the positive Z-axis. */
  public static Vector3f unitZ() {
    return new Vector3f(0f, 0f, 1f);
  }
  
  //////// Get/Set scalar components
  
  /** Get the X scalar component of the vector. */
  public float getX() {
    return coords[0];
  }
  
  /** Get the Y scalar component of the vector. */
  public float getY() {
    return coords[1];
  }
  
  /** Get the Z scalar component of the vector. */
  public float getZ() {
    return coords[2];
  }
  
  /** Returns a copy of the underlying coordinates array. */
  public float[] getCoords() {
    float[] result = new float[4];
    result[0] = coords[0];
    result[1] = coords[1];
    result[2] = coords[2];
    result[3] = coords[3];
    return result;
  }
  
  
  
  //////// Vector calculus
  
  /** Computes the length of the vector. */
  public float length() {
    return (float) Math.sqrt(coords[0]*coords[0] + coords[1]*coords[1] + coords[2]*coords[2]);
  }
  
  /** Computes the normalized vector. */
  public Vector3f normalize() {
    float length = length();
    float nx = coords[0]/length;
    float ny = coords[1]/length;
    float nz = coords[2]/length;
    return new Vector3f(nx, ny, nz);
  }
  
  
  /** Computes the negation of this vector. */
  public Vector3f negate() {
    return new Vector3f(-coords[0], -coords[1], -coords[2]);
  }
  
  /** Computes a scaled copy of this vector. */
  public Vector3f scale(float s) {
    float x = this.getX()*s;
    float y = this.getY()*s;
    float z = this.getZ()*s;
    return new Vector3f(x, y, z);
  }
  
  /** Computes a translated copy of this vector. */
  public Vector3f translate(float dx, float dy, float dz) {
    return new Vector3f(getX() + dx, getY() + dy, getZ() + dz);
  }
  
  /** Computes a copy of this vector rotated around the y and x axes, in order. */
  public Vector3f rotate(float thetaX, float thetaY) {
    
    // Rotate about the Y axis.
    Vector3f result = this.rotate(new Quaternion(new float[] {0f, 1f, 0f, 0f}, thetaY));
    
    // Rotate about the X axis.
    return result.rotate(new Quaternion(new float[] {1f, 0f, 0f, 0f}, thetaX));
  }
  
  /** Computes a copy of this vector rotated with a quaternion. */
  public Vector3f rotate(Quaternion q) {
    float[] v = q.mult(coords);
    return new Vector3f(v[0], v[1], v[2]);
  }
  
  
  //////// Vector addition
  
  /** Computes the sum of this vector and another. */
  public Vector3f add(Vector3f v) {
    return add(this, v);
  }
  
  public Vector3f add(float x, float y, float z) {
    return add(this, new Vector3f(x, y, z));
  }
  
  /** Computes the sum of two vectors. */
  public static Vector3f add(Vector3f u, Vector3f v) {
    float x = u.getX() + v.getX();
    float y = u.getY() + v.getY();
    float z = u.getZ() + v.getZ();
    return new Vector3f(x, y, z);
  }
  
  
  //////// Vector subtraction
  
  /** Computes the difference of this vector and another. */
  public Vector3f sub(Vector3f v) {
    return sub(this, v);
  }
  
  /** Computes the difference of two vectors. */
  public static Vector3f sub(Vector3f u, Vector3f v) {
    float x = u.getX() - v.getX();
    float y = u.getY() - v.getY();
    float z = u.getZ() - v.getZ();
    return new Vector3f(x, y, z);
  }
  
  
  
  
  
  /** Computes the dot product of this and another vector. */
  public float dot(Vector3f v) {
    return dot(this, v);
  }
  
  /** Computes the dot product of two vectors. */
  public static float dot(Vector3f u, Vector3f v) {
    return u.getX()*v.getX() + u.getY()*v.getY() + u.getZ()*v.getZ();
  }
  
  
  /** Computes the cross product of this and another vector. */
  public Vector3f cross(Vector3f v) {
    return cross(this, v);
  }
  
  /** Computes the cross product of two vectors. */
  public static Vector3f cross(Vector3f u, Vector3f v) {
    float x = u.getY()*v.getZ() - u.getZ()*v.getY();
    float y = u.getZ()*v.getX() - u.getX()*v.getZ();
    float z = u.getX()*v.getY() - u.getY()*v.getX();
    return new Vector3f(x, y, z);
  }
  
  /** Computes the smaller angle between two vectors. */
  public static float angleTo(Vector3f u, Vector3f v) {
    float dot = dot(u, v);
    float cos = dot/u.length()/v.length();
    return (float) Math.acos(cos);
  }
  
  
  //////// Rendering
  
  public String toString() {
    return "Vector3f:[" + getX() + ", " + getY() + "," + getZ() + "]";
  }
}