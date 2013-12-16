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

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import static javax.media.opengl.GL.*;  // GL constants
import static javax.media.opengl.GL2.*; // GL2 constants

/** 
 * Reads and compiles a shader program which can then be used for customized rendering. 
 * If for some reason, it fails to create the shader program, an Exception will
 * be printed with debug information which will (hopefully) provide some insight
 * into the problem.
 */
public class ShaderProgram {
  
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
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  
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
    
    // If there were any compile errors, throw an Exception with debug information.
    int status = glGetShaderi(gl, shader, GL_COMPILE_STATUS);
    if(status != GL_TRUE) {
      String msg = glGetShaderInfoLog(gl, shader);
      throw new Exception(msg);
    }
    
    return shader;
  }
  
  
  
  /** 
   * Attempts to attach the vertex and fragment shaders to the shader program object. 
   * An Exception is thrown if it fails. 
   */
  private void glAttachShaders(GL2 gl) throws Exception {
    gl.glAttachShader(shaderProgram, vShader);
    gl.glAttachShader(shaderProgram, fShader);
  }
  
  
  /** Attempts to link the shader program object. An Exception is thrown if it fails. */
  private void glLinkProgram(GL2 gl) throws Exception {
    gl.glLinkProgram(shaderProgram);
    
    int status = glGetProgrami(gl, GL_LINK_STATUS);
    if(status != GL_TRUE) {
      throw new Exception("Failed to link the shader program object.");
    }
  }
  
  
  /** Attempts to validate the shader program object. An Exception is thrown if it fails. */
  private void glValidateProgram(GL2 gl) throws Exception {
    gl.glValidateProgram(shaderProgram);
      
    // If the program failed to validate, throw an Exception containing 
    // information about why it failed.
    int status = glGetProgrami(gl, GL_VALIDATE_STATUS);
    if(status != GL_TRUE) {
      String msg = glGetProgramInfoLog(gl);
      throw new Exception(msg);
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
  
  //////// program/pointer indices
  
  /** Returns the GL index for the shader program object. */
  public int getProgram() {
    return shaderProgram;
  }
  
  /** Returns the GL index for the vertex shader. */
  public int getVertexShader() {
    return vShader;
  }
  
  /** Returns the GL index for the fragment shader. */
  public int getFragmentShader() {
    return fShader;
  }
  
  //////// Debug info
  
  /** Returns a parameter for the vertex shader. */
  public int glGetVertexShaderi(GL2 gl, int pname) {
    return glGetShaderi(gl, vShader, pname);
  }
  
  /** Returns the information log for the vertex shader. */
  public String glGetVertexShaderInfoLog(GL2 gl) {
    return glGetShaderInfoLog(gl, vShader);
  }
  
  /** Returns a parameter for the fragment shader. */
  public int glGetFragmentShaderi(GL2 gl, int pname) {
    return glGetShaderi(gl, fShader, pname);
  }
  
  /** Returns the information log for the fragment shader. */
  public String glGetFragmentShaderInfoLog(GL2 gl) {
    return glGetShaderInfoLog(gl, fShader);
  }
  
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
  
  
  //////// Shader vertex attributes and uniform variables
  
  /** Returns the index to a vertex attribute specified in the shader program. */
  public int getAttrib(GL2 gl, String name) {
    return gl.glGetAttribLocation(shaderProgram, name);
  }
  
  /** Returns the index to a uniform attribute specified in the shader program. */
  public int getUniform(GL2 gl, String name) {
    return gl.glGetUniformLocation(shaderProgram, name);
  }
  
  //////// Apply
  
  /** Switches the OpenGL state to use this shader program. */
  public void useProgram(GL2 gl) {
    gl.glUseProgram(shaderProgram);
  }
}
