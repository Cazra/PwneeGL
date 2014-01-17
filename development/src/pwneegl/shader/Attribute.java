package pwneegl.shader;

import pwneegl.GLNames;

public class Attribute {
  
  private String name;
  
  private int size;
  
  private int sizeBytes;
  
  private int sizeUnits;
  
  private int type;
  
  private int location;
  
  
  /** 
   * @param name is the name of the attribute.
   * @param size is the size of the attribute in units of its type.
   * @param type is the OpenGL type constant for the attribute.
   */
  public Attribute(String name, int size, int type, int location) {
    this.name = name;
    this.size = size;
    this.sizeBytes = size*GLNames.glSizeBytes(type);
    this.sizeUnits = size*GLNames.glSizeUnits(type);
    this.type = type;
    this.location = location;
  }
  
  
  /** Returns the name of the vertex attribute. */
  public String getName() {
    return name;
  }
  
  /** Returns the size of the attribute in units of its type. TODO : Better explanation. Every variable I've encountered so far for this returns 1. */
  public int getCount() {
    return size;
  }
  
  /** Returns the size of the attribute in bytes. */
  public int getSizeBytes() {
    return sizeBytes;
  }
  
  /** Returns the size of the attribute in units. For example, if the type of this attribute is GL_FLOAT_VEC3, this would return 3. */
  public int getSizeUnits() {
    return sizeUnits;
  }
  
  /** Returns the OpenGL type constant for the attribute. */
  public int getType() {
    return type;
  }
  
  /** 
   * Returns the unit type of the attribute. For example, if the attribute 
   * is a GL_FLOAT_VEC3, this would return GL_FLOAT. 
   */
  public int getUnitType() {
    return GLNames.glUnitType(type);
  }
  
  
  /** 
   * Returns true if this is a user-defined attribute. These are any 
   * attributes that are not built-in.
   */
  public boolean isUserDefined() {
    return !isBuiltIn();
  }
  
  /** 
   * Returns true iff this is a built-in attribute. Built-in attributes are 
   * any whose name starts with "gl_".
   */
  public boolean isBuiltIn() {
    return name.startsWith("gl_");
  }
  
  /**
   * Returns the location of the vertex attribute in graphics memory. 
   */
  public int getLocation() {
    return location;
  }
}
