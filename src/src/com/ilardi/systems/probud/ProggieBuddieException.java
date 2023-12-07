/**
 * Created Jun 8, 2023
 */
package com.ilardi.systems.probud;

import com.ilardi.systems.eclipse.IlardiSysEclipseException;

/**
 * @author robert.ilardi
 *
 */

public class ProggieBuddieException extends IlardiSysEclipseException {

  public ProggieBuddieException() {
    super();
  }

  /**
   * @param message
   */
  public ProggieBuddieException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public ProggieBuddieException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public ProggieBuddieException(String message, Throwable cause) {
    super(message, cause);

  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public ProggieBuddieException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
