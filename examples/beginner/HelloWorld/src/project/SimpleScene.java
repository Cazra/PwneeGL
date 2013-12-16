package project;

import java.awt.event.KeyEvent;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

import static javax.media.opengl.GL.*;  // GL constants
import static javax.media.opengl.GL2.*; // GL2 constants

import pwneegl.GameCanvas;
import pwneegl.GameWindow;
import pwneegl.geom.Poly3f;
import pwneegl.geom.Shapes;
import pwneegl.light.Fog;
import pwneegl.light.Light;
import pwneegl.light.LightDirectional;
import pwneegl.light.LightPoint;
import pwneegl.light.LightSpot;
import pwneegl.math.PwneeMath;
import pwneegl.shader.ShaderLibrary;
import pwneegl.shader.ShaderProgram;


/** 
 * Displays a simple rotating cube with keyboard controls. 
 * Right now though this example is more like a playground to test things I've
 * been implementing into the framework.
 */
public class SimpleScene extends GameCanvas {
  
  private float cubeAngleX = 0f;
  private float cubeSpeedX = -0f;
  
  private float cubeAngleY = 0f;
  private float cubeSpeedY = -PwneeMath.TAU/150;
  
  private float cubeAngleZ = 0f;
  private float cubeSpeedZ = -0f;
  
  private final float ANGLE_INC = PwneeMath.TAU/2000;
  
  private TestCube[] testCubes;
  
  private Light light;
  
  private Fog fog;
  
  //////// Shader-specific vars
  
  private float time;
  
  /** Creates the GameCanvas with the desired framerate. */
  public SimpleScene(int fps) {
    super(fps);
    
    testCubes = new TestCube[4000];
    
    for(int i = 0; i < 10; i++) {
      for(int j = 0; j < 10; j++) {
        for(int k = 0; k < 10; k++) {
          testCubes[i+10*j+100*k] = new TestCube(-2+j, -2+k, -7*(i+1));
          
          testCubes[i+10*j+100*k+1000] = new TestCube(-2.5f+j, -2+k, -7*(i+1));
          testCubes[i+10*j+100*k+2000] = new TestCube(-2+j, -2.5f+k, -7*(i+1));
          testCubes[i+10*j+100*k+3000] = new TestCube(-2.5f+j, -2.5f+k, -7*(i+1));
        }
      }
    }
    
  
    light =  new LightDirectional( -1f, 1f, 1f);
    
    fog = new Fog(0f, 0f, 0.5f);
    fog.setDensity(0.05f);
    
    time = 0f;
    
    setDisplayFrameRate(true);
  }
  
  
  @Override
  public void init(GLAutoDrawable drawable) {
    super.init(drawable);
    
    GL2 gl = drawable.getGL().getGL2();
    
    // Use our shader program.
    //shader = new ShaderProgram(gl, "perFragmentPhongVShader.glsl", "perFragmentPhongFShader.glsl", true);
    //shader.useProgram(gl);
    ShaderLibrary.put("shader", new ShaderProgram(gl, "perFragmentPhongVShader.glsl", "perFragmentPhongFShader.glsl", true));
    ShaderLibrary.use(gl, "shader");
  }
  
  
  
  
  /** Animate one frame. */
  public void update() {
    super.update();
    
    time += 0.01f;
    
    // keyboard controls for the cube's angle.
    // left/right controls rotation around y axis.
    if(keyboard.isPressed(KeyEvent.VK_LEFT)) {
      cubeSpeedY -= ANGLE_INC;
    }
    if(keyboard.isPressed(KeyEvent.VK_RIGHT)) {
      cubeSpeedY += ANGLE_INC;
    }
    
    // up/down controls rotation around x axis.
    if(keyboard.isPressed(KeyEvent.VK_UP)) {
      cubeSpeedX -= ANGLE_INC;
    }
    if(keyboard.isPressed(KeyEvent.VK_DOWN)) {
      cubeSpeedX += ANGLE_INC;
    }
    
    // z/x controls rotation around z axis.
    if(keyboard.isPressed(KeyEvent.VK_Z)) {
      cubeSpeedZ -= ANGLE_INC;
    }
    if(keyboard.isPressed(KeyEvent.VK_X)) {
      cubeSpeedZ += ANGLE_INC;
    }
    
    for(TestCube testCube : testCubes) {
      testCube.angleX += cubeSpeedX;
      testCube.angleY += cubeSpeedY;
      testCube.angleZ += cubeSpeedZ;
    }
  }
  
  
  /** Render one frame. */
  public void render(GLAutoDrawable drawable) {
    GL2 gl = drawable.getGL().getGL2();
    
    gl.glClearColor(0f, 0f, 0.5f, 1f);
    super.render(drawable);
    
    ShaderProgram shader = ShaderLibrary.get();
    
    int shader_time = shader.getUniform(gl, "time");
    gl.glUniform1f(shader_time, time);
    
    gl.glLoadIdentity(); // reset the model-view matrix.
    
    light.glLight(gl, 0);
    fog.glFog(gl);
    
    // Render the cube.
    for(TestCube testCube : testCubes) {
      testCube.render(gl);
    }
    
  }
  
  
  
  
  //////// Main
  
  public static void main(String[] args) {
    GameWindow window = new GameWindow("Hello World", 300, 300);
    GameCanvas canvas = new SimpleScene(60);
    window.add(canvas);
    window.setVisible(true);
    canvas.start();
  }
}
