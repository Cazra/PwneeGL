package pwneegl.geom.data;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import static javax.media.opengl.GL.*;  // GL constants
import static javax.media.opengl.GL2.*; // GL2 constants

import com.jogamp.common.nio.Buffers;

import pwneegl.GLNames;
import pwneegl.PwneeGLError;
import pwneegl.geom.Face3f;
import pwneegl.geom.Poly3f;
import pwneegl.geom.Vertex3f;
import pwneegl.shader.Attribute;
import pwneegl.shader.ShaderLibrary;

/** 
 * An object responsible for managing the flow of vertex attributes through the 
 * graphics pipeline for a Poly3f via Vertex Buffer Objects (VBOs). 
 */
public class VBOPipeline {
  
  /** The number of built-in float vertex attributes. */
  public static final int NUM_BUILTIN_ATTRIBSF = 13;
  
  
  /** The pointers to the VBOs. */
  private int[] buffers = null;
  
  /** The buffer containing the float attributes for all the vertices. */
  private FloatBuffer attribfBuffer = null;
  
  /** The buffer containing the int attributes for all the vertices. */
  private IntBuffer attribiBuffer = null;
  
  /** The buffer containing the double attributes for all the vertices. */
  private DoubleBuffer attribdBuffer = null;
  
  /** The buffer of vertex indices defining the faces. */
  private ShortBuffer elementBuffer = null;
  
  
  /** 
   * Clears the vertex buffer data from graphics memory so that vertex 
   * attributes can be recomputed. 
   */
  public void clean(GL2 gl) {
    if(buffers != null) {
      gl.glDeleteBuffers(buffers.length, buffers, 0);
      buffers = null;
    }
  }
  
  
  
  
  /////// Pipeline metrics
  
  /** Returns the number of bytes built-in attributes contribute to the pipeline's stride. */
  public static int strideBuiltInf() {
    return NUM_BUILTIN_ATTRIBSF * GLNames.glSizeBytes(GL_FLOAT);
  }
  
  
  
  /** Returns the total byte stride of the vertex float attribute pipeline. */
  public static int stridef() {
    return strideBuiltInf() + ShaderLibrary.get().getPipelineStridef();
  }
  
  /** Returns the total byte stride of the vertex int attribute pipeline. */
  public static int stridei() {
    return ShaderLibrary.get().getPipelineStridei();
  }
  
  /** Returns the total byte stride of the vertex double attribute pipeline. */
  public static int strided() {
    return ShaderLibrary.get().getPipelineStrided();
  }
  
  
  /** Returns the number of built-in float attributes contributing to the pipeline's stride. */
  public static int numFloatsBuiltIn() {
    return 13;
  }
  
  /** Returns the number of user-defined float attributes contributing to the pipeline's stride. */
  public static int numFloatsUserDefined() {
    int result = 0;
    for(Attribute att : ShaderLibrary.get().getUserAttribs()) {
      if(att.getUnitType() == GL_FLOAT) {
        result += att.getSizeUnits();
      }
    }
    return result;
  }
  
  /** Returns the number of float attributes contributing to the pipeline's stride. */
  public static int numFloats() {
    return numFloatsBuiltIn() + numFloatsUserDefined();
  }
  
  /** Returns the number of user-defined int attributes contributing to the pipeline's stride. */
  public static int numInts() {
    int result = 0;
    for(Attribute att : ShaderLibrary.get().getUserAttribs()) {
      if(att.getUnitType() == GL_INT || att.getUnitType() == GL_UNSIGNED_INT) {
        result += att.getSizeUnits();
      }
    }
    return result;
  }
  
  /** Returns the number of user-defined int attributes contributing to the pipeline's stride. */
  public static int numDoubles() {
    int result = 0;
    for(Attribute att : ShaderLibrary.get().getUserAttribs()) {
      if(att.getUnitType() == GL_DOUBLE) {
        result += att.getSizeUnits();
      }
    }
    return result;
  }
  
  
  // Buffer population
  
  /** Generates and fills the VBOs. */
  private void genBuffers(GL2 gl, List<Face3f> faces, List<Vertex3f> vertices) {
    if(buffers == null) {
      buffers = new int[4];
      gl.glGenBuffers(buffers.length, buffers, 0);
      
      // Fill the buffers.
      fillAttribfBuffer(gl, buffers[0], vertices);
      fillAttribiBuffer(gl, buffers[1], vertices);
      fillAttribdBuffer(gl, buffers[2], vertices);
      fillElementBuffer(gl, buffers[3], faces);
      
    }
  }
  
  //////// float attribute buffer
  
  /** Fills the buffer containing the float vertex attributes. */
  private void fillAttribfBuffer(GL2 gl, int glLoc, List<Vertex3f> vertices) {
    int numAttribfs = vertices.size() * numFloats();
    
    // Fill the buffer.
    attribfBuffer = FloatBuffer.allocate(numAttribfs);
    for(Vertex3f vertex : vertices) {
      fillAttribfBufferBuiltIn(vertex);
      fillAttribfBufferUserDefined(vertex);
    }
    attribfBuffer.flip();
    
    // Load the buffer data into graphics memory.
    gl.glBindBuffer(GL_ARRAY_BUFFER, glLoc);
    gl.glBufferData(GL_ARRAY_BUFFER, 
                    attribfBuffer.capacity()*Buffers.SIZEOF_FLOAT,
                    attribfBuffer,
                    GL_STATIC_DRAW);
  }
  
  
  /** Fills the buffer containing the float vertex attributes with the built-in attributes. */
  private void fillAttribfBufferBuiltIn(Vertex3f vertex) {
    attribfBuffer.put(vertex.getCoords());
    attribfBuffer.put(vertex.getColor());
    attribfBuffer.put(vertex.getNormal());
    attribfBuffer.put(vertex.getTexCoords());
  }
  
  
  /** Fills the buffer containing the float vertex attributes with the user-defined attributes. */
  private void fillAttribfBufferUserDefined(Vertex3f vertex) {
    int num = 0;
    for(float[] attrib : vertex.getAttribsf()) {
      attribfBuffer.put(attrib);
      num += attrib.length;
    }
    if(num != numFloatsUserDefined()) {
      throw new PwneeGLError("Misaligned user-defined attributes. Number of expected bytes doesn't match!");
    }
  }
  
  
  //////// int attribute buffer
  
  /** Fills the buffer containing the int vertex attributes. */
  private void fillAttribiBuffer(GL2 gl, int glLoc, List<Vertex3f> vertices) {
    int numAttribis = vertices.size() * numInts();
    
    // Fill the buffer.
    attribiBuffer = IntBuffer.allocate(numAttribis);
    for(Vertex3f vertex : vertices) {
      fillAttribiBufferUserDefined(vertex);
    }
    attribiBuffer.flip();
    
    // Load the buffer data into graphics memory.
    gl.glBindBuffer(GL_ARRAY_BUFFER, glLoc);
    gl.glBufferData(GL_ARRAY_BUFFER, 
                    attribiBuffer.capacity()*GLNames.glSizeBytes(GL_INT),
                    attribiBuffer,
                    GL_STATIC_DRAW);
  }
  
  
  /** Fills the buffer containing the int vertex attributes with the user-defined attributes. */
  private void fillAttribiBufferUserDefined(Vertex3f vertex) {
    int num = 0;
    for(int[] attrib : vertex.getAttribsi()) {
      attribiBuffer.put(attrib);
      num += attrib.length;
    }
    if(num != numInts()) {
      throw new PwneeGLError("Misaligned user-defined attributes. Number of expected bytes doesn't match!");
    }
  }
  
  
  
  
  //////// double attribute buffer
  
  /** Fills the buffer containing the double vertex attributes. */
  private void fillAttribdBuffer(GL2 gl, int glLoc, List<Vertex3f> vertices) {
    int numAttribis = vertices.size() * numDoubles();
    
    // Fill the buffer.
    attribdBuffer = DoubleBuffer.allocate(numAttribis);
    for(Vertex3f vertex : vertices) {
      fillAttribdBufferUserDefined(vertex);
    }
    attribdBuffer.flip();
    
    // Load the buffer data into graphics memory.
    gl.glBindBuffer(GL_ARRAY_BUFFER, glLoc);
    gl.glBufferData(GL_ARRAY_BUFFER, 
                    attribdBuffer.capacity()*GLNames.glSizeBytes(GL_DOUBLE),
                    attribdBuffer,
                    GL_STATIC_DRAW);
  }
  
  
  /** Fills the buffer containing the double vertex attributes with the user-defined attributes. */
  private void fillAttribdBufferUserDefined(Vertex3f vertex) {
    int num = 0;
    for(double[] attrib : vertex.getAttribsd()) {
      attribdBuffer.put(attrib);
      num += attrib.length;
    }
    if(num != numDoubles()) {
      throw new PwneeGLError("Misaligned user-defined attributes. Number of expected bytes doesn't match!");
    }
  }
  
  
  
  
  
  //////// element buffer
  
  /** Fills the element buffer with the vertex indices specified by the polygon's faces. */
  private void fillElementBuffer(GL2 gl, int glLoc, List<Face3f> faces) {
    int numIndices = faces.size()*3;
    
    // Fill the buffer
    elementBuffer = ShortBuffer.allocate(numIndices);
    for(Face3f face : faces) {
      elementBuffer.put(new short[] {face.getIndex1(), face.getIndex2(), face.getIndex3()});
    }
    elementBuffer.flip();
    
    // Load the buffer data into graphics memory.
    gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, glLoc);
    gl.glBufferData(GL_ELEMENT_ARRAY_BUFFER, 
                    elementBuffer.capacity()*Buffers.SIZEOF_SHORT,
                    elementBuffer,
                    GL_STATIC_DRAW);
  }
  
  
  //////// Built-in pipeline
  
  /** Adds all built-in shader attributes to the pipeline. */
  private void addGLPipeline(GL2 gl) {
    gl.glBindBuffer(GL_ARRAY_BUFFER, buffers[0]);
    
    // Add built-in attributes to the pipeline.
    gl.glEnableClientState(GL_VERTEX_ARRAY);
    gl.glVertexPointer(4, GL_FLOAT, stridef(), 0);
    
    gl.glEnableClientState(GL_COLOR_ARRAY);
    gl.glColorPointer(4, GL_FLOAT, stridef(), 4*Buffers.SIZEOF_FLOAT);
    
    gl.glEnableClientState(GL_NORMAL_ARRAY);
    gl.glNormalPointer(GL_FLOAT, stridef(), 8*Buffers.SIZEOF_FLOAT);
    
    gl.glEnableClientState(GL_TEXTURE_COORD_ARRAY);
    gl.glTexCoordPointer(2, GL_FLOAT, stridef(), 11*Buffers.SIZEOF_FLOAT);
  }
  
  
  //////// User-defined pipeline
  
  /** 
   * Adds a user-defined float-based vertex attribute to the pipeline. 
   * Returns the number of bytes added to the pipeline by the attribute. 
   */
  private int addToPipelinef(GL2 gl, Attribute att, int strideOffset) {
    gl.glVertexAttribPointer( att.getLocation(), 
                              att.getSizeUnits(), 
                              att.getUnitType(), 
                              false, // Vector attributes are expected to already be normalized if they need to be.
                              stridef(), 
                              strideOffset);
    gl.glEnableVertexAttribArray(att.getLocation());
    
  //  System.out.println(att.getLocation() + ", " + att.getSizeUnits() + ", " + GLNames.glName(att.getUnitType()) + ", " + strideOffset + "/" + stridef());
    
    return att.getSizeBytes();
  }
  
  
  /** 
   * Adds a user-defined int-based vertex attribute to the pipeline. 
   * Returns the number of bytes added to the pipeline by the attribute. 
   */
  private int addToPipelinei(GL2 gl, Attribute att, int strideOffset) {
    gl.glVertexAttribIPointer( att.getLocation(), 
                              att.getSizeUnits(), 
                              att.getUnitType(), 
                              stridei(), 
                              strideOffset);
    gl.glEnableVertexAttribArray(att.getLocation());
    
  //  System.out.println(att.getLocation() + ", " + att.getSizeUnits() + ", " + GLNames.glName(att.getUnitType()) + ", " + strideOffset + "/" + stridei());
    
    return att.getSizeBytes();
  }
  
  /** 
   * Adds a user-defined double-based vertex attribute to the pipeline. 
   * Returns the number of bytes added to the pipeline by the attribute. 
   */
  private int addToPipelined(GL2 gl, Attribute att, int strideOffset) {
    gl.getGL4().glVertexAttribLPointer( att.getLocation(), 
                              att.getSizeUnits(), 
                              att.getUnitType(), 
                              strided(), 
                              strideOffset);
    gl.glEnableVertexAttribArray(att.getLocation());
    
  //  System.out.println(att.getLocation() + ", " + att.getSizeUnits() + ", " + GLNames.glName(att.getUnitType()) + ", " + strideOffset + "/" + strided());
    
    return att.getSizeBytes();
  }
  
  
  /** 
   * Adds all user-defined shader attributes to the pipeline. 
   */
  private void addUserPipeline(GL2 gl) {
    gl.glBindBuffer(GL_ARRAY_BUFFER, buffers[0]);
    addUserAttribsfToPipeline(gl);
    
    gl.glBindBuffer(GL_ARRAY_BUFFER, buffers[1]);
    addUserAttribsiToPipeline(gl);
    
    gl.glBindBuffer(GL_ARRAY_BUFFER, buffers[2]);
    addUserAttribsdToPipeline(gl);
  }
  
  
  /** 
   * Adds the user-defined float shader attributes to the pipeline. 
   */
  private void addUserAttribsfToPipeline(GL2 gl) {
    int strideOffset = strideBuiltInf(); 
    
    for(Attribute att : ShaderLibrary.get().getUserAttribsf()) {
      strideOffset += addToPipelinef(gl, att, strideOffset);
    }
  }
  
  
  /** 
   * Adds the user-defined int shader attributes to the pipeline. 
   */
  private void addUserAttribsiToPipeline(GL2 gl) {
    int strideOffset = 0;
    
    for(Attribute att : ShaderLibrary.get().getUserAttribsi()) {
      strideOffset += addToPipelinei(gl, att, strideOffset);
    }
  }
  
  
  /** 
   * Adds the user-defined double shader attributes to the pipeline. 
   */
  private void addUserAttribsdToPipeline(GL2 gl) {
    int strideOffset = 0;
    
    for(Attribute att : ShaderLibrary.get().getUserAttribsd()) {
      strideOffset += addToPipelined(gl, att, strideOffset);
    }
  }
  
  
  //////// Render!
  
  /** Renders a polygon using VBO, given its faces and its vertices. */
  public void render(GL2 gl, List<Face3f> faces, List<Vertex3f> vertices) {
    // Generate and fill the buffers if needed.
    genBuffers(gl, faces, vertices);
    
    // Add vertex attributes to the pipeline.
    addGLPipeline(gl);
    addUserPipeline(gl);
    
    // draw!
    gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffers[3]);
    gl.glDrawElements( GL_TRIANGLES, elementBuffer.capacity(), GL_UNSIGNED_SHORT, 0);
    
    // disable arrays once we're done
    gl.glBindBuffer( GL.GL_ARRAY_BUFFER, 0 );
    gl.glBindBuffer( GL.GL_ELEMENT_ARRAY_BUFFER, 0 );
    gl.glDisableClientState( GL_VERTEX_ARRAY );
    gl.glDisableClientState( GL_COLOR_ARRAY );
    gl.glDisableClientState( GL_NORMAL_ARRAY );
    gl.glDisableClientState( GL_TEXTURE_COORD_ARRAY );
  } 
}