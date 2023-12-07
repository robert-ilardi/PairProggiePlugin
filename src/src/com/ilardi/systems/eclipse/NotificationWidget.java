/**
 * Created Jul 28, 2023
 */
package com.ilardi.systems.eclipse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author robert.ilardi
 *
 */

public class NotificationWidget {

  private static final Logger logger = LogManager.getLogger(NotificationWidget.class);

  private static final int MIN_DELAY = 3000;

  private static final int SHELL_X_BUF = 5;
  private static final int SHELL_Y_BUF = 5;

  private static final int NO_MESG_WIDTH = 200;
  private static final int NO_MESG_HEIGHT = 25;

  private static final int MIN_WIDTH = 350;
  private static final int MIN_HEIGHT = 200;

  private static final int MAX_WIDTH = 512;
  private static final int MAX_HEIGHT = 256;

  //private static final int DEFAULT_POS_X=250;
  //private static final int DEFAULT_POS_Y=250;

  private Shell notificationShell;

  private Text notificationArea;

  public NotificationWidget(Composite parent) {
    Display display;

    if (parent != null) {
      display = parent.getDisplay();
    }
    else {
      display = Display.getDefault();
    }

    display.syncExec(() -> {
      notificationShell = new Shell(display, SWT.TOOL | SWT.NO_TRIM | SWT.ON_TOP);
      notificationShell.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
      notificationShell.setLayout(new FillLayout());

      //notificationArea = new Label(notificationShell, SWT.NONE);
      notificationArea = new Text(notificationShell, SWT.MULTI | SWT.WRAP | SWT.NO_SCROLL);
      notificationArea.setEditable(false);
      notificationArea.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));

      notificationShell.open();
      notificationShell.setVisible(false);
    });
  }

  public NotificationWidget() {
    this(null);
  }

  public void showNotification(Point loc, String mesg, int secs) {
    notificationShell.getDisplay().asyncExec(() -> {
      try {
        logger.debug("Showing Notification Popup - (Duration: " + secs + "secs; At Location: " + loc + "; Mesg: " + mesg + ")");

        //Show the Notification
        Point optSz;
        notificationShell.setLocation(loc);
        notificationShell.setAlpha(255);

        notificationArea.setText(mesg);

        optSz = computeOptimalNotificationSize();

        notificationArea.setSize(optSz);
        //notificationArea.pack(true);

        //optSz = notificationArea.getSize();

        logger.debug("Notification Size: " + optSz);

        notificationShell.setSize(optSz.x + SHELL_X_BUF, optSz.y + SHELL_Y_BUF);

        notificationShell.setVisible(true);
        notificationShell.open();

        // Schedule the hiding of the Notification after "secs" seconds
        int delay = secs * 1000;

        if (delay > MIN_DELAY) {
          delay = MIN_DELAY;
        }

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
          @Override
          public void run() {
            notificationShell.getDisplay().asyncExec(() -> hideNotification());
          }
        }, delay);
      } //End try block
      catch (Exception e) {
        //e.printStackTrace();
        logger.throwing(e);
      }
    });
  }

  private void hideNotification() {
    logger.debug("Fading Out Notification Popup");

    try {
      for (int i = 250; i >= 0; i -= 2) {
        notificationShell.setAlpha(i);
        Thread.sleep(10);
      }

      notificationArea.setVisible(false);
    }
    catch (Exception e) {
      //e.printStackTrace();
      logger.throwing(e);
    }
  }

  private Point computeOptimalNotificationSize() throws IOException {
    Point optimalSz;
    GC g = null;
    String text;
    int maxLineLen, lineLen, lineCnt, charCnt;
    int optimalWidth = 0;
    int optimalHeight = 0;
    StringReader sr = null;
    BufferedReader br = null;
    String line;
    FontMetrics fMetrics;
    //Font fnt;
    //FontData[] fDataArr;
    //FontData fData;
    Device dev;
    int fHeightPts;
    double fAvgWidthPts;
    int fPxlsX, fPxlsY;
    Point dpi;

    logger.debug("Computing Optimal Notification Popup Size");

    try {
      text = notificationArea.getText();

      if (text != null) {
        text = text.trim();
        charCnt = text.length();
      }
      else {
        logger.warn("Text is NULL when trying to calculate Optimal Notification Area Size!");
        charCnt = 0;
      }

      if (charCnt > 0) {
        //Get Graphic Device Primitives
        g = new GC(notificationArea);
        dev = g.getDevice();
        dpi = dev.getDPI();

        //Get Font Data
        //fnt = notificationArea.getFont();
        fMetrics = g.getFontMetrics();
        //fDataArr = fnt.getFontData();
        //fData = fDataArr[0];

        //Get Font Point Sizes
        fHeightPts = fMetrics.getHeight();

        fAvgWidthPts = fMetrics.getAverageCharacterWidth();

        //Convert Font Points to Pixels
        logger.debug("DPI: " + dpi);
        fPxlsX = (int) Math.round(fAvgWidthPts * dpi.x / 72.0);
        fPxlsY = (int) Math.round(fHeightPts * dpi.y / 72.0);

        sr = new StringReader(text);
        br = new BufferedReader(sr);

        line = br.readLine();
        lineCnt = 0;
        maxLineLen = 0;

        while (line != null) {
          lineLen = line.length();
          lineCnt++;

          if (lineLen > maxLineLen) {
            maxLineLen = lineLen;
          }

          line = br.readLine();
        }

        //Calculate Final Optimal Width
        optimalWidth = maxLineLen * fPxlsX;

        if (optimalWidth > MAX_WIDTH) {
          optimalWidth = MAX_WIDTH;
        }
        else if (optimalWidth < MIN_WIDTH) {
          optimalWidth = MIN_WIDTH;
        }

        //Calculate Final Optimal Height
        if (lineCnt > 1) {
          optimalHeight = lineCnt * fPxlsY;
        }
        else {
          if ((fPxlsX * charCnt) >= optimalWidth) {
            optimalHeight = (int) (Math.round((((double) (fPxlsX * charCnt)) / (((double) optimalWidth))) * ((double) fPxlsY)));
          }
          else {
            optimalHeight = fPxlsY;
          }
        }

        if (optimalHeight > MAX_HEIGHT) {
          optimalHeight = MAX_HEIGHT;
        }
        else if (optimalHeight < MIN_HEIGHT) {
          optimalHeight = MIN_HEIGHT;
        }

        //Update Optimal Size
        optimalSz = new Point(optimalWidth, optimalHeight);

        logger.debug("Optimal Notification Area Size: " + optimalSz);
      } //End null text check
      else {
        logger.warn("Setting Optimal Size to NO MESG Default because Text is NULL or EMPTY!");
        optimalSz = new Point(NO_MESG_WIDTH, NO_MESG_HEIGHT);
      }
    } //End try block
    finally

    {
      dev = null;
      fMetrics = null;
      text = null;
      line = null;
      dpi = null;

      try {
        if (g != null) {
          g.dispose();
          sr = null;
        }
      }
      catch (Exception e) {
        //e.printStackTrace();
        logger.throwing(e);
      }

      try {
        if (sr != null) {
          br.close();
          br = null;
        }
      }
      catch (Exception e) {
        //e.printStackTrace();
        logger.throwing(e);
      }

      try {
        if (sr != null) {
          sr.close();
          sr = null;
        }
      }
      catch (Exception e) {
        //e.printStackTrace();
        logger.throwing(e);
      }
    } //End finally block

    return optimalSz;
  }

}
