/**
 * Created Jun 8, 2023
 */
package com.ilardi.systems.eclipse;

import com.ilardi.systems.util.IlardiSystemsException;

/**
 * @author robert.ilardi
 *
 */

public class IlardiSysEclipseException extends IlardiSystemsException {

  public IlardiSysEclipseException() {
    super();
  }

  /**
   * @param message
   */
  public IlardiSysEclipseException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public IlardiSysEclipseException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public IlardiSysEclipseException(String message, Throwable cause) {
    super(message, cause);

  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public IlardiSysEclipseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
