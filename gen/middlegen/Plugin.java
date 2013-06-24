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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.lang.reflect.Constructor;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;

import middlegen.swing.JColumnSettingsPanel;
import middlegen.swing.JTableSettingsPanel;

/**
 * This is the baseclass for plugins. It can be subclassed to add additional
 * functionality, or it can be used "as-is".
 *
 * @author <a href="mailto:aslak.hellesoy@bekk.no">Aslak Helles�y</a>
 * @created 3. april 2002
 */
public class Plugin {

   /**
    * @todo-javadoc Describe the field
    */
   private Middlegen _middlegen;

   /**
    * @todo-javadoc Describe the column
    */
   private final HashMap _columnDecorators = new HashMap();
   /**
    * @todo-javadoc Describe the column
    */
   private final HashMap _tableDecorators = new HashMap();
   /**
    * @todo-javadoc Describe the column
    */
   private final Class[] _columnDecoratorConstructorArgs = new Class[]{Column.class};
   /**
    * @todo-javadoc Describe the column
    */
   private final Class[] _tableDecoratorConstructorArgs = new Class[]{Table.class};

   /**
    * @todo-javadoc Describe the column
    */
   private File _destinationDir;

   /** The name of the plugin */
   private String _name;

   /**
    * @todo-javadoc Describe the field
    */
   private String _mergedir;

   /**
    * @todo-javadoc Describe the field
    */
   private HashMap _fileProducers = new HashMap();
   /**
    * @todo-javadoc Describe the field
    */
   private String _displayName;

   /** Whether or not to use the schema prefix in generated code. */
   private boolean _useSchemaPrefix = false;
   /** Get static reference to Log4J Logger */
   private static org.apache.log4j.Category _log = org.apache.log4j.Category.getInstance(Plugin.class.getName());


   /** Constructor */
   public Plugin() {
   }


   /**
    * Describe what the setUseSchemaPrefix constructor does
    *
    * @todo-javadoc Write javadocs for constructor
    * @todo-javadoc Write javadocs for method parameter
    * @param flag Describe what the parameter does
    */
   public void setUseSchemaPrefix(boolean flag) {
      _useSchemaPrefix = flag;
   }


   /**
    * Sets the Mergedir attribute of the Entity20Plugin object
    *
    * @param md The new Mergedir value
    */
   public void setMergedir(String md) {
      _mergedir = md;
   }



   /**
    * The root folder where the sources will be written. This value overrides
    * the destination attribute specified on the Ant task level.
    *
    * @param dir The new Destination value
    */
   public void setDestination(File dir) {
      _destinationDir = dir;
   }


   /**
    * Sets the logical plugin name. Not intended to be called from Ant, but by
    * PluginFinder
    *
    * @param name The new Name value
    */
   public final void setName(String name) {
      _name = name;
   }


   /**
    * Gets the UseSchemaPrefix attribute of the Plugin object
    *
    * @return The UseSchemaPrefix value
    */
   public boolean isUseSchemaPrefix() {
      return _useSchemaPrefix;
   }


   /**
    * Returns the name to be used in the relations. Can be overridden in
    * subclasses
    *
    * @todo-javadoc Write javadocs for method parameter
    * @param table Describe what the parameter does
    * @return The RelationName value
    */
   public String getRelationName(Table table) {
      return table.getSqlName() + "-" + getName();
   }


   /**
    * Gets the Middlegen attribute of the Plugin object
    *
    * @return The Middlegen value
    */
   public Middlegen getMiddlegen() {
      return _middlegen;
   }


   /**
    * Gets the DestinationDir attribute of the Plugin object
    *
    * @return The DestinationDir value
    */
   public File getDestinationDir() {
      return _destinationDir;
   }


   /**
    * Gets the ColumnSettingsPanel attribute of the ClassType object
    *
    * @return The ColumnSettingsPanel value
    */
   public JColumnSettingsPanel getColumnSettingsPanel() {
      return null;
   }


   /**
    * Gets the TableSettingsPanel attribute of the ClassType object
    *
    * @todo return a TableConfigurator interface instead, to avoid dependence on
    *      swing packae
    * @return The TableSettingsPanel value
    */
   public JTableSettingsPanel getTableSettingsPanel() {
      return null;
   }


   /**
    * Gets the DisplayName attribute of the ClassType object
    *
    * @return The DisplayName value
    */
   public final String getDisplayName() {
      return _displayName;
   }


   /**
    * Returns the name of the plugin.
    *
    * @return The Name value
    */
   public final String getName() {
      return _name;
   }


   /**
    * Gets the ColumnDecoratorClass attribute of the Plugin object
    *
    * @return The ColumnDecoratorClass value
    */
   public Class getColumnDecoratorClass() {
      return ColumnDecorator.class;
   }


   /**
    * Gets the TableDecoratorClass attribute of the Plugin object
    *
    * @return The TableDecoratorClass value
    */
   public Class getTableDecoratorClass() {
      return TableDecorator.class;
   }


   /**
    * Gets the Tables attribute of the Plugin object
    *
    * @return The Tables value
    */
   public final Collection getTables() {
      return _tableDecorators.values();
   }


   /**
    * Gets the Table attribute of the Plugin object
    *
    * @todo-javadoc Write javadocs for method parameter
    * @param sqlName Describe what the parameter does
    * @return The Table value
    */
   public final TableDecorator getTable(String sqlName) {
      return (TableDecorator)_tableDecorators.get(sqlName);
   }


   /**
    * Gets the Mergedir attribute of the Entity20Plugin object
    *
    * @return The Mergedir value
    */
   public String getMergedir() {
      return _mergedir;
   }



   /**
    * Adds a file producer. If the file producer's file name contains the String
    * {0}, Middlegen will assume this is a per-table file producer, and one
    * instance for each table will be created. This method can be called from
    * Ant or from subclasses. <BR>
    *
    *
    * @param fileProducer the FileProducer to add.
    */
   public void addConfiguredFileproducer(FileProducer fileProducer) {
      fileProducer.validate();
      String id = fileProducer.getId();
      if (id == null) {
         // YUK. Magic id :-(
         fileProducer.setId("__custom_" + _fileProducers.size());
      }
      /*
       *  else {
       *  / we're overriding.
       *  if (fileProducer.getDestinationDir() != null) {
       *  throw new IllegalStateException("Can't override the plugin's destination when overriding a template. " +
       *  "(It's assumed that the fileproducer is overriding since an id=\"" + id + "\" was specified).");
       *  }
       *  if (fileProducer.getDestinationFileName() != null) {
       *  throw new IllegalStateException("Can't override the plugin's filename when overriding a template. " +
       *  "(It's assumed that the fileproducer is overriding since an id=\"" + id + "\" was specified).");
       *  }
       *  }
       */
      FileProducer customFileProducer = (FileProducer)_fileProducers.get(id);
      if (customFileProducer != null) {
         // A custom file producer has been specified in Ant. Override the destination.
         customFileProducer.copyPropsFrom(fileProducer);
      }
      else {
         // use the added file producer, but perform some sanity checks first.
         _fileProducers.put(fileProducer.getId(), fileProducer);
      }
   }


   /**
    * Creates and caches decorators for all Tables and Columns.
    *
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for method parameter
    * @param tables Describe what the parameter does
    */
   public final void decorateAll(Collection tables) {
      // loop over all tables
      Iterator tableIterator = tables.iterator();
      while (tableIterator.hasNext()) {
         // decorate table
         DbTable table = (DbTable)tableIterator.next();
         TableDecorator tableDecorator = createDecorator(table);
         tableDecorator.setPlugin(this);
         // cache it using subject as key. will be by clients as argument to decorate()
         _tableDecorators.put(table.getSqlName(), tableDecorator);

         // decorate columns and store refs in newly created TableDecorator
         DbColumn pkColumn = (DbColumn)table.getPkColumn();
         if (pkColumn != null) {
            ColumnDecorator pkColumnDecorator = createDecorator(pkColumn);
            pkColumnDecorator.setTableDecorator(tableDecorator);
            tableDecorator.setPkColumnDecorator(pkColumnDecorator);
            _columnDecorators.put(pkColumn, pkColumnDecorator);
         }

         Collection columnDecorators = new ArrayList(table.getColumns().size());
         Iterator columns = table.getColumns().iterator();
         while (columns.hasNext()) {
            DbColumn column = (DbColumn)columns.next();
            ColumnDecorator columnDecorator = createDecorator(column);
            columnDecorator.setPlugin(this);
            columnDecorator.setTableDecorator(tableDecorator);
            _columnDecorators.put(column, columnDecorator);
            columnDecorators.add(columnDecorator);
         }
         tableDecorator.setColumnDecorators(columnDecorators);
      }
      // now that everything is properly set up, call init on all decorators.
      Iterator tableDecorators = _tableDecorators.values().iterator();
      while (tableDecorators.hasNext()) {
         ((TableDecorator)tableDecorators.next()).init();
      }

      Iterator columnDecorators = _columnDecorators.values().iterator();
      while (columnDecorators.hasNext()) {
         ((ColumnDecorator)columnDecorators.next()).init();
      }
   }


   /**
    * Validates that the plugin is correctly configured
    *
    * @exception MiddlegenException if the state is invalid
    */
   public void validate() throws MiddlegenException {
      if (_destinationDir == null) {
         throw new MiddlegenException("destination must be specified in <" + getName() + ">");
      }
   }


   /**
    * Describe what the method does
    *
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for return value
    * @param mergeFile Describe what the parameter does
    * @return Describe the return value
    */
   public boolean mergeFileExists(String mergeFile) {
      return new File(getMergedir(), mergeFile).exists();
   }


   /**
    * Sets the DisplayName attribute of the Plugin object
    *
    * @param s The new DisplayName value
    */
   protected final void setDisplayName(String s) {
      _displayName = s;
   }


   /**
    * Describe what the method does
    *
    * @todo-javadoc Write javadocs for method
    */
   protected void registerFileProducers() {
   }


   /**
    * Describe what the method does
    *
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for exception
    * @exception MiddlegenException Describe the exception
    */
   protected void generate() throws MiddlegenException {
      registerFileProducers();
      // Have to get a new engine for each plugin because the merge dir
      // is set at initialisation time.
      VelocityEngine velocityEngine = getEngine();

      //Iterator fileProducers = getConfiguredFileProducers().iterator();

      doIt(velocityEngine);

      /*
       *  while (fileProducers.hasNext()) {
       *  FileProducer fileProducer = (FileProducer)fileProducers.next();
       *  fileProducer.getContextMap().put("plugin", this);
       *  fileProducer.generate(velocityEngine);
       *  }
       */
   }


   /**
    * Sets the Middlegen attribute of the Plugin object
    *
    * @param middlegen The new Middlegen value
    */
   void setMiddlegen(Middlegen middlegen) {
      _middlegen = middlegen;
   }


   /**
    * Describe what the method does
    *
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for return value
    * @param column Describe what the parameter does
    * @return Describe the return value
    */
   final Column decorate(Column column) {
      if (column.getClass() != DbColumn.class) {
         throw new IllegalArgumentException("column must be of class " + DbColumn.class.getName() + Middlegen.BUGREPORT);
      }
      if (column == null) {
         throw new IllegalArgumentException("column can't be null!" + Middlegen.BUGREPORT);
      }
      ColumnDecorator result = (ColumnDecorator)_columnDecorators.get(column);
      if (result == null) {
         throw new IllegalArgumentException("result can't be null!" + Middlegen.BUGREPORT);
      }
      return result;
   }


   /**
    * Describe what the method does
    *
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for return value
    * @param table Describe what the parameter does
    * @return Describe the return value
    */
   final Table decorate(Table table) {
      if (!table.getClass().equals(DbTable.class)) {
         throw new IllegalArgumentException("table must be of class " + DbTable.class.getName() + Middlegen.BUGREPORT);
      }
      if (table == null) {
         throw new IllegalArgumentException("table can't be null!" + Middlegen.BUGREPORT);
      }
      TableDecorator result = (TableDecorator)_tableDecorators.get(table.getSqlName());
      if (result == null) {
         throw new IllegalArgumentException("result can't be null!" + Middlegen.BUGREPORT);
      }
      return result;
   }


   /**
    * Returns all the tabledecorators' file producers. Override this method if
    * you want different behaviour.
    *
    * @return The FileProducers value
    */
   private final Collection getFileProducers() {
      return _fileProducers.values();
      /*
       *  ArrayList result = new ArrayList();
       *  Iterator tables = getTables().iterator();
       *  while (tables.hasNext()) {
       *  TableDecorator tableDecorator = (TableDecorator)tables.next();
       *  result.addAll(tableDecorator.getFileProducers());
       *  }
       *  return result;
       */
   }


   /**
    * Gets the Engine attribute of the Middlegen object
    *
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for exception
    * @return The Engine value
    * @exception MiddlegenException Describe the exception
    */
   private VelocityEngine getEngine() throws MiddlegenException {
      VelocityEngine velocityEngine = new VelocityEngine();
      Properties props = new Properties();
      // only load templates from file we don't have access to the jar and use a workaround for that
      props.setProperty(RuntimeConstants.RESOURCE_LOADER, "file");
      // use a resource loader that won't throw an exception if a resource (file) isn't found
      props.setProperty("file.resource.loader.class", "middlegen.KindFileResourceLoader");
      // tell velocity where merge files are located
      if (getMergedir() != null) {
         props.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, getMergedir());
      }
      // use our own log system that doesn't close the appenders upon gc() (the velocity one does)
      props.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "middlegen.DontCloseLog4JLogSystem");
      try {
         velocityEngine.init(props);
         return velocityEngine;
      } catch (Exception e) {
         // Hmm, throwning Exception is bad API, Velocity guys ;-)
         e.printStackTrace();
         throw new MiddlegenException(e.getMessage());
      }
   }


   /**
    * Adds additional file producers. This method is called right before the
    * generation starts. Depending on the fileName and tableDecorators
    * parameters, several things can happen: <p>
    *
    * If fileName contains {0}, a copy of each of these file producers is
    * created, substituting the {0} with the table name, and the original one is
    * removed.
    *
    * @todo-javadoc Write javadocs for exception
    * @todo-javadoc Write javadocs for method parameter
    * @param engine Describe what the parameter does
    * @exception MiddlegenException Describe the exception
    */
   private void doIt(VelocityEngine engine) throws MiddlegenException {
      for (Iterator fileProducerIterator = getFileProducers().iterator(); fileProducerIterator.hasNext(); ) {
         FileProducer fileProducer = (FileProducer)fileProducerIterator.next();
         if (fileProducer.isGenerationPerTable()) {
            // explode this file producer in multiple instances, potentially
            // one for every table (or less, if the file producer knows exactly
            // what tables it cares about).
            for (Iterator tableDecoratorIterator = getTables().iterator(); tableDecoratorIterator.hasNext(); ) {
               TableDecorator tableDecorator = (TableDecorator)tableDecoratorIterator.next();
               // Check if we should generate for the table.
               if (tableDecorator.getTableElement().isGenerate()) {
                  // Check whether the file producer accepts this table
                  if (tableDecorator.isGenerate() && fileProducer.accept(tableDecorator)) {
//							FileProducer fileProducerCopy = fileProducer.copy(tableDecorator);
                     // register the table with the file producer so it can be accessed from the template
//							fileProducerCopy.getContextMap().put("table", tableDecorator);
//							result.add(fileProducerCopy);
                     fileProducer.getContextMap().put("plugin", this);
                     fileProducer.generateForTable(engine, tableDecorator);
                  }
               }
            }
         }
         else {
            // This file producer will take a collection of table decorators in stead of
            // one single table. Let's see if it wants all or only a subset.
            ArrayList acceptedTableDecorators = new ArrayList();
            for (Iterator tableDecoratorIterator = getTables().iterator(); tableDecoratorIterator.hasNext(); ) {
               TableDecorator tableDecorator = (TableDecorator)tableDecoratorIterator.next();
               if (tableDecorator.getTableElement().isGenerate()) {
                  if (tableDecorator.isGenerate() && fileProducer.accept(tableDecorator)) {
                     acceptedTableDecorators.add(tableDecorator);
                  }
               }
            }
            // Just add the unmodified fileProducer
//				result.add(fileProducer);
//				fileProducer.getContextMap().put("tables", acceptedTableDecorators);
            fileProducer.getContextMap().put("plugin", this);
            fileProducer.generateForTables(engine, acceptedTableDecorators);
         }
      }
   }


   /**
    * Describe what the method does
    *
    * @todo-javadoc Write javadocs for return value
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for method parameter
    * @param column Describe what the parameter does
    * @return Describe the return value
    */
   private final ColumnDecorator createDecorator(DbColumn column) {
      Object decorator = _columnDecorators.get(column);
      if (decorator == null) {
         decorator = createDecorator(column, getColumnDecoratorClass(), _columnDecoratorConstructorArgs);
      }
      else {
         //System.out.println("Already cached:" + column.getSqlName());
      }
      return (ColumnDecorator)decorator;
   }


   /**
    * Describe what the method does
    *
    * @todo-javadoc Write javadocs for return value
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for method parameter
    * @param table Describe what the parameter does
    * @return Describe the return value
    */
   private final TableDecorator createDecorator(DbTable table) {
      Object decorator = _tableDecorators.get(table.getSqlName());
      if (decorator == null) {
         decorator = createDecorator(table, getTableDecoratorClass(), _tableDecoratorConstructorArgs);
      }
      else {
//			System.out.println("Already cached:" + table.getSqlName());
      }
      return (TableDecorator)decorator;
   }


   /**
    * Describe what the method does
    *
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for return value
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for method parameter
    * @param subject Describe what the parameter does
    * @param decoratorClass Describe what the parameter does
    * @param decoratorConstructorArgs Describe what the parameter does
    * @return Describe the return value
    */
   private final Object createDecorator(Object subject, Class decoratorClass, Class[] decoratorConstructorArgs) {
      Object decorator = null;
      String declaredConstructor = decoratorClass.getName() + "(" + decoratorConstructorArgs[0].getName() + ")";
      String invokedConstructor = decoratorClass.getName() + "(" + subject.getClass().getName() + ")";
      try {
//			System.out.println("trying to find constructor " + declaredConstructor);
         Constructor constructor = decoratorClass.getConstructor(decoratorConstructorArgs);
         Object[] constructorArgs = new Object[]{subject};

//			System.out.println("trying to invoke constructor " + invokedConstructor);
         decorator = constructor.newInstance(constructorArgs);
      } catch (Exception e) {
         e.printStackTrace();
         throw new IllegalStateException("Couldn't invoke constructor " + invokedConstructor);
      }
      return decorator;
   }
   
   private String _author = "middlegen";
   public String getAuthor() {
		return _author;
	}
	public void setAuthor(String author) {
		_author = author;
	}
	
	private String _daoSuffix;
	
	private String _daoImplSuffix;
	
	private String _daoDir;
	
	private String _daoImplDir;
	
	private String _ibatisExt;
	
	private String _ModelName;

	//add by shishengfeng on 2009-03-17 for 增加domain的继承接口 begin
	
	
	//add by ww
	private File _testDir;
	private String _genType;
	
	private String queryObjectFields;
	
	private ArrayList _implements;
	public String getImplements(){
		StringBuffer sb = new StringBuffer();
		if(_implements!=null&&_implements.size()>0)
		{
			for(int i=0;i<_implements.size();i++)
			{
				sb.append(","+_implements.get(i));
			}
		}
		return sb.length()>0?sb.substring(1):"";
	}
	public void setImplements(String implemets)
	{
		if (implemets == null || implemets.trim().length() == 0) {
			return;
		}
		String[] imps = implemets.split(",");
		ArrayList list = new ArrayList(imps.length);
		for (int i = 0; i < imps.length; i++) {
			list.add(imps[i]);
		}
		_implements = list;
	}
	public void setImplements(ArrayList list)
	{
		_implements = list;
	}
	//add by shishengfeng end

	public String getIbatisExt() {
		return _ibatisExt;
	}

	public void setIbatisExt(String ibatisExt) {
		_ibatisExt = ibatisExt;
	}

	public String getDaoDir() {
		return _daoDir;
	}

	public void setDaoDir(String daoDir) {
		_daoDir = daoDir;
	}

	public String getDaoImplDir() {
		return _daoImplDir;
	}

	public void setDaoImplDir(String daoImplDir) {
		_daoImplDir = daoImplDir;
	}

	public String getDaoImplSuffix() {
		return _daoImplSuffix;
	}

	public void setDaoImplSuffix(String daoImplSuffix) {
		_daoImplSuffix = daoImplSuffix;
	}

	public String getDaoSuffix() {
		return _daoSuffix;
	}

	public void setDaoSuffix(String daoSuffix) {
		_daoSuffix = daoSuffix;
	}

	//WW_TODO 代码生成工具-增加的是否生成测试的目录
	public File getTestDir() {
		return _testDir;
	}


	public void setTestDir(File dir) {
		this._testDir = dir;
	}


	public String getGenType() {
		return _genType;
	}

	public void setGenType(String _genType) {
		this._genType = _genType;
	}


	public String getQueryObjectFields() {
		return queryObjectFields;
	}
	
	public List getQueryObjectList() {
		return Arrays.asList(queryObjectFields.split(","));
	}

	public void setQueryObjectFields(String queryObjectFields) {
		this.queryObjectFields = queryObjectFields;
	}
	
	
}
