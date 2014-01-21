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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import static javax.media.opengl.GL.*;  // GL constants
import static javax.media.opengl.GL2.*; // GL2 constants

import com.jogamp.common.nio.Buffers;

import pwneegl.PwneeGLError;
import pwneegl.math.Point3f;
import pwneegl.math.PwneeMath;
import pwneegl.math.Vector3f;
import pwneegl.shader.Attribute;
import pwneegl.shader.ShaderLibrary;

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
  
  /** 
   * The tangental vector for this vertex. This is orthogonal to the normal 
   * and oriented in the positive s direction of the texture coordinate system 
   * at this vertex. This is useful for special effects such as bump mapping.
   */
  private float[] tangental;
   
  /** Map of user-defined float attribute locations to their values. */
  private Map<Integer, float[]> attribsf;
  
  /** Map of user-defined int attribute locations to their values. */
  private Map<Integer, int[]> attribsi;
  
  /** Map of user-defined double attribute locations to their values. */
  private Map<Integer, double[]> attribsd;
  
  
  
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
    tangental = null;
    
    attribsf = new HashMap<>();
    attribsi = new HashMap<>();
    attribsd = new HashMap<>();
  }
  
  /** Creates the vertex, specifying only its model coordinates. */
  public Vertex3f(float[] xyz) {
    this(xyz[0], xyz[1], xyz[2]);
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
  
  /** 
   * Produces a copy of the vertex's 4-dimensional rgba color array. 
   * In shader programs, this result is available for the vertex in gl_Color.
   */
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
  
  /** 
   * Returns the 2-dimension texture coordinates array for this vertex. 
   * In shader programs, this result is available for the vertex in gl_MultiTexCoord0.
   */
  public float[] getTexCoords() {
    float[] result = new float[2];
    result[0] = texCoords[0];
    result[1] = texCoords[1];
    return result;
  }
  
  
  
  //////// vertex normal
  
  /** 
   * Returns the 3-dimensional normal vector array for this vertex. 
   * In shader programs, this result is available for the vertex in gl_Normal.
   */
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
  
  
  
  //////// Tangental vector
  
  /** 
   * Computes the tangental vector for this vertex, given two other vertices 
   * with which this vertex forms a face. 
   */
  public void computeTangentalVector(Vertex3f v2, Vertex3f v3) {
    Vector3f u = new Vector3f(v2.getX() - this.getX(), v2.getY() - this.getY(), v2.getZ() - this.getZ());
    Vector3f v = new Vector3f(v3.getX() - this.getX(), v3.getY() - this.getY(), v3.getZ() - this.getZ());
    
    float su = v2.getTexS() - this.getTexS();
    float sv = v3.getTexS() - this.getTexS();
    float tu = v2.getTexT() - this.getTexT();
    float tv = v3.getTexT() - this.getTexT();
    float dst = tv*su - tu*sv;
    
    if(dst == 0) {
      tangental = new float[] {1,0,0};
    }
    else {
      tangental = u.scale(tv).sub(v.scale(tu)).scale(dst).getCoords(3);
    }
  }
  
  /** Sets the tangental vector manually. */
  public void setTangental(float[] t) {
    this.tangental = new float[] {t[0], t[1], t[2]};
  }
  
  /** 
   * Returns the 3-dimensional tangental vector for this vertex, 
   * or [1,0,0] if it has not been computed. 
   * This is not a built-in attribute in the OpenGL pipeline. 
   * The user will need to define a custom vertex attribute in their shader to
   * use this.
   */
  public float[] getTangental() {
    if(tangental == null) {
      tangental = new float[] {1f, 0f, 0f};
    }
    return tangental;
  }
  
  
  
  
  
  //////// User-defined vertex attributes
  
  // Floats
  
  /** Sets a single-float attribute. */
  public void setAttribf(String name, float value) {
    int attLoc = ShaderLibrary.get().getAttribLocation(name);
    attribsf.put(attLoc, new float[] {value});
  }
  
  /** Sets a float-based attribute. */
  public void setAttribfv(String name, float[] values) {
    int attLoc = ShaderLibrary.get().getAttribLocation(name);
    attribsf.put(attLoc, values);
  }
  
  /** 
   * Gets the value of a float-based attribute, given its OpenGL location. 
   * If the vertex has no such attribute, a PwneeGLError is thrown.
   */
  public float[] getAttribf(int loc) {
    float[] result = attribsf.get(loc);
    if(result == null) {
      throw new PwneeGLError("Vertex does not have user-defined attribute at location: " + loc);
    }
    else {
      return result;
    }
  }
  
  /** 
   * Gets the value of a float-based attribute, given its name. 
   * If the vertex has no such attribute, an empty array is returned.
   */
  public float[] getAttribf(String name) {
    return getAttribf(ShaderLibrary.get().getAttribLocation(name));
  }
  
  /** 
   * Returns an array of the float-based attributes for this vertex, 
   * in context to the shader currently in use. 
   * Any attributes used by the shader that the vertex doesn't have are 
   * returned as empty arrays.
   */
  public float[][] getAttribsf() {
    List<Attribute> attributes = ShaderLibrary.get().getUserAttribsf();
    float[][] result = new float[attributes.size()][];
    for(int i = 0; i < attributes.size(); i++) {
      result[i] = getAttribf(attributes.get(i).getLocation());
    }
    return result;
  }
  
  
  // Ints
  
  /** Sets a single-int attribute. */
  public void setAttribi(String name, int value) {
    int attLoc = ShaderLibrary.get().getAttribLocation(name);
    attribsi.put(attLoc, new int[] {value});
  }
  
  /** Sets a int-based attribute. */
  public void setAttribiv(String name, int[] values) {
    int attLoc = ShaderLibrary.get().getAttribLocation(name);
    attribsi.put(attLoc, values);
  }
  
  
  /** 
   * Gets the value of a int-based attribute, given its OpenGL location. 
   * If the vertex has no such attribute, a PwneeGLError is thrown.
   */
  public int[] getAttribi(int loc) {
    int[] result = attribsi.get(loc);
    if(result == null) {
      throw new PwneeGLError("Vertex does not have user-defined attribute at location: " + loc);
    }
    else {
      return result;
    }
  }
  
  /** 
   * Gets the value of a int-based attribute, given its name. 
   * If the vertex has no such attribute, an empty array is returned.
   */
  public int[] getAttribi(String name) {
    return getAttribi(ShaderLibrary.get().getAttribLocation(name));
  }
  
  /** 
   * Returns an array of the int-based attributes for this vertex, 
   * in context to the shader currently in use. 
   * Any attributes used by the shader that the vertex doesn't have are 
   * returned as empty arrays.
   */
  public int[][] getAttribsi() {
    List<Attribute> attributes = ShaderLibrary.get().getUserAttribsi();
    int[][] result = new int[attributes.size()][];
    for(int i = 0; i < attributes.size(); i++) {
      result[i] = getAttribi(attributes.get(i).getLocation());
    }
    return result;
  }
  
  
  // Doubles
  
  /** Sets a single-double attribute. */
  public void setAttribd(String name, double value) {
    int attLoc = ShaderLibrary.get().getAttribLocation(name);
    attribsd.put(attLoc, new double[] {value});
  }
  
  /** Sets a double-based attribute. */
  public void setAttribdv(String name, double[] values) {
    int attLoc = ShaderLibrary.get().getAttribLocation(name);
    attribsd.put(attLoc, values);
  }
  
  
  /** 
   * Gets the value of a double-based attribute, given its OpenGL location. 
   * If the vertex has no such attribute, a PwneeGLError is thrown.
   */
  public double[] getAttribd(int loc) {
    double[] result = attribsd.get(loc);
    if(result == null) {
      throw new PwneeGLError("Vertex does not have user-defined attribute at location: " + loc);
    }
    else {
      return result;
    }
  }
  
  /** 
   * Gets the value of a double-based attribute, given its name. 
   * If the vertex has no such attribute, an empty array is returned.
   */
  public double[] getAttribd(String name) {
    return getAttribd(ShaderLibrary.get().getAttribLocation(name));
  }
  
  /** 
   * Returns an array of the double-based attributes for this vertex, 
   * in context to the shader currently in use. 
   * Any attributes used by the shader that the vertex doesn't have are 
   * returned as empty arrays.
   */
  public double[][] getAttribsd() {
    List<Attribute> attributes = ShaderLibrary.get().getUserAttribsd();
    double[][] result = new double[attributes.size()][];
    for(int i = 0; i < attributes.size(); i++) {
      result[i] = getAttribd(attributes.get(i).getLocation());
    }
    return result;
  }
  
  
  //////// Misc
  
  public String toString() {
    return "Vertex3f:(" + getX() + ", " + getY() + "," + getZ() + ")";
  }
  
}

