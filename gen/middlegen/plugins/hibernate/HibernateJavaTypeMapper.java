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

import java.sql.Types;

import middlegen.javax.JavaColumn;
import middlegen.javax.Sql2Java;
import middlegen.plugins.hibernate.interfaces.JavaTypeMapper;

/**
 * This class provides (one possible) implementation of a Java Type mapper for
 * the Middlegen plugin. This class is configured by the ant script by providing
 * the HibernateJavaTypeMapper as a preference to using Middlegen's exclusively.
 * Once a mapping has been determined or updated by the user through the GUI
 * then this mapper will not override the result.
 *
 * @author Daniel Rosenbaum and David Channon
 * @created 24 December 2003
 * @version 2.1
 */
public class HibernateJavaTypeMapper implements JavaTypeMapper {

   /**
    * Describe what the HibernateJavaTypeMapper constructor does
    *
    * @todo-javadoc Write javadocs for constructor
    */
   public HibernateJavaTypeMapper() {
   }


   /**
    * Gets the PreferredJavaType attribute of the HibernateJavaTypeMapper object
    *
    * @param column Describe what the parameter does
    * @return The PreferredJavaType value
    * @todo-javadoc Write javadocs for method parameter
    */
   public String getPreferredJavaType(JavaColumn column) {
      int sqlType = column.getSqlType();
      int size = column.getSize();
      int decimalDigits = column.getDecimalDigits();
      boolean nullable = column.isNullable();

      String result = null;
      if ((sqlType == Types.DECIMAL || sqlType == Types.NUMERIC) && decimalDigits == 0) {
         if (size < 10) {
            result = "Integer";
         }
         else if (size < 19) {
            result = "Long";
         }
         else {
            result = "java.math.BigDecimal";
         }
      }
      else if (sqlType == Types.BINARY || sqlType == Types.VARBINARY || sqlType == Types.LONGVARBINARY) {
         // if column type is any of the BINARYs, Sql2Java prefers java.io.InputStream or byte[]
         // which is not a valid Hibernate type, use Hibernate "binary", which converts to byte[]
         result = "binary";
      }
      else {
         // if not any of above get the default
         result = Sql2Java.getPreferredJavaType(sqlType, size, decimalDigits);
         if (result == null) {
            result = "java.lang.Object";
         }
      }

      // if the column is nullable make sure it is not a primitive.
      // Its best practice not to use primitives in primary keys.
      /*
      if (nullable || column.isPk()) {
         if ("int".equals(result) ||
               "short".equals(result) ||
               "long".equals(result) ||
               "byte".equals(result) ||
               "float".equals(result) ||
               "boolean".equals(result) ||
               "double".equals(result)) {
            result = Sql2Java.getClassForPrimitive(result);
         }
      }
      */
      return result;
   }
}
