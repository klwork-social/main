/*
 * Copyright (c) 2001, Aslak Helles�y, BEKK Consulting
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
package middlegen.plugins.hibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import middlegen.FileProducer;
import middlegen.MiddlegenException;
import middlegen.javax.JavaPlugin;
import middlegen.plugins.hibernate.interfaces.JavaTypeMapper;
import middlegen.swing.JColumnSettingsPanel;
import middlegen.swing.JTableSettingsPanel;
import middlegen.util.BooleanNode;

/**
 * This plugin generates a Hibernate class for each table in the database.
 * 
 * @author David Channon and Gavin King
 * @created 1 January 2003
 * @middlegen.plugin name = "hibernate"
 * 
 * @version 2.1
 */
public class HibernatePlugin extends JavaPlugin {

	/**
	 * Field and default value for XDoclet tag generation in the hbm meta
	 * attributes.
	 */
	private boolean _genXdocletTags = false;

	/** Use the lazy attribute reather than the proxy attribute */
	private boolean _genLazyNotProxy = false;

	/**
	 * Field for the default value for XDoclect generation of Composite key
	 * mappinsg to retain the key as integrated into the domain object rather
	 * than using the recommended separate class.
	 */
	private boolean _genIntegratedCompositeKeys = false;

	/** Set the generator type (sequence, assigned or native) */
	private String _standardIdGenerator = StandardIdGenerator;

	/** Set the generator type optional argument */
	private String _standardIdGeneratorArg = "";

	/** Set the cascade type for the relationships */
	private String _standardCascade = StandardCascadeId;

	/** Suffix for the generated class */
	private String _beansuffix = "";

	/** Database schema for the generated class */
	private String _databaseSchema = "";

	/** Custom persister for the generated class */
	private String _persister = "";

	/** BatchSize value for the generated class */
	private String _batchsize = "";

	/** Class description for the generated class */
	private String _classDescription = "";

	/** Field description for the generated field */
	private String _fieldDescription = "";

	/** Class scope for the generated class */
	private String _classScope = PublicId;

	/** Field scope for the generated class */
	private String _fieldScope = PrivateId;

	/** Field get method scope for the generated class */
	private String _getScope = PublicId;

	/** Class set method scope for the generated class */
	private String _setScope = PublicId;

	/** Access field for the generated class */
	private String _accessField = PropertyId;

	/** Custom base class for the generated class */
	private String _extends = "";

	/** Default setting for the constraint type on a bean property. */
	private String _beanPropertyType = "unbound";

	/**
	 * The name of the class to optionally use for sql to java mapping
	 */
	private JavaTypeMapper _javaTypeMapper;

	/** Proxy user class (v2.0+) generation. */
	private boolean _proxy = false;

	/** If this node is true, the classes will have proxies enabled */
	private final BooleanNode _proxyNode = BooleanNode.createRoot(false);

	/**
	 * If this node is true, the classes will have plain compound keys enabled
	 */
	private boolean _plainCKeys = true;

	/** Include unsaved value for versioned entities with simple keys */
	private boolean _versionUnsavedValue = false;

	/** Include unsaved value for versioned tables that are compound keys */
	private boolean _versionUnsavedValueCKeys = true;

	/**
	 * @todo-javadoc Describe the field
	 */
	private final BooleanNode _plainCKeyNode = BooleanNode.createRoot(false);

	/** <tt>Lifecycle</tt> interface generation. */
	private boolean _lifecycle = false;

	/**
	 * If node is true, generated classes will implement the <tt>Lifecycle</tt>
	 * interface by default
	 */
	private final BooleanNode _lifecycleNode = BooleanNode.createRoot(false);

	/** <tt>Validatable</tt> interface generation. */
	private boolean _validatable = false;

	/**
	 * If node is true, generated classes will implement the
	 * <tt>Validatable</tt> interface by default
	 */
	private final BooleanNode _validatableNode = BooleanNode.createRoot(false);

	/** <tt>Lifecycle</tt> interface generation. */
	private boolean _equalsHashcode = false;

	/**
	 * If node is true, generated classes will include the Equals and Hashcode
	 * methods.
	 */
	private final BooleanNode _equalsHashcodeNode = BooleanNode.createRoot(false);

	/** Mutable generation setting. */
	private boolean _mutable = true;

	/**
	 * If node is false, generated classes will not be mutable by default
	 */
	private final BooleanNode _mutableNode = BooleanNode.createRoot(true);

	/** DynamicUpdate generation setting. */
	private boolean _dynamicUpdate = false;

	/**
	 * If node is true, generated SQL for this class will only update the
	 * modifed fields rather than the complete tuple.
	 */
	private final BooleanNode _dynamicUpdateNode = BooleanNode.createRoot(false);

	/** DynamicInsert generation setting. */
	private boolean _dynamicInsert = false;

	/**
	 * If node is true, generated SQL for this class will only insert the not
	 * null fields rather than the complete tuple.
	 */
	private final BooleanNode _dynamicInsertNode = BooleanNode.createRoot(false);

	/** Select before Update generation setting. */
	private boolean _selectBeforeUpdate = false;

	/**
	 * If node is true, generated SQL for this class will select the data before
	 * updating the tuple.
	 */
	private final BooleanNode _selectBeforeUpdateNode = BooleanNode.createRoot(false);


	
	
	/** Default class implements default values */
	private ArrayList _implements = new ArrayList();

	/** ColumnsUpdateable generation setting. */
	private boolean _columnsUpdateable = true;

	/** ColumnsUpdateable generation setting. */
	private boolean _columnsInsertable = true;

	/** ColumnsUpdateable generation setting. */
	private boolean _columnsIncToString = false;

	/** ColumnsUpdateable generation setting. */
	private boolean _columnsGenProperty = true;

	/** ColumnsUpdateable generation setting. */
	private boolean _columnsIncEquals = false;

	/**
	 * @todo-javadoc Describe the field
	 */
	public final static String StandardIdGenerator = "assigned";

	/**
	 * @todo-javadoc Describe the field
	 */
	public final static String StandardCascadeId = "none";

	/**
	 * @todo-javadoc Describe the field
	 */
	public final static String FieldId = "field";

	/**
	 * @todo-javadoc Describe the field
	 */
	public final static String PropertyId = "property";

	/**
	 * @todo-javadoc Describe the field
	 */
	public final static String PublicId = "public";

	/**
	 * @todo-javadoc Describe the field
	 */
	public final static String PrivateId = "private";

	/**
	 * @todo-javadoc Describe the field
	 */
	public final static String ProtectedId = "protected";

	/**
	 * @todo-javadoc Describe the field
	 */
	public final static String ColPropertyId = "property";

	/**
	 * @todo-javadoc Describe the field
	 */
	public final static String ColKeyId = "key";

	/**
	 * @todo-javadoc Describe the field
	 */
	public final static String ColForeignKeyId = "foreign key";

	/**
	 * @todo-javadoc Describe the field
	 */
	public final static String ColCompoundKeyId = "compound key";

	/**
	 * @todo-javadoc Describe the field
	 */
	public final static String ColVersionId = "version";

	/**
	 * @todo-javadoc Describe the field
	 */
	public final static String ColTimestampId = "timestamp";

	/** Constructor for HibernatePlugin */
	public HibernatePlugin() {
		super();
		setDisplayName("Hibernate");
	}

	/**
	 * Sets the standard cascade setting used for all relationships.
	 * 
	 * @param s
	 * @middlegen.setting required = "false" default = "none"
	 * 
	 */
	public void setStandardCascade(String s) {
		_standardCascade = s;
	}

	/**
	 * Sets the standard id generator. For example, 'assigned' (default),
	 * 'sequence' or 'native' where it is assumed that the native form used does
	 * not necessarily require an argument.
	 * 
	 * @param s
	 * @middlegen.setting required = "false" default = "assigned"
	 * 
	 */
	public void setStandardGeneratorScheme(String s) {
		_standardIdGenerator = s;
	}

	/**
	 * Sets the standard id generator argumnent. This is optional and maybe used
	 * for the 'sequence' or 'native' generator. This can have a '{0}' table
	 * name place holder in the argument string.
	 * 
	 * @param s
	 * @middlegen.setting required = "false" default = ""
	 * 
	 */
	public void setStandardGeneratorArg(String s) {
		_standardIdGeneratorArg = s;
	}

	/**
	 * The suffix to use for the generated classes
	 * 
	 * @param s
	 *            The new Beansuffix value
	 * @middlegen.setting required = "false" default = ""
	 * 
	 */
	public void setBeansuffix(String s) {
		_beansuffix = s;
	}

	/**
	 * If true, the generated classes will implement the <tt>Lifecycle</tt>
	 * interface by default
	 * 
	 * @param flag
	 *            The new Lifecycle value
	 * @middlegen.setting required = "false" default = "false"
	 * 
	 */
	public void setLifecycle(boolean flag) {
		_lifecycle = flag;
	}

	/**
	 * If true, the generated classes will include the Equals and Hashcode
	 * methods
	 * 
	 * @param flag
	 *            The new equals hashcode value
	 * @middlegen.setting required = "false" default = "false"
	 * 
	 */
	public void setEqualsHashcode(boolean flag) {
		_equalsHashcode = flag;
	}

	/**
	 * If true, the generated classes will implement the <tt>Validatable</tt>
	 * interface by default
	 * 
	 * @param flag
	 *            The new Validatable value
	 * @middlegen.setting required = "false" default = "false"
	 * 
	 */
	public void setValidatable(boolean flag) {
		_validatable = flag;
	}

	/**
	 * If this node is true, the classes will have proxies enabled
	 * 
	 * @param flag
	 *            The new Proxy value
	 * @middlegen.setting required = "false" default = "false"
	 * 
	 */
	public void setProxy(boolean flag) {
		_proxy = flag;
	}

	/**
	 * If this node is true, the classes will have plain compound keys enabled
	 * 
	 * @param flag
	 *            The new Plain Compound key value
	 * @middlegen.setting required = "false" default = "true"
	 * 
	 */
	public void setPlainCompoundKeys(boolean flag) {
		_plainCKeys = flag;
	}

	/**
	 * If this node is true, the classes with a version property will have
	 * unsave value setting.
	 * 
	 * @param flag
	 *            The unsaved value for simple keys.
	 * @middlegen.setting required = "false" default = "false"
	 * 
	 */
	public void setVersionUnsavedValueSimpleKey(boolean flag) {
		_versionUnsavedValue = flag;
	}

	/**
	 * If this node is true, the classes with a version property and compound
	 * keys enabled will have unsave value setting.
	 * 
	 * @param flag
	 *            The Unsave Value Compound key value
	 * @middlegen.setting required = "false" default = "true"
	 * 
	 */
	public void setVersionUnsavedValueCompoundKeys(boolean flag) {
		_versionUnsavedValueCKeys = flag;
	}

	/**
	 * If this node is true, the classes will have dynamic-update enabled
	 * 
	 * @param flag
	 *            The new dynamic update value
	 * @middlegen.setting required = "false" default = "false"
	 * 
	 */
	public void setDynamicUpdate(boolean flag) {
		_dynamicUpdate = flag;
	}

	/**
	 * If this node is true, the classes will have dynamic-insert enabled
	 * 
	 * @param flag
	 *            The new dynamic insert value
	 * @middlegen.setting required = "false" default = "false"
	 * 
	 */
	public void setDynamicInsert(boolean flag) {
		_dynamicInsert = flag;
	}

	/**
	 * If this node is true, the classes will have select-before-update enabled
	 * 
	 * @param flag
	 *            The new dynamic insert value
	 * @middlegen.setting required = "false" default = "false"
	 * 
	 */
	public void setSelectBeforeUpdate(boolean flag) {
		_selectBeforeUpdate = flag;
	}

	/**
	 * If this node is false, the classes will not be mutable
	 * 
	 * @param flag
	 *            The new mutable value
	 * @middlegen.setting required = "false" default = "true"
	 * 
	 */
	public void setMutable(boolean flag) {
		_mutable = flag;
	}

	/**
	 * Sets the database schema that is to be used.
	 * 
	 * @param sname
	 *            The new DatabaseSchema value
	 * @middlegen.setting required = "false" default = ""
	 * 
	 */
	public void setDatabaseSchema(String sname) {
		_databaseSchema = sname.trim();
	}

	/**
	 * Sets the class description that is to be used.
	 * 
	 * @param classd
	 *            The new Class Description value
	 * @middlegen.setting required = "false" default = ""
	 * 
	 */
	public void setClassDescription(String classd) {
		_classDescription = classd.trim();
	}

	/**
	 * Sets the field description that is to be used.
	 * 
	 * @param fieldd
	 *            The new FieldDescription value
	 * @middlegen.setting required = "false" default = ""
	 * 
	 */
	public void setFieldDescription(String fieldd) {
		_fieldDescription = fieldd.trim();
	}

	/**
	 * Sets the access field value that is to be used.
	 * 
	 * @param val
	 *            The new access field value
	 * @middlegen.setting required = "false" default = "property"
	 * 
	 */
	public void setAccessField(String val) {
		_accessField = val.trim();
	}

	/**
	 * Sets the class scope that is to be used.
	 * 
	 * @param classscope
	 *            The new Class Scope value
	 * @middlegen.setting required = "false" default = "public"
	 * 
	 */
	public void setClassScope(String classscope) {
		_classScope = classscope.trim();
	}

	/**
	 * Sets the field scope that is to be used.
	 * 
	 * @param fieldscope
	 *            The new Field Scope value
	 * @middlegen.setting required = "false" default = "private"
	 * 
	 */
	public void setFieldScope(String fieldscope) {
		_fieldScope = fieldscope.trim();
	}

	/**
	 * Sets the field get scope that is to be used.
	 * 
	 * @param getscope
	 *            The new Get Scope value
	 * @middlegen.setting required = "false" default = "public"
	 * 
	 */
	public void setFieldGetScope(String getscope) {
		_getScope = getscope.trim();
	}

	/**
	 * Sets the field set scope that is to be used.
	 * 
	 * @param setscope
	 *            The new Set Scope value
	 * @middlegen.setting required = "false" default = "public"
	 * 
	 */
	public void setFieldSetScope(String setscope) {
		_setScope = setscope.trim();
	}

	/**
	 * Sets the bean constraint name so code generator will create the
	 * constraint processing subsystem.
	 * 
	 * @param value
	 *            The bean property type value
	 * @middlegen.setting required = "false" default = "unbound"
	 * 
	 */
	public void setBeanPropertyType(String value) {
		_beanPropertyType = value.trim();
	}

	/**
	 * Sets the base class that is to be used.
	 * 
	 * @param basec
	 *            The new extends value
	 * @middlegen.setting required = "false" default = ""
	 * 
	 */
	public void setExtends(String basec) {
		_extends = basec.trim();
	}

	/**
	 * Sets the implement class interfaces that are to be used.
	 * 
	 * @param impls
	 *            The new interface(s) the implement
	 * @middlegen.setting required = "false" default = ""
	 * 
	 */
//	public void setImplements(ArrayList impls) {
//		_implements = impls;
//	}
//
//	public void setImplements(String impls) {
//		if (impls == null || impls.trim().length() == 0) {
//			return;
//		}
//		String[] imps = impls.split(",");
//		ArrayList list = new ArrayList(imps.length);
//		for (int i = 0; i < imps.length; i++) {
//			list.add(imps[i]);
//		}
//		_implements = list;
//	}

	/**
	 * Sets the persister that is to be used.
	 * 
	 * @param dPersister
	 *            The new Persister value
	 * @middlegen.setting required = "false" default = ""
	 * 
	 */
	public void setPersister(String dPersister) {
		_persister = dPersister.trim();
	}

	/**
	 * Sets the batch size that is to be used.
	 * 
	 * @param dBatchSize
	 *            The new BatchSize value
	 * @middlegen.setting required = "false" default = ""
	 * 
	 */
	public void setBatchSize(String dBatchSize) {
		_batchsize = dBatchSize.trim();
	}

	/**
	 * If true, the SQL generated will allow updates on the columns.
	 * 
	 * @param flag
	 *            The new columns updateable value
	 * @middlegen.setting required = "false" default = "true"
	 * 
	 */
	public void setColumnsUpdateable(boolean flag) {
		_columnsUpdateable = flag;
	}

	/**
	 * If true, the SQL generated will allow inserts on the columns.
	 * 
	 * @param flag
	 *            The new columns insertable value
	 * @middlegen.setting required = "false" default = "true"
	 * 
	 */
	public void setColumnsInsertable(boolean flag) {
		_columnsInsertable = flag;
	}

	/**
	 * If true, the generated code will include the fields in the toString
	 * method.
	 * 
	 * @param flag
	 *            The new columns include in toString method value
	 * @middlegen.setting required = "false" default = "false"
	 * 
	 */
	public void setColumnsIncToString(boolean flag) {
		_columnsIncToString = flag;
	}

	/**
	 * If true, the generated code will include the fields in the
	 * Equals/HashCode method.
	 * 
	 * @param flag
	 *            The new columns include in Equals/HashCode method value
	 * @middlegen.setting required = "false" default = "false"
	 * 
	 */
	public void setColumnsIncEquals(boolean flag) {
		_columnsIncEquals = flag;
	}

	/**
	 * If true, the generated code will include the fields.
	 * 
	 * @param flag
	 *            The new columns generate property value
	 * @middlegen.setting required = "false" default = "true"
	 * 
	 */
	public void setColumnsGenProperty(boolean flag) {
		_columnsGenProperty = flag;
	}

	/**
	 * Setup the status value so XDoclet hbm meta tags are included in the
	 * source code.
	 * 
	 * @param flag
	 * @middlegen.setting required = "false" default = "false"
	 * 
	 */
	public void setGenXDocletTags(boolean flag) {
		_genXdocletTags = flag;
	}

	/**
	 * Setup the status value so XDoclet hbm meta tags are included in the
	 * source code.
	 * 
	 * @param flag
	 * @middlegen.setting required = "false" default = "false"
	 * 
	 */
	public void setGenLazyNotProxy(boolean flag) {
		_genLazyNotProxy = flag;
	}

	/**
	 * Setup the status value so composite class is either inetgrated or not
	 * into the domain object. Recommended to use an external class for
	 * composite keys.
	 * 
	 * @param flag
	 *            false set the external compoundkey class.
	 * @middlegen.setting required = "false" default = "false"
	 * 
	 */
	public void setGenIntegratedCompositeKeys(boolean flag) {
		_genIntegratedCompositeKeys = flag;
	}

	/**
	 * Sets the optional JavaTypeMapper class to use to determine sql to java
	 * type mappings
	 * 
	 * @param javaTypeMapper
	 *            The class name
	 * @exception InstantiationException
	 * @exception IllegalAccessException
	 * @exception ClassNotFoundException
	 * @middlegen.setting required = "false" default = ""
	 * 
	 */
	public void setJavaTypeMapper(String javaTypeMapper) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		if (javaTypeMapper != null) {
			_javaTypeMapper = (JavaTypeMapper) getClass().getClassLoader().loadClass(
					javaTypeMapper).newInstance();
		}
	}

	/**
	 * Sets the optional DbNameConverter class used to determine java class name
	 * and field mappings.
	 * 
	 * @param javaDbNameConverter
	 *            The new DbNameConverter value
	 * @middlegen.setting required = "false" default = null or"" *
	 * 
	 */
	public void setDbNameConverter(String javaDbNameConverter) {
		if (javaDbNameConverter != null && javaDbNameConverter.trim().length() > 0) {
			System.setProperty("middlegen.DbNameConverter", javaDbNameConverter);
		}
	}

	/**
	 * Gets the JavaTypeMapper attribute of the HibernatePlugin object
	 * 
	 * @return The JavaTypeMapper value
	 */
	public JavaTypeMapper getJavaTypeMapper() {
		return _javaTypeMapper;
	}

	/**
	 * Gets the DbNameConverter class reference.
	 * 
	 * @return The DbNameConverter class value
	 */
	public String getDbNameConverter() {
		return System.getProperty("middlegen.DbNameConverter");
	}

	/**
	 * Gets external compound key generation attribute.
	 * 
	 * @return
	 */
	public boolean getGenIntegratedCompositeKeys() {
		return _genIntegratedCompositeKeys;
	}

	/**
	 * Gets that XDoclet hbm meta tag generation attribute.
	 * 
	 * @return
	 */
	public boolean getGenXDocletTags() {
		return _genXdocletTags;
	}

	/**
	 * Gets that attribute for setting the output to include the lazy attribute
	 * rather than the proxy attribute.
	 * 
	 * @return
	 */
	public boolean getGenLazyNotProxy() {
		return _genLazyNotProxy;
	}

	/**
	 * Gets the class used by this plugin to decorate the Table objects
	 * 
	 * @return The TableDecoratorClass value
	 */
	public Class getTableDecoratorClass() {
		return HibernateTable.class;
	}

	/**
	 * Gets the class used by this plugin to decorate the Column objects
	 * 
	 * @return The ColumnDecoratorClass value
	 */
	public Class getColumnDecoratorClass() {
		return HibernateColumn.class;
	}

	/**
	 * Gets the standard Cascade value to use for the generated relationships.
	 * 
	 * @return The Cascade value.
	 */
	public String getStandardCascade() {
		return _standardCascade;
	}

	/**
	 * Gets the Standard Generator Scheme to use for the generated classes
	 * 
	 * @return The StandardIdGenerator value
	 */
	public String getStandardGeneratorScheme() {
		return _standardIdGenerator;
	}

	/**
	 * Gets the Standard Generator Arg to use for the generated classes
	 * 
	 * @return The StandardIdGeneratorArg value
	 */
	public String getStandardGeneratorArg() {
		return _standardIdGeneratorArg;
	}

	/**
	 * Gets the suffix to use for the generated classes
	 * 
	 * @return The Beansuffix value
	 */
	public String getBeansuffix() {
		return _beansuffix;
	}

	/**
	 * Returns true if the generated classes implement the <tt>Lifecycle</tt>
	 * interface by default
	 * 
	 * @return The Lifecycle value
	 */
	public boolean isLifecycle() {
		return _lifecycle;
	}

	/**
	 * Returns true if the generated classes include the Equals Hashcode
	 * methods.
	 * 
	 * @return The Equals Hashcode value
	 */
	public boolean isEqualsHashcode() {
		return _equalsHashcode;
	}

	/**
	 * Returns true if the generated classes implement the <tt>Validatable</tt>
	 * interface by default
	 * 
	 * @return The Validatable value
	 */
	public boolean isValidatable() {
		return _validatable;
	}

	/**
	 * Returns true if the generated classes have proxies enabled
	 * 
	 * @return The CallBack value
	 */
	public boolean isProxy() {
		return _proxy;
	}

	/**
	 * Returns true if the generated classes have plain compound keys enabled
	 * 
	 * @return The CallBack value
	 */
	public boolean isPlainCompoundKeys() {
		return _plainCKeys;
	}

	/**
	 * Returns true if the generated classes have unsaved value on the version
	 * property when simple keys.
	 * 
	 * @return The CallBack value
	 */
	public boolean isVersionUnsavedValueSimpleKey() {
		return _versionUnsavedValue;
	}

	/**
	 * Returns true if the generated classes have unsaved value on the version
	 * property when compound keys enabled.
	 * 
	 * @return The CallBack value
	 */
	public boolean isVersionUnsavedValueCompoundKeys() {
		return _versionUnsavedValueCKeys;
	}

	/**
	 * Returns true if the generated classes have dynamic insert enabled
	 * 
	 * @return The CallBack value
	 */
	public boolean isDynamicInsert() {
		return _dynamicInsert;
	}

	/**
	 * Returns true if the generated classes have select before update enabled
	 * 
	 * @return The CallBack value
	 */
	public boolean isSelectBeforeUpdate() {
		return _selectBeforeUpdate;
	}

	/**
	 * Returns true if the generated classes have dynamic update enabled
	 * 
	 * @return The CallBack value
	 */
	public boolean isDynamicUpdate() {
		return _dynamicUpdate;
	}

	/**
	 * Returns true if the generated classes are mutable
	 * 
	 * @return The CallBack value
	 */
	public boolean isMutable() {
		return _mutable;
	}

	/**
	 * If false no update SQL used for all fields.
	 * 
	 * @return The Updateable value
	 */
	public boolean isColumnsUpdateable() {
		return _columnsUpdateable;
	}

	/**
	 * If false no insert SQL used for all fields.
	 * 
	 * @return The Insertable value
	 */
	public boolean isColumnsInsertable() {
		return _columnsInsertable;
	}

	/**
	 * If true all generated classes has this field in the toString method.
	 * 
	 * @return The Include To String value
	 */
	public boolean isColumnsIncludeToString() {
		return _columnsIncToString;
	}

	/**
	 * If true all generated classes has this field in the equals/hashcode
	 * method.
	 * 
	 * @return The Include Equals/HashCode value
	 */
	public boolean isColumnsIncludeEquals() {
		return _columnsIncEquals;
	}

	/**
	 * If false no class attribute and methods are generated all fields.
	 * 
	 * @return The Generate property value
	 */
	public boolean isColumnsGenProperty() {
		return _columnsGenProperty;
	}

	/**
	 * Gets the panel used to edit the column settings
	 * 
	 * @return The ColumnSettingsPanel value
	 */
	public JColumnSettingsPanel getColumnSettingsPanel() {
		return new middlegen.plugins.hibernate.swing.JHibernateColumnSettingsPanel();
	}

	/**
	 * Gets the panel used to edit the table settings
	 * 
	 * @return The TableSettingsPanel value
	 */
	public JTableSettingsPanel getTableSettingsPanel() {
		return new middlegen.plugins.hibernate.swing.JHibernateTableSettingsPanel();
	}

	/**
	 * Gets the ColumnsUpdateable value
	 * 
	 * @return The ColumnsUpdateableNode value
	 */
	public boolean getColumnsUpdateable() {
		return _columnsUpdateable;
	}

	/**
	 * Gets the ColumnsInsertable value
	 * 
	 * @return The ColumnsInsertableNode value
	 */
	public boolean getColumnsInsertable() {
		return _columnsInsertable;
	}

	/**
	 * Gets the ColumnsIncludeToString value
	 * 
	 * @return The ColumnsIncludeToStringNode value
	 */
	public boolean getColumnsIncludeToString() {
		return _columnsIncToString;
	}

	/**
	 * Gets the ColumnsIncludeEquals value
	 * 
	 * @return The ColumnsIncludeEqualsNode value
	 */
	public boolean getColumnsIncludeEquals() {
		return _columnsIncEquals;
	}

	/**
	 * Gets the ColumnsGenProperty value
	 * 
	 * @return The ColumnsGenPropertyNode value
	 */
	public boolean getColumnsGenProperty() {
		return _columnsGenProperty;
	}

	/**
	 * Gets the access field val
	 * 
	 * @return The field access field value
	 */
	public String getAccessField() {
		return _accessField;
	}

	/**
	 * Gets the field scope
	 * 
	 * @return The field scope value
	 */
	public String getFieldScope() {
		return _fieldScope;
	}

	/**
	 * Gets the field get scope
	 * 
	 * @return The field get scope value
	 */
	public String getFieldGetScope() {
		return _getScope;
	}

	/**
	 * Gets the field set scope
	 * 
	 * @return The field set scope value
	 */
	public String getFieldSetScope() {
		return _setScope;
	}

	/**
	 * Gets the the bean property type for the column
	 * 
	 * @return The bean property type constraint value
	 */
	public String getBeanPropertyType() {
		return _beanPropertyType.trim();
	}

	/**
	 * Validate the plugin
	 * 
	 * @exception MiddlegenException
	 *                Describe the exception
	 */
	public void validate() throws MiddlegenException {
		super.validate();
	}

	/**
	 * Gets the database schema
	 * 
	 * @return The Database Schema Value
	 */
	protected String getDatabaseSchema() {
		return _databaseSchema;
	}

	/**
	 * Gets the data persister
	 * 
	 * @return The Data persister Value
	 */
	protected String getPersister() {
		return _persister;
	}

	/**
	 * Gets the data batchsize
	 * 
	 * @return The Data batchsize Value
	 */
	protected String getBatchSize() {
		return _batchsize;
	}

	/**
	 * Gets the class description
	 * 
	 * @return The class description Value
	 */
	protected String getClassDescription() {
		return _classDescription;
	}

	/**
	 * Gets the field description
	 * 
	 * @return The field description Value
	 */
	protected String getFieldDescription() {
		return _fieldDescription;
	}

	/**
	 * Gets the class scope
	 * 
	 * @return The class scope Value
	 */
	protected String getClassScope() {
		return _classScope;
	}

	/**
	 * Gets the base class name
	 * 
	 * @return The extends Value
	 */
	protected String getExtends() {
		return _extends;
	}

	/**
	 * Gets the Lifecycle value
	 * 
	 * @return The Lifecycle value
	 */
	protected boolean getLifecycle() {
		return _lifecycle;
	}

	/**
	 * Gets the Equals/Hashcode value
	 * 
	 * @return The Equals hashcode value
	 */
	protected boolean getEqualsHashcode() {
		return _equalsHashcode;
	}

	/**
	 * Gets the Lifecycle node
	 * 
	 * @return The Lifecycle value
	 */
	protected BooleanNode getLifecycleNode() {
		return _lifecycleNode;
	}

	/**
	 * Gets the Equals Hashcode node
	 * 
	 * @return The Equals Hashcode value
	 */
	protected BooleanNode getEqualsHashcodeNode() {
		return _equalsHashcodeNode;
	}

	/**
	 * Gets the Validatable value
	 * 
	 * @return The Validatable value
	 */
	protected boolean getValidatable() {
		return _validatable;
	}

	/**
	 * Gets the Validatable node
	 * 
	 * @return The Validatable Node
	 */
	protected BooleanNode getValidatableNode() {
		return _validatableNode;
	}

	/**
	 * Gets the Plain Compound Keys flag
	 * 
	 * @return The Plain Compound Keys value
	 */
	protected boolean getPlainCompoundKeys() {
		return _plainCKeys;
	}

	/**
	 * Gets the Plain Compound Keys Node
	 * 
	 * @return The Plain Compound Keys node value
	 */
	protected BooleanNode getPlainCompoundKeyNode() {
		return _plainCKeyNode;
	}

	/**
	 * Gets the Proxy value
	 * 
	 * @return The Proxy value
	 */
	protected boolean getProxy() {
		return _proxy;
	}

	/**
	 * Gets the Proxy node
	 * 
	 * @return The Proxy node
	 */
	protected BooleanNode getProxyNode() {
		return _proxyNode;
	}

	/**
	 * Gets the Dynamic Update value
	 * 
	 * @return The Dynamic Update value
	 */
	protected boolean getDynamicUpdate() {
		return _dynamicUpdate;
	}

	/**
	 * Gets the Dynamic Insert value
	 * 
	 * @return The Dynamic Insert value
	 */
	protected boolean getDynamicInsert() {
		return _dynamicInsert;
	}

	/**
	 * Gets the Select before update value
	 * 
	 * @return The Select Before Update value
	 */
	protected boolean getSelectBeforeUpdate() {
		return _selectBeforeUpdate;
	}

	/**
	 * Gets the Dynamic Update node
	 * 
	 * @return The Dynamic Update node
	 */
	protected BooleanNode getDynamicUpdateNode() {
		return _dynamicUpdateNode;
	}

	/**
	 * Gets the Dynamic Insert node
	 * 
	 * @return The Dynamic Insert node
	 */
	protected BooleanNode getDynamicInsertNode() {
		return _dynamicInsertNode;
	}

	/**
	 * Gets the Select Before Update node
	 * 
	 * @return The Select Before Update node
	 */
	protected BooleanNode getSelectBeforeUpdateNode() {
		return _selectBeforeUpdateNode;
	}

	/**
	 * Gets the Mutable value
	 * 
	 * @return The Mutable value
	 */
	protected boolean getMutable() {
		return _mutable;
	}

	/**
	 * Gets the Mutable node
	 * 
	 * @return The Mutable node
	 */
	protected BooleanNode getMutableNode() {
		return _mutableNode;
	}

	/**
	 * Gets the Implements List
	 * 
	 * @return The Implement values
	 */
//	protected ArrayList getImplements() {
//		return (ArrayList) _implements.clone();
//	}

	private String _version;

	/**
	 * @return Returns the _version.
	 */
	public String getVersion() {
		return _version;
	}

	/**
	 * @param _version
	 *            The _version to set.
	 */
	public void setVersion(String version) {
		this._version = version;
	}

	private String _dir;

	/**
	 * @return Returns the _dir.
	 */
	public String getDir() {
		return _dir;
	}
	
	/**
	 * @return Returns the _dir.
	 */
	public String getClassDir() {
		if(_dir != null)
			return _dir.trim().replace("/", ".");
		return _dir;
	}

	/**
	 * @param _dir
	 *            The _dir to set.
	 */
	public void setDir(String dir) {
		this._dir = dir;
	}

	//DomainObject
	private HibernateDomainObjectElement _hibernateDomainObject = null;
	public void setHibernateDomainObject(HibernateDomainObjectElement dao) {
		_hibernateDomainObject = dao;
	}
	public HibernateDomainObjectElement getHibernateDomainObject() {
		return _hibernateDomainObject;
	}
	public void addConfiguredHibernateDomainObject(HibernateDomainObjectElement dao) {
		_hibernateDomainObject = dao;
	}
	
	//Manager
	private HibernateManagerElement _hibernateManager = null;
	public void setHibernateManager(HibernateManagerElement dao) {
		_hibernateManager = dao;
	}
	public HibernateManagerElement getHibernateManager() {
		return _hibernateManager;
	}
	public void addConfiguredHibernateManager(HibernateManagerElement dao) {
		_hibernateManager = dao;
	}
	
	//Controller
	private HibernateControllerElement _hibernateController = null;
	public void setHibernateController(HibernateControllerElement dao) {
		_hibernateController = dao;
	}
	public HibernateControllerElement getHibernateController() {
		return _hibernateController;
	}
	public void addConfiguredHibernateController(HibernateControllerElement dao) {
		_hibernateController = dao;
	}

	//ServiceImpl
	private HibernateServiceImplElement _hibernateServiceImpl = null;
	public void setHibernateServiceImplElement(HibernateServiceImplElement dao) {
		_hibernateServiceImpl = dao;
	}
	public HibernateServiceImplElement getHibernateServiceImplElement() {
		return _hibernateServiceImpl;
	}
	public void addConfiguredHibernateServiceImplElement(HibernateServiceImplElement dao) {
		_hibernateServiceImpl = dao;
	}

	/** Register the file producers for this plugin */
	protected void registerFileProducers() {
		// ibatis
		String dir1 = "";
		if (getDir() != null && getDir().trim().length() > 0) {
			dir1 = "/" + getDir().trim() + "/";
		}

		ConfigureFile config = ConfigureFile.getInstance();
		//WW_TODO 代码生成工具-生成代码文件
		System.out.println("====4567.dir=============="+config.getProperty("test.dir"));
		System.out.println("====queryObject=============="+getQueryObjectFields());
		
		setDaoSuffix(config.getProperty("dao.suffix").trim());
		setDaoImplSuffix(config.getProperty("daoImpl.suffix").trim());
		setDaoDir(config.getProperty("dao.dir").trim());
		setDaoImplDir(config.getProperty("daoImpl.dir").trim().replace("/", "."));
		setIbatisExt(config.getProperty("ibatisExt").trim());
		if(config.getProperty("implements")!=null&&config.getProperty("implements").trim().length()>0){
			setImplements(config.getProperty("implements").trim());
		}
		
		if(permitGenCode("entity")){
		//ibitis map.xml
		addConfiguredFileproducer(new FileProducer(getDestinationDir(), dir1 + "{0}"
				+ getBeansuffix() + "_SqlMap.xml", getClass().getResource("iBatis.vm")));
		}
		
		//edit by shishengfeng end


		// _hibernateDomainObject
		if (_hibernateDomainObject != null && permitGenCode("entity")) {
			String dir = "";
			if (_hibernateDomainObject.getDir() != null
					&& _hibernateDomainObject.getDir().trim().length() > 0) {
				dir = "/" + _hibernateDomainObject.getDir().trim() + "/";
			}
			addConfiguredFileproducer(new FileProducer(getDestinationDir(), dir
					+ _hibernateDomainObject.getPrefix() + "{0}" + _hibernateDomainObject.getSuffix()
					+ ".java", getClass().getResource("DomainObject.vm")));
			//生成查询对象
			addConfiguredFileproducer(new FileProducer(getDestinationDir(), dir
					+ _hibernateDomainObject.getPrefix() + "{0}" + _hibernateDomainObject.getSuffix()
					+ "Query.java", getClass().getResource("QueryParameter.vm")));
			
		}
		
		// service
		if (_hibernateManager != null) {
			String dir = "";
			if (_hibernateManager.getDir() != null
					&& _hibernateManager.getDir().trim().length() > 0) {
				dir = "/" + _hibernateManager.getDir().trim() + "/";
			}

			/*addConfiguredFileproducer(new FileProducer(getDestinationDir(), dir
					+ _hibernateManager.getPrefix() + "{0}" + _hibernateManager.getSuffix()
					+ ".java", getClass().getResource("ServiceImpl.vm")));*/
			if(permitGenCode("service")){
			addConfiguredFileproducer(new FileProducer(getDestinationDir(), "/"+_hibernateManager.getInterFaceDir().trim()+"/"
					+ _hibernateManager.getPrefix() + "{0}" + _hibernateManager.getInterFaceSuffix()
					+ ".java", getClass().getResource("Service.vm")));
			}
		}
		
		// _hibernateController
		/*if (_hibernateController != null) {
			String dir = "";
			if (_hibernateController.getDir() != null
					&& _hibernateController.getDir().trim().length() > 0) {
				dir = "/" + _hibernateController.getDir().trim() + "/";
			}

			addConfiguredFileproducer(new FileProducer(getDestinationDir(), dir
					+ _hibernateController.getPrefix() + "{0}" + _hibernateController.getSuffix()
					+ ".java", getClass().getResource("Controller.vm")));
		}*/
		
		//dao
		if(permitGenCode("service")){
		addConfiguredFileproducer(new FileProducer(getDestinationDir(), "/"+config.getProperty("dao.dir").trim()+"/"
				+ config.getProperty("daoImpl.prefix").trim() + "{0}" + config.getProperty("dao.suffix").trim()
				+ ".java", getClass().getResource("Repository.vm")));
		}
		//生成测试代码 
		if (permitGenCode("test")){
			String path = getDestinationDir().getPath();
			//System.out.println(path.substring(0,path.length() - this.getDestination() ));
			String responsePath = "/"+config.getProperty("dao.dir").trim()+"/"
					+ config.getProperty("daoImpl.prefix").trim() + "{0}" + config.getProperty("dao.suffix").trim()
					+ "Test.java";
			
			FileProducer fileProducer = new FileProducer(getTestDir(), responsePath, getClass().getResource("RepositoryTest.vm"));
			addConfiguredFileproducer(fileProducer);
			
			//service test
			String dir = "";
			if (_hibernateManager.getDir() != null
					&& _hibernateManager.getDir().trim().length() > 0) {
				dir = "/" + _hibernateManager.getDir().trim() + "/";
			}

			addConfiguredFileproducer(new FileProducer(getTestDir(), "/"+_hibernateManager.getInterFaceDir().trim()+"/"
					+ _hibernateManager.getPrefix() + "{0}" + _hibernateManager.getInterFaceSuffix()
					+ "Test.java", getClass().getResource("ServiceTest.vm")));
		}
		

	}

	private boolean permitGenCode(String type) {
		String gtype = this.getGenType();
		Map<String,String> map = new HashMap<String,String>();
		map.put("test", "0,");
		map.put("entity", "0,2,1,");
		map.put("service", "0,1,");
		String s = map.get(type);
		if(s != null && s.indexOf(gtype + ",") >= 0){
			return true;
		}
		return false;
	}

}
