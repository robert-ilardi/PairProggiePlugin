/**
 * Created Jul 2, 2023
 */
package com.ilardi.systems.probud;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

/**
 * @author robert.ilardi
 *
 */

public class ProggieBuddieUi {

  //private static final Logger logger = LogManager.getLogger(ProggieBuddieUi.class);

  protected Image titleImg;

  protected Composite parent;

  protected CTabFolder tabFolder;

  protected CTabItem chatTab;
  protected Composite chatComposite;
  protected Composite chatContextControlPanel;
  protected Composite chatRingBufferPanel;
  protected Composite chatInputControlPanel;
  protected Composite chatDisplayPanel;
  protected Label chatContextLabel;
  protected Label chatRingBufferLabel;
  protected Combo chatContextCombo;
  protected Text chatDisplay;
  protected Text chatInput;
  protected Button chatSendButton;
  protected Button chatRingBufferClearButton;

  protected CTabItem consoleTab;
  protected Composite consoleComposite;
  protected Text consoleDisplay;

  protected CTabItem propertiesTab;
  protected Composite propertiesComposite;
  protected Composite propertiesTablePanel;
  protected Table propertiesTable;
  protected TableColumn propNameCol;
  protected TableColumn propValueCol;
  protected Composite propertiesButtonPanel;
  protected Button loadPropsButton;
  protected Button savePropsButton;
  protected Button addPropsButton;
  protected Button removePropsButton;
  protected Button editPropsButton;

  protected CTabItem historyTab;
  protected Composite historyComposite;
  protected CTabFolder historySubTabFolder;

  protected CTabItem historyGlobalChatTab;
  protected Composite historyGlobalChatComposite;
  protected Text historyGlobalChatDisplay;

  protected CTabItem historyReqResTab;
  protected Composite historyReqResComposite;

  public ProggieBuddieUi(Composite parent) {
    this.parent = parent;
  }

}
