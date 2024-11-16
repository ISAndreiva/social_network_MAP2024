package internal.andreiva.socialnetwork.gui;

public class GuiException extends RuntimeException
{
  public GuiException()
  {
    super();
  }

  public GuiException(String message)
  {
    super(message);
  }

  public GuiException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public GuiException(Throwable cause)
  {
    super(cause);
  }

  protected GuiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
  {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
