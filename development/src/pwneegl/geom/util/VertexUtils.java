package pwneegl.geom.util;

import java.util.List;

import pwneegl.geom.Poly3f;
import pwneegl.geom.Vertex3f;
import pwneegl.math.Point3f;
import pwneegl.math.PwneeMath;
import pwneegl.math.Vector3f;



public class VertexUtils {
  
  /** 
   * Generates and sets the vertex normals for a Poly3f, using spherical 
   * coordinates. 
   */
  public static void genNormalsSphere(Poly3f shape) {
    Point3f center = getCenter(shape);
    
    for(Vertex3f v : shape.getVertices()) {
      Vector3f vec = new Vector3f(  v.getX() - center.getX(), 
                                    v.getY() - center.getY(), 
                                    v.getZ() - center.getZ());
      vec = vec.normalize();
      
      v.setNormal(vec.getCoords());
    }
  }
  
  
  /** 
   * Generates and sets the texture coordinates for a Poly3f's vertices, using
   * spherical coordinates.
   */
  public static void genTexCoordsSphere(Poly3f shape) {
    Point3f center = getCenter(shape);
    
    for(Vertex3f v : shape.getVertices()) {
      Vector3f vec = new Vector3f(  v.getX() - center.getX(), 
                                    v.getY() - center.getY(), 
                                    v.getZ() - center.getZ());
      
      float[] polar = PwneeMath.toRadial(vec.getX(), vec.getY(), vec.getZ());
      float texS = polar[2]/PwneeMath.TAU;
      float texT = (PwneeMath.TAU/4-polar[1])/(PwneeMath.TAU/2);
      v.setTexCoords(texS, texT);
    }
  }
  
  
  
  /** Returns the central point of a shape. This is the average point among all the vertices in the shape. */
  public static Point3f getCenter(Poly3f shape) {
    List<Vertex3f> vertices = shape.getVertices();
    
    // find the center of the shape by averaging its vertices.
    float avgX = 0;
    float avgY = 0;
    float avgZ = 0;
    for(Vertex3f v : vertices) {
      avgX += v.getX();
      avgY += v.getY();
      avgZ += v.getZ();
    }
    avgX /= vertices.size();
    avgY /= vertices.size();
    avgZ /= vertices.size();
    
    return new Point3f(avgX, avgY, avgZ);
  }
}

