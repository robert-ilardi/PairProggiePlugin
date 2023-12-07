/**
 * Created Jan 31, 2021
 */
package com.ilardi.systems.util;

/**
 * @author rilardi
 *
 */

public class IlardiSystemsRuntimeException extends RuntimeException {

  public IlardiSystemsRuntimeException() {
    super();
  }

  /**
   * @param message
   */
  public IlardiSystemsRuntimeException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public IlardiSystemsRuntimeException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public IlardiSystemsRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public IlardiSystemsRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
