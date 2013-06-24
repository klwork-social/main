/*
 * Copyright (c) 2001, Aslak Hellesøy, BEKK Consulting
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
package middlegen.plugins.hibernate.swing;

import java.util.ResourceBundle;

/**
 * Provide localized messages. This class should be processed xdoclet task with
 * externalizer subtask. This class is described with utf-8 encoding.
 *
 * @author Takashi Okamoto
 * @created 27 August 2004
 * @version 2.1
 * @msg.bundle
 *
 */
public class LocalStrings {

   // -----------------------------------------------------
   // -- Messages for JHibernateTableSettingsPanel.java ---
   // -----------------------------------------------------
   /**
    * @msg.bundle
    *       msg = " Key generator"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = " 主キーの生成方法"
    *
    */
   public static final String KEY_GENERATOR = "key_generator";

   /**
    * @msg.bundle
    *       msg = " Domain class name"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = " ドメインクラス名"
    *
    */
   public static final String DOMAIN_CLASS_NAME = "domain_class_name";

   /**
    * @msg.bundle
    *       msg = " Schema name"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = " スキーマ名"
    *
    */
   public static final String SCHEMA_NAME = "schema_name";

   /**
    * @msg.bundle
    *       msg = " Persister"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = "Persisterクラス"
    *
    */
   public static final String PERSISTER = "persister";

   /**
    * @msg.bundle
    *       msg = " Class description"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = " クラスの説明"
    *
    */
   public static final String CLASS_DESCRIPTION = "class_description";

   /**
    * @msg.bundle
    *       msg = " Extends"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = " 継承する親クラス"
    *
    */
   public static final String EXTENDS = "extends";

   /**
    * @msg.bundle
    *       msg = "Lifecycle interface"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = "Lifecycleインタフェース"
    *
    */
   public static final String LIFECYCLE = "lifecycle";

   /**
    * @msg.bundle
    *       msg = "Validatable interface"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = "Validatableインタフェース"
    *
    */
   public static final String VALIDATABLE = "validatable";

   /**
    * @msg.bundle
    *       msg = "Plain Compoundkey"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = "主キークラスを生成"
    *
    */
   public static final String COMPOUNDKEY = "compoundkey";

   /**
    * @msg.bundle
    *       msg = "Enable proxies"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = " プロキシを使用"
    *
    */
   public static final String PROXY = "proxy";

   /**
    * @msg.bundle
    *       msg = "Mutable"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = "更新可能"
    *
    */
   public static final String MUTABLE = "mutable";

   /**
    * @msg.bundle
    *       msg = "Dynamic Update"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = "更新SQL動的生成"
    *
    */
   public static final String DYNAMIC_UPDATE = "dynamic_update";

   /**
    * @msg.bundle
    *       msg = "Dynamic Insert"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = "挿入SQL動的生成"
    *
    */
   public static final String DYNAMIC_INSERT = "dynamic_insert";

   /**
    * @msg.bundle
    *       msg = "Select Before Update"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = "updateの前にselectを実行"
    *
    */
   public static final String SELECT_BEFORE_UPDATE = "select_before_update";

   /**
    * @msg.bundle
    *       msg = " Class scope"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = " クラススコープ"
    *
    */
   public static final String CLASS_SCOPE = "class_scope";

   /**
    * @msg.bundle
    *       msg = " Manage external class interfaces"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = " 外部インタフェース追加"
    *
    */
   public static final String EXTERNAL_INTERFACE = "external_interface";

   /**
    * @msg.bundle
    *       msg = " Batch Size "
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = " バッチサイズ "
    *
    */
   public static final String BATCH_SIZE = "batch_size";

   /**
    * @msg.bundle
    *       msg = " Where "
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = " WHERE句"
    *
    */
   public static final String WHERE = "where";

   /**
    * @msg.bundle
    *       msg = "Generate Equals/Hash"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = "Equals/Hash生成"
    *
    */
   public static final String GENERATE_EQUALS_HASH = "generate_equals_hash";

   /**
    * @msg.bundle
    *       msg = "Table Mapping Attributes"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = "表マッピング属性"
    *
    */
   public static final String MAPPING_ATTRIBUTE = "mapping_attribute";

   /**
    * @msg.bundle
    *       msg = "Domain Class Meta Attributes"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = "ドメインクラスメタ属性"
    *
    */
   public static final String CLASS_ATTRIBUTE = "class_attribute";

   /**
    * @msg.bundle
    *       msg = "Add"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = "追加"
    *
    */
   public static final String ADD = "add";

   /**
    * @msg.bundle
    *       msg = "Remove"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = "削除"
    *
    */
   public static final String REMOVE = "remove";

   // -----------------------------------------------------
   // -- Messages for JHibernateColumnSettingsPanel.java --
   // -----------------------------------------------------
   /**
    * @msg.bundle
    *       msg = "  Java property name"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = "Javaプロパティ"
    *
    */
   public static final String JAVA_PROPERTY = "java_property";

   /**
    * @msg.bundle
    *       msg = "  Java type"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = "Java型"
    *
    */
   public static final String JAVA_TYPE = "java_type";

   /**
    * @msg.bundle
    *       msg = "  Hibernate mapping specialty"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = "フィールドマッピング特性"
    *
    */
   public static final String MAPPING_SPECIALTY = "mapping_specialty";

   /**
    * @msg.bundle
    *       msg = "Column updateable"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = "列更新可"
    *
    */
   public static final String COLUMN_UPDATABLE = "column_updatable";

   /**
    * @msg.bundle
    *       msg = "Column insertable"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = "列挿入可"
    *
    */
   public static final String COLUMN_INSERTABLE = "column_insertable";

   /**
    * @msg.bundle
    *       msg = "  Field description"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = "フィールド説明"
    *
    */
   public static final String FIELD_DESCRIPTION = "field_description";

   /**
    * @msg.bundle
    *       msg = "  Field access method"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = "フィールドへのアクセス方法"
    *
    */
   public static final String FIELD_ACCESS_METHOD = "field_access_method";

   /**
    * @msg.bundle
    *       msg = "  Property get scope"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = "getterスコープ"
    *
    */
   public static final String PROPERTY_GET_SCOPE = "property_get_scope";

   /**
    * @msg.bundle
    *       msg = "  Property set scope"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = "setterスコープ"
    *
    */
   public static final String PROPERTY_SET_SCOPE = "property_set_scope";

   /**
    * @msg.bundle
    *       msg = "  Field scope"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = "フィールドスコープ"
    *
    */
   public static final String FIELD_SET_SCOPE = "field_set_scope";

   /**
    * @msg.bundle
    *       msg = "Include in toString"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = "toStringに含める"
    *
    */
   public static final String INCLUDE_TOSTRING = "include_tostring";

   /**
    * @msg.bundle
    *       msg = "Include in Equals/Hash"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = "Euqals/Hashに含める"
    *
    */
   public static final String INCLUDE_EQUALS_HASH = "include_equals_hash";

   /**
    * @msg.bundle
    *       msg = "Generate the property"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = "プロパティ生成"
    *
    */
   public static final String GENERATE_PROPERTY = "generate_property";

   /**
    * @msg.bundle
    *       msg = "  Bean constraint property type"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = "Bean制約プロパティ型"
    *
    */
   public static final String CONSTRAINT_PROPERTY = "constraint_property";

   /**
    * @msg.bundle
    *       msg = "Column Mapping Attributes"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = "列マッピング属性"
    *
    */
   public static final String COLUMN_MAPPING = "column_mapping";

   /**
    * @msg.bundle
    *       msg = "Domain Property Meta Attributes"
    *
    * @msg.bundle
    *       language = "ja"
    *       msg = "ドメインプロパティメタ属性"
    *
    */
   public static final String PROPERTY_META_ATTR = "property_meta_attr";
   /**
    * @todo-javadoc Describe the field
    */
   private static ResourceBundle rb = ResourceBundle.getBundle(LocalStrings.class.getName() + "Messages");


   /**
    * Gets the Msg attribute of the LocalStrings class
    *
    * @todo-javadoc Write javadocs for method parameter
    * @param name Describe what the parameter does
    * @return The Msg value
    */
   public static String getMsg(String name) {
      return (String)rb.getString(name);
   }
}
