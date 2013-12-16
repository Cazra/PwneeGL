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


import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL2;

/** Singleton maintains a cache of Materials used by the application. */
public class MaterialLibrary {
  
  /** The singleton instance. */
  private static MaterialLibrary instance = null;
  
  /** The cache of Materials in use. */
  private Map<String, Material> materials;
  
  private MaterialLibrary() {
    materials = new HashMap<>();
  }
  
  
  /** Obtains the singleton instance, creating it if necessary. */
  private static MaterialLibrary getInstance() {
    if(instance == null) {
      instance = new MaterialLibrary();
    }
    return instance;
  }
  
  /** Returns true if the library contains a Material associated with the given key. */
  public static boolean contains(String key) {
    return (get(key) != null);
  }
  
  /** Gets the Material associated with the given key. */
  public static Material get(String key) {
    return getInstance().materials.get(key);
  }
  
  
  /** Caches a Material with the given key. */
  public static void put(String key, Material mat) {
    getInstance().materials.put(key, mat);
  }
  
  /** Removes the Material associated with the given key from the library and from graphics memory. */
  public static void remove(GL2 gl, String key) {
    Material mat = getInstance().materials.remove(key);
    mat.clean(gl);
  }
  
  
  /** 
   * Binds the material associated with the given key to the OpenGL state and 
   * the current shader program with the given uniform attribute name. 
   */
  public static Material use(GL2 gl, String key, String texUni) {
    Material mat = get(key);
    mat.glMaterial(gl, texUni);
    return mat;
  }
}

