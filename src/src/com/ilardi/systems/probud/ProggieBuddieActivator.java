package com.ilardi.systems.probud;

import static com.ilardi.systems.probud.ProggieBuddieConstants.PROPS_ENABLE_TEXT_TO_SPEECH;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.ilardi.systems.speech.FreeTtsAdapter;
import com.ilardi.systems.util.ApplicationContext;
import com.ilardi.systems.util.IlardiSystemsException;
import com.ilardi.systems.util.LogEventQueue;

/**
 * The activator class controls the plug-in life cycle
 */

public class ProggieBuddieActivator extends AbstractUIPlugin {

  private static final Logger logger = LogManager.getLogger(ProggieBuddieActivator.class);

  // The plug-in ID
  public static final String PLUGIN_ID = "com.ilardi.systems.probud.ProggieBuddie"; //$NON-NLS-1$

  // The shared instance
  private static ProggieBuddieActivator plugin;

  //Ilardi Systems App Context
  private ApplicationContext appCntxt;

  private boolean textToSpeechEnabled;

  //private ChatSession cSession;

  public ProggieBuddieActivator() {
    super();
  }

  @Override
  public void start(BundleContext context) throws Exception {
    super.start(context);
    plugin = this;

    logger.debug("Starting Plugin Activator: " + this.getClass().getName());

    appCntxt = ApplicationContext.getInstance();

    loadUserProperties();

    //cSession = initNewChatSession();
    //appCntxt.keyValueStorePut(APP_CNTXT_KEY_CHAT_SESSION, cSession);

    appCntxt.setAppRunning(true);

    logger.debug("Finished Starting Plugin Activator: " + this.getClass().getName());
  }

  private void loadUserProperties() throws IlardiSystemsException {
    String tmp;

    logger.debug("Proggie Buddie is Requesting the Application Context Loads User Properties...");

    appCntxt.loadUserProperties();

    tmp = appCntxt.getUserProperty(PROPS_ENABLE_TEXT_TO_SPEECH);
    if (tmp != null) {
      tmp = tmp.trim();
    }
    textToSpeechEnabled = "TRUE".equalsIgnoreCase(tmp);
  }

  @Override
  public void stop(BundleContext context) throws Exception {
    plugin = null;
    super.stop(context);

    logger.debug("Stopping Plugin Activator: " + this.getClass().getName());

    closeCodeObserver();

    closeFreeTts();

    closeLogEventQueue();

    appCntxt.setAppRunning(false);
    appCntxt.destroy();
    appCntxt = null;
  }

  private void closeLogEventQueue() {
    LogEventQueue leQueue;

    leQueue = LogEventQueue.getInstance();
    leQueue.setQueueEnabled(false);
    leQueue.clearQueue();
  }

  private void closeCodeObserver() {
    CodeObserver cdOb = CodeObserver.getInstance();
    cdOb.stopObservation();
  }

  private void closeFreeTts() throws ProggieBuddieException {
    if (textToSpeechEnabled) {
      FreeTtsAdapter tts = FreeTtsAdapter.getInstance();
      tts.destroy();
    }
  }

  /**
   * Returns the shared instance
   *
   * @return the shared instance
   */
  public static ProggieBuddieActivator getDefault() {
    return plugin;
  }

}
