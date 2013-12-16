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

import pwneegl.material.Material;

/** 
 * A polyhedral comprised of 1 or more Face3f's. 
 * Also contains texture information for those faces. 
 */
public class Poly3f {
  
  /** The vertices making up the polygon. */
  private Vertex3f[] vertices;
  
  /** The list of faces making up the polygon. */
  private List<Face3f> faces;
  
  /** The material used for the polygon. */
  public Material material;
  
  /** 
   * Constructs the polyhedral from an existing array of vertices. 
   */
  public Poly3f(Vertex3f[] vertices, Material material) {
    this.vertices = vertices;
    this.material = material;
    
    faces = new ArrayList<>();
  }
  
  
  //////// Vertices
  
  
  public Vertex3f getVertex(int index) {
    return vertices[index];
  }
  
  
  //////// Faces
  
  /**
   * Adds a face to this polygon.   
   */
  public void addFace(Face3f face) {
    if(face.addToPoly(this)) {
      faces.add(face);
    }
  }
  
  
  /** Adds a face to this polygon. */
  public void addFace(int v1, int v2, int v3) {
    addFace(new Face3f((short) v1, (short) v2, (short) v3));
  }
  
  /** Adds 2 faces sharing an edge to this polygon. Conventient for making quad-like faces. */
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
  
  
  //////// Old GL Rendering
  
  /** Draws the engire polygon using a naiive opengl drawing approach. */
  public void render(GL gl) {
    GL2 gl2 = gl.getGL2();
    
    gl2.glBegin(GL_TRIANGLES);
    draw(gl2);
    gl2.glEnd();
  }
  
  public void draw(GL2 gl) {
    // bind the polygon's material to the OpenGL state.
    material.glMaterial(gl);
    
    // Draw each face.
    for(Face3f face : faces) {
      face.draw(gl);
    }
  }
  
  
  //////// VBO Rendering
  
  /** Returns the number of vertices making up this polygon. */
  public int getNumVertices() {
    return vertices.length; 
  }
  
  /** Returns the array of all the vertices in this polygon. */
  public Vertex3f[] getVertices() {
    return vertices;
  }
  
  /** Returns the number of faces making up this polygon. */
  public int getNumFaces() {
    return faces.size();
  }
  
  /** Returns the total number of vertex indices making up the faces of this polygon. */
  public int getNumIndices() {
    return faces.size()*3;
  }
  
  /** 
   * Causes the polygon to forget its cached vertex information so that it 
   * must be recomputed. 
   * Most polygons' vertices don't change though once the polygon is created, 
   * so for most cases, it is not necessary for a polygon to compute its
   * vertex information more than once. 
   */
  public void markDirty() {
    vertices = null;
    vboDirty = true;
  }
  
  
  // Buffers for VBO are cached for increased performance. 
  // Only repopulate the buffers if markDirty() is called on the polygon.
  private boolean vboDirty = true;
  
  /** The pointers to the VBOs. */
  private int[] buffers = null;
  
  /** The buffer containing the attributes for all the vertices. */
  private FloatBuffer megaBuffer = null;
  
  /** The buffer of vertex indices defining the faces. */
  private ShortBuffer elementBuffer = null;

  
  
  /** Generates and fills the VBOs. */
  private void genBuffers(GL2 gl) {
    if(vboDirty) {
      if(buffers != null) {
        gl.glDeleteBuffers(buffers.length, buffers, 0);
      }
      buffers = new int[2];
      
      // Fill the buffers.
      megaBuffer = FloatBuffer.allocate(vertices.length*13);
      elementBuffer = ShortBuffer.allocate(getNumIndices());
      
      for(Vertex3f vertex : vertices) {
        megaBuffer.put(vertex.getCoords());
        megaBuffer.put(vertex.getColor());
        megaBuffer.put(vertex.getNormal());
        megaBuffer.put(vertex.getTexCoords());
      }
      for(Face3f face : faces) {
        elementBuffer.put(new short[] {face.getIndex1(), face.getIndex2(), face.getIndex3()});
      }
      
      // Prepare the buffers to be read.
      megaBuffer.flip();
      elementBuffer.flip();
      
      // Bind the buffers.
      gl.glGenBuffers(buffers.length, buffers, 0);
      
      gl.glBindBuffer(GL_ARRAY_BUFFER, buffers[0]);
      gl.glBufferData(GL_ARRAY_BUFFER, 
                      megaBuffer.capacity()*Buffers.SIZEOF_FLOAT,
                      megaBuffer,
                      GL_STATIC_DRAW);
      
      gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffers[1]);
      gl.glBufferData(GL_ELEMENT_ARRAY_BUFFER, 
                      elementBuffer.capacity()*Buffers.SIZEOF_SHORT,
                      elementBuffer,
                      GL_STATIC_DRAW);
      
      vboDirty = false;
    }
  }
  
  
  
  
  /** Render the polygon using VBO. (Fast!)*/
  public void renderVBO(GL2 gl) {
    
    // bind the polygon's material to the OpenGL state.
    material.glMaterial(gl);
    
    // update the vertex information if necessary.
    genBuffers(gl);
    
    gl.glEnableClientState(GL_VERTEX_ARRAY);
    gl.glBindBuffer(GL_ARRAY_BUFFER, buffers[0]);
    gl.glVertexPointer(4, GL_FLOAT, 13*Buffers.SIZEOF_FLOAT, 0);
    
    gl.glEnableClientState(GL_COLOR_ARRAY);
    gl.glBindBuffer(GL_ARRAY_BUFFER, buffers[0]);
    gl.glColorPointer(4, GL_FLOAT, 13*Buffers.SIZEOF_FLOAT, 4*Buffers.SIZEOF_FLOAT);
    
    gl.glEnableClientState(GL_NORMAL_ARRAY);
    gl.glBindBuffer(GL_ARRAY_BUFFER, buffers[0]);
    gl.glNormalPointer(GL_FLOAT, 13*Buffers.SIZEOF_FLOAT, 8*Buffers.SIZEOF_FLOAT);
    
    gl.glEnableClientState(GL_TEXTURE_COORD_ARRAY);
    gl.glBindBuffer(GL_ARRAY_BUFFER, buffers[0]);
    gl.glTexCoordPointer(2, GL_FLOAT, 13*Buffers.SIZEOF_FLOAT, 11*Buffers.SIZEOF_FLOAT);
    
    // draw!
    gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffers[1]);
    gl.glDrawElements( GL_TRIANGLES, elementBuffer.capacity(), GL_UNSIGNED_SHORT, 0);
  //  gl.glFlush(); // SLOW!
    
    // disable arrays once we're done
    gl.glBindBuffer( GL.GL_ARRAY_BUFFER, 0 );
    gl.glBindBuffer( GL.GL_ELEMENT_ARRAY_BUFFER, 0 );
    gl.glDisableClientState( GL_VERTEX_ARRAY );
    gl.glDisableClientState( GL_COLOR_ARRAY );
    gl.glDisableClientState( GL_NORMAL_ARRAY );
    gl.glDisableClientState( GL_TEXTURE_COORD_ARRAY );
  }
  
} 

