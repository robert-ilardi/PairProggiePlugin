/**
 * Created Jul 6, 2023
 */
package com.ilardi.systems.util;

import static com.ilardi.systems.probud.ProggieBuddieConstants.DEFAULT_USER_PROPS_FILENAME;
import static com.ilardi.systems.probud.ProggieBuddieConstants.SYS_PROP_DEFAULT_USER_PROPS_FILEPATH;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author robert.ilardi
 *
 */

public final class ApplicationContext {

  private static final Logger logger = LogManager.getLogger(ApplicationContext.class);

  //public static final String APP_CNTXT_KEY_USER_PROPERTIES = "com.ilardi.systems.AppContext.UserProperties";

  private static ApplicationContext instance = null;

  private final Object instanceLock;

  private boolean destroyed;

  private HashMap<Object, Object> keyValueStore;

  private boolean appRunning;

  private String usrHome;
  private String usrPropsFilepath;

  private Properties usrProps;

  private ApplicationContext() {
    instanceLock = new Object();

    destroyed = false;

    appRunning = false;

    keyValueStore = new HashMap<Object, Object>();
  }

  public final synchronized static ApplicationContext getInstance() throws IlardiSystemsException {
    if (instance == null) {
      instance = new ApplicationContext();
      instance.init();
    }

    return instance;
  }

  private void init() throws IlardiSystemsException {
    synchronized (instanceLock) {
      logger.debug("Application Context Initializing...");

      locateUserHomeDirectory();

      locateUserPropertiesFilepath();

      instanceLock.notifyAll();
    }
  }

  public void destroy() throws IlardiSystemsException {
    synchronized (instanceLock) {
      if (destroyed) {
        return;
      }

      usrHome = null;

      keyValueStore.clear();
      keyValueStore = null;

      destroyed = true;

      instanceLock.notifyAll();
    }
  }

  public String getUsrHome() {
    return usrHome;
  }

  public void setUsrHome(String usrHome) {
    this.usrHome = usrHome;
  }

  public String getUsrPropsFilepath() {
    return usrPropsFilepath;
  }

  public void setUsrPropsFilepath(String usrPropsFilepath) {
    this.usrPropsFilepath = usrPropsFilepath;
  }

  public Object keyValueStorePut(Object key, Object value) {
    synchronized (instanceLock) {
      return keyValueStore.put(key, value);
    }
  }

  public Object keyValueStoreGet(Object key) {
    synchronized (instanceLock) {
      return keyValueStore.get(key);
    }
  }

  public Object keyValueStoreRemove(Object key) {
    synchronized (instanceLock) {
      return keyValueStore.remove(key);
    }
  }

  public void keyValueStoreClear() {
    synchronized (instanceLock) {
      keyValueStore.clear();
    }
  }

  public int keyValueStoreSize() {
    synchronized (instanceLock) {
      return keyValueStore.size();
    }
  }

  public boolean keyValueStoreContainsKey(Object key) {
    synchronized (instanceLock) {
      return keyValueStore.containsKey(key);
    }
  }

  public boolean keyValueStoreIsEmpty() {
    synchronized (instanceLock) {
      return keyValueStore.isEmpty();
    }
  }

  public boolean isAppRunning() {
    synchronized (instanceLock) {
      return appRunning;
    }
  }

  public void setAppRunning(boolean appRunning) {
    synchronized (instanceLock) {
      this.appRunning = appRunning;
      instanceLock.notifyAll();
    }
  }

  private void locateUserHomeDirectory() {
    StringBuilder sb = null;

    synchronized (instanceLock) {
      if (usrHome == null) {
        logger.debug("Locating User Home Directory");

        usrHome = SystemUtils.GetHomeDirectory();

        if (usrHome != null) {
          if (!usrHome.endsWith("\\") && !usrHome.endsWith("/")) {
            sb = new StringBuilder();

            sb.append(usrHome);
            sb.append("/");

            usrHome = sb.toString();
            sb = null;
          }
        }

        logger.debug("Setting User Home Directory to: " + usrHome);
      }
    }
  }

  public String getUserProperty(String propName) throws IlardiSystemsException {
    synchronized (instanceLock) {
      try {
        if (usrProps == null) {
          loadUserProperties();
        }

        return usrProps.getProperty(propName);
      } //End try block
      catch (Exception e) {
        throw new IlardiSystemsException("An error occurred while attempting to Get User Property. System Message: " + e.getMessage(), e);
      }
    }
  }

  public void setUserProperty(String propName, String propValue) throws IlardiSystemsException {
    synchronized (instanceLock) {
      try {
        if (usrProps == null) {
          loadUserProperties();
        }

        usrProps.setProperty(propName, propValue);
      } //End try block
      catch (Exception e) {
        throw new IlardiSystemsException("An error occurred while attempting to Set User Property. System Message: " + e.getMessage(), e);
      }
    }
  }

  public void removeUserProperty(String propName) throws IlardiSystemsException {
    synchronized (instanceLock) {
      try {
        if (usrProps == null) {
          loadUserProperties();
        }

        usrProps.remove(propName);
      } //End try block
      catch (Exception e) {
        throw new IlardiSystemsException("An error occurred while attempting to Remove User Property. System Message: " + e.getMessage(), e);
      }
    }
  }

  public void clearUserProperty() throws IlardiSystemsException {
    synchronized (instanceLock) {
      try {
        if (usrProps == null) {
          loadUserProperties();
        }

        usrProps.clear();
      } //End try block
      catch (Exception e) {
        throw new IlardiSystemsException("An error occurred while attempting to Clear User Properties. System Message: " + e.getMessage(), e);
      }
    }
  }

  private void locateUserPropertiesFilepath() {
    StringBuilder sb;
    String sysProp;

    synchronized (instanceLock) {
      if (usrPropsFilepath == null) {
        logger.debug("Locating User Properties Filepath...");

        sysProp = System.getProperty(SYS_PROP_DEFAULT_USER_PROPS_FILEPATH);

        if (sysProp == null || sysProp.isBlank()) {
          sb = new StringBuilder();
          sb.append(usrHome);

          sb.append(DEFAULT_USER_PROPS_FILENAME);

          usrPropsFilepath = sb.toString();
          sb = null;

          logger.debug("Using User Home Directory");
        }
        else {
          usrPropsFilepath = sysProp.trim();
          sysProp = null;

          logger.debug("Using System Property Override");
        }

        logger.debug("Setting User Properties Filepath to: " + usrPropsFilepath);
      } //End null usrPropsFilepath Check
    } //End Sync Block
  }

  public void loadUserProperties() throws IlardiSystemsException {
    synchronized (instanceLock) {
      try {
        if (usrProps != null) {
          logger.debug("User Properties was Previously Loaded. Reloading from File.");
          usrProps = null;
        }

        logger.debug("Loading User Properties File into Application Context");

        usrProps = loadPropertiesFile(usrPropsFilepath);

        logger.debug("Successfully Loaded " + usrProps.size() + " Properties from User Properties in: " + usrPropsFilepath);

        ///keyValueStorePut(APP_CNTXT_KEY_USER_PROPERTIES, usrProps);

        //logger.debug("Persisted User Properties to Application Context In-Memory Key Value Store; Access via the App Context Key = " + APP_CNTXT_KEY_USER_PROPERTIES + " ; Use Key Constant: " + ApplicationContext.class + ".APP_CNTXT_KEY_USER_PROPERTIES");
      } //End try block
      catch (Exception e) {
        throw new IlardiSystemsException("An error occurred while attempting to Load User Properties. System Message: " + e.getMessage(), e);
      }
    }
  }

  private Properties loadPropertiesFile(String propsFilepath) throws IOException {
    Properties props = null;
    FileInputStream fis = null;

    try {
      logger.debug("Loading Properties File: " + propsFilepath);

      fis = new FileInputStream(propsFilepath);

      props = new Properties();
      props.load(fis);

      return props;
    } //End try block
    finally {
      try {
        if (fis != null) {
          fis.close();
          fis = null;
        }
      }
      catch (Exception e) {
        e.printStackTrace();
        logger.throwing(e);
      }
    }
  }

  public void saveUserProperties(String optionalComment) throws IlardiSystemsException {

    synchronized (instanceLock) {
      logger.debug("Saving User Properties File from Application Context");

      try {
        if (usrProps == null) {
          loadUserProperties();
        }

        saveProperties(usrProps, usrPropsFilepath, optionalComment);
      } //End try block
      catch (Exception e) {
        throw new IlardiSystemsException("An error occurred while attempting to Save User Properties. System Message: " + e.getMessage(), e);
      }
    }
  }

  public void saveProperties(Properties props, String filepath, String optionalComment) throws IOException {
    FileOutputStream fos = null;

    synchronized (instanceLock) {
      logger.debug("Loading Properties File: " + filepath);

      try {
        fos = new FileOutputStream(filepath);
        props.store(fos, optionalComment);
      }
      finally {
        try {
          if (fos != null) {
            fos.close();
            fos = null;
          }
        }
        catch (Exception e) {
          e.printStackTrace();
          logger.throwing(e);
        }
      }
    }

  }

  public List<String> getUserPropertyNames() throws IlardiSystemsException {
    Set<Object> keySet;
    Iterator<Object> iter;
    String propName;
    ArrayList<String> propNames;

    synchronized (instanceLock) {
      try {
        if (usrProps == null) {
          loadUserProperties();
        }

        keySet = usrProps.keySet();
        iter = keySet.iterator();

        propNames = new ArrayList<String>();

        while (iter.hasNext()) {
          propName = (String) iter.next();

          if (propName != null) {
            propName = propName.trim();
          }

          propNames.add(propName);
        }

        iter = null;
        keySet = null;
        propName = null;

        return propNames;
      } //End try block
      catch (Exception e) {
        throw new IlardiSystemsException("An error occurred while attempting to Build List of User Property Names. System Message: " + e.getMessage(), e);
      }
    }
  }

  public Properties getUserPropertiesByPropPrefix(String propsPrefix) {
    Properties subProps;

    subProps = StringUtils.GetPropertiesUsingPropNamePrefix(usrProps, propsPrefix);

    return subProps;
  }

}
