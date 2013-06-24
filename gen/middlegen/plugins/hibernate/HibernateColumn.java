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

import middlegen.Column;
import middlegen.Util;
import middlegen.javax.JavaColumn;
import middlegen.javax.Sql2Java;
import middlegen.plugins.hibernate.interfaces.JavaTypeMapper;
import middlegen.util.BooleanNode;

/**
 * The column decorator used by the Hibernate plugin.
 *
 * @author David Channon and Gavin King
 * @created 1 January 2003
 * @version 2.1
 */
public class HibernateColumn extends JavaColumn {

   /**
    * @todo-javadoc Describe the field
    */
   private BooleanNode _updateable = BooleanNode.createRoot(true);
   /**
    * @todo-javadoc Describe the field
    */
   private BooleanNode _insertable = BooleanNode.createRoot(true);
   /**
    * @todo-javadoc Describe the field
    */
   private BooleanNode _incToString = BooleanNode.createRoot(false);
   /**
    * @todo-javadoc Describe the field
    */
   private BooleanNode _genProperty = BooleanNode.createRoot(true);
   /**
    * @todo-javadoc Describe the field
    */
   private BooleanNode _incEquals = BooleanNode.createRoot(false);
   /**
    * @todo-javadoc Describe the field
    */
   private String _getScope;
   /**
    * @todo-javadoc Describe the field
    */
   private String _setScope;
   /**
    * @todo-javadoc Describe the field
    */
   private String _fieldScope;
   /**
    * @todo-javadoc Describe the field
    */
   private String _accessField;
   /** Optional Name of the field description */
   private String _fieldDescription;
   /** Optional Name of the type of Bean constraint */
   private String _beanPropertyType;
   /** Name of the column specialty */
   private String _columnSpecialty;


   /**
    * Constructor for HibernateColumn.
    *
    * @param subject The original column object to wrap
    */
   public HibernateColumn(Column subject) {
      super(subject);
   }


   /**
    * If false, the field will not have SQL updates generated or it.
    *
    * @param flag The new updateable value
    */
   public void setUpdateable(boolean flag) {
      setPrefsValue("updateable", Util.string(flag));
      _updateable.setValue(flag);
   }


   /**
    * If false, the field will not have SQL inserts generated or it.
    *
    * @param flag The new Insertable value
    */
   public void setInsertable(boolean flag) {
      setPrefsValue("insertable", Util.string(flag));
      _insertable.setValue(flag);
   }


   /**
    * If true, the generated class will add the field to the toString method.
    *
    * @param flag The new IncToString value
    */
   public void setIncludeToString(boolean flag) {
      setPrefsValue("inctostring", Util.string(flag));
      _incToString.setValue(flag);
   }


   /**
    * If true, the generated class will add the field to the Equals and Hashcode
    * method.
    *
    * @param flag The new IncEquals value
    */
   public void setIncludeEquals(boolean flag) {
      setPrefsValue("incequals", Util.string(flag));
      _incEquals.setValue(flag);
   }


   /**
    * If false, the generated class will not include this field and access
    * methods.
    *
    * @param flag The new GenProperty value
    */
   public void setGenProperty(boolean flag) {
      setPrefsValue("genproperty", Util.string(flag));
      _genProperty.setValue(flag);
   }


   /**
    * Sets the field get scope
    *
    * @param value The field get scope value
    */
   public void setFieldGetScope(String value) {
      setPrefsValue("getscope", value);
      _getScope = value;
   }


   /**
    * Sets the field set scope
    *
    * @param value The field set scope value
    */
   public void setFieldSetScope(String value) {
      setPrefsValue("setscope", value);
      _setScope = value;
   }


   /**
    * Sets the field scope
    *
    * @param value The field scope value
    */
   public void setFieldScope(String value) {
      setPrefsValue("fieldscope", value);
      _fieldScope = value;
   }


   /**
    * Sets the access field
    *
    * @param value The access field value
    */
   public void setAccessField(String value) {
      setPrefsValue("accessfield", value);
      _accessField = value;
   }


   /**
    * Sets the field description name
    *
    * @param value The field description name value
    */
   public void setFieldDescription(String value) {
      setPrefsValue("description", value);
      _fieldDescription = value;
   }


   /**
    * Sets the Column Specialty name
    *
    * @param value The Column Specialty name value
    */
   public void setColumnSpecialty(String value) {
      setPrefsValue("columnspecialty", value);
      _columnSpecialty = value;
   }


   /**
    * Sets the bean constraint name so code generator will create the constraint
    * processing subsystem.
    *
    * @param value The bean property type value
    */
   public void setBeanPropertyType(String value) {
      setPrefsValue("beanpropertytype", value);
      _beanPropertyType = value;
   }


   /**
    * If false no update SQL used for this field.
    *
    * @return The Updateable value
    */
   public boolean isUpdateable() {
      return _updateable.isCompletelyTrue();
   }


   /**
    * If false no insert SQL used for this field.
    *
    * @return The Insertable value
    */
   public boolean isInsertable() {
      return _insertable.isCompletelyTrue();
   }


   /**
    * If true generated class has this field in the toString method.
    *
    * @return The Include To String value
    */
   public boolean isIncludeToString() {
      return _incToString.isCompletelyTrue();
   }


   /**
    * If true generated class has this field in the include equals method.
    *
    * @return The Include Equals Hashcode value
    */
   public boolean isIncludeEquals() {
      return _incEquals.isCompletelyTrue();
   }


   /**
    * If false no class attribute and methods are generated this field.
    *
    * @return The Generate property value
    */
   public boolean isGenProperty() {
      return _genProperty.isCompletelyTrue();
   }


   /**
    * Gets the UpdateableNode object
    *
    * @return The UpdateableNode value
    */
   public BooleanNode getUpdateableNode() {
      return _updateable;
   }


   /**
    * Gets the InsertableNode object
    *
    * @return The InsertableNode value
    */
   public BooleanNode getInsertableNode() {
      return _insertable;
   }


   /**
    * Gets the IncludeToStringNode object
    *
    * @return The IncludeToStringNode value
    */
   public BooleanNode getIncludeToStringNode() {
      return _incToString;
   }


   /**
    * Gets the IncludeEqualsNode object
    *
    * @return The IncludeEqualsNode value
    */
   public BooleanNode getIncludeEqualsNode() {
      return _incEquals;
   }


   /**
    * Gets the GenPropertyNode object
    *
    * @return The GenPropertyNode value
    */
   public BooleanNode getGenPropertyNode() {
      return _genProperty;
   }


   /**
    * Gets the field scope
    *
    * @return The field scope value
    */
   public String getFieldScope() {
      return _fieldScope.trim();
   }


   /**
    * Gets the access field
    *
    * @return The access field value
    */
   public String getAccessField() {
      return _accessField.trim();
   }


   /**
    * Gets the field get scope
    *
    * @return The field get scope value
    */
   public String getFieldGetScope() {
      return _getScope.trim();
   }


   /**
    * Gets the field set scope
    *
    * @return The field set scope value
    */
   public String getFieldSetScope() {
      return _setScope.trim();
   }


   /**
    * Gets the the field description
    *
    * @return The field description value
    */
   public String getFieldDescription() {
      return _fieldDescription.trim();
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
    * Gets the the Column Specialty for the column
    *
    * @return The Column Specialty value
    */
   public String getColumnSpecialty() {
      return _columnSpecialty.trim();
   }


   /**
    * Returns the method that takes a string as parameter and return a value of
    * the type of the column
    *
    * @return The parse method
    */
   public String getParseMethod() {
      if (isPrimitive()) {
         return getClassForPrimitive() + ".parse" + Util.capitalise(getJavaType());
      }
      else if ("java.lang.String".equals(getJavaType())) {
         return "";
      }
      else {
         return "new " + getJavaType();
      }
   }


   /**
    * Gets an indicator thats there is a Sized attribute of the HibernateColumn
    * object
    *
    * @return The indication value
    */
   public boolean isSized() {
      return super.getSize() != 0;
   }


   /**
    * Gets the SpecialtyProperty attribute of the HibernateColumn object
    *
    * @return The SpecialtyProperty value
    */
   public boolean isSpecialtyProperty() {
      return _columnSpecialty.equals(HibernatePlugin.ColPropertyId);
   }


   /**
    * Gets the SpecialtyVersionProperty attribute of the HibernateColumn object
    *
    * @return The SpecialtyVersionProperty value
    */
   public boolean isSpecialtyVersionProperty() {
      return _columnSpecialty.equals(HibernatePlugin.ColVersionId);
   }


   /**
    * Gets the SpecialtyTimestampProperty attribute of the HibernateColumn
    * object
    *
    * @return The SpecialtyTimestampProperty value
    */
   public boolean isSpecialtyTimestampProperty() {
      return _columnSpecialty.equals(HibernatePlugin.ColTimestampId);
   }


   /**
    * Returns indication that non default meta data settings is present for this
    * column
    *
    * @return The indication value
    */
   public boolean isMetaData() {
      return isIncludeToString() ||
            isIncludeEquals() ||
            isFieldScopeName() ||
            isFieldGetScopeName() ||
            isFieldSetScopeName() ||
            isFieldDescriptionName() ||
            isBeanPropertyTypeName() ||
            !isGenProperty();
   }


   /**
    * Gets the status indicating that a access field value is in existence.
    * Property is the default so returns false if property access or not
    * present.
    *
    * @return presence of a access field scope indicator other than the default.
    */
   public boolean isAccessFieldName() {
      return (_accessField == null || _accessField.trim().length() == 0 || _accessField.equals(HibernatePlugin.PropertyId)) ? false : true;
   }


   /**
    * Gets the status indicating that a field scope value is in existence.
    * Private is the default so returns false if private scope or scope not
    * present.
    *
    * @return presence of a valid field scope indicator other than the default.
    */
   public boolean isFieldScopeName() {
      return (_fieldScope == null || _fieldScope.trim().length() == 0 || _fieldScope.equals(HibernatePlugin.PrivateId)) ? false : true;
   }


   /**
    * Gets the status indicating that a field get scope value is in existence.
    * Public is the default so returns false if public scope or scope not
    * present.
    *
    * @return presence of a valid field get scope indicator other than the
    *      default.
    */
   public boolean isFieldGetScopeName() {
      return (_getScope == null || _getScope.trim().length() == 0 || _getScope.equals(HibernatePlugin.PublicId)) ? false : true;
   }


   /**
    * Gets the status indicating that a field set scope value is in existence.
    * Public is the default so returns false if public scope or scope not
    * present.
    *
    * @return presence of a valid field set scope indicator other than the
    *      default.
    */
   public boolean isFieldSetScopeName() {
      return (_setScope == null || _setScope.trim().length() == 0 || _setScope.equals(HibernatePlugin.PublicId)) ? false : true;
   }


   /**
    * Gets the status indicating that a field description value is in existence.
    *
    * @return presence of a field description name
    */
   public boolean isFieldDescriptionName() {
      return (_fieldDescription == null || _fieldDescription.trim().length() == 0) ? false : true;
   }


   /**
    * Gets the status indicating that bean property value is in existence.
    *
    * @return presence of a constrained property type name
    */
   public boolean isBeanPropertyTypeName() {
      return (_beanPropertyType == null || _beanPropertyType.equals("unbound")) ? false : true;
   }


   /**
    * Gets the JavaTypeMapper attribute of the HibernateColumn object
    *
    * @return The JavaTypeMapper value
    */
   public JavaTypeMapper getJavaTypeMapper() {
      return ((HibernatePlugin)getPlugin()).getJavaTypeMapper();
   }


   /**
    * Overridden method to check for presence of a JavaTypeMapper. If one is
    * defined it uses it to determine the java type, else it uses the default
    * Sql2Java type.
    */
   protected void setJavaType() {
      if (getPrefsValue("java-type") != null) {
         setJavaType(getPrefsValue("java-type"));
      }
      else {
         JavaTypeMapper javaTypeMapper = getJavaTypeMapper();
         if (javaTypeMapper == null) {
            setJavaType(Sql2Java.getPreferredJavaType(getSqlType(), getSize(), getDecimalDigits()));
         }
         else {
            setJavaType(javaTypeMapper.getPreferredJavaType(this));
         }
      }

   }


   /**
    * Initialise the column with the values coming from the user preferences or
    * use defaults otherwise.
    */
   protected void init() {
      super.init();
      HibernatePlugin plugin = (HibernatePlugin)getPlugin();
      _updateable.setValue(plugin.isColumnsUpdateable());
      if (getPrefsValue("updateable") != null) {
         setUpdateable(Util.bool(getPrefsValue("updateable")));
      }
      _insertable.setValue(plugin.isColumnsInsertable());
      if (getPrefsValue("insertable") != null) {
         setInsertable(Util.bool(getPrefsValue("insertable")));
      }
      _incEquals.setValue(plugin.isColumnsIncludeEquals());
      if (getPrefsValue("incequals") != null) {
         setIncludeEquals(Util.bool(getPrefsValue("incequals")));
      }
      _incToString.setValue(plugin.isColumnsIncludeToString());
      if (getPrefsValue("inctostring") != null) {
         setIncludeToString(Util.bool(getPrefsValue("inctostring")));
      }
      _genProperty.setValue(plugin.isColumnsGenProperty());
      if (getPrefsValue("genproperty") != null) {
         setGenProperty(Util.bool(getPrefsValue("genproperty")));
      }
      _accessField = plugin.getAccessField();
      if (getPrefsValue("accessfield") != null) {
         setAccessField(getPrefsValue("accessfield"));
      }
      _fieldScope = plugin.getFieldScope();
      if (getPrefsValue("fieldscope") != null) {
         setFieldScope(getPrefsValue("fieldscope"));
      }
      _getScope = plugin.getFieldGetScope();
      if (getPrefsValue("getscope") != null) {
         setFieldGetScope(getPrefsValue("getscope"));
      }
      _setScope = plugin.getFieldSetScope();
      if (getPrefsValue("setscope") != null) {
         setFieldSetScope(getPrefsValue("setscope"));
      }
      _fieldDescription = plugin.getFieldDescription();
      if (getPrefsValue("description") != null) {
         setFieldDescription(getPrefsValue("description"));
         
      }
      _beanPropertyType = plugin.getBeanPropertyType();
      if (getPrefsValue("beanpropertytype") != null) {
         setBeanPropertyType(getPrefsValue("beanpropertytype"));
      }
      _columnSpecialty = HibernatePlugin.ColPropertyId;
      if (getPrefsValue("columnspecialty") != null) {
         setColumnSpecialty(getPrefsValue("columnspecialty"));
      }

   }
}
