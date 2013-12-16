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

import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL2;

/** Singleton maintains a cache of shader programs in use by the application. */
public class ShaderLibrary {
  
  private static ShaderLibrary instance = null;
  
  /** The cache of shader programs in use. */
  private Map<String, ShaderProgram> shaders;
  
  /** A reference to the shader program currently in use. */
  private ShaderProgram curShader = null;
  
  
  private ShaderLibrary() {
    shaders = new HashMap<>();
  }
  
  
  /** Obtains the singleton instance, creating it if necessary. */
  public static ShaderLibrary getInstance() {
    if(instance == null) {
      instance = new ShaderLibrary();
    }
    return instance;
  }
  
  
  /** Returns true iff the library contains a shader program associated with the given key. */
  public static boolean contains(String key) {
    return (get(key) != null);
  }
  
  
  /** Gets the shader program associated with the given key. */
  public static ShaderProgram get(String key) {
    return getInstance().shaders.get(key);
  }
  
  /** Caches a shader program with the given key. */
  public static void put(String key, ShaderProgram program) {
    getInstance().shaders.put(key, program);
  }
  
  
  /** 
   * Removes the shader program with the given key from the library and 
   * from graphics memory. 
   */
  public static void remove(GL2 gl, String key) {
    ShaderProgram program = getInstance().shaders.remove(key);
    program.clean(gl);
  }
  
  
  /** Binds OpenGL to use the shader program with the given key. */
  public static ShaderProgram use(GL2 gl, String key) {
    ShaderProgram program = get(key);
    program.useProgram(gl);
    getInstance().curShader = program;
    return program;
  }
  
  
  /** Gets the shader program currently in use. */
  public static ShaderProgram get() {
    return getInstance().curShader;
  }
}

