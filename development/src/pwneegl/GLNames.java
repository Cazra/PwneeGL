package pwneegl;

import static javax.media.opengl.GL.*;  // GL constants
import static javax.media.opengl.GL2.*; // GL2 constants
import static javax.media.opengl.GL3.*; // GL2 constants
import static javax.media.opengl.GL4.*; // GL2 constants

/** 
 * Provides utilities for representing GL constants as Strings representing 
 * their names. This is mainly just for debugging. Returns empty String "" if
 * the type is not recognized.
 */
public class GLNames {
  
  public static String glName(int type) {
    switch(type) {
      case GL_FLOAT :
        return "GL_FLOAT";
      case GL_FLOAT_VEC2 :
        return "GL_FLOAT_VEC2";
      case GL_FLOAT_VEC3 :
        return "GL_FLOAT_VEC3";
      case GL_FLOAT_VEC4 :
        return "GL_FLOAT_VEC4";
      case GL_FLOAT_MAT2 :
        return "GL_FLOAT_MAT2";
      case GL_FLOAT_MAT3 :
        return "GL_FLOAT_MAT3";
      case GL_FLOAT_MAT4 :
        return "GL_FLOAT_MAT4";
      case GL_FLOAT_MAT2x3 :
        return "GL_FLOAT_MAT2x3";
      case GL_FLOAT_MAT2x4 :
        return "GL_FLOAT_MAT2x4";
      case GL_FLOAT_MAT3x2 :
        return "GL_FLOAT_MAT3x2";
      case GL_FLOAT_MAT3x4 :
        return "GL_FLOAT_MAT3x4";
      case GL_FLOAT_MAT4x2 :
        return "GL_FLOAT_MAT4x2";
      case GL_FLOAT_MAT4x3 :
        return "GL_FLOAT_MAT4x3";
      case GL_INT :
        return "GL_INT";
      case GL_INT_VEC2 :
        return "GL_INT_VEC2";
      case GL_INT_VEC3 :
        return "GL_INT_VEC3";
      case GL_INT_VEC4 :
        return "GL_INT_VEC4";
      case GL_UNSIGNED_INT : 
        return "GL_UNSIGNED_INT";
      case GL_UNSIGNED_INT_VEC2 :
        return "GL_UNSIGNED_INT_VEC2";
      case GL_UNSIGNED_INT_VEC3 :
        return "GL_UNSIGNED_INT_VEC3";
      case GL_UNSIGNED_INT_VEC4 :
        return "GL_UNSIGNED_INT_VEC4";
      case GL_DOUBLE :
        return "GL_DOUBLE";
      case GL_DOUBLE_VEC2 :
        return "GL_DOUBLE_VEC2";
      case GL_DOUBLE_VEC3 :
        return "GL_DOUBLE_VEC3";
      case GL_DOUBLE_VEC4 :
        return "GL_DOUBLE_VEC4";
      case GL_DOUBLE_MAT2 :
        return "GL_DOUBLE_MAT2";
      case GL_DOUBLE_MAT3 :
        return "GL_DOUBLE_MAT3";
      case GL_DOUBLE_MAT4 :
        return "GL_DOUBLE_MAT4";
      case GL_DOUBLE_MAT2x3 :
        return "GL_DOUBLE_MAT2x3";
      case GL_DOUBLE_MAT2x4 :
        return "GL_DOUBLE_MAT2x4";
      case GL_DOUBLE_MAT3x2 :
        return "GL_DOUBLE_MAT3x2";
      case GL_DOUBLE_MAT3x4 :
        return "GL_DOUBLE_MAT3x4";
      case GL_DOUBLE_MAT4x2 :
        return "GL_DOUBLE_MAT4x2";
      case GL_DOUBLE_MAT4x3 :
        return "GL_DOUBLE_MAT4x3";
      case GL_SAMPLER_1D :
        return "GL_SAMPLER_1D";
      case GL_SAMPLER_2D :
        return "GL_SAMPLER_2D";
      case GL_SAMPLER_3D :
        return "GL_SAMPLER_3D";
      case GL_SAMPLER_CUBE :
        return "GL_SAMPLER_CUBE";
      case GL_SAMPLER_1D_SHADOW :
        return "GL_SAMPLER_1D_SHADOW";
      case GL_SAMPLER_2D_SHADOW :
        return "GL_SAMPLER_2D_SHADOW";
      case GL_SAMPLER_1D_ARRAY :
        return "GL_SAMPLER_1D_ARRAY";
      case GL_SAMPLER_2D_ARRAY :
        return "GL_SAMPLER_2D_ARRAY";
      case GL_SAMPLER_1D_ARRAY_SHADOW :
        return "GL_SAMPLER_1D_ARRAY_SHADOW";
      case GL_SAMPLER_2D_ARRAY_SHADOW :
        return "GL_SAMPLER_2D_ARRAY_SHADOW";
      case GL_SAMPLER_2D_MULTISAMPLE :
        return "GL_SAMPLER_2D_MULTISAMPLE";
      case GL_SAMPLER_2D_MULTISAMPLE_ARRAY :
        return "GL_SAMPLER_2D_MULTISAMPLE_ARRAY";
      case GL_SAMPLER_CUBE_SHADOW :
        return "GL_SAMPLER_CUBE_SHADOW";
      case GL_SAMPLER_BUFFER :
        return "GL_SAMPLER_BUFFER";
      case GL_SAMPLER_2D_RECT :
        return "GL_SAMPLER_2D_RECT";
      case GL_SAMPLER_2D_RECT_SHADOW :
        return "GL_SAMPLER_2D_RECT_SHADOW";
      case GL_INT_SAMPLER_1D :
        return "GL_INT_SAMPLER_1D";
      case GL_INT_SAMPLER_2D :
        return "GL_INT_SAMPLER_2D";
      case GL_INT_SAMPLER_3D :
        return "GL_INT_SAMPLER_3D";
      case GL_INT_SAMPLER_CUBE :
        return "GL_INT_SAMPLER_CUBE";
      case GL_INT_SAMPLER_1D_ARRAY :
        return "GL_INT_SAMPLER_1D_ARRAY";
      case GL_INT_SAMPLER_2D_ARRAY :
        return "GL_INT_SAMPLER_2D_ARRAY";
      case GL_INT_SAMPLER_2D_MULTISAMPLE :
        return "GL_INT_SAMPLER_2D_MULTISAMPLE";
      case GL_INT_SAMPLER_2D_MULTISAMPLE_ARRAY :
        return "GL_INT_SAMPLER_2D_MULTISAMPLE_ARRAY";
      case GL_INT_SAMPLER_BUFFER :
        return "GL_INT_SAMPLER_BUFFER";
      case GL_INT_SAMPLER_2D_RECT :
        return "GL_INT_SAMPLER_2D_RECT";
      case GL_UNSIGNED_INT_SAMPLER_1D :
        return "GL_UNSIGNED_INT_SAMPLER_1D";
      case GL_UNSIGNED_INT_SAMPLER_2D :
        return "GL_UNSIGNED_INT_SAMPLER_2D";
      case GL_UNSIGNED_INT_SAMPLER_3D :
        return "GL_UNSIGNED_INT_SAMPLER_3D";
      case GL_UNSIGNED_INT_SAMPLER_CUBE :
        return "GL_UNSIGNED_INT_SAMPLER_CUBE";
      case GL_UNSIGNED_INT_SAMPLER_1D_ARRAY :
        return "GL_UNSIGNED_INT_SAMPLER_1D_ARRAY";
      case GL_UNSIGNED_INT_SAMPLER_2D_ARRAY :
        return "GL_UNSIGNED_INT_SAMPLER_2D_ARRAY";
      case GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE :
        return "GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE";
      case GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE_ARRAY :
        return "GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE_ARRAY";
      case GL_UNSIGNED_INT_SAMPLER_BUFFER :
        return "GL_UNSIGNED_INT_SAMPLER_BUFFER";
      case GL_UNSIGNED_INT_SAMPLER_2D_RECT :
        return "GL_UNSIGNED_INT_SAMPLER_2D_RECT";
      case GL_IMAGE_1D :
        return "GL_IMAGE_1D";
      case GL_IMAGE_2D :
        return "GL_IMAGE_2D";
      case GL_IMAGE_3D :
        return "GL_IMAGE_3D";
      case GL_IMAGE_2D_RECT :
        return "GL_IMAGE_2D_RECT";
      case GL_IMAGE_CUBE :
        return "GL_IMAGE_CUBE";
      case GL_IMAGE_BUFFER :
        return "GL_IMAGE_BUFFER";
      case GL_IMAGE_1D_ARRAY :
        return "GL_IMAGE_1D_ARRAY";
      case GL_IMAGE_2D_ARRAY :
        return "GL_IMAGE_2D_ARRAY";
      case GL_IMAGE_2D_MULTISAMPLE :
        return "GL_IMAGE_2D_MULTISAMPLE";
      case GL_IMAGE_2D_MULTISAMPLE_ARRAY :
        return "GL_IMAGE_2D_MULTISAMPLE_ARRAY";
      case GL_INT_IMAGE_1D :
        return "GL_INT_IMAGE_1D";
      case GL_INT_IMAGE_2D :
        return "GL_INT_IMAGE_2D";
      case GL_INT_IMAGE_3D :
        return "GL_INT_IMAGE_3D";
      case GL_INT_IMAGE_2D_RECT :
        return "GL_INT_IMAGE_2D_RECT";
      case GL_INT_IMAGE_CUBE :
        return "GL_INT_IMAGE_CUBE";
      case GL_INT_IMAGE_BUFFER :
        return "GL_INT_IMAGE_BUFFER";
      case GL_INT_IMAGE_1D_ARRAY :
        return "GL_INT_IMAGE_1D_ARRAY";
      case GL_INT_IMAGE_2D_ARRAY :
        return "GL_INT_IMAGE_2D_ARRAY";
      case GL_INT_IMAGE_2D_MULTISAMPLE :
        return "GL_INT_IMAGE_2D_MULTISAMPLE";
      case GL_INT_IMAGE_2D_MULTISAMPLE_ARRAY :
        return "GL_INT_IMAGE_2D_MULTISAMPLE_ARRAY";
      case GL_UNSIGNED_INT_IMAGE_1D :
        return "GL_UNSIGNED_INT_IMAGE_1D";
      case GL_UNSIGNED_INT_IMAGE_2D :
        return "GL_UNSIGNED_INT_IMAGE_2D";
      case GL_UNSIGNED_INT_IMAGE_3D :
        return "GL_UNSIGNED_INT_IMAGE_3D";
      case GL_UNSIGNED_INT_IMAGE_2D_RECT :
        return "GL_UNSIGNED_INT_IMAGE_2D_RECT";
      case GL_UNSIGNED_INT_IMAGE_CUBE :
        return "GL_UNSIGNED_INT_IMAGE_CUBE";
      case GL_UNSIGNED_INT_IMAGE_BUFFER :
        return "GL_UNSIGNED_INT_IMAGE_BUFFER";
      case GL_UNSIGNED_INT_IMAGE_1D_ARRAY :
        return "GL_UNSIGNED_INT_IMAGE_1D_ARRAY";
      case GL_UNSIGNED_INT_IMAGE_2D_ARRAY :
        return "GL_UNSIGNED_INT_IMAGE_2D_ARRAY";
      case GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE :
        return "GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE";
      case GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE_ARRAY :
        return "GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE_ARRAY";
      case GL_UNSIGNED_INT_ATOMIC_COUNTER :
        return "GL_UNSIGNED_INT_ATOMIC_COUNTER";
    }
    return "";
  }
  
  
  
  /** 
   * Returns the size of an OpenGL type in bytes. 
   * Returns -1 if the type is not recognized or its size is unknown. 
   */
  public static int glSizeBytes(int type) {
    int units = glSizeUnits(type);
    switch(type) {
      case GL_FLOAT :
        return 4*units;
      case GL_FLOAT_VEC2 :
        return 4*units;
      case GL_FLOAT_VEC3 :
        return 4*units;
      case GL_FLOAT_VEC4 :
        return 4*units;
      case GL_FLOAT_MAT2 :
        return 4*units;
      case GL_FLOAT_MAT3 :
        return 4*units;
      case GL_FLOAT_MAT4 :
        return 4*units;
      case GL_FLOAT_MAT2x3 :
        return 4*units;
      case GL_FLOAT_MAT2x4 :
        return 4*units;
      case GL_FLOAT_MAT3x2 :
        return 4*units;
      case GL_FLOAT_MAT3x4 :
        return 4*units;
      case GL_FLOAT_MAT4x2 :
        return 4*units;
      case GL_FLOAT_MAT4x3 :
        return 4*units;
      case GL_INT :
        return 4*units;
      case GL_INT_VEC2 :
        return 4*units;
      case GL_INT_VEC3 :
        return 4*units;
      case GL_INT_VEC4 :
        return 4*units;
      case GL_UNSIGNED_INT : 
        return 4*units;
      case GL_UNSIGNED_INT_VEC2 :
        return 4*units;
      case GL_UNSIGNED_INT_VEC3 :
        return 4*units;
      case GL_UNSIGNED_INT_VEC4 :
        return 4*units;
      case GL_DOUBLE :
        return 8*units;
      case GL_DOUBLE_VEC2 :
        return 8*units;
      case GL_DOUBLE_VEC3 :
        return 8*units;
      case GL_DOUBLE_VEC4 :
        return 8*units;
      case GL_DOUBLE_MAT2 :
        return 8*units;
      case GL_DOUBLE_MAT3 :
        return 8*units;
      case GL_DOUBLE_MAT4 :
        return 8*units;
      case GL_DOUBLE_MAT2x3 :
        return 8*units;
      case GL_DOUBLE_MAT2x4 :
        return 8*units;
      case GL_DOUBLE_MAT3x2 :
        return 8*units;
      case GL_DOUBLE_MAT3x4 :
        return 8*units;
      case GL_DOUBLE_MAT4x2 :
        return 8*units;
      case GL_DOUBLE_MAT4x3 :
        return 8*units;
    }
    return -1;
  }
  
  /** 
   * Returns the size of an OpenGL type in units of its base type. 
   * Returns -1 if the type is not recognized or its size is unknown. 
   */
  public static int glSizeUnits(int type) {
    switch(type) {
      case GL_FLOAT :
        return 1;
      case GL_FLOAT_VEC2 :
        return 2;
      case GL_FLOAT_VEC3 :
        return 3;
      case GL_FLOAT_VEC4 :
        return 4;
      case GL_FLOAT_MAT2 :
        return 4;
      case GL_FLOAT_MAT3 :
        return 9;
      case GL_FLOAT_MAT4 :
        return 16;
      case GL_FLOAT_MAT2x3 :
        return 6;
      case GL_FLOAT_MAT2x4 :
        return 8;
      case GL_FLOAT_MAT3x2 :
        return 6;
      case GL_FLOAT_MAT3x4 :
        return 12;
      case GL_FLOAT_MAT4x2 :
        return 8;
      case GL_FLOAT_MAT4x3 :
        return 12;
      case GL_INT :
        return 1;
      case GL_INT_VEC2 :
        return 2;
      case GL_INT_VEC3 :
        return 3;
      case GL_INT_VEC4 :
        return 4;
      case GL_UNSIGNED_INT : 
        return 1;
      case GL_UNSIGNED_INT_VEC2 :
        return 2;
      case GL_UNSIGNED_INT_VEC3 :
        return 3;
      case GL_UNSIGNED_INT_VEC4 :
        return 4;
      case GL_DOUBLE :
        return 1;
      case GL_DOUBLE_VEC2 :
        return 2;
      case GL_DOUBLE_VEC3 :
        return 3;
      case GL_DOUBLE_VEC4 :
        return 4;
      case GL_DOUBLE_MAT2 :
        return 4;
      case GL_DOUBLE_MAT3 :
        return 9;
      case GL_DOUBLE_MAT4 :
        return 16;
      case GL_DOUBLE_MAT2x3 :
        return 6;
      case GL_DOUBLE_MAT2x4 :
        return 8;
      case GL_DOUBLE_MAT3x2 :
        return 6;
      case GL_DOUBLE_MAT3x4 :
        return 12;
      case GL_DOUBLE_MAT4x2 :
        return 8;
      case GL_DOUBLE_MAT4x3 :
        return 12;
    }
    return -1;
  }
  
  /** 
   * Returns the unit GL type of the given GL type. For example, the unit
   * type of GL_FLOAT_VEC3 would be GL_FLOAT.
   * Returns -1 if the type is not recognized. 
   */
  public static int glUnitType(int type) {
    switch(type) {
      case GL_FLOAT :
        return GL_FLOAT;
      case GL_FLOAT_VEC2 :
        return GL_FLOAT;
      case GL_FLOAT_VEC3 :
        return GL_FLOAT;
      case GL_FLOAT_VEC4 :
        return GL_FLOAT;
      case GL_FLOAT_MAT2 :
        return GL_FLOAT;
      case GL_FLOAT_MAT3 :
        return GL_FLOAT;
      case GL_FLOAT_MAT4 :
        return GL_FLOAT;
      case GL_FLOAT_MAT2x3 :
        return GL_FLOAT;
      case GL_FLOAT_MAT2x4 :
        return GL_FLOAT;
      case GL_FLOAT_MAT3x2 :
        return GL_FLOAT;
      case GL_FLOAT_MAT3x4 :
        return GL_FLOAT;
      case GL_FLOAT_MAT4x2 :
        return GL_FLOAT;
      case GL_FLOAT_MAT4x3 :
        return GL_FLOAT;
      case GL_INT :
        return GL_INT;
      case GL_INT_VEC2 :
        return GL_INT;
      case GL_INT_VEC3 :
        return GL_INT;
      case GL_INT_VEC4 :
        return GL_INT;
      case GL_UNSIGNED_INT : 
        return GL_UNSIGNED_INT;
      case GL_UNSIGNED_INT_VEC2 :
        return GL_UNSIGNED_INT;
      case GL_UNSIGNED_INT_VEC3 :
        return GL_UNSIGNED_INT;
      case GL_UNSIGNED_INT_VEC4 :
        return GL_UNSIGNED_INT;
      case GL_DOUBLE :
        return GL_DOUBLE;
      case GL_DOUBLE_VEC2 :
        return GL_DOUBLE;
      case GL_DOUBLE_VEC3 :
        return GL_DOUBLE;
      case GL_DOUBLE_VEC4 :
        return GL_DOUBLE;
      case GL_DOUBLE_MAT2 :
        return GL_DOUBLE;
      case GL_DOUBLE_MAT3 :
        return GL_DOUBLE;
      case GL_DOUBLE_MAT4 :
        return GL_DOUBLE;
      case GL_DOUBLE_MAT2x3 :
        return GL_DOUBLE;
      case GL_DOUBLE_MAT2x4 :
        return GL_DOUBLE;
      case GL_DOUBLE_MAT3x2 :
        return GL_DOUBLE;
      case GL_DOUBLE_MAT3x4 :
        return GL_DOUBLE;
      case GL_DOUBLE_MAT4x2 :
        return GL_DOUBLE;
      case GL_DOUBLE_MAT4x3 :
        return GL_DOUBLE;
    }
    return -1;
  }
  
}