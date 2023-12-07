/**
 * Created Jun 22, 2023
 */
package com.ilardi.systems.probud;

import com.ilardi.systems.eclipse.IlardiSysEclipseException;

/**
 * @author robert.ilardi
 *
 */

public class ProggieBuddieRuntimeException extends IlardiSysEclipseException {

  public ProggieBuddieRuntimeException() {
    super();
  }

  /**
   * @param message
   */
  public ProggieBuddieRuntimeException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public ProggieBuddieRuntimeException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public ProggieBuddieRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public ProggieBuddieRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
