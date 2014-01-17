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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import static javax.media.opengl.GL.*;  // GL constants
import static javax.media.opengl.GL2.*; // GL2 constants

import com.jogamp.common.nio.Buffers;

import pwneegl.GLNames;
import pwneegl.geom.data.VBOPipeline;
import pwneegl.material.Material;
import pwneegl.shader.Attribute;
import pwneegl.shader.ShaderLibrary;

/** 
 * A polyhedral comprised of a set of vertices and a set of faces formed 
 * from those vertices. 
 */
public class Poly3f {
  
  /** The vertices making up the polygon. */
  private List<Vertex3f> vertices;
  
  /** The list of faces making up the polygon. */
  private List<Face3f> faces;
  
  /** The programmable vbo pipeline manager for this polygon. */
  private VBOPipeline pipeline;
  
  /** Creates the polyhedral from the given set of vertices. The faces still need to be defined. */
  public Poly3f(Vertex3f[] vArr) {
    vertices = new ArrayList<>();
    for(Vertex3f v : vArr) {
      vertices.add(v);
    }
    faces = new ArrayList<>();
    pipeline = new VBOPipeline();
  }
  
  /** Creates the polyhedral from the given set of vertices. The faces still need to be defined. */
  public Poly3f(List<Vertex3f> vertices) {
    this.vertices = vertices;
    faces = new ArrayList<>();
    pipeline = new VBOPipeline();
  }
  
  /** Creates the polyhedral from the given set of vertices and faces. */
  public Poly3f(List<Vertex3f> vertices, List<Face3f> faces) {
    this.vertices = vertices;
    this.faces = faces;
    pipeline = new VBOPipeline();
  }
  
  
  /** Removes the vertex buffer data for this polyhedral from graphics memory. */
  public void clean(GL2 gl) {
    pipeline.clean(gl);
  }
  
  //////// Vertices
  
  /** Gets the vertex in this polyhedron at the specified index. */
  public Vertex3f getVertex(int index) {
    return vertices.get(index);
  }
  
  /** Returns the number of vertices making up this polygon. */
  public int getNumVertices() {
    return vertices.size(); 
  }
  
  /** Returns a copy of the list of vertices in this polygon. */
  public List<Vertex3f> getVertices() {
    return new ArrayList<Vertex3f>(vertices);
  }
  
  
  //////// Faces
  
  /**
   * Adds a face to this polygon.
   */
  public void addFace(Face3f face) {
    if(face.addToPoly(this)) {
      faces.add(face);
      
      // Compute the tangental vectors  for the 3 vertices.
      Vertex3f v1 = face.getVertex1();
      Vertex3f v2 = face.getVertex2();
      Vertex3f v3 = face.getVertex3();
      
      v1.computeTangentalVector(v2, v3);
      v2.computeTangentalVector(v3, v1);
      v3.computeTangentalVector(v1, v2);
    }
  }
  
  
  /** Adds a face to this polygon. */
  public void addFace(int v1, int v2, int v3) {
    addFace(new Face3f((short) v1, (short) v2, (short) v3));
  }
  
  /** 
   * Adds 2 faces sharing an edge to this polygon. 
   * Conventient for making quad-like faces. 
   */
  public void addFace(int v1, int v2, int v3, int v4) {
    addFace(v1, v2, v3);
    addFace(v1, v3, v4);
  }
  
  /** 
   * Adds an entire triangle strip of faces to this polygon. 
   * v must have at least 3 elements. 
   */
  public void addFaceStrip(int[] v) {
    for(int i = 2; i < v.length; i++) {
      int v1 = v[i-2];
      int v2 = v[i-1];
      int v3 = v[i];
      
      if(i % 2 == 0) {
        addFace(v1, v2, v3);
      }
      else {
        addFace(v1, v3, v2);
      }
    }
  }
  
  /** 
   * Adds a fan strip of faces to this polygon. 
   * v must have at least 3 elements. v[0] is the index of the pivotal vertex. 
   */
  public void addFaceFan(int[] v) {
    int pivot = v[0];
    
    for(int i = 2; i < v.length; i++) {
      int v2 = v[i-1];
      int v3 = v[i];
      addFace(pivot, v2, v3);
    }
  }
  
  /** 
   * Returns two separate lists of the opaque faces and the translucent faces 
   * in this polygon. In a scene where there is a mix of opaque and translucent 
   * faces, we want to render all the opaque ones first. We want to render 
   * all the translucent ones last, sorted from farthest to nearest for correct 
   * rendering. Note: This method doesn't sort the translucent faces.
   *
   * The first element returned is the list of opaque faces. The second element
   * is the list of unsorted translucent polygons. 
   */
  public List<Face3f>[] getFaceLists() {
    List<Face3f>[] result = new List[2];
    result[0] = new ArrayList<Face3f>();
    result[1] = new ArrayList<Face3f>();
    
    for(Face3f face : faces) {
      if(face.isOpaque()) {
        result[0].add(face);
      }
      else {
        result[1].add(face);
      } 
    }
    
    return result;
  }
  
  /** Returns a copy of the list of faces for this polygon. */
  public List<Face3f> getFaces() {
    return new ArrayList<Face3f>(faces);
  }
  
  
  /** Returns the number of faces making up this polygon. */
  public int getNumFaces() {
    return faces.size();
  }
  
  /** Returns the total number of vertex indices making up the faces of this polygon. */
  public int getNumIndices() {
    return faces.size()*3;
  }
  
  //////// Colors
  
  /** 
   * Sets the vertex color of all faces in this polygon, given the color's  
   * rgba components as floats in the range [0, 1]. 
   */
  public void setColor(float r, float g, float b, float a) {
    for(Face3f face : faces) {
      face.setColor(r, g, b, a);
    }
  }
  
  /** 
   * Sets the vertex color of all faces in this polygon, given the color's rgb components as 
   * floats in the range [0, 1].
   */
  public void setColor(float r, float g, float b) {
    setColor(r, g, b, 1f);
  }
  
  /** 
   * Sets the vertex color of all faces in this polygon, given the color's rgba components as 
   * ints in the range [0, 255].
   */
  public void setColor(int r, int g, int b, int a) {
    setColor(r/255f, g/255f, b/255f, a/255f);
  }
  
  /** 
   * Sets the vertex color of all faces in this polygon, given the color's rgb components as 
   * ints in the range [0, 255].
   */
  public void setColor(int r, int g, int b) {
    setColor(r/255f, g/255f, b/255f, 1f);
  }
  
  /** 
   * Sets the vertex color of all faces in this polygon, given the 
   * argb hexidecimal value for that color. 
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
   * Sets the vertex color of all faces in this polygon, given the rgb hexidecimal value for that color.
   * Red is in bits 16-23, green is in bits 8-15, and 
   * blue is in bits 0-7.
   */
  public void setColor(int rgb) {
    setColor(rgb, false);
  }
  
  
  //////// Rendering
  
  /** Render the polygon using VBO. (Fast!)*/
  public void render(GL2 gl) {
    pipeline.render(gl, faces, vertices);
  }
  
} 

