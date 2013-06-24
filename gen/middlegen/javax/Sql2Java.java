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
package middlegen.javax;

import java.sql.Types;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Comparator;

/**
 * This class has static methods for mapping SQL types to Java types. Both
 * supported and preferred types are supported.
 *
 * @author <a href="mailto:aslak.hellesoy@bekk.no">Aslak Helles�y</a>
 * @created 3. oktober 2001
 * @todo mark non-serializable types with a warning. they will call an exception
 *      in cmr (and maybe other cases too) (at least on wls 6.1). This is
 *      InputStream, Reader, Clob, Blob, Array, Ref (at least I think it's a
 *      problem. Have to test it)
 * @version $Id: Sql2Java.java,v 1.2 2009/03/17 07:29:43 shisf Exp $
 */
public class Sql2Java {

   /**
    * Get static reference to Log4J Logger
    */
   private static org.apache.log4j.Category _log = org.apache.log4j.Category.getInstance(Sql2Java.class.getName());

   // see resultset.gif (25 SQL types)
   // see java.sql.Types (28 SQL types in JDK1.3, 30 SQL types in JDK1.4)
   // JDK 1.3 java.sql.Types missing from table: DISTINCT, NULL, OTHER
   // JDK 1.4 java.sql.Types missing from table: DISTINCT, NULL, OTHER, DATALINK, BOOLEAN

   /**
    * @todo-javadoc Describe the column
    */
   private final static IntStringMap _javaTypesForSqlType = new IntStringMap();
   /**
    * @todo-javadoc Describe the column
    */
   private final static IntStringMap _preferredJavaTypeForSqlType = new IntStringMap();

   /**
    * @todo-javadoc Describe the column
    */
   private final static Comparator _typeComparator =
      new Comparator() {
         public int compare(Object o1, Object o2) {
            String s1 = (String)o1;
            String s2 = (String)o2;
            boolean isS1Class = s1.indexOf('.') != -1;
            boolean isS2Class = s2.indexOf('.') != -1;
            if ((isS1Class && isS2Class) || (!isS1Class && !isS2Class)) {
               // Both are class or both are primitive. Compare normally
               return s1.compareTo(s2);
            }
            else {
               // One is primitive and one is class. Primitive always first
               return isS1Class ? 1 : -1;
            }
         }


         public boolean equals(Object o1, Object o2) {
            return o1.equals(o2);
         }
      };

   /**
    * @todo-javadoc Describe the column
    */
   private final static HashMap _primitiveToClassMap = new HashMap();

   /**
    * @todo-javadoc Describe the column
    */
   private final static String[] _allJavaTypes = new String[]{
         "boolean",
         "byte",
         "byte[]",
         "double",
         "float",
         "int",
         "long",
         "short",
         "java.io.InputStream",
         "java.io.Reader",
         "Boolean",
         "Byte",
         "Double",
         "Float",
         "Integer",
         "Long",
         "Short",
         "String",
         "Object",
         "java.math.BigDecimal",
         "java.math.BigInteger",
         "java.sql.Array",
         "java.sql.Blob",
         "java.sql.Clob",
         "java.sql.Date",
         "java.sql.Ref",
         "java.sql.Time",
         "java.sql.Timestamp",
         "java.util.Date"
         };

   /**
    * @todo-javadoc Describe the field
    */
   private final static Set _numericClasses = new HashSet();


   /**
    * Gets the TypeComparator attribute of the Sql2Java object
    *
    * @return The TypeComparator value
    */
   public static Comparator getTypeComparator() {
      return _typeComparator;
   }


   /**
    * Gets the preferred Java Type for an SQL type. It has special logic for
    * handling DECIMAL and NUMERICs with zero decimal places to return the most
    * apppropriate type.
    *
    * @param sqlType Describe what the parameter does
    * @param size Describe what the parameter does
    * @param decimalDigits Describe what the parameter does
    * @return The PreferredJavaType value
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for method parameter
    */
   public static String getPreferredJavaType(int sqlType, int size, int decimalDigits) {
      if ((sqlType == Types.DECIMAL || sqlType == Types.NUMERIC) && decimalDigits == 0) {
         if (size == 1) {
            // https://sourceforge.net/tracker/?func=detail&atid=415993&aid=662953&group_id=36044
            return "Boolean";
         }
         else if (size < 3) {
            return "Byte";
         }
         else if (size < 5) {
            return "Short";
         }
         else if (size < 10) {
            return "Integer";
         }
         else if (size < 19) {
            return "Long";
         }
         else {
            return "java.math.BigDecimal";
         }
      }
      String result = _preferredJavaTypeForSqlType.getString(sqlType);
      if (result == null) {
         result = "Object";
      }
      return result;
   }


   /**
    * Gets the preferred Java Type for non-key columns. It has special coding
    * for handling DECIMAL and NUMERICs with zero decimal places.
    *
    * @param sqlType The SQL type code. Must be one of java.sql.Types
    * @param size Describe what the parameter does
    * @param decimalDigits Describe what the parameter does
    * @return The Java Type for this non-key column
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for method parameter
    * @author Jonathan O'Connor
    * @created 03 April 2002
    */
   public static String getPreferredJavaTypeNoPrimitives(int sqlType, int size, int decimalDigits) {
      String pjt = getPreferredJavaType(sqlType, size, decimalDigits);
      String pjtc = getClassForPrimitive(pjt);
      if (pjtc != null) {
         return pjtc;
      }
      else {
         return pjt;
      }
   }


   /**
    * Gets the JavaTypes attribute of the Sql2Java class
    *
    * @param sqlType Describe what the parameter does
    * @return The JavaTypes value
    * @todo-javadoc Write javadocs for method parameter
    */
   public static String[] getJavaTypes(int sqlType) {
      String[] result = _javaTypesForSqlType.getStrings(sqlType);
      if (result == null) {
         // we're dealing with non SQL'92 types, like for example MSSQL NVARCHAR
         // return all types so the user can select.
         result = _allJavaTypes;
      }
      return result;
   }


   /**
    * Gets the ClassForPrimitive attribute of the Sql2Java class
    *
    * @param primitive Describe what the parameter does
    * @return The ClassForPrimitive value
    * @todo-javadoc Write javadocs for method parameter
    */
   public static String getClassForPrimitive(String primitive) {
      return (String)_primitiveToClassMap.get(primitive);
   }


   /**
    * Gets the Primitive attribute of the Sql2Java class
    *
    * @param type Describe what the parameter does
    * @return The Primitive value
    * @todo-javadoc Write javadocs for method parameter
    */
   public static boolean isPrimitive(String type) {
      return getClassForPrimitive(type) != null;
   }


   /**
    * Gets the NumericClass attribute of the Sql2Java class
    *
    * @param type Describe what the parameter does
    * @return The NumericClass value
    * @todo-javadoc Write javadocs for method parameter
    */
   public static boolean isNumericClass(String type) {
      return _numericClasses.contains(type);
   }


   /**
    * Describe what the method does
    *
    * @param sqlType Describe what the parameter does
    * @param javaType Describe what the parameter does
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for method parameter
    */
   public static void overridePreferredJavaTypeForSqlType(int sqlType, String javaType) {
      _preferredJavaTypeForSqlType.put(sqlType, javaType);
   }


   /**
    * Describe what the method does
    *
    * @param sqlType Describe what the parameter does
    * @param javaTypes Describe what the parameter does
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for method parameter
    */
   public static void overrideAllowedJavaTypesForSqlType(int sqlType, String[] javaTypes) {
      _javaTypesForSqlType.put(sqlType, javaTypes);
   }


   /**
    * Describe what this class does
    *
    * @author Aslak Helles�y
    * @created 24. mars 2002
    * @todo-javadoc Write javadocs
    */
   private static class IntStringMap extends HashMap {

      /**
       * Gets the String attribute of the IntStringMap object
       *
       * @param i Describe what the parameter does
       * @return The String value
       * @todo-javadoc Write javadocs for method parameter
       */
      public String getString(int i) {
         return (String)get(new Integer(i));
      }


      /**
       * Gets the Strings attribute of the IntStringMap object
       *
       * @param i Describe what the parameter does
       * @return The Strings value
       * @todo-javadoc Write javadocs for method parameter
       */
      public String[] getStrings(int i) {
         return (String[])get(new Integer(i));
      }


      /**
       * Describe what the method does
       *
       * @param i Describe what the parameter does
       * @param s Describe what the parameter does
       * @todo-javadoc Write javadocs for method
       * @todo-javadoc Write javadocs for method parameter
       * @todo-javadoc Write javadocs for method parameter
       */
      public void put(int i, String s) {
         put(new Integer(i), s);
      }


      /**
       * Describe what the method does
       *
       * @param i Describe what the parameter does
       * @param sa Describe what the parameter does
       * @todo-javadoc Write javadocs for method
       * @todo-javadoc Write javadocs for method parameter
       * @todo-javadoc Write javadocs for method parameter
       */
      public void put(int i, String[] sa) {
         put(new Integer(i), sa);
      }
   }
   static {
      _primitiveToClassMap.put("byte", "Byte");
      _primitiveToClassMap.put("short", "Short");
      _primitiveToClassMap.put("int", "Integer");
      _primitiveToClassMap.put("long", "Long");
      _primitiveToClassMap.put("float", "Float");
      _primitiveToClassMap.put("boolean", "Boolean");
      _primitiveToClassMap.put("double", "Double");
   }

   static {
      _numericClasses.add("Byte");
      _numericClasses.add("Short");
      _numericClasses.add("Integer");
      _numericClasses.add("Long");
      _numericClasses.add("Float");
      _numericClasses.add("Boolean");
      _numericClasses.add("Double");
   }

   static {
      _javaTypesForSqlType.put(Types.TINYINT, new String[]{
            "boolean",
            "byte",
            "double",
            "float",
            "int",
            "long",
            "short",
            "Boolean",
            "Byte",
            "Double",
            "Float",
            "Integer",
            "Long",
            "Short",
            "String",
            "Object",
            "java.math.BigDecimal",
            "java.math.BigInteger"
            });
      _javaTypesForSqlType.put(Types.SMALLINT, new String[]{
            "boolean",
            "byte",
            "double",
            "float",
            "int",
            "long",
            "short",
            "Boolean",
            "Byte",
            "Double",
            "Float",
            "Integer",
            "Long",
            "Short",
            "String",
            "Object",
            "java.math.BigDecimal",
            "java.math.BigInteger"
            });
      _javaTypesForSqlType.put(Types.INTEGER, new String[]{
            "boolean",
            "byte",
            "double",
            "float",
            "int",
            "long",
            "short",
            "Boolean",
            "Byte",
            "Double",
            "Float",
            "Integer",
            "Long",
            "Short",
            "String",
            "Object",
            "java.math.BigDecimal",
            "java.math.BigInteger"
            });
      _javaTypesForSqlType.put(Types.BIGINT, new String[]{
            "boolean",
            "byte",
            "double",
            "float",
            "int",
            "long",
            "short",
            "Boolean",
            "Byte",
            "Double",
            "Float",
            "Integer",
            "Long",
            "Short",
            "String",
            "Object",
            "java.math.BigDecimal",
            "java.math.BigInteger"
            });
      _javaTypesForSqlType.put(Types.REAL, new String[]{
            "boolean",
            "byte",
            "double",
            "float",
            "int",
            "long",
            "short",
            "Boolean",
            "Byte",
            "Double",
            "Float",
            "Integer",
            "Long",
            "Short",
            "String",
            "Object",
            "java.math.BigDecimal",
            "java.math.BigInteger"
            });
      _javaTypesForSqlType.put(Types.FLOAT, new String[]{
            "boolean",
            "byte",
            "double",
            "float",
            "int",
            "long",
            "short",
            "Boolean",
            "Byte",
            "Double",
            "Float",
            "Integer",
            "Long",
            "Short",
            "String",
            "Object",
            "java.math.BigDecimal",
            "java.math.BigInteger"
            });
      _javaTypesForSqlType.put(Types.DOUBLE, new String[]{
            "boolean",
            "byte",
            "double",
            "float",
            "int",
            "long",
            "short",
            "Boolean",
            "Byte",
            "Double",
            "Float",
            "Integer",
            "Long",
            "Short",
            "String",
            "Object",
            "java.math.BigDecimal",
            "java.math.BigInteger"
            });
      _javaTypesForSqlType.put(Types.DECIMAL, new String[]{
            "boolean",
            "byte",
            "double",
            "float",
            "int",
            "long",
            "short",
            "Boolean",
            "Byte",
            "Double",
            "Float",
            "Integer",
            "Long",
            "Short",
            "String",
            "Object",
            "java.math.BigDecimal",
            "java.math.BigInteger"
            });
      _javaTypesForSqlType.put(Types.NUMERIC, new String[]{
            "boolean",
            "byte",
            "double",
            "float",
            "int",
            "long",
            "short",
            "Boolean",
            "Byte",
            "Double",
            "Float",
            "Integer",
            "Long",
            "Short",
            "String",
            "Object",
            "java.math.BigDecimal",
            "java.math.BigInteger"
            });
      _javaTypesForSqlType.put(Types.BIT, new String[]{
            "boolean",
            "byte",
            "double",
            "float",
            "int",
            "long",
            "short",
            "Boolean",
            "Byte",
            "Double",
            "Float",
            "Integer",
            "Long",
            "Short",
            "String",
            "Object",
            "java.math.BigDecimal",
            "java.math.BigInteger"
            });
      _javaTypesForSqlType.put(Types.CHAR, new String[]{
            "boolean",
            "byte",
            "double",
            "float",
            "int",
            "long",
            "short",
            "java.io.InputStream",
            "java.io.Reader",
            "Boolean",
            "Byte",
            "Double",
            "Float",
            "Integer",
            "Long",
            "Object",
            "Short",
            "String",
            "java.math.BigDecimal",
            "java.math.BigInteger",
            "java.sql.Date",
            "java.sql.Time",
            "java.sql.Timestamp",
            "java.util.Date"
            });
      _javaTypesForSqlType.put(Types.VARCHAR, new String[]{
            "boolean",
            "byte",
            "double",
            "float",
            "int",
            "long",
            "short",
            "java.io.InputStream",
            "java.io.Reader",
            "Boolean",
            "Byte",
            "Double",
            "Float",
            "Integer",
            "Long",
            "Object",
            "Short",
            "String",
            "java.math.BigDecimal",
            "java.math.BigInteger",
            "java.sql.Date",
            "java.sql.Time",
            "java.sql.Timestamp",
            "java.util.Date"
            });
      _javaTypesForSqlType.put(Types.LONGVARCHAR, new String[]{
            "boolean",
            "byte",
            "double",
            "float",
            "int",
            "long",
            "short",
            "java.io.InputStream",
            "java.io.Reader",
            "Boolean",
            "Byte",
            "Double",
            "Float",
            "Integer",
            "Long",
            "Object",
            "Short",
            "String",
            "java.math.BigDecimal",
            "java.math.BigInteger",
            "java.sql.Date",
            "java.sql.Time",
            "java.sql.Timestamp",
            "java.util.Date"
            });
      _javaTypesForSqlType.put(Types.BINARY, new String[]{
            "byte[]",
            "String",
            "Object",
            "java.io.InputStream",
            "java.io.Reader"
            });
      _javaTypesForSqlType.put(Types.VARBINARY, new String[]{
            "byte[]",
            "String",
            "Object",
            "java.io.InputStream",
            "java.io.Reader"
            });
      _javaTypesForSqlType.put(Types.LONGVARBINARY, new String[]{
            "byte[]",
            "String",
            "Object",
            "java.io.InputStream",
            "java.io.Reader",
            });
      _javaTypesForSqlType.put(Types.DATE, new String[]{
            "String",
            "Object",
            "java.sql.Date",
            "java.sql.Timestamp",
            "java.util.Date",
            });
      _javaTypesForSqlType.put(Types.TIME, new String[]{
            "String",
            "Object",
            "java.sql.Time",
            "java.sql.Timestamp",
            "java.util.Date",
            });
      _javaTypesForSqlType.put(Types.TIMESTAMP, new String[]{
            "String",
            "Object",
            "java.sql.Date",
            "java.sql.Time",
            "java.sql.Timestamp",
            "java.util.Date",
            });
      _javaTypesForSqlType.put(Types.CLOB, new String[]{
            "Object",
            "java.sql.Clob"
            });
      _javaTypesForSqlType.put(Types.BLOB, new String[]{
            "Object",
            "java.sql.Blob"
            });
      _javaTypesForSqlType.put(Types.ARRAY, new String[]{
            "Object",
            "java.sql.Array"
            });
      _javaTypesForSqlType.put(Types.REF, new String[]{
            "Object",
            "java.sql.Ref"
            });
      _javaTypesForSqlType.put(Types.STRUCT, new String[]{
            "Object"
            });
      _javaTypesForSqlType.put(Types.JAVA_OBJECT, new String[]{
            "Object"
            });
   }

   static {
//      _preferredJavaTypeForSqlType.put(Types.TINYINT, "Byte");
//      _preferredJavaTypeForSqlType.put(Types.SMALLINT, "Short");
	   _preferredJavaTypeForSqlType.put(Types.TINYINT, "Integer");
	   _preferredJavaTypeForSqlType.put(Types.SMALLINT, "Integer");
      _preferredJavaTypeForSqlType.put(Types.INTEGER, "Integer");
      _preferredJavaTypeForSqlType.put(Types.BIGINT, "Long");
      _preferredJavaTypeForSqlType.put(Types.REAL, "Float");
      _preferredJavaTypeForSqlType.put(Types.FLOAT, "Double");
      _preferredJavaTypeForSqlType.put(Types.DOUBLE, "Double");
      _preferredJavaTypeForSqlType.put(Types.DECIMAL, "java.math.BigDecimal");
      _preferredJavaTypeForSqlType.put(Types.NUMERIC, "java.math.BigDecimal");
      _preferredJavaTypeForSqlType.put(Types.BIT, "Boolean");
      _preferredJavaTypeForSqlType.put(Types.CHAR, "String");
      _preferredJavaTypeForSqlType.put(Types.VARCHAR, "String");
      // according to resultset.gif, we should use java.io.Reader, but String is more convenient for EJB
      _preferredJavaTypeForSqlType.put(Types.LONGVARCHAR, "String");
      _preferredJavaTypeForSqlType.put(Types.BINARY, "byte[]");
      _preferredJavaTypeForSqlType.put(Types.VARBINARY, "byte[]");
      _preferredJavaTypeForSqlType.put(Types.LONGVARBINARY, "java.io.InputStream");
      _preferredJavaTypeForSqlType.put(Types.DATE, "java.sql.Date");
      _preferredJavaTypeForSqlType.put(Types.TIME, "java.sql.Time");
     // _preferredJavaTypeForSqlType.put(Types.TIMESTAMP, "java.sql.Timestamp");
      _preferredJavaTypeForSqlType.put(Types.TIMESTAMP, "java.util.Date");
      _preferredJavaTypeForSqlType.put(Types.CLOB, "java.sql.Clob");
      _preferredJavaTypeForSqlType.put(Types.BLOB, "java.sql.Blob");
      _preferredJavaTypeForSqlType.put(Types.ARRAY, "java.sql.Array");
      _preferredJavaTypeForSqlType.put(Types.REF, "java.sql.Ref");
      _preferredJavaTypeForSqlType.put(Types.STRUCT, "Object");
      _preferredJavaTypeForSqlType.put(Types.JAVA_OBJECT, "Object");
   }

}

