/**
 * Created Jul 18, 2023
 */
package com.ilardi.systems.chatai;

import java.util.HashMap;

/**
 * @author robert.ilardi
 *
 */

public class MultiChatRingBuffers {

  private final HashMap<ChatKey, SmartChatRingBuffer> ringBufferMap;

  public MultiChatRingBuffers() {
    ringBufferMap = new HashMap<ChatKey, SmartChatRingBuffer>();
  }

  public synchronized SmartChatRingBuffer addRingBuffer(ChatKey cKey, SmartChatRingBuffer ringBuffer) {
    return ringBufferMap.put(cKey, ringBuffer);
  }

  public synchronized SmartChatRingBuffer getRingBuffer(ChatKey cKey) {
    return ringBufferMap.get(cKey);
  }

  public synchronized SmartChatRingBuffer removeRingBuffer(ChatKey cKey) {
    return ringBufferMap.remove(cKey);
  }

  public synchronized boolean containsRingBuffer(ChatKey cKey) {
    return ringBufferMap.containsKey(cKey);
  }

  public synchronized void clearRingBufferMap() {
    ringBufferMap.clear();
  }

  public synchronized int ringBufferCount() {
    return ringBufferMap.size();
  }

  public synchronized SmartChatRingBuffer addRingBuffer(SmartChatRingBuffer ringBuffer) {
    return addRingBuffer(ringBuffer.getChatKey(), ringBuffer);
  }

}
