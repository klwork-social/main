/*
 * 
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * - Neither the name of BEKK Consulting nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package middlegen;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import middlegen.swing.JMiddlegenFrame;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DynamicConfigurator;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/**
 * This Ant task is a thin wrapper around Middlegen that makes it possible to
 * fire up Middlegen (with or without gui) from Ant. The task also provides
 * configuration of Middlegen: What tables to read, what class types to generate
 * and so on.
 *
 * @author <a href="mailto:aslak.hellesoy@netcom.no">Aslak Helles�y</a>
 * @created 21. mars 2002
 * @todo refactor out all logic. This class should only delegate to an Ant-less
 *      class
 */
public class MiddlegenTask extends Task implements DynamicConfigurator {
   /**
    * @todo-javadoc Describe the column
    */
   private final Middlegen _middlegen;
   /**
    * @todo-javadoc Describe the column
    */
   private boolean _isGui = false;

   /**
    * @todo-javadoc Describe the field
    */
   private boolean _isIncludeViews = false;

   /**
    * @todo-javadoc Describe the column
    */
   private String _providerURL;
   /**
    * @todo-javadoc Describe the column
    */
   private String _initialContextFactory;

   /**
    * @todo-javadoc Describe the column
    */
   private String _driver;

   /**
    * @todo-javadoc Describe the column
    */
   private String _databaseURL;
   /**
    * @todo-javadoc Describe the column
    */
   private String _username;
   /**
    * @todo-javadoc Describe the column
    */
   private String _password;
   /**
    * @todo-javadoc Describe the column
    */
   private String _catalog;
   /**
    * @todo-javadoc Describe the column
    */
   private String _schema;

   /**
    * @todo-javadoc Describe the column
    */
   private String _databaseType;

   /**
    * @todo-javadoc Describe the column
    */
   private boolean _many2many = false;

   /** Sort the columns in the table */
   private String _sortColumns = null;

   /**
    * @todo-javadoc Describe the field
    */
   private File _prefsDir = new File(System.getProperty("user.home") + File.separator + ".middlegen");

   /** We're storing the classpath for error reporting. */
   private final String _classpath;

   /**
    * @todo-javadoc Describe the field
    */
   private Map _many2Many = new HashMap();

   /** a flag to indicate if middlegen should singularize names */
   private static boolean _singularize = true;

   /**
    * a flag to indicate if middlegen should name its classes the same as the
    * corresponding db table names
    */
   private static boolean _useDBTableNames = false;

   /**
    * @todo-javadoc Describe the column
    */
   private static String _dataSourceJNDIName;

   /** Sync lock used to keep Ant waiting until gui is closed. */
   private final static Object _lock = new Object();


   /**
    * Describe what the MiddlegenTask constructor does
    *
    * @todo-javadoc Write javadocs for constructor
    */
   public MiddlegenTask() {
      _middlegen = new Middlegen(this);
      try {
         //_classpath = ((AntClassLoader)getClass().getClassLoader()).getClasspath();
    	  String r = getClass().getResource("").getPath();
    	  _classpath = r.substring(0,r.length() - "middlegen".length()-1);
    	  //_classpath = getClass().getResource("/").getPath();//((AntClassLoader)getClass().getClassLoader()).getClasspath();
          System.out.println("classpaht:---------" + _classpath);
         PluginFinder.registerPlugins(_middlegen, _classpath);
      } catch (ClassCastException e) {
         throw new BuildException("middlegen.jar should not be on the system classpath when starting Ant. It should be on a path passed to the <taskdef> using classpath or classpathref.");
      }
   }


   /**
    * Sets the DatabaseType attribute of the MiddlegenTask object
    *
    * @param databaseType The new DatabaseType value
    */
   public void setDatabaseType(String databaseType) {
      _databaseType = databaseType;
   }


   /**
    * Sets the Datasourcename attribute of the MiddlegenTask object
    *
    * @param dataSourceJNDIName The new DatasourceJNDIName value
    */
   public void setDatasourceJNDIName(String dataSourceJNDIName) {
      _dataSourceJNDIName = dataSourceJNDIName;
   }


   /**
    * Sets the InitialContextFactory attribute of the MiddlegenTask object
    *
    * @param initialContextFactory The new InitialContextFactory value
    */
   public void setInitialContextFactory(String initialContextFactory) {
      _initialContextFactory = initialContextFactory;
   }


   /**
    * Sets the ProviderURL attribute of the MiddlegenTask object
    *
    * @param providerURL The new ProviderURL value
    */
   public void setProviderURL(String providerURL) {
      _providerURL = providerURL;
   }


   /**
    * Sets the Many2many attribute of the MiddlegenTask object
    *
    * @param many2many The new Many2many value
    */
   public void setMany2many(boolean many2many) {
      throw new BuildException("many2many is obsolete. please use a nested many2many element instead.");
   }


   /**
    * Sets the Driver attribute of the MiddlegenTask object
    *
    * @param driver The new Driver value
    */
   public void setDriver(String driver) {
      _driver = driver;
   }


   /**
    * The registry id where prefs will be read from
    *
    * @param s The new Prefsid value
    */
   public void setAppname(String s) {
      _middlegen.setAppname(s);
   }


   /**
    * Sets the Xmlprefs attribute of the MiddlegenTask object
    *
    * @todo-javadoc Write javadocs for return value
    * @param flag The new Xmlprefs value
    */
   public void setXmlprefs(boolean flag) {
      Prefs.setXmlprefs(flag);
   }


   /**
    * Sets the Databaseurl attribute of the MiddlegenTask object
    *
    * @param databaseURL The new DatabaseURL value
    */
   public void setDatabaseURL(String databaseURL) {
      log("Database URL:" + databaseURL, Project.MSG_INFO);
      _databaseURL = databaseURL;
   }


   /**
    * Sets the Username attribute of the MiddlegenTask object
    *
    * @param username The new Username value
    */
   public void setUsername(String username) {
      _username = username;
   }


   /**
    * Sets the Password attribute of the MiddlegenTask object
    *
    * @param password The new Password value
    */
   public void setPassword(String password) {
      _password = password;
   }


   /**
    * Sets the Schema attribute of the MiddlegenTask object
    *
    * @param schema The new Schema value
    */
   public void setSchema(String schema) {
      _schema = schema;
   }


   /**
    * Sets the Catalog attribute of the MiddlegenTask object
    *
    * @param catalog The new Catalog value
    */
   public void setCatalog(String catalog) {
      _catalog = catalog;
   }


   /**
    * Sets the Gui attribute of the MiddlegenTask object
    *
    * @param flag The new Gui value
    */
   public void setGui(boolean flag) {
      _isGui = flag;
   }


   /**
    * Sets the IncludeViews attribute of the MiddlegenTask object
    *
    * @param flag The new IncludeViews value
    */
   public void setIncludeViews(boolean flag) {
      _isIncludeViews = flag;
   }


   /**
    * Sorts the columns in each table if true
    *
    * @param value The new sortColumn value
    */
   public void setSortColumns(String value) {
      _sortColumns = value;
   }


   /**
    * Sets the DynamicAttribute attribute of the MiddlegenTask object
    *
    * @param name The new DynamicAttribute value
    * @param value The new DynamicAttribute value
    */
   public void setDynamicAttribute(String name, String value) {
      throw new BuildException("The <" + getTaskName() + "> task doesn't support the \"" + name + "\" attribute.");
   }


   /**
    * Sets the PrefsDir attribute of the MiddlegenTask class
    *
    * @param prefsDir The new PrefsDir value
    */
   public void setPrefsdir(File prefsDir) {
      _prefsDir = prefsDir;
   }


   /**
    * Gets the IncludeViews attribute of the MiddlegenTask object
    *
    * @return The IncludeViews value
    */
   public boolean isIncludeViews() {
      return _isIncludeViews;
   }


   /**
    * Called by Ant for each nested
    * <table>
    *   element after all attributes are set.
    *
    * @todo-javadoc Write javadocs for exception
    * @todo-javadoc Describe the method parameter
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for return value
    * @param tableElement Describe the method parameter
    */
   public void addConfiguredTable(TableElement tableElement) {
      _middlegen.addTableElement(tableElement);
   }


   /**
    * Describe the method
    *
    * @todo-javadoc Describe the method
    * @todo-javadoc Describe the method parameter
    * @param many2manyElement Describe the method parameter
    */
   public void addConfiguredMany2many(Many2ManyElement many2manyElement) {
      many2manyElement.order();
      Collection c = (Collection)_many2Many.get(many2manyElement.getOrderedNameWithoutJoinTable());
      if (c == null) {
         c = new LinkedList();
         _many2Many.put(many2manyElement.getOrderedNameWithoutJoinTable(), c);
      }
      c.add(many2manyElement);
      // Add the tables in the m2m element too. Dupes are ignored
      _middlegen.addTableElement(many2manyElement.getTablea());
      _middlegen.addTableElement(many2manyElement.getJointable());
      _middlegen.addTableElement(many2manyElement.getTableb());
   }


   /**
    * Describe what the method does
    *
    * @todo-javadoc Write javadocs for exception
    * @todo-javadoc Write javadocs for method
    * @exception BuildException Describe the exception
    */
   public void execute() throws BuildException {
      validate();
      try {
         Prefs.getInstance().init(_prefsDir, _middlegen.getAppname());
      } catch (MiddlegenException e) {
         throw new BuildException(e);
      } catch (NoClassDefFoundError e) {
         log("It seems you're running a JDK inferior to 1.4. You should upgrade to JDK 1.4 if you want Middlegen to remember the settings.", Project.MSG_WARN);
      }

      try {
         Database database;
         if (_databaseURL != null) {
            // the preferred way, since it's faster than using JNDI lookup
            log("Getting database connection via standard JDBC", Project.MSG_DEBUG);
            database = new StandardDatabase(_driver, _databaseURL, _username, _password);
         }
         else {
            log("Getting database connection via JNDI and DataSource", Project.MSG_DEBUG);
            // Verify that the initial context factory class is on the classpath
            /*
             *  Apparently this doesn't work. It has to be on the system CLASSPATH for some reason.
             *  An error message in JNDIDatabase will inform about this.
             *  try {
             *  Class.forName(_initialContextFactory);
             *  } catch (Exception e) {
             *  throw new BuildException("You're trying to obtain a database connection via JNDI, and you have specified that the " + _initialContextFactory +
             *  " class should be used to access the JNDI resource. However, this class is not on the classpath specified in the <taskdef> that defined " + getTaskName() + "." +
             *  " Your classpath is: " + _classpath
             *  );
             *  }
             */
            database = new JNDIDatabase(_initialContextFactory, _providerURL, _dataSourceJNDIName);
         }
         MiddlegenPopulator middlegenPopulator = new MiddlegenPopulator(_middlegen, database, _schema, _catalog, _many2Many, _sortColumns);

         if (_middlegen.getTableElements().isEmpty()) {
            log("No <table> elements specified. Reading all tables. This might take a while...", Project.MSG_WARN);
            middlegenPopulator.addRegularTableElements();
         }
         middlegenPopulator.populate(_middlegen.getTableElements());
         middlegenPopulator.closeConnection();

         _middlegen.validate();

         // Instantiate all plugins' decorators.
         _middlegen.decorateAll();

         if (_isGui) {
            // fire up the gui

            String title = "Middlegen - " + _middlegen.getDatabaseInfo().getDriverName() + " - " + _databaseURL;

            JMiddlegenFrame frame = new JMiddlegenFrame(_middlegen, title);
            frame.setVisible(true);
            frame.setSplitter();

            synchronized (getLock()) {
               try {
                  // wait here until gui notifies us (when it quits)
                  getLock().wait();
               } catch (InterruptedException e) {
                  e.printStackTrace();
               }
            }

         }
         else {
            _middlegen.writeSource();
         }
         try {
            Prefs.getInstance().save();
         } catch (NoClassDefFoundError ignore) {
         }
      } catch (MiddlegenException e) {
         throw new BuildException(e);
      } catch (Throwable e) {
         e.printStackTrace();
         throw new BuildException(e);
      }
   }


   /**
    * Describe what the method does
    *
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for return value
    * @param name Describe what the parameter does
    * @return Describe the return value
    */
   public Object createDynamicElement(String name) {
      Class pluginClass = _middlegen.getPluginClass(name);
      if (pluginClass == null) {
         throw new BuildException("Nothing known about plugin <" + name + ">. Make sure the jar file containing the " +
               "corresponding plugin class is on the classpath specified in the <taskdef> that defined " + getTaskName() + "." +
               " Your classpath is: " + _classpath
               );
      }
      Plugin plugin = null;
      try {
         plugin = (Plugin)pluginClass.newInstance();
         // set the default name
         plugin.setName(name);
         _middlegen.addPlugin(plugin);
      } catch (Exception e) {
         throw new BuildException("Couldn't instantiate Plugin " + pluginClass.getName(), e);
      }
      return plugin;
   }


   /**
    * Describe what the method does
    *
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for method parameter
    * @param message Describe what the parameter does
    */
   private void warnIfNoJdk14(String message) {
      try {
         Class.forName("java.util.prefs.Preferences");
      } catch (Throwable t) {
         log(message, Project.MSG_WARN);
      }
   }


   /**
    * Describe what the method does
    *
    * @todo-javadoc Write javadocs for exception
    * @todo-javadoc Write javadocs for method
    * @exception BuildException Describe the exception
    */
   private void validate() throws BuildException {
      if (_driver == null && _dataSourceJNDIName == null) {
         throw new BuildException("You must either specify driver or dataSourceJNDIName");
      }

      if (_middlegen.getAppname() == null) {
         throw new BuildException("You must specify the appname");
      }

      presentRequired(_dataSourceJNDIName, "dataSourceJNDIName", _providerURL, "providerURL");
      presentRequired(_dataSourceJNDIName, "dataSourceJNDIName", _initialContextFactory, "initialContextFactory");
      presentRequired(_databaseURL, "databaseURL", _driver, "driver");
   }


   /**
    * Describe what the method does
    *
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for exception
    * @param presentValue Describe what the parameter does
    * @param presentName Describe what the parameter does
    * @param illegalValue Describe what the parameter does
    * @param illegalName Describe what the parameter does
    * @exception BuildException Describe the exception
    */
   private void presentIllegal(String presentValue, String presentName, String illegalValue, String illegalName) throws BuildException {
      if (presentValue != null && illegalValue != null) {
         throw new BuildException("You can't specify both " + presentName + " and " + illegalName);
      }
   }


   /**
    * Describe what the method does
    *
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for exception
    * @param presentValue Describe what the parameter does
    * @param presentName Describe what the parameter does
    * @param requiredValue Describe what the parameter does
    * @param requiredName Describe what the parameter does
    * @exception BuildException Describe the exception
    */
   private void presentRequired(String presentValue, String presentName, String requiredValue, String requiredName) throws BuildException {
      if (presentValue != null && requiredValue == null) {
         throw new BuildException("When " + presentName + " is specified, you also have to specify " + requiredName);
      }
   }


   /**
    * Sets the singularize attribute of the MiddlegenTask class
    *
    * @param flag The singularize flag.
    */
   public static void setSingularize(boolean flag) {
      _singularize = flag;
   }


   /**
    * Sets the useDbTableNames attribute of the MiddlegenTask class
    *
    * @param flag The useDbTableNames flag.
    */
   public static void setUseDBTableNames(boolean flag) {
      _useDBTableNames = flag;
   }


   /**
    * Gets the singularize attribute of the MiddlegenTask object.
    *
    * @return The value of the singularize flag
    */
   public static boolean getSingularize() {
      return _singularize;
   }


   /**
    * Gets the useDbTableNames attribute of the MiddlegenTask object.
    *
    * @return The value of the useDbTableNames flag.
    */
   public static boolean getUseDBTableNames() {
      return _useDBTableNames;
   }


   /**
    * Gets the DataSourceName attribute of the MiddlegenTask object
    *
    * @return The DataSourceName value
    */
   public static String getDatasourceJNDIName() {
      return _dataSourceJNDIName;
   }


   /**
    * Gets the Lock attribute of the MiddlegenTask class
    *
    * @return The Lock value
    */
   public static Object getLock() {
      return _lock;
   }
}
