/**
 * Created May 23, 2023
 */
package com.ilardi.systems.probud;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 * @author robert.ilardi
 *
 */

public class ProggieBuddiePlugin extends ViewPart {

  private static final Logger logger = LogManager.getLogger(ProggieBuddiePlugin.class);

  public static final String ID = "com.ilardi.systems.probud.ProggieBuddieUi";

  private ProggieBuddieUiCore uiCore;

  public ProggieBuddiePlugin() throws ProggieBuddieRuntimeException {
    super();
  }

  @Override
  public void createPartControl(Composite parent) {
    try {
      logger.debug("Creating Proggie Buddie Plugin UI...");

      uiCore = ProggieBuddieUiCore.getInstance();
      uiCore.init(this, parent);

      logger.debug("Proggie Buddie Plugin UI Successfully Created and Initialized!");
    } //End try block
    catch (ProggieBuddieException e) {
      logger.throwing(e);
      //e.printStackTrace();
    }
  }

  @Override
  public void setFocus() {
    if (uiCore != null) {
      uiCore.setFocus();
    }
  }

  @Override
  public void setPartName(String partName) {
    super.setPartName(partName);
  };

  @Override
  public void setContentDescription(String contentDescription) {
    super.setContentDescription(contentDescription);
  };

  @Override
  public void setTitleToolTip(String titleToolTip) {
    super.setTitleToolTip(titleToolTip);
  };

  @Override
  public void setTitleImage(Image titleImage) {
    super.setTitleImage(titleImage);
  };

}
