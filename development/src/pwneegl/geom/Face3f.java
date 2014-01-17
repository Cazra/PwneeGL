package pwneegl.geom;

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

import java.awt.*;
import java.awt.geom.*;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import static javax.media.opengl.GL.*;  // GL constants
import static javax.media.opengl.GL2.*; // GL2 constants

import pwneegl.math.Point3f;
import pwneegl.math.Vector3f;

/** 
 * A triangular polygon defined by 3 vertices in ccw order. The vertices are 
 * stored by their index into the containing Poly3f's vertex array.
 *
 * Faces have no actual properties themselves except for the indices of the 3 
 * vertices that compose them.
 */
public class Face3f {
  
  /** A reference to the polygon this face belongs to. */
  private Poly3f polygon = null;
  
  /** 
   * The 3 points comprising this face, given by their vertex array indices in 
   * ccw order.
   */
  private short v1, v2, v3;
  
  /** Constructs the face, given its vertex indices in CCW order. */
  public Face3f(short v1, short v2, short v3) {
    this.v1 = v1;
    this.v2 = v2;
    this.v3 = v3;
  }
  
  
  //////// Polygon
  
  /** 
   * Assigns this face to be part of a polygon. 
   * Once a face is assigned to a polygon, it cannot be assigned to any other
   * polygon. This method is crucial for the construction of polygons from
   * faces.
   *
   * Returns true iff the face was added to the polygon successfully.
   */
  public boolean addToPoly(Poly3f poly) {
    if(polygon == null) {
      polygon = poly;
      polygon.addFace(this);
      return true;
    }
    else {
      return false;
    }
  }
  
  //////// Vertices
  
  
  /** Get the first vertex for this face. */
  public Vertex3f getVertex1() {
    return polygon.getVertex(v1);
  }
  
  /** Get the second vertex for this face. */
  public Vertex3f getVertex2() {
    return polygon.getVertex(v2);
  }
  
  /** Get the third vertex for this face. */
  public Vertex3f getVertex3() {
    return polygon.getVertex(v3);
  }
  
  /** Get the index for the first vertex for this face. */
  public short getIndex1() {
    return v1;
  }
  
  /** Get the index for the second vertex for this face. */
  public short getIndex2() {
    return v2;
  }
  
  /** Get the index for the third vertex for this face. */
  public short getIndex3() {
    return v3;
  }
  
  //////// Math
  
  /** 
   * Gets the normal vector for this face. 
   */
  public Vector3f getNormal() {
    Vertex3f p1 = getVertex1();
    Vertex3f p2 = getVertex2();
    Vertex3f p3 = getVertex3();
    
    Vector3f v1_2 = new Vector3f(p1, p2);
    Vector3f v1_3 = new Vector3f(p1, p3);
    return v1_2.cross(v1_3);
  }
  
  
  //////// Colors
  
  /** 
   * Sets the vertex color of all vertices in this face, given the color's  
   * rgba components as floats in the range [0, 1]. 
   */
  public void setColor(float r, float g, float b, float a) {
    getVertex1().setColor(r, g, b, a);
    getVertex2().setColor(r, g, b, a);
    getVertex3().setColor(r, g, b, a);
  }
  
  /** 
   * Sets the vertex color of all vertices in this face, given the color's rgb components as 
   * floats in the range [0, 1].
   */
  public void setColor(float r, float g, float b) {
    setColor(r, g, b, 1f);
  }
  
  /** 
   * Sets the vertex color of all vertices in this face, given the color's rgba components as 
   * ints in the range [0, 255].
   */
  public void setColor(int r, int g, int b, int a) {
    setColor(r/255f, g/255f, b/255f, a/255f);
  }
  
  /** 
   * Sets the vertex color of all vertices in this face, given the color's rgb components as 
   * ints in the range [0, 255].
   */
  public void setColor(int r, int g, int b) {
    setColor(r/255f, g/255f, b/255f, 1f);
  }
  
  /** 
   * Sets the vertex color of all vertices in this face, given the argb 
   * hexidecimal value for that color.
   * Alpha is in bits 24-31, red is in bits 16-23, green is in bits 8-15, and 
   * blue is in bits 0-7.
   */
  public void setColor(int argb, boolean hasAlpha) {
    float r = ((argb >> 16) & 0x000000FF)/255f;
    float g = ((argb >> 8) & 0x000000FF)/255f;
    float b = (argb & 0x000000FF)/255f;
    float a = 1f;
    if(hasAlpha) {
      a = ((argb >> 24)& 0x000000FF)/255f;
    }
    
    setColor(r, g, b, a);
  }
  
  /** 
   * Sets the vertex color of all vertices in this face, given the rgb hexidecimal value for that color.
   * Red is in bits 16-23, green is in bits 8-15, and 
   * blue is in bits 0-7.
   */
  public void setColor(int rgb) {
    setColor(rgb, false);
  }
  
  
  /** Returns true iff this face is completely opaque. */
  public boolean isOpaque() {
    return (getVertex1().isOpaque() && getVertex2().isOpaque() && getVertex3().isOpaque());
  }
  
  
  //////// Misc
  
  public String toString() {
    return "Face3f:{" + getVertex1() + ", " + getVertex2() + ", " + getVertex3() + "}";
  }
  
  
}