package pwneegl.geom.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import pwneegl.PwneeGLError;
import pwneegl.geom.Face3f;
import pwneegl.geom.Poly3f;
import pwneegl.geom.Vertex3f;

/** 
 * A utility class for reading Poly3f objects from Wavefront OBJ text files
 * and for writing Poly3f objects to Wavefront OBJ text files. 
 */
public class WavefrontIO {
  
  /** Reads the object from a file path. */
  public static Poly3f readFromFile(String path) {
    FileReader fr;
    try {
      fr = new FileReader(path);
    }
    catch (FileNotFoundException e) {
      throw new PwneeGLError("Could not load Wavefront OBJ file: " + path, e);
    }
    return readFromReader(fr);
  }
  
  /** Reads the object from a resource path. */
  public static Poly3f readFromResource(String path) {
    InputStream is = new WavefrontIO().getClass().getClassLoader().getResourceAsStream(path);
    return readFromReader(new InputStreamReader(is));
  }
  
  /** Reads the object from a Reader. */
  public static Poly3f readFromReader(Reader r) {
    BufferedReader br = new BufferedReader(r);
    
    List<float[]> xyz = new ArrayList<>();
    List<float[]> n = new ArrayList<>();
    List<float[]> st = new ArrayList<>();
    
    List<Vertex3f> vertices = new ArrayList<>();
    List<Face3f> faces = new ArrayList<>();
    
    try {
      String line = br.readLine().replace("  ", " ");
      while(line != null) {
        line = line.replace("  ", " ");
        String[] tokens = line.split(" ");
        
        // Specify a vertex
        if(line.startsWith("v ")) {
          float x = Float.parseFloat(tokens[1]);
          float y = Float.parseFloat(tokens[2]);
          float z = Float.parseFloat(tokens[3]);
          xyz.add(new float[] {x, y, z});
        }
        
        // vertex normals
        else if(line.startsWith("vn ")) {
          float x = Float.parseFloat(tokens[1]);
          float y = Float.parseFloat(tokens[2]);
          float z = Float.parseFloat(tokens[3]);
          n.add(new float[] {x, y, z});
        }
        
        // vertex texture coordinates
        else if(line.startsWith("vt ")) {
          float s = Float.parseFloat(tokens[1]);
          float t = Float.parseFloat(tokens[2]);
          st.add(new float[] {s, t});
        }
        
        // Specify a face
        else if(line.startsWith("f ")) {
          int index = vertices.size();
          
          // tri poly
          if(tokens.length == 4) {
            vertices.add(createVertex(xyz, st, n, tokens[1]));
            vertices.add(createVertex(xyz, st, n, tokens[2]));
            vertices.add(createVertex(xyz, st, n, tokens[3]));
            
            faces.add(new Face3f(index, index+1, index+2));
          }
          
          // quad poly
          if(tokens.length == 5) {
            vertices.add(createVertex(xyz, st, n, tokens[1]));
            vertices.add(createVertex(xyz, st, n, tokens[2]));
            vertices.add(createVertex(xyz, st, n, tokens[3]));
            vertices.add(createVertex(xyz, st, n, tokens[4]));
            
            faces.add(new Face3f(index, index+1, index+2));
            faces.add(new Face3f(index, index+2, index+3));
          }
          
        }
        
        line = br.readLine();
      }
    }
    catch (IOException e) {
      throw new PwneeGLError("Error reading Wavefront object", e);
    }
    
    return new Poly3f(vertices, faces);
  }
  
  
  private static Vertex3f createVertex(List<float[]> xyz, List<float[]> st, List<float[]> n, String token) {
    String[] v = token.split("/");
    
    int xyzIndex = Integer.parseInt(v[0]) - 1;
    Vertex3f result = new Vertex3f(xyz.get(xyzIndex));
    
    if(v.length >= 2 && !"".equals(v[1])) {
      int stIndex = Integer.parseInt(v[1]) - 1;
      result.setTexCoords(st.get(stIndex));
    }
    else if(st.size() == xyz.size()) {
      result.setTexCoords(st.get(xyzIndex));
    }
    
    if(v.length >= 3 && !"".equals(v[2])) {
      int nIndex = Integer.parseInt(v[2]) - 1;
      result.setNormal(n.get(nIndex));
    }
    else if(n.size() == xyz.size()) {
      result.setNormal(n.get(xyzIndex));
    }
    
    
    return result;
  }
  
  
}
