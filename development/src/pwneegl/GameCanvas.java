package pwneegl;

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

import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.FPSCounter;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

import javax.swing.JFrame;

import com.jogamp.opengl.math.FloatUtil;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.AnimatorBase;
import com.jogamp.opengl.util.FPSAnimator;

import static javax.media.opengl.GL.*;  // GL constants
import static javax.media.opengl.GL2.*; // GL2 constants

import pwneegl.input.Keyboard;
import pwneegl.input.Mouse;


public abstract class GameCanvas extends GLCanvas implements GLEventListener {
  
  /** OpenGL Utility Library for this canvas. */
  public GLU glu;

  /** Keyboard input. */
  public Keyboard keyboard;
  
  /** Mouse input. */
  public Mouse mouse;
  
  /** The animator for the game. */
  public AnimatorBase animator;
  
  
  //////// Construction
  
  /** Creates the GameCanvas with a FPSAnimator. */
  public GameCanvas(GLCapabilities caps, int fps) {
    super(caps); 
    animator = new FPSAnimator(fps);
    
    initListeners();
  }
  
  /** Creates the GameCanvas with a FPSAnimator. */
  public GameCanvas(int fps) {
    this(new GLCapabilities(GLProfile.getDefault()), fps);
  }
  
  /** Creates the GameCanvas with a Animator (not FPS-driven). */
  public GameCanvas(GLCapabilities caps) {
    super(caps);
    animator = new Animator();
    
    initListeners();
  }
  
  /** Creates the GameCanvas with a Animator (not FPS-driven). */
  public GameCanvas() {
    this(new GLCapabilities(GLProfile.getDefault()));
  }
  
  
  private void initListeners() {
    addGLEventListener(this);
    
    keyboard = new Keyboard(this);
    mouse = new Mouse(this);
    
    animator.add(this);
  }
  
  
  //////// Animation state and control
  
  /** Starts animating the game. Call this after the game has been fully constructed. */
  public void start() {
    animator.start();
    requestFocus();
  }
  
  /** Returns true iff the game's animator is running. */
  public boolean isRunning() {
    return animator.isAnimating();
  }
  
  /** Returns true iff the game's animator is hard-paused. */
  public boolean isPaused() {
    return animator.isPaused();
  }
  
  /** Hard-pauses the game. */
  public void pause() {
    animator.pause();
  }
  
  
  /** Set whether to display the framerate. */
  public void setDisplayFrameRate(boolean show) {
    if(show) {
      // TODO : Make output stream to support displaying the frame rate in the game window.
      animator.setUpdateFPSFrames(20, System.out);
    }
    else {
      animator.setUpdateFPSFrames(-1, null);
    }
  }
  
  
  //////// GLEventListener
  
  /** Called every frame of animation. */
  @Override
  public void display(GLAutoDrawable drawable) {
    update();
    render(drawable);
  }
  
  /** Sets up defaults for OGL state. */
  @Override
  public void init(GLAutoDrawable drawable) {
  
    GL2 gl = drawable.getGL().getGL2();
    glu = new GLU(); // obtain GL Utilities
    
    gl.glClearColor(0f, 0f, 0f, 0f); // transparent
    gl.glClearDepth(1f); // farthest
    
    gl.glEnable(GL_DEPTH_TEST); // Do depth test for fragment clipping.
    gl.glDepthFunc(GL_LEQUAL); // Overwrite fragment if depth is closer or equal.
    gl.glPolygonMode(GL_FRONT, GL_FILL); // While rendering, only draw polygons whose normals face the camera.
    
    // Use back-face culling
    gl.glEnable(GL_CULL_FACE);
    gl.glCullFace(GL_BACK);
    gl.glFrontFace(GL_CCW);
    
    // Use nicest perspective correction.
    gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); 
    
    // use "smooth" shading.
    gl.glShadeModel(GL_SMOOTH); 
    
    // Enable standard alpha-blending.
    gl.glEnable(GL_BLEND);
    gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
  }
  
  /** Called when the game terminates. */
  @Override
  public void dispose(GLAutoDrawable drawable) {
    // put your cleanup code here
  }
  
  /** Called when the game's view resizes. */
  @Override
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    // called when the user resizes the window.
    
    GL2 gl = drawable.getGL().getGL2();
    
    // The aspect ratio.
    float aspect = (float) (1.0*width/Math.max(height,1));
    
    // Resize view to fit window.
    gl.glViewport(0, 0, width, height); 
    
    // Reset our perspective projection matrix stack so that the aspect ratio matches the viewport.
    gl.glMatrixMode(GL_PROJECTION);
    gl.glLoadIdentity();
    glu.gluPerspective(45.0, aspect, 0.1, 100.0); // fovy, aspect, zNear, zFar
    
    // Reset the model-view matrix stack.
    gl.glMatrixMode(GL_MODELVIEW);
    gl.glLoadIdentity();
    
    
  }
  
  
  //////// Game model update
  
  /** Polls the mouse and keyboard, then performs the game logic for 1 frame. Override me! */
  public void update() { 
    // Poll for the mouse/keyboard state since the last frame.
    mouse.poll();
    keyboard.poll();
  }
  
  
  
  //////// Rendering
  
  /** Clears the OpenGL buffers for the view, then renders the game. Override me! */
  public void render(GLAutoDrawable drawable) {
    
    // Override me and call super.render(drawable).
    
    // clear both the color and depth buffers.
    GL gl = drawable.getGL().getGL2();
    gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); 
    
    
  }
  
}



