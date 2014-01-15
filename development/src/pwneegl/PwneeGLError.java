package pwneegl;


/** A RuntimeException for any errors specific to PwneeGL. */
public class PwneeGLError extends RuntimeException {
  
  public PwneeGLError() {
    super();
  }
  
  public PwneeGLError(String message) {
    super(message);
  }
  
  public PwneeGLError(String message, Throwable cause) {
    super(message, cause);
  }
  
  public PwneeGLError(Throwable cause) {
    super(cause);
  }
  
}
