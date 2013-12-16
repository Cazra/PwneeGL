package pwneegl.material;

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

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import static javax.media.opengl.GL.*;  // GL constants
import static javax.media.opengl.GL2.*; // GL2 constants

import pwneegl.math.PwneeMath;

/** Defines a colored surface material. */
public class Material {
  
  /** 
   * The ambient color rgba array for the material. 
   */
  private float[] ambient;
  
  /** 
   * The diffuse color rgba array for the material.
   */
   private float[] diffuse;
  
  /** 
   * The specular color rgba array for the material. 
   */
   private float[] specular;
  
  /** 
   * The shininess property for the material. This is the exponent for the 
   * specular component of the lighting computation. 
   */
   private float shininess;
  
  /** 
   * The emissive color rgba array of the material. This color is always added  
   * to the lighting computation, without being scaled by some light 
   * coefficient. It can be used to produce a glowing effect.
   */
  private float[] emission;
  
  /** Defines how the material is applied to the front and back surfaces of objects. */
  private int frontBack;
  
  /** 
   * Creates a material with the given diffuse color. 
   * The specular and ambient colors are given the default white values
   * {1f, 1f, 1f, 1f}. 
   * The emission color is, by default, transparent black {0f, 0f, 0f, 0f}.
   * The material is given the default shininess property 1f.
   */
  public Material(float[] diffuseRGBA) {
    this(diffuseRGBA[0], diffuseRGBA[1], diffuseRGBA[2], diffuseRGBA[3]);
  }
  
  public Material(float r, float g, float b, float a) {
    init(r, g, b, a);
  }
  
  public Material(float r, float g, float b) {
    this(new float[] {r, g, b, 1f});
  }
  
  
  public Material(int argb, boolean hasAlpha) {
    float r = ((argb >> 16) & 0x000000FF)/255f;
    float g = ((argb >> 8) & 0x000000FF)/255f;
    float b = (argb & 0x000000FF)/255f;
    float a = 1f;
    if(hasAlpha) {
      a = ((argb >> 24)& 0x000000FF)/255f;
    }
    
    init(r, g, b, a);
  }
  
  
  public Material(int rgb) {
    this(rgb, false);
  }
  
  private void init(float r, float g, float b, float a) {
    this.diffuse = new float[4];
    this.diffuse[0] = r;
    this.diffuse[1] = g;
    this.diffuse[2] = b;
    this.diffuse[3] = a;
    
    this.ambient = new float[] {1f,1f,1f,1f};
    this.specular = new float[] {1f,1f,1f,1f};
    this.shininess = 1f;
    this.emission = new float[] {0f,0f,0f,0f};
  }
  
  //////// Diffuse coloring
  
  /** 
   * Get the diffuse color rgba array for the material. This defines the main
   * color of the material.
   */
  public float[] getDiffuse() {
    return diffuse;
  }
  
  /** Sets the diffuse color of the material. */
  public void setDiffuse(float[] rgba) {
    this.diffuse[0] = rgba[0];
    this.diffuse[1] = rgba[1];
    this.diffuse[2] = rgba[2];
    this.diffuse[3] = rgba[3];
  }
  
  /** Sets the diffuse color of the material. */
  public void setDiffuse(float r, float g, float b, float a) {
    this.diffuse[0] = r;
    this.diffuse[1] = g;
    this.diffuse[2] = b;
    this.diffuse[3] = a;
  }
  
  
  //////// Specular coloring
  
  /** 
   * Get the specular color rgba array for the material. 
   * This defines the color of the shiny parts of the material.
   */
  public float[] getSpecular() {
    return specular;
  }
  
  /** Sets the specular color of the material. */
  public void setSpecular(float[] rgba) {
    this.specular[0] = rgba[0];
    this.specular[1] = rgba[1];
    this.specular[2] = rgba[2];
    this.specular[3] = rgba[3];
  }
  
  /** Sets the specular color of the material. */
  public void setSpecular(float r, float g, float b, float a) {
    this.specular[0] = r;
    this.specular[1] = g;
    this.specular[2] = b;
    this.specular[3] = a;
  }
  
  /** Returns the shininess exponent property of the material. */
  public float getShininess() {
    return shininess;
  }
  
  /** Sets the shininess property of this material to a value in the range [1, 100]. */
  public void setShininess(float shininess) {
    this.shininess = shininess;
  }
  
  
  //////// Ambient coloring
  
  /** 
   * Get the ambient color rgba array for the material. 
   * This defines how the material is affected by ambient lighting.
   */
  public float[] getAmbient() {
    return ambient;
  }
  
  /** Sets the ambient color of the material. */
  public void setAmbient(float[] rgba) {
    this.ambient[0] = rgba[0];
    this.ambient[1] = rgba[1];
    this.ambient[2] = rgba[2];
    this.ambient[3] = rgba[3];
  }
  
  /** Sets the ambient color of the material. */
  public void setAmbient(float r, float g, float b, float a) {
    this.ambient[0] = r;
    this.ambient[1] = g;
    this.ambient[2] = b;
    this.ambient[3] = a;
  }
  
  //////// Emission coloring
  
  /** 
   * Get the emission color rgba array for the material.
   * This causes the material to glow by adding the emission components to 
   * the lighting computations. This doesn't actually produce any light though, 
   * so it won't affect the shading of other objects.
   */
   public float[] getEmission() {
    return emission;
   }
   
   /** Sets the emission color of the material. */
  public void setEmission(float[] rgba) {
    this.emission[0] = rgba[0];
    this.emission[1] = rgba[1];
    this.emission[2] = rgba[2];
    this.emission[3] = rgba[3];
  }
  
  /** Sets the emission color of the material. */
  public void setEmission(float r, float g, float b, float a) {
    this.emission[0] = r;
    this.emission[1] = g;
    this.emission[2] = b;
    this.emission[3] = a;
  }
  
  
  
  //////// Rendering
  
  /** Applies this material to the OpenGL state. */
  public void glMaterial(GL2 gl) {
    
    gl.glDisable(GL_TEXTURE_2D);
    gl.glMaterialfv(GL_FRONT, GL_AMBIENT, ambient, 0);
    gl.glMaterialfv(GL_FRONT, GL_DIFFUSE, diffuse, 0);
    gl.glMaterialfv(GL_FRONT, GL_SPECULAR, specular, 0);
    gl.glMaterialf(GL_FRONT, GL_SHININESS, shininess);
    gl.glMaterialfv(GL_FRONT, GL_EMISSION, emission, 0);
    
  }
}

