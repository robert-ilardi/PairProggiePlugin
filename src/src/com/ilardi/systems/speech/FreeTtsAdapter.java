/**
 * Created Jul 5, 2023
 */
package com.ilardi.systems.speech;

import com.ilardi.systems.probud.ProggieBuddieException;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

/**
 * @author robert.ilardi
 *
 */

public class FreeTtsAdapter {

  private static FreeTtsAdapter instance = null;

  private VoiceManager voiceManager;
  private Voice voice;

  private FreeTtsAdapter() {}

  public synchronized static FreeTtsAdapter getInstance() throws ProggieBuddieException {
    if (instance == null) {
      instance = new FreeTtsAdapter();
      instance.init();
    }

    return instance;
  }

  private synchronized void init() throws ProggieBuddieException {
    // Create a voice using FreeTTS
    voiceManager = VoiceManager.getInstance();
    voice = voiceManager.getVoice("kevin16");

    if (voice == null) {
      throw new ProggieBuddieException("Cannot find the specified voice.");
    }

    // Allocate the voice
    voice.allocate();
  }

  public synchronized void speak(String text) throws ProggieBuddieException {
    boolean spoke;

    spoke = voice.speak(text);

    if (!spoke) {
      throw new ProggieBuddieException("Could Not Speak Selected Text.");
    }
  }

  public synchronized void destroy() {
    if (voice != null) {
      voice.deallocate();
    }
  }

}
