/**
 * Created Jun 8, 2023
 */
package com.ilardi.systems.chatai;

import com.ilardi.systems.util.IlardiSystemsException;

/**
 * @author robert.ilardi
 *
 */

public class ChatAiException extends IlardiSystemsException {

  public ChatAiException() {
    super();
  }

  /**
   * @param message
   */
  public ChatAiException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public ChatAiException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public ChatAiException(String message, Throwable cause) {
    super(message, cause);

  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public ChatAiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
