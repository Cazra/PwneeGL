package pwneegl.shader;

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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import static javax.media.opengl.GL.*;  // GL constants
import static javax.media.opengl.GL2.*; // GL2 constants

import pwneegl.GLNames;
import pwneegl.PwneeGLError;

/** 
 * Reads and compiles a shader program which can then be used for customized rendering. 
 * If for some reason, it fails to create the shader program, an Exception will
 * be printed with debug information which will (hopefully) provide some insight
 * into the problem.
 */
public class ShaderProgram {
  
  /** Whether to print debugging information about the shader to the console. */
  public static boolean printDebug = true;
  
  
  /** The path to the loaded vertex shader. */
  private String vertexShaderPath;
  
  /** The path to the loaded fragment shader. */
  private String fragmentShaderPath;
  
  /** The index to the vertex shader in the OpenGL state. */
  private int vShader;
  
  /** The index to the fragment shader in the OpenGL state. */
  private int fShader;
  
  /** The index to the shader program object in the OpenGL state. */
  private int shaderProgram;
  
  
  /** Dictionary of vertex attributes used in the shader. */
  private Map<String, Attribute> attributes;
  
  /** Dictionary of uniform variables used in the shader. */
  private Map<String, Uniform> uniforms;
  
  /** The number of bytes custom float-based vertex attributes in the shader contribute to the pipeline. */
  private int attribsfBytes;
  
  /** The number of bytes custom int-based vertex attributes in the shader contribute to the pipeline. */
  private int attribsiBytes;
  
  /** The number of bytes custom double-based vertex attributes in the shader contribute to the pipeline. */
  private int attribsdBytes;
  
  
  /** 
   * The constructor will read the source files for the shader program and 
   * initialize them in the OpenGL context. 
   */
  public ShaderProgram(GL2 gl, String vertexShaderPath, String fragmentShaderPath, boolean loadAsResources) {
    this.vertexShaderPath = vertexShaderPath;
    this.fragmentShaderPath = fragmentShaderPath;
    
    try {
      vShader = compileShader(gl, GL_VERTEX_SHADER, vertexShaderPath, loadAsResources);
      fShader = compileShader(gl, GL_FRAGMENT_SHADER, fragmentShaderPath, loadAsResources);
      shaderProgram = gl.glCreateProgram();
      
      glAttachShaders(gl);
      glLinkProgram(gl);
      glValidateProgram(gl);
      
      initAttribs(gl);
      initUniforms(gl);
      
      // Once the program is built, we can delete the shaders to save memory.
      // The compiled shaders won't actually be deleted until the program is deleted. 
      gl.glDeleteShader(vShader);
      gl.glDeleteShader(fShader);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  /** Read the shader program from the specified external files. */
  public ShaderProgram(GL2 gl, String vertexShaderPath, String fragmentShaderPath) {
    this(gl, vertexShaderPath, fragmentShaderPath, false);
  }  
  
  
  
  /** Reads and compiles the source for a vertex or fragment shader. */
  private int compileShader(GL2 gl, int glShaderType, String path, boolean loadAsResource) throws Exception {
  
    // Read the source into a String.
    BufferedReader br;
    if(loadAsResource) {
      InputStream is = getClass().getClassLoader().getResourceAsStream(path);
      br = new BufferedReader(new InputStreamReader(is));
    }
    else {
      br = new BufferedReader(new FileReader(path));
      
    }
    
    String shaderContents = "";
    String line = br.readLine();
    while(line != null) {
      shaderContents += line + "\n";
      line = br.readLine();
    }
    
    br.close();
    
    // Construct and compile the shader.
    int shader = gl.glCreateShader(glShaderType);
    gl.glShaderSource(shader, 1, new String[] {shaderContents}, new int[] {shaderContents.length()}, 0); 
    gl.glCompileShader(shader);
    
    // If there were any compile errors, throw a PwneeGLError with debug information.
    int status = glGetShaderi(gl, shader, GL_COMPILE_STATUS);
    if(status != GL_TRUE) {
      String msg = glGetShaderInfoLog(gl, shader);
      throw new PwneeGLError(msg);
    }
    
    return shader;
  }
  
  
  
  /** 
   * Attempts to attach the vertex and fragment shaders to the shader program object. 
   */
  private void glAttachShaders(GL2 gl) {
    gl.glAttachShader(shaderProgram, vShader);
    gl.glAttachShader(shaderProgram, fShader);
  }
  
  
  /** Attempts to link the shader program object. An PwneeGLError is thrown if it fails. */
  private void glLinkProgram(GL2 gl) {
    gl.glLinkProgram(shaderProgram);
    
    int status = glGetProgrami(gl, GL_LINK_STATUS);
    if(status != GL_TRUE) {
      throw new PwneeGLError("Failed to link the shader program object.");
    }
  }
  
  
  /** Attempts to validate the shader program object. A PwneeGLError is thrown if it fails. */
  private void glValidateProgram(GL2 gl) {
    gl.glValidateProgram(shaderProgram);
      
    // If the program failed to validate, throw an PwneeGLError containing 
    // information about why it failed.
    int status = glGetProgrami(gl, GL_VALIDATE_STATUS);
    if(status != GL_TRUE) {
      String msg = glGetProgramInfoLog(gl);
      throw new PwneeGLError(msg);
    }
  }
  
  /** Returns a parameter for a shader. */
  private int glGetShaderi(GL2 gl, int shader, int pname) {
    int[] params = new int[1];
    gl.glGetShaderiv(shader, pname, params, 0);
    return params[0];
  }
  
  /** Returns the information log for a shader. */
  private String glGetShaderInfoLog(GL2 gl, int shader) {
    int maxLength = glGetShaderi(gl, shader, GL_INFO_LOG_LENGTH);
    int[] length = new int[1];
    byte[] data = new byte[maxLength];
    gl.glGetShaderInfoLog(shader, maxLength, length, 0, data, 0);
    return new String(data);
  }
  
  
  /** Populates the vertex attribute information for this program. */
  private void initAttribs(GL2 gl) {
    int numAttribs = glGetProgrami(gl, GL_ACTIVE_ATTRIBUTES);
    debugln("Number active attributes: " + numAttribs);
    
    int maxLength = glGetProgrami(gl, GL_ACTIVE_ATTRIBUTE_MAX_LENGTH);
    debugln("Max attribute name length: " + maxLength);
    
    attributes = new HashMap<>();
    attribsfBytes = 0;
    attribsiBytes = 0;
    attribsdBytes = 0;
    
    // Create objects to cache the information about the attributes. 
    for(int i = 0; i < numAttribs; i++) {
      int lengthPointer[] = new int[1];
      int sizePointer[] = new int[1];
      int typePointer[] = new int[1];
      byte nameBytes[] = new byte[maxLength];
      gl.glGetActiveAttrib(shaderProgram, i, maxLength, 
                            lengthPointer, 0, 
                            sizePointer, 0, 
                            typePointer, 0, 
                            nameBytes, 0);
      int length = lengthPointer[0];
      int size = sizePointer[0];
      int type = typePointer[0];
      String name = new String(nameBytes).trim();
      int location = gl.glGetAttribLocation(shaderProgram, name);
      
      debugln("Attrib " + i + ": " + name + ", length " + length + ", size " + size + ", type " + GLNames.glName(type) + ", location " + location);
      Attribute att = new Attribute(name, size, type, location);
      attributes.put(name, att);
      
      // Add up the number of bytes each user-defined attribute contributes to the pipeline.
      if(att.isUserDefined()) {
        if(att.getUnitType() == GL_FLOAT) {
          attribsfBytes += att.getSizeBytes();
        }
        else if(att.getUnitType() == GL_INT) {
          attribsiBytes += att.getSizeBytes();
        }
        else if(att.getUnitType() == GL_DOUBLE){
          attribsdBytes += att.getSizeBytes();
        }
      }
    }
  }
  
  
  
  
  /** Populates the uniform variables information for this program. */
  private void initUniforms(GL2 gl) {
    int numUnis = glGetProgrami(gl, GL_ACTIVE_UNIFORMS);
    debugln("Number active uniforms: " + numUnis);
    
    int maxLength = glGetProgrami(gl, GL_ACTIVE_UNIFORM_MAX_LENGTH);
    debugln("Max uniform name length: " + maxLength);
    
    uniforms = new HashMap<>();
    
    // Create objects to cache the information about the uniforms.
    for(int i = 0; i < numUnis; i++) {
      int lengthPointer[] = new int[1];
      int sizePointer[] = new int[1];
      int typePointer[] = new int[1];
      byte nameBytes[] = new byte[maxLength];
      gl.glGetActiveUniform(shaderProgram, i, maxLength, 
                            lengthPointer, 0, 
                            sizePointer, 0, 
                            typePointer, 0, 
                            nameBytes, 0);
      int length = lengthPointer[0];
      int size = sizePointer[0];
      int type = typePointer[0];
      String name = new String(nameBytes).trim();
      
      debugln("Uniform " + i + ": " + name + ", length " + length + ", size " + size + ", type " + GLNames.glName(type));
      uniforms.put(name, new Uniform(name, size, type));
    }
  }
  
  /** 
   * Returns the number of bytes that user-defined float-based vertex
   * attributes are expected to contribute to the stride of the pipeline.
   */
  public int getPipelineStridef() {
    return attribsfBytes;
  }
  
  /** 
   * Returns the number of bytes that user-defined int-based vertex
   * attributes are expected to contribute to the stride of the pipeline.
   */
  public int getPipelineStridei() {
    return attribsiBytes;
  }
  
  /** 
   * Returns the number of bytes that user-defined double-based vertex
   * attributes are expected to contribute to the stride of the pipeline.
   */
  public int getPipelineStrided() {
    return attribsdBytes;
  }
  
  
  /** Removes the shader program from graphics memory. */
  public void clean(GL2 gl) {
    gl.glDeleteProgram(shaderProgram);
  }
  
  
  //////// Debug info
  
  /** Returns a parameter for the program object. */
  public int glGetProgrami(GL2 gl, int pname) {
    int[] params = new int[1];
    gl.glGetProgramiv(shaderProgram, pname, params, 0);
    return params[0];
  }
  
  /** Returns the information log for a program object. */
  public String glGetProgramInfoLog(GL2 gl) {
    int maxLength = glGetProgrami(gl, GL_INFO_LOG_LENGTH);
    int[] length = new int[1];
    byte[] data = new byte[maxLength];
    gl.glGetProgramInfoLog(shaderProgram, maxLength, length, 0, data, 0);
    return new String(data);
  }
  
  
  public void debugln(String str) {
    if(printDebug) {
      System.err.println(str);
    }
  }
  
  
  //////// Vertex Attributes
  
  
  /** Returns the index to a vertex attribute specified in the shader program. */
  public int getAttribLocation(GL2 gl, String name) {
    return gl.glGetAttribLocation(shaderProgram, name);
  }
  
  
  public int getAttribLocation(String name) {
    Attribute att = attributes.get(name);
    if(att != null) {
      return att.getLocation();
    }
    else {
      return 0;
    }
  }
  
  
  /** Returns information about a vertex attribute used by this shader. */
  public Attribute getAttrib(String name) {
    Attribute result = attributes.get(name);
    if(result == null) {
      throw new PwneeGLError("Vertex attribute " + name + " isn't used in this shader.");
    }
    return attributes.get(name);
  }
  
  
  /** Return true iff the manager has the specified attribute. */
  public boolean hasAttribute(String name) {
    return attributes.containsKey(name);
  }
  
  
  /** Returns the list of user-defined vertex attributes for this shader. */
  public List<Attribute> getUserAttribs() {
    List<Attribute> result = new ArrayList<>();
    for(Attribute att : attributes.values()) {
      if(att.isUserDefined()) {
        result.add(att);
      }
    }
    return result;
  }
  
  /** Returns the list of user-defined float-based attributes for this shader. */
  public List<Attribute> getUserAttribsf() {
    List<Attribute> result = new ArrayList<>();
    for(Attribute att : attributes.values()) {
      if(att.isUserDefined() && att.getUnitType() == GL_FLOAT) {
        result.add(att);
      }
    }
    return result;
  }
  
  /** Returns the list of user-defined int-based attributes for this shader. */
  public List<Attribute> getUserAttribsi() {
    List<Attribute> result = new ArrayList<>();
    for(Attribute att : attributes.values()) {
      if(att.isUserDefined() && (att.getUnitType() == GL_INT || att.getUnitType() == GL_UNSIGNED_INT)) {
        result.add(att);
      }
    }
    return result;
  }
  
  /** Returns the list of user-defined double-based attributes for this shader. */
  public List<Attribute> getUserAttribsd() {
    List<Attribute> result = new ArrayList<>();
    for(Attribute att : attributes.values()) {
      if(att.isUserDefined() && att.getUnitType() == GL_DOUBLE) {
        result.add(att);
      }
    }
    return result;
  }
  
  
  
  
  //////// Uniform variables
  
  /** Returns the index to a uniform attribute specified in the shader program. */
  public int getUniformLocation(GL2 gl, String name) {
    return gl.glGetUniformLocation(shaderProgram, name);
  }
  
  
  
  /** Returns information about a uniform variable used by this shader. */
  public Uniform getUniform(String name) {
    Uniform result = uniforms.get(name);
    if(result == null) {
      throw new PwneeGLError("Uniform variable " + name + " isn't used in this shader.");
    }
    return uniforms.get(name);
  }
  
  
  /** Returns the list of user-defined uniform variables for this shader. */
  public List<Uniform> getCustomUniforms() {
    List<Uniform> result = new ArrayList<>();
    for(Uniform uni : uniforms.values()) {
      if(uni.isUserDefined()) {
        result.add(uni);
      }
    }
    return result;
  }
  
  
  
  /** Sets the value for some uniform float variable in the shader. */
  public void setUniformf(GL2 gl, String name, float value) {
    int uniLoc = getUniformLocation(gl, name);
    gl.glUniform1f(uniLoc, value);
  }
  
  /** Sets the value for some uniform float variable in the shader. */
  public void setUniformfv(GL2 gl, String name, float[] values) {
    int size = values.length;
    int loc = getUniformLocation(gl, name);
    if(size == 1) {
      gl.glUniform1fv(loc, 1, values, 0);
    }
    else if(size == 2) {
      gl.glUniform2fv(loc, 2, values, 0);
    }
    else if(size == 3) {
      gl.glUniform3fv(loc, 3, values, 0);
    }
    else if(size == 4) {
      gl.glUniform4fv(loc, 4, values, 0);
    }
    else {
      throw new PwneeGLError("Size of float array not supported.");
    }
  }
  
  
  
  
  /** Sets the value for some uniform int variable in the shader. */
  public void setUniformi(GL2 gl, String name, int value) {
    int uniLoc = getUniformLocation(gl, name);
    gl.glUniform1i(uniLoc, value);
  }
  
  /** Sets the value for some uniform int variable in the shader. */
  public void setUniformiv(GL2 gl, String name, int[] values) {
    int size = values.length;
    int loc = getUniformLocation(gl, name);
    if(size == 1) {
      gl.glUniform1iv(loc, 1, values, 0);
    }
    else if(size == 2) {
      gl.glUniform2iv(loc, 2, values, 0);
    }
    else if(size == 3) {
      gl.glUniform3iv(loc, 3, values, 0);
    }
    else if(size == 4) {
      gl.glUniform4iv(loc, 4, values, 0);
    }
    else {
      throw new PwneeGLError("Size of float array not supported.");
    }
  }
  
  
  
  
  /** Sets the value for some uniform unsigned int variable in the shader. */
  public void setUniformui(GL2 gl, String name, int value) {
    int uniLoc = getUniformLocation(gl, name);
    gl.getGLES3().glUniform1ui(uniLoc, value);
  }
  
  /** Sets the value for some uniform unsigned int variable in the shader. */
  public void setUniformuiv(GL2 gl, String name, int[] values) {
    int size = values.length;
    int loc = getUniformLocation(gl, name);
    if(size == 1) {
      gl.getGLES3().glUniform1uiv(loc, 1, values, 0);
    }
    else if(size == 2) {
      gl.getGLES3().glUniform2uiv(loc, 2, values, 0);
    }
    else if(size == 3) {
      gl.getGLES3().glUniform3uiv(loc, 3, values, 0);
    }
    else if(size == 4) {
      gl.getGLES3().glUniform4uiv(loc, 4, values, 0);
    }
    else {
      throw new PwneeGLError("Size of float array not supported.");
    }
  }
  
  
  // TODO : Matrix support
  
  
  
  /** Return true iff the manager has the specified uniform. */
  public boolean hasUniform(String name) {
    return uniforms.containsKey(name);
  }
  
  
  //////// Apply
  
  /** Switches the OpenGL state to use this shader program. */
  public void useProgram(GL2 gl) {
    gl.glUseProgram(shaderProgram);
  }
}
