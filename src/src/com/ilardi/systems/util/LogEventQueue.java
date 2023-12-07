/**
 * Created Jul 18, 2023
 */
package com.ilardi.systems.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author robert.ilardi
 *
 */

public class LogEventQueue {

  private static LogEventQueue instance = null;

  private final Object queueLock;

  private final List<String> queue;

  private volatile boolean queueEnabled;

  private LogEventQueue() {
    queueLock = new Object();
    queue = new ArrayList<String>();
    queueEnabled = true;
  }

  public static synchronized LogEventQueue getInstance() {
    if (instance == null) {
      instance = new LogEventQueue();
    }

    return instance;
  }

  public String dequeueLogMessage() throws InterruptedException {
    synchronized (queueLock) {
      try {
        String logMesgTxt = null;

        while (queue.isEmpty() && queueEnabled) {
          queueLock.wait();
        }

        if (!queue.isEmpty() && queueEnabled) {
          logMesgTxt = queue.remove(0);
        }

        return logMesgTxt;
      }
      finally {
        queueLock.notifyAll();
      }
    }
  }

  public void enqueueLogMessage(String logMesgTxt) {
    synchronized (queueLock) {
      if (queueEnabled) {
        queue.add(logMesgTxt);
      }

      queueLock.notifyAll();
    }
  }

  public void clearQueue() {
    synchronized (queueLock) {
      queue.clear();
      queueLock.notifyAll();
    }
  }

  public boolean isQueueEnabled() {
    synchronized (queueLock) {
      return queueEnabled;
    }
  }

  public void setQueueEnabled(boolean queueEnabled) {
    synchronized (queueLock) {
      this.queueEnabled = queueEnabled;
      queueLock.notifyAll();
    }
  }

}
