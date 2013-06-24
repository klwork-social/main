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
package middlegen.plugins.hibernate;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;

import middlegen.CodeGenUtil;
import middlegen.Column;
import middlegen.Table;
import middlegen.Util;
import middlegen.ColumnMap;
import middlegen.javax.JavaTable;
import middlegen.plugins.hibernate.predicates.relation.NotTargetInOriginPK;
import middlegen.plugins.hibernate.predicates.relation.TargetInOriginPK;
import middlegen.plugins.hibernate.predicates.relation.TargetOneToOneFKRoles;
import middlegen.predicate.And;
import middlegen.predicate.CompositePredicate;
import middlegen.predicates.column.Basic;
import middlegen.predicates.column.PrimaryKey;
import middlegen.util.BooleanNode;
import middlegen.RelationshipRole;

/**
 * The table decorator used by the Hibernate plugin.
 * 
 * @author Gavin King and David Channon
 * @created 1 January 2003
 * @version 2.1
 */
public class HibernateTable extends JavaTable {

	/**
	 * If node is true, generated classes will implement the <tt>Lifecycle</tt>
	 * interface by default
	 */
	private BooleanNode _lifecycle;

	/**
	 * If node is true, generated classes will implement the
	 * <tt>Validatable</tt> interface by default
	 */
	private BooleanNode _validatable;

	/** If node is true, generated classes have equalsHashcode methods */
	private BooleanNode _equalsHashcode;

	/** If node is true, generated classes have proxies enabled */
	private BooleanNode _proxy;

	/** If node is true, generated classes have plain compound keys enabled */
	private BooleanNode _plainCKey;

	/** If node is false, generated classes will not be mutable */
	private BooleanNode _mutable;

	/** If node is true, generated classes have dynamic update enabled */
	private BooleanNode _dynamicUpdate;

	/** If node is true, generated classes have dynamic insert enabled */
	private BooleanNode _dynamicInsert;

	/** If node is true, generated classes have select-before-update enabled */
	private BooleanNode _selectBeforeUpdate;

	/** Name of the generated class */
	private String _destinationClassName;

	/** Optional Name of the database schema */
	private String _databaseSchema;

	/** Optional Name of the persister */
	private String _persister;

	/** Optional Name of the batchsize */
	private String _batchsize;

	/** Optional Name of the where */
	private String _where;

	/** Optional Name of the class description */
	private String _classDescription;

	/** Optional Name of the class scope */
	private String _classScope;

	/** Optional Name of the extend base class */
	private String _extends;

	/**
	 * Optional Extra implements class identifiers for code generation
	 */
	private ArrayList _implements;

	/**
	 * If node is true, the primary key will be an inner class generated in the
	 * bean by default
	 */
	private BooleanNode _pkInnerClass;

	/** Name of the key generator */
	private String _keyGenerator;

	/** Optional Name of the key generator argument */
	private String _keyGeneratorArg;

	/**
	 * Constructor for HibernateTable
	 * 
	 * @param table
	 *            The original table object to wrap
	 */
	public HibernateTable(Table table) {
		super(table);
	}

	/**
	 * If true, the generated class will implement the <tt>Lifecycle</tt>
	 * interface
	 * 
	 * @param flag
	 *            The new Lifecycle value
	 */
	public void setImplementLifecycle(boolean flag) {
		setPrefsValue("lifecycle", Util.string(flag));
		_lifecycle.setValue(flag);
	}

	/**
	 * If true, the generated class will implement the Equals/Hashcode methods.
	 * 
	 * @param flag
	 *            The equals hashcode indicator
	 */
	public void setEqualsHashcode(boolean flag) {
		setPrefsValue("equalshashcode", Util.string(flag));
		_equalsHashcode.setValue(flag);
	}

	/**
	 * If true, the generated class will implement the <tt>Validatable</tt>
	 * interface
	 * 
	 * @param flag
	 *            The new Validatable value
	 */
	public void setImplementValidatable(boolean flag) {
		setPrefsValue("validatable", Util.string(flag));
		_validatable.setValue(flag);
	}

	/**
	 * Sets the Plain Compound Key attribute of the HibernateTable object
	 * 
	 * @param flag
	 *            The new Proxy value
	 */
	public void setPlainCompoundKey(boolean flag) {
		setPrefsValue("plainCKey", Util.string(flag));
		_plainCKey.setValue(flag);
	}

	/**
	 * Sets the Proxy attribute of the HibernateTable object
	 * 
	 * @param flag
	 *            The new Proxy value
	 */
	public void setProxy(boolean flag) {
		setPrefsValue("proxy", Util.string(flag));
		_proxy.setValue(flag);
	}

	/**
	 * Sets the Mutable attribute of the HibernateTable object
	 * 
	 * @param flag
	 *            The new Mutable value
	 */
	public void setMutable(boolean flag) {
		setPrefsValue("mutable", Util.string(flag));
		_mutable.setValue(flag);
	}

	/**
	 * Sets the dynamic update attribute of the HibernateTable object
	 * 
	 * @param flag
	 *            The new Dynamic Update value
	 */
	public void setDynamicUpdate(boolean flag) {
		setPrefsValue("dynamicUpdate", Util.string(flag));
		_dynamicUpdate.setValue(flag);
	}

	/**
	 * Sets the dynamic insert attribute of the HibernateTable object
	 * 
	 * @param flag
	 *            The new Dynamic Insert value
	 */
	public void setDynamicInsert(boolean flag) {
		setPrefsValue("dynamicInsert", Util.string(flag));
		_dynamicInsert.setValue(flag);
	}

	/**
	 * Sets the select before update attribute of the HibernateTable object
	 * 
	 * @param flag
	 *            The new select before update value
	 */
	public void setSelectBeforeUpdate(boolean flag) {
		setPrefsValue("selectBeforeUpdate", Util.string(flag));
		_selectBeforeUpdate.setValue(flag);
	}

	/**
	 * Sets the destination class name
	 * 
	 * @param value
	 *            The new DestinationClassName value
	 */
	public void setDestinationClassName(String value) {
		setPrefsValue("destinationClassName", value);
		_destinationClassName = value;
	}

	/**
	 * Sets the database schema name
	 * 
	 * @param value
	 *            The new database schema name value
	 */
	public void setDatabaseSchema(String value) {
		setPrefsValue("databaseSchema", value);
		_databaseSchema = value;
	}

	/**
	 * Sets the data persister name
	 * 
	 * @param value
	 *            The new data persister name value
	 */
	public void setPersister(String value) {
		setPrefsValue("persister", value);
		_persister = value;
	}

	/**
	 * Sets the data batchsize name
	 * 
	 * @param value
	 *            The new data batchsize name value
	 */
	public void setBatchSize(String value) {
		setPrefsValue("batchsize", value);
		_batchsize = value;
	}

	/**
	 * Sets the data where name
	 * 
	 * @param value
	 *            The new data where name value
	 */
	public void setWhere(String value) {
		setPrefsValue("where", value);
		_where = value;
	}

	/**
	 * Sets the class description name
	 * 
	 * @param value
	 *            The class description name value
	 */
	public void setClassDescription(String value) {
		setPrefsValue("description", value);
		_classDescription = value;
	}

	/**
	 * Sets the class scope
	 * 
	 * @param value
	 *            The class scope value
	 */
	public void setClassScope(String value) {
		setPrefsValue("classscope", value);
		_classScope = value;
	}

	/**
	 * Sets the object base class
	 * 
	 * @param value
	 *            The new class extention name value
	 */
	public void setExtends(String value) {
		setPrefsValue("extends", value);
		_extends = value;
	}

	/**
	 * Sets the implements list
	 * 
	 * @param value
	 *            The implements list value
	 */
	public void setImplements(ArrayList value) {
		setPrefsValue("implements", encodeImplementsStr(value));
		_implements = value;
	}

	/**
	 * Sets the data KeyGenerator name
	 * 
	 * @param value
	 *            The key generator name
	 */
	public void setKeyGenerator(String value) {
		setPrefsValue("keygenerator", value);
		_keyGenerator = value;
	}

	/**
	 * Sets the data KeyGenerator arg
	 * 
	 * @param value
	 *            The key generator arg
	 */
	public void setKeyGeneratorArg(String value) {
		setPrefsValue("keygeneratorarg", value);
		_keyGeneratorArg = value;
	}

	/**
	 * Gets the destination class name
	 * 
	 * @return The DestinationFileName value
	 */
	public String getDestinationClassName() {
		return _destinationClassName;
	}
	
	
	/**
	 * 得到类的名称，第一个字母小写
	 * 
	 * @return The DestinationFileName value
	 */
	public String getFirstLowerClassName() {
		//WW_TODO 代码生成工具-加入代码，定义名小写
		return CodeGenUtil.firstWordToLower(getDestinationClassName());
	}
	

	public String getClassNameUpperCase() {
		return _destinationClassName.toUpperCase();
	}

	/**
	 * Gets the compound key destination class name
	 * 
	 * @return The CompondKeyDestinationFileName value
	 */
	public String getCompoundKeyDestinationClassName() {
		return _destinationClassName + "PK";
	}

	/**
	 * Gets the the database schema
	 * 
	 * @return The Database Schema value
	 */
	public String getDatabaseSchema() {
		return _databaseSchema.trim();
	}

	/**
	 * Gets the the data persister
	 * 
	 * @return The Data persister value
	 */
	public String getPersister() {
		return _persister.trim();
	}

	/**
	 * Gets the the data batchsize
	 * 
	 * @return The Data batchsize value
	 */
	public String getBatchSize() {
		return _batchsize.trim();
	}

	/**
	 * Gets the the data where
	 * 
	 * @return The Data where value
	 */
	public String getWhere() {
		return _where.trim();
	}

	/**
	 * Gets the the class description
	 * 
	 * @return The class description value
	 */
	public String getClassDescription() {
		return _classDescription.trim();
	}

	/**
	 * Gets the the class scope
	 * 
	 * @return The class scope value
	 */
	public String getClassScope() {
		return _classScope.trim();
	}

	/**
	 * Gets the the base class value
	 * 
	 * @return The base class value
	 */
	public String getExtends() {
		return _extends.trim();
	}

	/**
	 * Get the Versioning Column if one is present.
	 * 
	 * @return Version Column or null
	 */
	public HibernateColumn getVersioningField() {
		Collection columns = getColumns();
		Iterator citr = columns.iterator();
		while (citr.hasNext()) {
			HibernateColumn curCol = (HibernateColumn) citr.next();
			if (curCol.getColumnSpecialty().equals(HibernatePlugin.ColVersionId)
					|| curCol.getColumnSpecialty().equals(HibernatePlugin.ColTimestampId)) {
				return curCol;
			}
		}
		return null;
	}

	/**
	 * If true, the generated class will implement the <tt>Lifecycle</tt>
	 * interface
	 * 
	 * @return The Lifecycle value
	 */
	public boolean isImplementLifecycle() {
		return _lifecycle.isCompletelyTrue();
	}

	/**
	 * If true, the generated class will implement the Equals Hashcode methods.
	 * 
	 * @return The Equals Hashcode indicator
	 */
	public boolean isEqualsHashcode() {
		return _equalsHashcode.isCompletelyTrue();
	}

	/**
	 * If true, the generated class will implement the <tt>Validatable</tt>
	 * interface
	 * 
	 * @return The Validatable value
	 */
	public boolean isImplementValidatable() {
		return _validatable.isCompletelyTrue();
	}

	/**
	 * Gets the Proxy attribute of the HibernateTable object
	 * 
	 * @return The Proxy value
	 */
	public boolean isProxy() {
		return _proxy.isCompletelyTrue();
	}

	/**
	 * Gets the Plain Compound Key attribute of the HibernateTable object
	 * 
	 * @return The Plain Compound Key value
	 */
	public boolean isPlainCompoundKey() {
		return _plainCKey.isCompletelyTrue();
	}

	/**
	 * Gets the Mutable attribute of the HibernateTable object
	 * 
	 * @return The mutable value
	 */
	public boolean isMutable() {
		return _mutable.isCompletelyTrue();
	}

	/**
	 * Gets the Dynamic Update attribute of the HibernateTable object
	 * 
	 * @return The Dynamic Update value
	 */
	public boolean isDynamicUpdate() {
		return _dynamicUpdate.isCompletelyTrue();
	}

	/**
	 * Gets the Dynamic Insert attribute of the HibernateTable object
	 * 
	 * @return The Dynamic Insert value
	 */
	public boolean isDynamicInsert() {
		return _dynamicInsert.isCompletelyTrue();
	}

	/**
	 * Gets the select before update attribute of the HibernateTable object
	 * 
	 * @return The Dynamic Insert value
	 */
	public boolean isSelectBeforeUpdate() {
		return _selectBeforeUpdate.isCompletelyTrue();
	}

	/**
	 * Gets the status indicating that a schema value is in existence.
	 * 
	 * @return presence of a schema name
	 */
	public boolean isSchemaName() {
		return (_databaseSchema == null || _databaseSchema.trim().length() == 0) ? false
				: true;
	}

	/**
	 * Gets the status indicating that a persister value is in existence.
	 * 
	 * @return presence of a persister name
	 */
	public boolean isPersisterName() {
		return (_persister == null || _persister.trim().length() == 0) ? false : true;
	}

	/**
	 * Gets the status indicating that a batchsize value is in existence.
	 * 
	 * @return presence of a batchsize value
	 */
	public boolean isBatchSizeVal() {
		return (_batchsize == null || _batchsize.trim().length() == 0) ? false : true;
	}

	/**
	 * Gets the status indicating that a where value is in existence.
	 * 
	 * @return presence of a where value
	 */
	public boolean isWhereVal() {
		return (_where == null || _where.trim().length() == 0) ? false : true;
	}

	/**
	 * Gets the status indicating that a class description value is in
	 * existence.
	 * 
	 * @return presence of a class description name
	 */
	public boolean isClassDescriptionName() {
		return (_classDescription == null || _classDescription.trim().length() == 0) ? false
				: true;
	}

	/**
	 * Gets the status indicating that a class scope value is in existence.
	 * Public is the default so returns false if public scope or scope not
	 * present.
	 * 
	 * @return presence of a valid class scope indicator other than the default.
	 */
	public boolean isClassScopeName() {
		return (_classScope == null || _classScope.trim().length() == 0 || _classScope
				.equals(HibernatePlugin.PublicId)) ? false : true;
	}

	/**
	 * Gets the status indicating that an extends value is in existence.
	 * 
	 * @return presence of a extends name
	 */
	public boolean isExtendsName() {
		return (_extends == null || _extends.trim().length() == 0) ? false : true;
	}

	/**
	 * Gets the status indicating that a CompositeKey is in existence.
	 * 
	 * @return presence of a composite key
	 */
	public boolean isCompositeKey() {
		return getPkColumn() == null ? true : false;
	}

	/**
	 * Gets the status indicating that a versioning field is in existence.
	 * 
	 * @return presence of a versioning field
	 */
	public boolean isVersioningPresent() {
		return getVersioningField() == null ? false : true;
	}

	/**
	 * Gets the LifecycleNode object
	 * 
	 * @return The LifecycleNode value
	 */
	public BooleanNode getLifecycleNode() {
		return _lifecycle;
	}

	/**
	 * Gets the EqualsHashcodeNode object
	 * 
	 * @return The EqualsHashcodeNode value
	 */
	public BooleanNode getEqualsHashcodeNode() {
		return _equalsHashcode;
	}

	/**
	 * Gets the ValidatableNode object
	 * 
	 * @return The ValidatableNode value
	 */
	public BooleanNode getValidatableNode() {
		return _validatable;
	}

	/**
	 * Gets the PlainCompoundKeyNode attribute of the HibernateTable object
	 * 
	 * @return The ProxyNode value
	 */
	public BooleanNode getPlainCompoundKeyNode() {
		return _plainCKey;
	}

	/**
	 * Gets the ProxyNode attribute of the HibernateTable object
	 * 
	 * @return The ProxyNode value
	 */
	public BooleanNode getProxyNode() {
		return _proxy;
	}

	/**
	 * Gets the DynamicUpdateNode attribute of the HibernateTable object
	 * 
	 * @return The DynamicUpdateNode value
	 */
	public BooleanNode getDynamicUpdateNode() {
		return _dynamicUpdate;
	}

	/**
	 * Gets the DynamicInsertNode attribute of the HibernateTable object
	 * 
	 * @return The DynamicInsertNode value
	 */
	public BooleanNode getDynamicInsertNode() {
		return _dynamicInsert;
	}

	/**
	 * Gets the SelectBeforeUpdateNode attribute of the HibernateTable object
	 * 
	 * @return The SelectBeforeUpdateNode value
	 */
	public BooleanNode getSelectBeforeUpdateNode() {
		return _selectBeforeUpdate;
	}

	/**
	 * Gets the MutableNode attribute of the HibernateTable object
	 * 
	 * @return The MutableNode value
	 */
	public BooleanNode getMutableNode() {
		return _mutable;
	}

	/**
	 * Gets the implements list attribute of the HibernateTable object
	 * 
	 * @return The ArrayList implements value
	 */
	public ArrayList getImplements() {
		return _implements;
	}

	/**
	 * Gets the the data for the key generator
	 * 
	 * @return The key generator value
	 */
	public String getKeyGenerator() {
		return _keyGenerator.trim();
	}

	/**
	 * Gets the raw data for the key generator argument
	 * 
	 * @return The key generator value
	 */
	public String getKeyGeneratorArg() {
		return _keyGeneratorArg.trim();
	}

	/**
	 * Gets the formatted data for the key generator argument. Substitutes the
	 * table name for '{0}' entry within the argument.
	 * 
	 * @return The key generator value
	 */
	public String getFormattedKeyGeneratorArg() {
		String genArgValue = _keyGeneratorArg.trim();
		if (genArgValue.indexOf("{0}") != -1) {
			// Parameterised key generator arg. Replace {0} with table name.
			genArgValue = MessageFormat.format(genArgValue, new String[] { this
					.getSqlName() });
		}
		return genArgValue;
	}

	/**
	 * Detemines if the id known generator required the unsaved value to be set
	 * otherwise system defaults to a 'NULL'.
	 * 
	 * @return Unsaved Value or NULL (the default).
	 */
	public String getGeneratorUnsavedValue() {
		if ("identity".equals(_keyGenerator)) {
			return "0";
		}
		return null;
	}

	/**
	 * Parameters for the select ID generator. Allows the hbm xml and/or code to
	 * be a little bit compact.
	 * 
	 * @return
	 */
	public boolean isGeneratorRequiresParams() {
		if ("hilo".equals(_keyGenerator) || "seqhilo".equals(_keyGenerator)
				|| "sequence".equals(_keyGenerator)) {
			return true;
		}
		return false;
	}

	/**
	 * Gets the RelationshipRoles that are part of the primary key
	 * 
	 * @return The TargetInOriginPK value
	 */
	public Collection getPrimaryKeyRoles() {
		return getRelationshipRoles(TargetInOriginPK.getInstance());
	}

	/**
	 * Gets the RelationshipRoles that are part of the primary key
	 * 
	 * @return The NotTargetInOriginPK value
	 */
	public Collection getNotPrimaryKeyRoles() {
		return getRelationshipRoles(NotTargetInOriginPK.getInstance());
	}

	/**
	 * Gets the RelationshipRoles that are part of the primary key
	 * 
	 * @return The TargetOneToOneFKRoles value
	 */
	public Collection getChildSidePrimaryKeyOnlyOneToOneRoles() {
		return getRelationshipRoles(TargetOneToOneFKRoles.getInstance());
	}

	/**
	 * Gets the FkColumnsInactiveSideOfRelation attribute of the HibernateTable
	 * object that are not also primary keys.
	 * 
	 * @todo-javadoc Write javadocs for method parameter
	 * @todo-javadoc Write javadocs for method parameter
	 * @param role
	 *            Describe what the parameter does
	 * @return The FkColumnsInactiveSideOfRelation value
	 */
	public Collection getFkColsInactive(RelationshipRole role) {
		return this.getColsInactive(role, false);
	}

	/**
	 * Gets the FkColumnsInactiveSideOfRelation attribute of the HibernateTable
	 * object that are also primary keys.
	 * 
	 * @todo-javadoc Write javadocs for method parameter
	 * @todo-javadoc Write javadocs for method parameter
	 * @param role
	 *            Describe what the parameter does
	 * @return The FkColumnsInactiveSideOfRelation value
	 */
	public Collection getFkColsPkInactive(RelationshipRole role) {
		return this.getColsInactive(role, true);
	}

	/**
	 * Gets the FkColumnsInactiveSideOfRelation attribute of the HibernateTable
	 * object. Selector choose PK columns or non PK Cols.
	 * 
	 * @todo-javadoc Write javadocs for method parameter
	 * @todo-javadoc Write javadocs for method parameter
	 * @todo-javadoc Write javadocs for method parameter
	 * @param role
	 *            Describe what the parameter does
	 * @param pkSelector
	 *            Describe what the parameter does
	 * @return The FkColumnsInactiveSideOfRelation value
	 */
	public Collection getColsInactive(RelationshipRole role, boolean pkSelector) {
		Collection retVal = new ArrayList();
		if (!role.getRelation().isMany2Many()) {
			ColumnMap[] cMap = role.getTargetRole().getColumnMaps();
			for (int i = 0; i < cMap.length; i++) {
				HibernateColumn col = (HibernateColumn) this.getColumn(cMap[i]
						.getForeignKey());
				if (pkSelector) {
					if (col.isPk()) {
						retVal.add(col);
					}
				} else {
					if (!col.isPk()) {
						retVal.add(col);
					}
				}
			}
		}
		return retVal;
	}

	/**
	 * Delermines if any Relationships exists involving multiple fields where it
	 * has PK and non PK members. Does not include many-to-many relationships.
	 * 
	 * @return true or false value.
	 */
	public boolean isRelationshipPkAndNonPkMembers() {
		boolean retVal = false;
		// Mixed relationships are in not-primarykey-only roles.
		Collection rel = this.getNotPrimaryKeyRoles();
		Iterator itr = rel.iterator();
		while (itr.hasNext()) {
			RelationshipRole role = (RelationshipRole) itr.next();
			if (role.getRelation().isMany2Many()) {
				continue;
			}
			RelationshipRole trole = role.getTargetRole();
			ColumnMap[] colMaps = trole.getColumnMaps();
			boolean pk = false;
			boolean nonPk = false;
			for (int i = 0; i < colMaps.length; i++) {
				Column col = this.getColumn(colMaps[i].getForeignKey());
				if (col.isPk()) {
					pk = true;
				} else {
					nonPk = true;
				}
			}
			if (pk && nonPk) {
				retVal = true;
			}
		}
		return retVal;
	}

	/**
	 * Gets the RoleOneToOnePrimaryKeys attribute of the HibernateTable object
	 * 
	 * @param role
	 *            Describe what the parameter does
	 * @param target
	 *            Describe what the parameter does
	 * @return The RoleOneToOnePrimaryKeys value
	 */
	public boolean isRoleOneToOnePrimaryKeys(RelationshipRole role, HibernateTable target) {
		boolean retVal = true;
		ColumnMap[] cMap = role.getColumnMaps();
		if (cMap.length <= 0) {
			return false;
		}
		int roleColCount = cMap.length;
		for (int i = 0; i < cMap.length; i++) {
			HibernateColumn col_lefts = (HibernateColumn) this.getColumn(cMap[i]
					.getPrimaryKey());
			HibernateColumn col_rights = (HibernateColumn) target.getColumn(cMap[i]
					.getForeignKey());
			if (!(col_rights.isPk() && col_lefts.isPk())) {
				return false;
			}
		}

		// Check that lengths are correct.
		int lPKCount = this.getColumns(PrimaryKey.getInstance()).size();
		if (lPKCount != roleColCount) {
			return false;
		}
		int rPKCount = target.getColumns(PrimaryKey.getInstance()).size();
		if (rPKCount != roleColCount) {
			return false;
		}

		return retVal;
	}

	/**
	 * Gets the FKRoleHasAnyEqualsHashcode attribute of the Relationship object
	 * 
	 * @param role
	 *            Describe what the parameter does
	 * @return The RoleHasAnyEqualsHashcode value
	 */
	public boolean isFKRoleHasAnyEqualsHashcode(RelationshipRole role) {
		boolean retVal = false;
		ColumnMap[] cMap = role.getTargetRole().getColumnMaps();
		if (cMap.length <= 0) {
			return false;
		}
		int roleColCount = cMap.length;
		for (int i = 0; i < cMap.length; i++) {
			HibernateColumn col = (HibernateColumn) this.getColumn(cMap[i]
					.getForeignKey());
			if (col.isIncludeEquals()) {
				return true;
			}
		}
		return retVal;
	}

	/**
	 * Gets the isCompoundKeyHasAnyEqualsHashcode attribute
	 * 
	 * @return The CompoundKeyHasAnyEqualsHashcode value
	 */
	public boolean isCompoundKeyHasAnyEqualsHashcode() {
		boolean retVal = false;
		if (this.isCompositeKey()) {
			Iterator itr = this.getPrimaryKeyColumns().iterator();
			while (itr.hasNext()) {
				HibernateColumn col = (HibernateColumn) itr.next();
				if (col.isIncludeEquals()) {
					return true;
				}
			}
		}
		return retVal;
	}

	/**
	 * Just a helper function to reduce the size of the velocity script and
	 * factor out duplicate code.
	 * 
	 * @param role
	 * @return
	 */
	public String getCardinalityValue(RelationshipRole role) {
		if (role.getRelation().isMany2Many()) {
			return "many-to-many";
		} else if (role.getRelation().isOne2One()) {
			return "one-to-one";
		} else if (role.isTargetMany()) {
			return "one-to-many";
		} else {
			return "many-to-one";
		}
	}

	/**
	 * just a helper function to reduce the size of the velocity script and
	 * factor out duplicated code.
	 * 
	 * @param role
	 * @return
	 */
	public String getDirectionality(RelationshipRole role) {
		if (role.getRelation().isBidirectional()) {
			return "bi-directional";
		} else {
			return "uni-directional";
		}
	}

	/**
	 * Initialise the table with the values coming from the user preferences
	 */
	protected void init() {
		super.init();
		HibernatePlugin plugin = (HibernatePlugin) getPlugin();
		_lifecycle = plugin.getLifecycleNode().createChild(plugin.isLifecycle());
		if (getPrefsValue("lifecycle") != null) {
			setImplementLifecycle(Util.bool(getPrefsValue("lifecycle")));
		}
		_validatable = plugin.getValidatableNode().createChild(plugin.isValidatable());
		if (getPrefsValue("validatable") != null) {
			setImplementValidatable(Util.bool(getPrefsValue("validatable")));
		}
		_equalsHashcode = plugin.getEqualsHashcodeNode().createChild(
				plugin.isEqualsHashcode());
		if (getPrefsValue("equalshashcode") != null) {
			setEqualsHashcode(Util.bool(getPrefsValue("equalshashcode")));
		}
		_plainCKey = plugin.getPlainCompoundKeyNode().createChild(
				plugin.isPlainCompoundKeys());
		if (getPrefsValue("plainCKey") != null) {
			setPlainCompoundKey(Util.bool(getPrefsValue("plainCKey")));
		}
		_proxy = plugin.getProxyNode().createChild(plugin.isProxy());
		if (getPrefsValue("proxy") != null) {
			setProxy(Util.bool(getPrefsValue("proxy")));
		}
		_mutable = plugin.getMutableNode().createChild(plugin.isMutable());
		if (getPrefsValue("mutable") != null) {
			setMutable(Util.bool(getPrefsValue("mutable")));
		}
		_dynamicUpdate = plugin.getDynamicUpdateNode().createChild(
				plugin.isDynamicUpdate());
		if (getPrefsValue("dynamicUpdate") != null) {
			setDynamicUpdate(Util.bool(getPrefsValue("dynamicUpdate")));
		}
		_dynamicInsert = plugin.getDynamicInsertNode().createChild(
				plugin.isDynamicInsert());
		if (getPrefsValue("dynamicInsert") != null) {
			setDynamicInsert(Util.bool(getPrefsValue("dynamicInsert")));
		}
		_selectBeforeUpdate = plugin.getSelectBeforeUpdateNode().createChild(
				plugin.isSelectBeforeUpdate());
		if (getPrefsValue("selectBeforeUpdate") != null) {
			setSelectBeforeUpdate(Util.bool(getPrefsValue("selectBeforeUpdate")));
		}
		_databaseSchema = plugin.getDatabaseSchema();
		if (getPrefsValue("databaseSchema") != null) {
			setDatabaseSchema(getPrefsValue("databaseSchema"));
		}
		_classDescription = plugin.getClassDescription();
		if (getPrefsValue("description") != null) {
			setClassDescription(getPrefsValue("description"));
		} 
		_classScope = plugin.getClassScope();
		if (getPrefsValue("classscope") != null) {
			setClassScope(getPrefsValue("classscope"));
		}
		_extends = plugin.getExtends();
		if (getPrefsValue("extends") != null) {
			setExtends(getPrefsValue("extends"));
		}
//		_implements = plugin.getImplements();
//		if (getPrefsValue("implements") != null) {
//			setImplements(decodeImplementsStr(getPrefsValue("implements"), _implements));
//		}
		_persister = plugin.getPersister();
		if (getPrefsValue("persister") != null) {
			setPersister(getPrefsValue("persister"));
		}
		_batchsize = plugin.getBatchSize();
		if (getPrefsValue("batchsize") != null) {
			setBatchSize(getPrefsValue("batchsize"));
		}
		_where = "";
		if (getPrefsValue("where") != null) {
			setWhere(getPrefsValue("where"));
		}
		if (getPrefsValue("destinationClassName") != null) {
			_destinationClassName = getPrefsValue("destinationClassName");
		} else {
			_destinationClassName = getBaseClassName() + plugin.getBeansuffix();
			try {
				String tablename = getTableElement().getPhysicalName();
				int pos = tablename.indexOf('_');
				if (pos > 0) {
					String tableprefix = tablename.substring(0, pos);
					String classpackage = getPackage();
					pos = classpackage.lastIndexOf(".");
					if (pos > 0) {
						String packagesuffix = classpackage.substring(pos + 1);
						if (tableprefix.equalsIgnoreCase(packagesuffix)) {
							_destinationClassName = _destinationClassName
									.substring(tableprefix.length());
							String caps = _destinationClassName.substring(0, 1)
									.toUpperCase();
							String rest = _destinationClassName.substring(1);
							_destinationClassName = caps + rest;
						}
					}
				}
				System.out.println(_destinationClassName+"====================");
				ConfigureFile config = ConfigureFile.getInstance();
				if(config.getProperty("isvalid")!=null && config.getProperty("isvalid").equalsIgnoreCase("TRUE"))
				{
					String invalidTableName = config.getProperty("invalidtablename");
					if(invalidTableName != null)
					{
						invalidTableName = invalidTableName.trim();
					}
					while(_destinationClassName.startsWith(invalidTableName))
					{
						_destinationClassName = _destinationClassName.substring(invalidTableName.length());
						System.out.println(_destinationClassName+"@@@@@@@@@@@@@@@@@@@@@@@@@@");
					}
				}
				
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
		}
		_keyGenerator = plugin.getStandardGeneratorScheme();
		if (getPrefsValue("keygenerator") != null) {
			_keyGenerator = getPrefsValue("keygenerator");
		}
		_keyGeneratorArg = plugin.getStandardGeneratorArg();
		if (getPrefsValue("keygeneratorarg") != null) {
			_keyGeneratorArg = getPrefsValue("keygeneratorarg");
		}

	}
   /**
    * Gets the variable name. This method is intentionally private. The method
    * that takes a RelationshipRole as argument should be used from templates.
    *
    * @return The VariableName value
    */
   public String getVariableName() {
      return Util.decapitalise(getDestinationClassName());
   }

	public String getReplaceName() {
		return getDestinationClassName();
	}

	/**
	 * This encodes the ArrayList for the multiple implements structure into a
	 * String. The Encoding standard used involves the values being separated by
	 * a comma.
	 * 
	 * @param orig
	 *            The orginal ArrayList.
	 * @return An encoded String.
	 */
	private String encodeImplementsStr(ArrayList orig) {
		StringBuffer encodeBuffer = new StringBuffer();
		for (int i = 0; i < orig.size(); i++) {
			encodeBuffer.append((String) orig.get(i));
			if (i + 1 < orig.size()) {
				encodeBuffer.append(",");
			}
		}
		return encodeBuffer.toString();
	}

	/**
	 * This decode the value that has been encoded for the multiple implements
	 * structure. Encoding standard used involves the values being separated by
	 * a comma.
	 * 
	 * @param encodedStr
	 *            The encoded string
	 * @param orig
	 *            The orginal ArrayList returned if unable to decode the string
	 * @return An array list of Strings or the original ArrayList
	 */
	private ArrayList decodeImplementsStr(String encodedStr, ArrayList orig) {
		ArrayList retVal = orig;
		if (!(encodedStr == null || encodedStr.length() == 0)) {
			retVal = new ArrayList();
			StringTokenizer st = new StringTokenizer(encodedStr, ",");
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				if (token.length() > 0) {
					retVal.add(token);
				}
			}
		}
		return retVal;
	}

	/**
	 * get formatter date
	 * 
	 * @return
	 */
	public String getNow() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}
}
