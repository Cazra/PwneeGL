package pwneegl.shader;

import pwneegl.GLNames;

public class Uniform {
  
  private String name;
  
  private int size;
  
  private int sizeBytes;
  
  private int sizeUnits;
  
  private int type;
  
  
  /** 
   * @param name is the name of the uniform.
   * @param size is the size of the uniform in units of its type.
   * @param type is the OpenGL type constant for the uniform.
   */
  public Uniform(String name, int size, int type) {
    this.name = name;
    this.size = size;
    this.sizeBytes = size*GLNames.glSizeBytes(type);
    this.sizeUnits = size*GLNames.glSizeUnits(type);
    this.type = type;
  }
  
  /** Returns the name of the uniform variable. */
  public String getName() {
    return name;
  }
  
  /** Returns the size of the uniform in units of its type. TODO : Better explanation. Every variable I've encountered so far for this returns 1. */
  public int getCount() {
    return size;
  }
  
  /** Returns the size of the uniform in bytes. */
  public int getSizeBytes() {
    return sizeBytes;
  }
  
  /** Returns the size of the attribute in units. For example, if the type of this attribute is GL_FLOAT_VEC3, this would return 3. */
  public int getSizeUnits() {
    return sizeUnits;
  }
  
  /** Returns the OpenGL type constant for the uniform. */
  public int getType() {
    return type;
  }
  
  
  /** 
   * Returns the unit type of the uniform. For example, if the uniform 
   * is a GL_FLOAT_VEC3, this would return GL_FLOAT. 
   */
  public int getUnitType() {
    return GLNames.glUnitType(type);
  }
  
  
  
  /** 
   * Returns true if this is a user-defined uniform. These are any 
   * uniforms that are not built-in.
   */
  public boolean isUserDefined() {
    return !isBuiltIn();
  }
  
  /** 
   * Returns true iff this is a built-in uniform. Built-in uniforms are 
   * any whose name starts with "gl_".
   */
  public boolean isBuiltIn() {
    return name.startsWith("gl_");
  }
}
