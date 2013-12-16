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
   * @param color       The argb value for the color.
   * @return            A Poly3f approximating a sphere.
   */
  public static Poly3f makeSphere(int latSides, int lonSides, int color) {
    float topLat = PwneeMath.TAU/4;
    float bottomLat = 0-PwneeMath.TAU/4;
    
    float latInc = PwneeMath.TAU/2/latSides;
    float lonInc = PwneeMath.TAU/lonSides;
    
    return null;
  }
  
  
  /** Produces an approximation of a cylinder, with a height of 2 units and a radius of 2 units.*/
  public static Poly3f makeCylinder(int lonSides, int color) {
    float lonInc = PwneeMath.TAU/lonSides;
    
    return null;
  }
  
  
  
  /** Creates a rectangle of the specified dimensions and color. */
  public static Poly3f makeRect(float w, float h, int color) {
    float left = 0f - w/2f;
    float top = h/2f;
    float right = w/2f;
    float bottom = 0f - h/2f;
    
    return null;
  }
}
