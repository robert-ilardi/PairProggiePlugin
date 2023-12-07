/**
 * Created Sep 10, 2023
 */
package com.ilardi.systems.chatai;

import java.io.Serializable;

/**
 * @author robert.ilardi
 *
 */

public enum SystemChatTag implements Serializable {

  SYS_MAIN, SYS_DYNAMIC, SYS_NON_PROJECT, NON_SYSTEM;

  public static boolean isSystemChatTag(SystemChatTag sysTag) {
    boolean sysChat;

    sysChat = (sysTag != null && !NON_SYSTEM.equals(sysTag));

    return sysChat;
  }

}
