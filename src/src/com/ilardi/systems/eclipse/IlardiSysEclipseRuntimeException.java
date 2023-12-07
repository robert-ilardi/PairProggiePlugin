/**
 * Created Jun 22, 2023
 */
package com.ilardi.systems.eclipse;

import com.ilardi.systems.util.IlardiSystemsRuntimeException;

/**
 * @author robert.ilardi
 *
 */

public class IlardiSysEclipseRuntimeException extends IlardiSystemsRuntimeException {

  public IlardiSysEclipseRuntimeException() {
    super();
  }

  /**
   * @param message
   */
  public IlardiSysEclipseRuntimeException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public IlardiSysEclipseRuntimeException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public IlardiSysEclipseRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public IlardiSysEclipseRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
