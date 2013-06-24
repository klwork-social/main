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

/**
 * Baseclass for Column decorators. Subclasses can add additional functionality
 * which extends the information that can be retrieved from the database.
 *
 * @author <a href="mailto:aslak.hellesoy@bekk.no">Aslak Helles�y</a>
 * @created 3. oktober 2001
 * @version $Id: ColumnDecorator.java,v 1.9 2003/12/20 11:45:59 dchannon Exp $
 */
public class ColumnDecorator extends PreferenceAware implements Column {

   /**
    * @todo-javadoc Describe the column
    */
   private final Column _subject;

   /**
    * @todo-javadoc Describe the column
    */
   private TableDecorator _tableDecorator;
   /**
    * Get static reference to Log4J Logger
    */
   private static org.apache.log4j.Category _log = org.apache.log4j.Category.getInstance(ColumnDecorator.class.getName());


   /**
    * Describe what the ColumnDecorator constructor does
    *
    * @param subject Describe what the parameter does
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for constructor
    * @todo-javadoc Write javadocs for method parameter
    */
   public ColumnDecorator(Column subject) {
      if (!(subject instanceof DbColumn)) {
         throw new IllegalArgumentException("subject must be of class " + DbColumn.class.getName() + ". Was:" + subject.getClass().getName());
      }
      _subject = subject;
   }


   /**
    * Gets the Table attribute of the ColumnDecorator object
    *
    * @return The Table value
    */
   public final Table getTable() {
      return _tableDecorator;
   }


   /**
    * Gets the SqlType attribute of the ColumnDecorator object
    *
    * @return The SqlType value
    */
   public final int getSqlType() {
      return _subject.getSqlType();
   }


   /**
    * Gets the SqlTypeName attribute of the ColumnDecorator object
    *
    * @return The SqlTypeName value
    */
   public final String getSqlTypeName() {
      return _subject.getSqlTypeName();
   }

   
   /**
    * 新加的方法
    * @return
    */
   public final String getSqlTypeNameShort() {
	   //WW_TODO 代码生成工具-进行代码生成，数据库进行转换
		String typename = getSqlTypeName();
		typename = typename.toUpperCase();
		if (typename == null) {
			return typename;
		} else if (typename.indexOf("NUMBER") != -1) {
			return "NUMERIC";
		} else if (typename.indexOf("TIMESTAMP") != -1) {
			return "TIMESTAMP";
		} else if (typename.indexOf("DATE") != -1) {
			return "DATETIME";			
		} else if (typename.indexOf("VARCHAR") != -1) {
			return "VARCHAR";
		} else if (typename.indexOf("INT") != -1) {
			return "INTEGER";
		} else if (typename.indexOf("DOUBLE") != -1) {
			return "NUMERIC";
		}else if (typename.indexOf("BOOL") != -1) {
			return "BOOLEAN";
		}else if (typename.indexOf("CHAR") != -1) {
			return "CHAR";
		}else {
			return typename;
		}
	}
   
   /**
    * Gets the SqlName attribute of the ColumnDecorator object
    *
    * @return The SqlName value
    */
   public final String getSqlName() {
      return _subject.getSqlName();
   }


   /**
    * Gets the Size attribute of the ColumnDecorator object
    *
    * @return The Size value
    */
   public final int getSize() {
      return _subject.getSize();
   }


   /**
    * Gets the DecimalDigits attribute of the ColumnDecorator object
    *
    * @return The DecimalDigits value
    */
   public final int getDecimalDigits() {
      return _subject.getDecimalDigits();
   }


   /**
    * Gets the Pk attribute of the ColumnDecorator object
    *
    * @return The Pk value
    */
   public final boolean isPk() {
      return _subject.isPk();
   }


   /**
    * Gets the Fk attribute of the ColumnDecorator object
    *
    * @return The Fk value
    */
   public final boolean isFk() {
      return _subject.isFk();
   }


   /**
    * Gets the Nullable attribute of the ColumnDecorator object
    *
    * @return The Nullable value
    */
   public final boolean isNullable() {
      return _subject.isNullable();
   }


   /**
    * Gets the Indexed attribute of the ColumnDecorator object
    *
    * @return The Indexed value
    */
   public final boolean isIndexed() {
      return _subject.isIndexed();
   }

   /**
	* Gets the Unique attribute of the ColumnDecorator object
	*
	* @return The Unique value
	*/
   public final boolean isUnique() {
	  return _subject.isUnique();
   }
   
   /**
    * Gets the DefaultValue attribute of the ColumnDecorator object
    *
    * @return The DefaultValue value
    */
   public final String getDefaultValue() {
      return _subject.getDefaultValue();
   }


   /**
    * Describe what the method does
    *
    * @return Describe the return value
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for return value
    */
   protected final String prefsPrefix() {
      return getPlugin().getName() + "/tables/" + _subject.getTable().getSqlName() + "/columns/" + getSqlName();
   }


   /**
    * Sets the TableDecorator attribute of the ColumnDecorator object
    *
    * @param tableDecorator The new TableDecorator value
    */
   void setTableDecorator(TableDecorator tableDecorator) {
      _tableDecorator = tableDecorator;
   }
}
