/**
 * Created Sep 3, 2023
 */
package com.ilardi.systems.util;

import java.io.Serializable;

/**
 * @author robert.ilardi
 *
 */

public class IntWrapper implements Serializable {

  private int num;

  public IntWrapper() {}

  public int getNum() {
    return num;
  }

  public void setNum(int num) {
    this.num = num;
  }

}
