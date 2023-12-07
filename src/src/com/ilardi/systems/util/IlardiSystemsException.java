/**
 * Created Jan 7, 2021
 */
package com.ilardi.systems.util;

/**
 * @author rilardi
 *
 */

public class IlardiSystemsException extends Exception {

  public IlardiSystemsException() {
    super();
  }

  /**
   * @param message
   */
  public IlardiSystemsException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public IlardiSystemsException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public IlardiSystemsException(String message, Throwable cause) {
    super(message, cause);
    // TODO Auto-generated constructor stub
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public IlardiSystemsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
