package pwneegl.light;

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

import javax.media.opengl.GL2;

/** Represents an OpenGL light source. */
public abstract class Light {
  
  private float[] diffuse;
  private float[] specular;
  private float[] ambient;
  private float[] attenuation;
  
  
  /** Initializes the light. */
  public Light() {
    diffuse = new float[4];
    setDiffuse(0xFFFFFF);
    
    specular = new float[4];
    setSpecular(0xFFFFFF);
    
    ambient = new float[4];
    setAmbient(0x000000);
    
    attenuation = new float[3];
    attenuation[0] = 1f;
    attenuation[1] = 0f;
    attenuation[2] = 0f;
  }
  
  
  ////// Diffuse color
 
  /** Gets the diffuse rgba color array. */
  public float[] getDiffuse() {
    float[] result = new float[4];
    result[0] = diffuse[0];
    result[1] = diffuse[1];
    result[2] = diffuse[2];
    result[3] = diffuse[3];
    return result;
  }
  
  /** Sets the diffuse color for the light. */
  public void setDiffuse(float r, float g, float b, float a) {
    diffuse[0] = r;
    diffuse[1] = g;
    diffuse[2] = b;
    diffuse[3] = a;
  }
  
  public void setDiffuse(float r, float g, float b) {
    setDiffuse(r, g, b, 1f);
  }
  
  /** Sets the diffuse color for the light, given the color's rgb int value. */
  public void setDiffuse(int rgba, boolean hasAlpha) {
    float r = ((rgba & 0x00FF0000) >> 16)/255f;
    float g = ((rgba & 0x0000FF00) >> 8)/255f;
    float b = (rgba & 0x000000FF)/255f;
    float a = 1f;
    if(hasAlpha) {
      a = ((rgba & 0xFF000000) >> 24)/255f;
    }
    setDiffuse(r, g, b, a);
  }
  
  public void setDiffuse(int rgb) {
    setDiffuse(rgb, false);
  }
  
  
  
  
  
  //////// Specular color
  
  /** Gets the specular rgba color array. */
  public float[] getSpecular() {
    float[] result = new float[4];
    result[0] = specular[0];
    result[1] = specular[1];
    result[2] = specular[2];
    result[3] = specular[3];
    return result;
  }
  
  /** Sets the specular color for the light. */
  public void setSpecular(float r, float g, float b, float a) {
    specular[0] = r;
    specular[1] = g;
    specular[2] = b;
    specular[3] = a;
  }
  
  public void setSpecular(float r, float g, float b) {
    setSpecular(r, g, b, 1f);
  }
  
  /** Sets the specular color for the light, given the color's rgb int value. */
  public void setSpecular(int rgba, boolean hasAlpha) {
    float r = ((rgba & 0x00FF0000) >> 16)/255f;
    float g = ((rgba & 0x0000FF00) >> 8)/255f;
    float b = (rgba & 0x000000FF)/255f;
    float a = 1f;
    if(hasAlpha) {
      a = ((rgba & 0xFF000000) >> 24)/255f;
    }
    setSpecular(r, g, b, a);
  }
  
  public void setSpecular(int rgb) {
    setSpecular(rgb, false);
  }
  
  
  
  //////// Ambient color
  
  /** Gets the ambient rgba color array. */
  public float[] getAmbient() {
    float[] result = new float[3];
    result[0] = ambient[0];
    result[1] = ambient[1];
    result[2] = ambient[2];
    return result;
  }
  
  /** Sets the ambient color for the light. */
  public void setAmbient(float r, float g, float b, float a) {
    ambient[0] = r;
    ambient[1] = g;
    ambient[2] = b;
    ambient[3] = a;
  }
  
  public void setAmbient(float r, float g, float b) {
    setAmbient(r, g, b, 1f);
  }
  
  /** Sets the ambient color for the light, given the color's rgb int value. */
  public void setAmbient(int rgba, boolean hasAlpha) {
    float r = ((rgba & 0x00FF0000) >> 16)/255f;
    float g = ((rgba & 0x0000FF00) >> 8)/255f;
    float b = (rgba & 0x000000FF)/255f;
    float a = 1f;
    if(hasAlpha) {
      a = ((rgba & 0xFF000000) >> 24)/255f;
    }
    setAmbient(r, g, b, a);
  }
  
  public void setAmbient(int rgb) {
    setAmbient(rgb, false);
  }
  
  
  
  
  
  //////// Attenuation
  
  /** Get the attenuation coefficients for the light. */
  public float[] getAttenuation() {
    float[] result = new float[3];
    result[0] = attenuation[0];
    result[1] = attenuation[1];
    result[2] = attenuation[2];
    return result;
  }
  
  /** Set the attenuation coefficients for the light. */
  public void setAttenuation(float constant, float linear, float quadratic) {
    attenuation[0] = constant;
    attenuation[1] = linear;
    attenuation[2] = quadratic;
  }
  
  
  ////////
  
  /** 
   * The light applies itself to the scene using the OpenGL lighting api. 
   * (This only supports 8 lights though, so it would be a better idea to 
   * implement the lights in a shader program. TODO)
   */
  public abstract void glLight(GL2 gl, int index);
  
  
}
