/**
 * Created Jun 22, 2023
 */
package com.ilardi.systems.chatai;

import com.ilardi.systems.util.IlardiSystemsRuntimeException;

/**
 * @author robert.ilardi
 *
 */

public class ChatAiRuntimeException extends IlardiSystemsRuntimeException {

  public ChatAiRuntimeException() {
    super();
  }

  /**
   * @param message
   */
  public ChatAiRuntimeException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public ChatAiRuntimeException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public ChatAiRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public ChatAiRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
