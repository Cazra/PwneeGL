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

import java.nio.FloatBuffer;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import static javax.media.opengl.GL.*;  // GL constants
import static javax.media.opengl.GL2.*; // GL2 constants

import com.jogamp.common.nio.Buffers;

import pwneegl.math.Point3f;
import pwneegl.math.PwneeMath;

/** 
 * A vertex is a point in 3D space with extra properties such as color, 
 * texture coordinates, and extra goodies that are helpful for vertices to
 * have in OpenGL.
 */
public class Vertex3f extends Point3f {
  
  /** The rgba color array. */
  private float[] color;
  
  /** The texture coordinates array. */
  private float[] texCoords;
  
  /** The per-vertex normal for this vertex. This is used to produce smoother shading. */
  private float[] normal;
  
  
  /** Creates the vertex, specifying only its model coordinates. */
  public Vertex3f(float x, float y, float z) {
    super(x, y, z);
    
    // Initialize the color to be transparent black.
    color = new float[4];
    color[0] = 0f;
    color[1] = 0f;
    color[2] = 0f;
    color[3] = 0f;
    
    // Initialize the texture coordinates to be (0,0).
    texCoords = new float[2];
    texCoords[0] = 0f;
    texCoords[1] = 0f;
    
    normal = null;
  }
  
  
  //////// Color
  
  public float getRed() {
    return color[0];
  }
  
  public float getGreen() {
    return color[1];
  }
  
  public float getBlue() {
    return color[2];
  }
  
  public float getAlpha() {
    return color[3];
  }
  
  /** Produces a copy of the vertex's 4-dimensional rgba color array. */
  public float[] getColor() {
    float[] result = new float[4];
    result[0] = color[0];
    result[1] = color[1];
    result[2] = color[2];
    result[3] = color[3];
    return result;
  }
  
  /** 
   * Sets the color of the vertex, given the color's rgba components as 
   * floats in the range [0, 1].
   */
  public void setColor(float r, float g, float b, float a) {
    color[0] = r;
    color[1] = g;
    color[2] = b;
    color[3] = a;
  }
  
  /** 
   * Sets the color of the vertex, given the color's rgb components as 
   * floats in the range [0, 1].
   */
  public void setColor(float r, float g, float b) {
    setColor(r, g, b, 1f);
  }
  
  /** 
   * Sets the color of the vertex, given the rgba hexidecimal value for that color.
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
   * Sets the color of the vertex, given the rgb hexidecimal value for that color.
   * Red is in bits 16-23, green is in bits 8-15, and 
   * blue is in bits 0-7.
   */
  public void setColor(int rgb) {
    setColor(rgb, false);
  }
  
  
  /** Returns true iff the color of this vertex is completely opaque (alpha = 1.0f). */
  public boolean isOpaque() {
    return (getAlpha() == 1.0f);
  }
  
  
  
  //////// Texture coordinates
  
  /** Set texture coordinates for a single texture. */
  public void setTexCoords(float s, float t) {
    texCoords[0] = s;
    texCoords[1] = t;
  }
  
  /** Set texture coordinates for double-texturing. */
  public void setTexCoords(float s1, float t1, float s2, float t2) {
    if(texCoords.length < 4) {
      texCoords = new float[4];
    }
    
    texCoords[0] = s1;
    texCoords[1] = t1;
    texCoords[2] = s2;
    texCoords[3] = t2;
  }
  
  /** Returns the S texture coordinate of the vertex. */
  public float getTexS() {
    return texCoords[0];
  }
  
  /** Returns the T texture coordinate of the vertex. */
  public float getTexT() {
    return texCoords[1];
  }
  
  /** Returns the 2-dimension texture coordinates array for this vertex. */
  public float[] getTexCoords() {
    float[] result = new float[2];
    result[0] = texCoords[0];
    result[1] = texCoords[1];
    return result;
  }
  
  
  
  //////// Per-vertex normal
  
  /** Returns the 3-dimensional normal vector array for this vertex. */
  public float[] getNormal() {
    if(normal == null) {
      return new float[] {1f, 0f, 0f};
    }
    else {
      return normal;
    }
  }
  
  public void setNormal(float[] n) {
    normal = PwneeMath.normalize(new float[] {n[0], n[1], n[2]});
  }
  
  public void setNormal(float nx, float ny, float nz) {
    normal = PwneeMath.normalize(new float[] {nx, ny, nz});
  }
  
  
  //////// Custom shader-specific attributes
  
  /** 
   * Returns an array of the vertex's custom shader attributes.
   * The default implementation returns null.
   * This should be overridden if you plan to use custom shader attributes for 
   * your vertices.
   */
  public float[] getAttribs() {
    return null;
  }
  
  /** 
   * Returns an array of custom shader attribute names. 
   * Each of these names corresponds by index to an attribute 
   * returned by getAttribs.
   * The default implementation returns null.
   * This should be overridden if you plan to use custom shader attributes for
   * your vertices.
   */
  public String[] getAttribNames() {
    return null;
  }
  
  
  //////// Rendering
  
  /** Renders the vertex naiively in GL_POINTS mode. */
  public void render(GL gl) {
    GL2 gl2 = gl.getGL2();
    
    gl2.glBegin(GL_POINTS);
    draw(gl2);
    gl2.glEnd();
  }
  
  
  /** Draws the vertex, assuming glBegin has already been called. */
  public void draw(GL2 gl) {
    gl.glColor4f(getRed(), getGreen(), getBlue(), getAlpha());
  //  gl.glNormal3fv(getNormal(), 0);
    gl.glVertex3f(getX(), getY(), getZ());
  }
  
  
  public String toString() {
    return "Vertex3f:(" + getX() + ", " + getY() + "," + getZ() + ")";
  }
  
}

