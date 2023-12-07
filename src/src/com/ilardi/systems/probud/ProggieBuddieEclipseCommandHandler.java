/**
 * Created Jul 6, 2023
 */
package com.ilardi.systems.probud;

import static com.ilardi.systems.probud.ProggieBuddieConstants.PB_ACTION_CODE_PARAM_NAME;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PB_ACTION_CODE_PARAM_VALUE_CODE_GEN;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PB_ACTION_CODE_PARAM_VALUE_CODE_GEN_CLASS;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PB_ACTION_CODE_PARAM_VALUE_CODE_GEN_METHOD;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PB_ACTION_CODE_PARAM_VALUE_CODE_REVIEW;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PB_ACTION_CODE_PARAM_VALUE_CODE_REVIEW_CLASS;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PB_ACTION_CODE_PARAM_VALUE_CODE_REVIEW_METHOD;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PB_ACTION_CODE_PARAM_VALUE_COMMENT;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PB_ACTION_CODE_PARAM_VALUE_COMMENT_CLASS;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PB_ACTION_CODE_PARAM_VALUE_COMMENT_METHOD;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PB_ACTION_CODE_PARAM_VALUE_GENERAL_CHAT;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PB_ACTION_CODE_PARAM_VALUE_HOTKEY;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PB_ACTION_CODE_PARAM_VALUE_OPTIMIZE;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PB_ACTION_CODE_PARAM_VALUE_OPTIMIZE_CLASS;
import static com.ilardi.systems.probud.ProggieBuddieConstants.PB_ACTION_CODE_PARAM_VALUE_OPTIMIZE_METHOD;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;

/**
 * @author robert.ilardi
 *
 */
public class ProggieBuddieEclipseCommandHandler extends AbstractHandler {

  private static final Logger logger = LogManager.getLogger(ProggieBuddieEclipseCommandHandler.class);

  private ProggieBuddieUiCore uiCore;

  public ProggieBuddieEclipseCommandHandler() {
    super();

    uiCore = ProggieBuddieUiCore.getInstance();
  }

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    try {
      //System.out.println("PB Eclipse Command Handler Received Event: " + event);
      logger.debug("PB Eclipse Command Handler Received Event: " + event);

      //IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
      //MessageDialog.openInformation(window.getShell(), "Your Context Submenu", "Submenu item selected");

      String actionCode = event.getParameter(PB_ACTION_CODE_PARAM_NAME);

      logger.debug("PB Eclipse Command Handler Action Code = " + actionCode);

      if (PB_ACTION_CODE_PARAM_VALUE_HOTKEY.equals(actionCode)) {
        // Determine the trigger source
        Object trigger = event.getTrigger();

        logger.debug("Event Trigger: " + trigger);

        if (trigger instanceof Event) {
          Event te = (Event) trigger;

          logger.debug("TriggerEvent = " + te + "; Key Code = " + te.keyCode);

          if (te.keyCode == SWT.SPACE) {
            processHotKey();
          }
        }
      }
      else if (PB_ACTION_CODE_PARAM_VALUE_GENERAL_CHAT.equals(actionCode)) {
        processGeneralChat();
      }
      else if (PB_ACTION_CODE_PARAM_VALUE_COMMENT.equals(actionCode)) {
        processComment();
      }
      else if (PB_ACTION_CODE_PARAM_VALUE_COMMENT_CLASS.equals(actionCode)) {
        processCommentClass();
      }
      else if (PB_ACTION_CODE_PARAM_VALUE_COMMENT_METHOD.equals(actionCode)) {
        processCommentMethod();
      }
      else if (PB_ACTION_CODE_PARAM_VALUE_CODE_GEN.equals(actionCode)) {
        processCodeGen();
      }
      else if (PB_ACTION_CODE_PARAM_VALUE_CODE_GEN_CLASS.equals(actionCode)) {
        processCodeGenClass();
      }
      else if (PB_ACTION_CODE_PARAM_VALUE_CODE_GEN_METHOD.equals(actionCode)) {
        processCodeGenMethod();
      }
      else if (PB_ACTION_CODE_PARAM_VALUE_CODE_REVIEW.equals(actionCode)) {
        processCodeReview();
      }
      else if (PB_ACTION_CODE_PARAM_VALUE_CODE_REVIEW_CLASS.equals(actionCode)) {
        processCodeReviewClass();
      }
      else if (PB_ACTION_CODE_PARAM_VALUE_CODE_REVIEW_METHOD.equals(actionCode)) {
        processCodeReviewMethod();
      }
      else if (PB_ACTION_CODE_PARAM_VALUE_OPTIMIZE.equals(actionCode)) {
        processOptimize();
      }
      else if (PB_ACTION_CODE_PARAM_VALUE_OPTIMIZE_CLASS.equals(actionCode)) {
        processOptimizeClass();
      }
      else if (PB_ACTION_CODE_PARAM_VALUE_OPTIMIZE_METHOD.equals(actionCode)) {
        processOptimizeMethod();
      }

      return null;
    } //End try block
    catch (Exception e) {
      throw new ExecutionException(e.getMessage(), e);
    }
  }

  private void processHotKey() throws ProggieBuddieException {
    uiCore.handleEclipseHotKeyInlineChat();
    uiCore.processLastResponse(isListenerAttached());
  }

  private void processGeneralChat() throws ProggieBuddieException {
    uiCore.handleEclipseMenuItemSendInlineGeneralChat();
    uiCore.processLastResponse(isListenerAttached());
  }

  private void processComment() throws ProggieBuddieException {
    uiCore.handleEclipseMenuItemAskComment();
    uiCore.processLastResponse(isListenerAttached());
  }

  private void processCommentClass() throws ProggieBuddieException {
    uiCore.handleEclipseMenuItemAskCommentClass();
    uiCore.processLastResponse(isListenerAttached());
  }

  private void processCommentMethod() throws ProggieBuddieException {
    uiCore.handleEclipseMenuItemAskCommentMethod();
    uiCore.processLastResponse(isListenerAttached());
  }

  private void processCodeGen() throws ProggieBuddieException {
    uiCore.handleEclipseMenuItemAskCodeGen();
    uiCore.processLastResponse(isListenerAttached());
  }

  private void processCodeGenClass() throws ProggieBuddieException {
    uiCore.handleEclipseMenuItemAskCodeGenClass();
    uiCore.processLastResponse(isListenerAttached());
  }

  private void processCodeGenMethod() throws ProggieBuddieException {
    uiCore.handleEclipseMenuItemAskCodeGenMethod();
    uiCore.processLastResponse(isListenerAttached());
  }

  private void processCodeReview() throws ProggieBuddieException {
    uiCore.handleEclipseMenuItemAskCodeReview();
    uiCore.processLastResponse(isListenerAttached());
  }

  private void processCodeReviewClass() throws ProggieBuddieException {
    uiCore.handleEclipseMenuItemAskCodeReviewClass();
    uiCore.processLastResponse(isListenerAttached());
  }

  private void processCodeReviewMethod() throws ProggieBuddieException {
    uiCore.handleEclipseMenuItemAskCodeReviewMethod();
    uiCore.processLastResponse(isListenerAttached());
  }

  private void processOptimize() throws ProggieBuddieException {
    uiCore.handleEclipseMenuItemAskOptimize();
    uiCore.processLastResponse(isListenerAttached());
  }

  private void processOptimizeClass() throws ProggieBuddieException {
    uiCore.handleEclipseMenuItemAskOptimizeClass();
    uiCore.processLastResponse(isListenerAttached());
  }

  private void processOptimizeMethod() throws ProggieBuddieException {
    uiCore.handleEclipseMenuItemAskOptimizeMethod();
    uiCore.processLastResponse(isListenerAttached());
  }

}
