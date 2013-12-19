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

import java.util.ArrayList;
import java.util.List;

import pwneegl.geom.Face3f;
import pwneegl.geom.Vertex3f;
import pwneegl.material.Material;
import pwneegl.math.PwneeMath;


/** Produces a variety of basic shapes. */
public class Shapes {


  /** Produces a 2-unit cube. */
  public static Poly3f makeCube() {
    
    // Create all the vertices.
    Vertex3f[] vertices = new Vertex3f[] {
        // top (green)
        new Vertex3f( 1f,  1f, -1f),
        new Vertex3f(-1f,  1f, -1f),
        new Vertex3f(-1f,  1f,  1f),
        new Vertex3f( 1f,  1f,  1f),
         
        // bottom (orange)
        new Vertex3f( 1f, -1f,  1f),
        new Vertex3f(-1f, -1f,  1f),
        new Vertex3f(-1f, -1f, -1f),
        new Vertex3f( 1f, -1f, -1f),
         
        // front (red)
        new Vertex3f( 1f,  1f,  1f),
        new Vertex3f(-1f,  1f,  1f),
        new Vertex3f(-1f, -1f,  1f),
        new Vertex3f( 1f, -1f,  1f),
         
        // back (yellow)
        new Vertex3f(-1f,  1f, -1f),
        new Vertex3f( 1f,  1f, -1f),
        new Vertex3f( 1f, -1f, -1f),
        new Vertex3f(-1f, -1f, -1f),
        
        // left (blue)
        new Vertex3f(-1f,  1f,  1f),
        new Vertex3f(-1f,  1f, -1f),
        new Vertex3f(-1f, -1f, -1f),
        new Vertex3f(-1f, -1f,  1f),
        
        // right (magenta)
        new Vertex3f( 1f,  1f, -1f),
        new Vertex3f( 1f,  1f,  1f),
        new Vertex3f( 1f, -1f,  1f),
        new Vertex3f( 1f, -1f, -1f)
    };
    
    // set the colors for vertices on each side of the cube. 
    // top
    int green = 0xFF00FF00;
    vertices[0].setColor(green, true);
    vertices[1].setColor(green, true);
    vertices[2].setColor(green, true);
    vertices[3].setColor(green, true);
    
    // bottom
    int orange = 0xFFFF8800;
    vertices[4].setColor(orange, true);
    vertices[5].setColor(orange, true);
    vertices[6].setColor(orange, true);
    vertices[7].setColor(orange, true);
    
    // front
    int red = 0xFFFF0000;
    vertices[8].setColor(red, true);
    vertices[9].setColor(red, true);
    vertices[10].setColor(red, true);
    vertices[11].setColor(red, true);
    
    // back
    int yellow = 0xFFFFFF00;
    vertices[12].setColor(yellow, true);
    vertices[13].setColor(yellow, true);
    vertices[14].setColor(yellow, true);
    vertices[15].setColor(yellow, true);
    
    // left
    int blue = 0xFF0000FF;
    vertices[16].setColor(blue, true);
    vertices[17].setColor(blue, true);
    vertices[18].setColor(blue, true);
    vertices[19].setColor(blue, true);
    
    // right
    int magenta = 0xFFFF00FF;
    vertices[20].setColor(magenta, true);
    vertices[21].setColor(magenta, true);
    vertices[22].setColor(magenta, true);
    vertices[23].setColor(magenta, true);
    
    
    // Set the surface normals for vertices on each side of the cube.
    // top
    vertices[0].setNormal( 0f,  1f,  0f);
    vertices[1].setNormal( 0f,  1f,  0f);
    vertices[2].setNormal( 0f,  1f,  0f);
    vertices[3].setNormal( 0f,  1f,  0f);
    
    // bottom
    vertices[4].setNormal( 0f, -1f,  0f);
    vertices[5].setNormal( 0f, -1f,  0f);
    vertices[6].setNormal( 0f, -1f,  0f);
    vertices[7].setNormal( 0f, -1f,  0f);
    
    // front
    vertices[8].setNormal(  0f,  0f,  1f);
    vertices[9].setNormal(  0f,  0f,  1f);
    vertices[10].setNormal( 0f,  0f,  1f);
    vertices[11].setNormal( 0f,  0f,  1f);
    
    // back
    vertices[12].setNormal( 0f,  0f, -1f);
    vertices[13].setNormal( 0f,  0f, -1f);
    vertices[14].setNormal( 0f,  0f, -1f);
    vertices[15].setNormal( 0f,  0f, -1f);
    
    // left
    vertices[16].setNormal(-1f,  0f,  0f);
    vertices[17].setNormal(-1f,  0f,  0f);
    vertices[18].setNormal(-1f,  0f,  0f);
    vertices[19].setNormal(-1f,  0f,  0f);
    
    // right
    vertices[20].setNormal( 1f,  0f,  0f);
    vertices[21].setNormal( 1f,  0f,  0f);
    vertices[22].setNormal( 1f,  0f,  0f);
    vertices[23].setNormal( 1f,  0f,  0f);
    
    
    // Set the texture coordinates for the vertices on each side of the cube.
    vertices[0].setTexCoords(1f, 0f);
    vertices[1].setTexCoords(0f, 0f);
    vertices[2].setTexCoords(0f, 1f);
    vertices[3].setTexCoords(1f, 1f);
    
    vertices[4].setTexCoords(1f, 0f);
    vertices[5].setTexCoords(0f, 0f);
    vertices[6].setTexCoords(0f, 1f);
    vertices[7].setTexCoords(1f, 1f);
    
    vertices[8].setTexCoords(1f, 0f);
    vertices[9].setTexCoords(0f, 0f);
    vertices[10].setTexCoords(0f, 1f);
    vertices[11].setTexCoords(1f, 1f);
    
    vertices[12].setTexCoords(1f, 0f);
    vertices[13].setTexCoords(0f, 0f);
    vertices[14].setTexCoords(0f, 1f);
    vertices[15].setTexCoords(1f, 1f);
    
    vertices[16].setTexCoords(1f, 0f);
    vertices[17].setTexCoords(0f, 0f);
    vertices[18].setTexCoords(0f, 1f);
    vertices[19].setTexCoords(1f, 1f);
    
    vertices[20].setTexCoords(1f, 0f);
    vertices[21].setTexCoords(0f, 0f);
    vertices[22].setTexCoords(0f, 1f);
    vertices[23].setTexCoords(1f, 1f);
    
    
    // Create the polygon and its faces.
    Poly3f result = new Poly3f(vertices);
    result.addFace(0, 1, 2, 3);
    result.addFace(4, 5, 6, 7);
    result.addFace(8, 9, 10, 11);
    result.addFace(12, 13, 14, 15);
    result.addFace(16, 17, 18, 19);
    result.addFace(20, 21, 22, 23);
    return result;
  }
  
  
  /** 
   * Creates a new approximation of a unit sphere. 
   * @param latSides    The number of latitude sides for the sphere.
   * @param lonSides    The number of longitude sides for the sphere.
   * @return            A Poly3f approximating a sphere.
   */
  public static Poly3f makeSphere(int latSides, int lonSides) {
    float topLat = PwneeMath.TAU/4;
    float bottomLat = 0-PwneeMath.TAU/4;
    float leftLon = 0f;
    float rightLon = PwneeMath.TAU;
    
    float latInc = PwneeMath.TAU/2/latSides;
    float lonInc = PwneeMath.TAU/lonSides;
    
    
    // Create all the vertices for our "sphere".
    int rows = lonSides+1;
    int cols = latSides+1;
    int numVertices = rows*cols;
    Vertex3f[] vertices = new Vertex3f[numVertices];
    
    float lat = bottomLat;
    for(int row = 0; row < rows; row++) {
      if(row == rows-1) {
        lat = topLat;
      }
      
      float lon = leftLon;
      for(int col = 0; col < cols; col++) {
        if(col == cols-1) {
          lon = rightLon;
        }
        
        float[] xyz = PwneeMath.toXYZ(1, lat, lon);
        Vertex3f v = new Vertex3f(xyz[0], xyz[1], xyz[2]);
        v.setNormal(xyz);
        v.setTexCoords(col/(0f + cols-1), 1 - row/(0f + rows-1));
        
        vertices[col + row*cols] = v;
        
        lon += lonInc;
      }
      
      lat += latInc;
    }
    
    // Build the polygon and its faces.
    Poly3f result = new Poly3f(vertices);
    
    for(int row = 0; row < rows-1; row++) {
      for(int col = 0; col < cols-1; col++) {
        
        int v1 = col + row*cols;
        int v2 = col + 1 + row*cols;
        int v3 = col + 1 + (row+1)*cols;
        int v4 = col + (row+1)*cols;
        
        result.addFace(v1, v2, v3, v4);
      }
    }
    
    return result;
  }
  
  
  /** Produces an approximation of a cylinder, with a height of 2 units and a radius of 2 units.*/
  public static Poly3f makeCylinder(int lonSides) {
    float lonInc = PwneeMath.TAU/lonSides;
    float leftLon = 0f;
    float rightLon = PwneeMath.TAU;
    
    int cols = lonSides+1;
    Vertex3f[] vertices = new Vertex3f[cols*4];
    float lon;
    
    // top disc
    lon = leftLon;
    for(int col = 0; col < cols; col++) {
      if(col == cols-1) {
        lon = rightLon;
      }
      
      float[] xyz = PwneeMath.toXYZ(1, 0, lon);
      Vertex3f v = new Vertex3f(xyz[0], 1, xyz[2]);
      v.setNormal(new float[] {0, 1, 0});
      v.setTexCoords(0.5f + xyz[0], 0.5f + xyz[2]/2f);
      
      vertices[col] = v;
      
      lon += lonInc;
    }
    
    // middle
    lon = leftLon;
    for(int col = 0; col < cols; col++) {
      if(col == cols-1) {
        lon = rightLon;
      }
      
      float[] xyz = PwneeMath.toXYZ(1, 0, lon);
      float[] normal = new float[] {xyz[0], 0, xyz[2]};
      
      // upper
      Vertex3f v = new Vertex3f(xyz[0], 1, xyz[2]);
      v.setNormal(normal);
      v.setTexCoords(col/(cols - 1f), 0);
      vertices[col+1*cols] = v;
      
      // lower
      v = new Vertex3f(xyz[0], -1, xyz[2]);
      v.setNormal(normal);
      v.setTexCoords(col/(cols - 1f), 1);
      vertices[col+2*cols] = v;
      
      lon += lonInc;
    }
    
    
    // bottom disc
    lon = leftLon;
    for(int col = 0; col < cols; col++) {
      if(col == cols-1) {
        lon = rightLon;
      }
      
      float[] xyz = PwneeMath.toXYZ(1, 0, lon);
      Vertex3f v = new Vertex3f(xyz[0], -1, xyz[2]);
      v.setNormal(new float[] {0, -1, 0});
      v.setTexCoords(0.5f + xyz[0], 0.5f - xyz[2]/2f);
      
      vertices[col + 3*cols] = v;
      
      lon += lonInc;
    }
    
    // Build the polygon and its faces.
    Poly3f result = new Poly3f(vertices);
    
    // top
    int[] topVs = new int[cols];
    for(int col = 0; col < cols; col++) {
      topVs[col] = col;
    }
    result.addFaceFan(topVs);
    
    // middle
    for(int col = 0; col < cols-1; col++) {
      int v1 = col + 2*cols;
      int v2 = (col + 1) + 2*cols;
      int v3 = (col + 1) + 1*cols;
      int v4 = col + 1*cols;
      result.addFace(v1, v2, v3, v4);
    }
    
    // top
    int[] bottomVs = new int[cols];
    for(int col = 0; col < cols; col++) {
      bottomVs[cols-col-1] = col + 3*cols;
    }
    result.addFaceFan(bottomVs);
    
    return result;
  }
  
  
  
  /** Creates a rectangle of the specified dimensions. */
  public static Poly3f makeRect(float w, float h) {
    float left = 0f - w/2f;
    float top = h/2f;
    float right = w/2f;
    float bottom = 0f - h/2f;
    
    Vertex3f[] vertices = new Vertex3f[8];
    Vertex3f v;
    
    // front 
    float[] frontNormal = new float[] {0f, 0f, 1f};
    
    v = new Vertex3f(left, top, 0f);
    v.setNormal(frontNormal);
    v.setTexCoords(0,0);
    vertices[0] = v;
    
    v = new Vertex3f(left, bottom, 0f);
    v.setNormal(frontNormal);
    v.setTexCoords(0,1);
    vertices[1] = v;
    
    v = new Vertex3f(right, bottom, 0f);
    v.setNormal(frontNormal);
    v.setTexCoords(1,1);
    vertices[2] = v;
    
    v = new Vertex3f(right, top, 0f);
    v.setNormal(frontNormal);
    v.setTexCoords(1,0);
    vertices[3] = v;
    
    // back 
    float[] backNormal = new float[] {0f, 0f, -1f};
    
    v = new Vertex3f(left, top, 0f);
    v.setNormal(backNormal);
    v.setTexCoords(1,0);
    vertices[4] = v;
    
    v = new Vertex3f(left, bottom, 0f);
    v.setNormal(backNormal);
    v.setTexCoords(1,1);
    vertices[5] = v;
    
    v = new Vertex3f(right, bottom, 0f);
    v.setNormal(backNormal);
    v.setTexCoords(0,1);
    vertices[6] = v;
    
    v = new Vertex3f(right, top, 0f);
    v.setNormal(backNormal);
    v.setTexCoords(0,0);
    vertices[7] = v;
    
    // The polygon and its faces.
    Poly3f result = new Poly3f(vertices);
    result.addFace(0, 1, 2, 3);
    result.addFace(4, 7, 6, 5);
    
    return result;
  }
}
