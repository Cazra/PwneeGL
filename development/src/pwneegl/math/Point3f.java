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

import java.awt.geom.Point2D;

/** A point in 3D space. */
public class Point3f {
  
  /** The XYZ model coordinates array. */
  public float[] coords;
  
  
  
  //////// Constructors
  
  public Point3f(float x, float y, float z) {
    coords = new float[4];
    coords[0] = x;
    coords[1] = y;
    coords[2] = z;
    coords[3] = 1f; // Allows us to apply 4x4 translation matrices.
  }
  
  
  //////// Get/Set coordinates
  
  /** Get the X coordinate. */
  public float getX() {
    return coords[0];
  }
  
  /** Get the Y coordinate. */
  public float getY() {
    return coords[1];
  }
  
  /** Get the Z coordinate. */
  public float getZ() {
    return coords[2];
  }
  
  
  /** 
   * Returns a copy of the underlying 4-dimensional coordinates array. 
   * In shader programs, this result is available for the vertex in gl_Vertex.
   */
  public float[] getCoords() {
    float[] result = new float[4];
    result[0] = coords[0];
    result[1] = coords[1];
    result[2] = coords[2];
    result[3] = coords[3];
    return result;
  }
  
  
  /** Set the X coordinate. */
  public void setX(float x) {
    coords[0] = x;
  }
  
  /** Set the Y coordinate. */
  public void setY(float y) {
    coords[1] = y;
  }
  
  /** Set the Z coordate. */
  public void setZ(float z) {
    coords[2] = z;
  }
  
  
  
  //////// Distance
  
  /** Computes the distance to another point. */
  public float distance(Point3f pt) {
    return distance(this, pt);
  }
  
  /** Computes the distance to another point. */
  public float distance(float x, float y, float z) {
    return distance(this, new Point3f(x, y, z));
  }
  
  /** Computes the distance between two points. */
  public static float distance(Point3f p1, Point3f p2) {
    float dx = p2.getX() - p1.getX();
    float dy = p2.getY() - p1.getY();
    float dz = p2.getZ() - p1.getZ();
    return (float) Math.sqrt(dx*dx + dy*dy + dz*dz);
  }
  
  /** Computes the distance between two points. */
  public static float distance(float x1, float y1, float z1, float x2, float y2, float z2) {
    return distance(new Point3f(x1, y1, z1), new Point3f(x2, y2, z2));
  }
  
  
  
  //////// Square distance functions for when it would be inefficient to use the actual distance.
  
  /** Computes the square distance to another point. */
  public float distanceSq(Point3f pt) {
    return distanceSq(this, pt);
  }
  
  /** Computes the square distance to another point. */
  public float distanceSq(float x, float y, float z) {
    return distanceSq(this, new Point3f(x, y, z));
  }
  
  /** Computes the square distance between two points. */
  public static float distanceSq(Point3f p1, Point3f p2) {
    float dx = p2.getX() - p1.getX();
    float dy = p2.getY() - p1.getY();
    float dz = p2.getZ() - p1.getZ();
    return dx*dx + dy*dy + dz*dz;
  }
  
  /** Computes the square distance between two points. */
  public static float distanceSq(float x1, float y1, float z1, float x2, float y2, float z2) {
    return distanceSq(new Point3f(x1, y1, z1), new Point3f(x2, y2, z2));
  }
  
  
  
  //////// Conversion
  
  /** Convert this Point3f into a Point2D that can be used in java.awt graphics applications. */
  public Point2D toPoint2D() {
    return new Point2D.Float(getX(), getY());
  }
  
  
  //////// Rendering
  
  public String toString() {
    return "Point3f:(" + getX() + ", " + getY() + "," + getZ() + ")";
  }
}