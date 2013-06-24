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

import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;

import middlegen.Column;
import middlegen.javax.JavaColumn;
import middlegen.javax.Sql2Java;
import middlegen.swing.JColumnSettingsPanel;
import middlegen.swing.BooleanNodeCheckBox;
import middlegen.plugins.hibernate.HibernatePlugin;
import middlegen.plugins.hibernate.HibernateColumn;
import middlegen.plugins.hibernate.HibernateTable;

import middlegen.plugins.hibernate.interfaces.JavaTypeMapper;

/**
 * This panel renders and modifies columns
 *
 * @author David Channon and Gavin King
 * @created 1 January 2003
 * @version 2.1
 */
public class JHibernateColumnSettingsPanel extends JColumnSettingsPanel {

   /**
    * @todo-javadoc Describe the column
    */
   protected HibernateColumn[] _currentColumns;
   /**
    * @todo-javadoc Describe the column
    */
   private final JLabel _columnNameLabel = new JLabel(LocalStrings.getMsg(LocalStrings.JAVA_PROPERTY));
   /**
    * @todo-javadoc Describe the column
    */
   private final JTextField _columnNameField = new JTextField();
   /**
    * @todo-javadoc Describe the column
    */
   private final JLabel _columnTypeLabel = new JLabel(LocalStrings.getMsg(LocalStrings.JAVA_TYPE));
   /**
    * @todo-javadoc Describe the column
    */
   private final JComboBox _columnTypeCombo = new JComboBox();

   /**
    * @todo-javadoc Describe the field
    */
   private final JRadioButton _accessFieldRadioButton = new JRadioButton(HibernatePlugin.FieldId);
   /**
    * @todo-javadoc Describe the field
    */
   private final JRadioButton _accessPropertyRadioButton = new JRadioButton(HibernatePlugin.PropertyId);
   /**
    * @todo-javadoc Describe the field
    */
   private final JRadioButton _publicGetRadioButton = new JRadioButton(HibernatePlugin.PublicId);
   /**
    * @todo-javadoc Describe the field
    */
   private final JRadioButton _protectedGetRadioButton = new JRadioButton(HibernatePlugin.ProtectedId);
   /**
    * @todo-javadoc Describe the field
    */
   private final JRadioButton _privateGetRadioButton = new JRadioButton(HibernatePlugin.PrivateId);
   /**
    * @todo-javadoc Describe the field
    */
   private final JRadioButton _publicSetRadioButton = new JRadioButton(HibernatePlugin.PublicId);
   /**
    * @todo-javadoc Describe the field
    */
   private final JRadioButton _protectedSetRadioButton = new JRadioButton(HibernatePlugin.ProtectedId);
   /**
    * @todo-javadoc Describe the field
    */
   private final JRadioButton _privateSetRadioButton = new JRadioButton(HibernatePlugin.PrivateId);
   /**
    * @todo-javadoc Describe the field
    */
   private final JRadioButton _publicFieldRadioButton = new JRadioButton(HibernatePlugin.PublicId);
   /**
    * @todo-javadoc Describe the field
    */
   private final JRadioButton _protectedFieldRadioButton = new JRadioButton(HibernatePlugin.ProtectedId);
   /**
    * @todo-javadoc Describe the field
    */
   private final JRadioButton _privateFieldRadioButton = new JRadioButton(HibernatePlugin.PrivateId);
   /**
    * @todo-javadoc Describe the field
    */
   private final JLabel _columnSpecialtyLabel = new JLabel(LocalStrings.getMsg(LocalStrings.MAPPING_SPECIALTY));
   /**
    * @todo-javadoc Describe the field
    */
   private final JComboBox _columnSpecialtyCombo = new JComboBox();
   /**
    * @todo-javadoc Describe the field
    */
   private final BooleanNodeCheckBox _columnUpdateableCheckBox = new BooleanNodeCheckBox(LocalStrings.getMsg(LocalStrings.COLUMN_UPDATABLE), null);
   /**
    * @todo-javadoc Describe the field
    */
   private final BooleanNodeCheckBox _columnInsertableCheckBox = new BooleanNodeCheckBox(LocalStrings.getMsg(LocalStrings.COLUMN_INSERTABLE), null);

   /**
    * @todo-javadoc Describe the field
    */
   private final JLabel _fieldDescriptionLabel = new JLabel(LocalStrings.getMsg(LocalStrings.FIELD_DESCRIPTION));
   /**
    * @todo-javadoc Describe the field
    */
   private final JTextField _fieldDescriptionField = new JTextField();
   /**
    * @todo-javadoc Describe the field
    */
   private final JLabel _fieldAccessMethodLabel = new JLabel(LocalStrings.getMsg(LocalStrings.FIELD_ACCESS_METHOD));
   /**
    * @todo-javadoc Describe the field
    */
   private final JLabel _propertyGetScopeLabel = new JLabel(LocalStrings.getMsg(LocalStrings.PROPERTY_GET_SCOPE));
   /**
    * @todo-javadoc Describe the field
    */
   private final JLabel _propertySetScopeLabel = new JLabel(LocalStrings.getMsg(LocalStrings.PROPERTY_SET_SCOPE));
   /**
    * @todo-javadoc Describe the field
    */
   private final JLabel _fieldScopeLabel = new JLabel(LocalStrings.getMsg(LocalStrings.FIELD_SET_SCOPE));
   /**
    * @todo-javadoc Describe the field
    */
   private final BooleanNodeCheckBox _includeInEqualsCheckBox = new BooleanNodeCheckBox(LocalStrings.getMsg(LocalStrings.INCLUDE_EQUALS_HASH), null);
   /**
    * @todo-javadoc Describe the field
    */
   private final BooleanNodeCheckBox _includeInTostringCheckBox = new BooleanNodeCheckBox(LocalStrings.getMsg(LocalStrings.INCLUDE_TOSTRING), null);
   /**
    * @todo-javadoc Describe the field
    */
   private final BooleanNodeCheckBox _genPropertyCheckBox = new BooleanNodeCheckBox(LocalStrings.getMsg(LocalStrings.GENERATE_PROPERTY), null);
   /**
    * @todo-javadoc Describe the field
    */
   private final JLabel _beanPropertyTypeLabel = new JLabel(LocalStrings.getMsg(LocalStrings.CONSTRAINT_PROPERTY));
   /**
    * @todo-javadoc Describe the field
    */
   private final JComboBox _beanPropertyTypeCombo = new JComboBox();

   /**
    * @todo-javadoc Describe the field
    */
   private final DocumentListener _fieldDescriptionDocumentListener =
      new DocumentListener() {
         public void changedUpdate(DocumentEvent event) {
            update();
         }


         public void insertUpdate(DocumentEvent event) {
            update();
         }


         public void removeUpdate(DocumentEvent event) {
            update();
         }


         private void update() {
            for (int i = 0; i < _currentColumns.length; i++) {
               _currentColumns[i].setFieldDescription(_fieldDescriptionField.getText());
            }
         }
      };

   /**
    * @todo-javadoc Describe the column
    */
   private final ActionListener _comboListener =
      new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            update();
         }


         private void update() {
            String type = (String)_columnTypeCombo.getSelectedItem();
            if (type != null) {
               for (int i = 0; i < _currentColumns.length; i++) {
                  String[] acceptableTypes = Sql2Java.getJavaTypes(_currentColumns[i].getSqlType());
                  boolean yesOK = false;
                  for (int j = 0; j < acceptableTypes.length; j++) {
                     if (acceptableTypes[j].equals(type)) {
                        yesOK = true;
                        break;
                     }
                  }
                  if (yesOK) {
                     _currentColumns[i].setJavaType(type);
                  }
                  else {
                     JavaTypeMapper javaTypeMapper = _currentColumns[i].getJavaTypeMapper();
                     if (javaTypeMapper == null) {
                        _currentColumns[i].setJavaType(Sql2Java.getPreferredJavaType(_currentColumns[i].getSqlType(), 0, 0));
                     }
                     else {
                        _currentColumns[i].setJavaType(javaTypeMapper.getPreferredJavaType(_currentColumns[i]));
                     }
                  }
               }
            }
         }
      };

   /**
    * @todo-javadoc Describe the field
    */
   private final ActionListener _beanComboListener =
      new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            update();
         }


         private void update() {
            for (int i = 0; i < _currentColumns.length; i++) {
               String type = (String)_beanPropertyTypeCombo.getSelectedItem();
               if (type != null) {
                  _currentColumns[i].setBeanPropertyType(type);
               }
            }
         }
      };

   /**
    * @todo-javadoc Describe the field
    */
   private final ActionListener _columnSpecialtyComboListener =
      new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            update();
         }


         private void update() {
            for (int i = 0; i < _currentColumns.length; i++) {
               String type = (String)_columnSpecialtyCombo.getSelectedItem();
               if (type != null) {
                  _currentColumns[i].setColumnSpecialty(type);
               }
            }
         }
      };

   /**
    * @todo-javadoc Describe the field
    */
   private final ActionListener toggleBtnUpdateableAction =
      new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < _currentColumns.length; i++) {
               _currentColumns[i].setUpdateable(((BooleanNodeCheckBox)e.getSource()).isSelected());
            }
         }
      };

   /**
    * @todo-javadoc Describe the field
    */
   private final ActionListener toggleBtnInsertableAction =
      new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < _currentColumns.length; i++) {
               _currentColumns[i].setInsertable(((BooleanNodeCheckBox)e.getSource()).isSelected());
            }
         }
      };

   /**
    * @todo-javadoc Describe the field
    */
   private final ActionListener toggleBtnIncludeToStringAction =
      new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < _currentColumns.length; i++) {
               _currentColumns[i].setIncludeToString(((BooleanNodeCheckBox)e.getSource()).isSelected());
            }
         }
      };

   /**
    * @todo-javadoc Describe the field
    */
   private final ActionListener toggleBtnIncludeEqualsAction =
      new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < _currentColumns.length; i++) {
               _currentColumns[i].setIncludeEquals(((BooleanNodeCheckBox)e.getSource()).isSelected());
            }
         }
      };

   /**
    * @todo-javadoc Describe the field
    */
   private final ActionListener toggleBtnGenPropertyAction =
      new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < _currentColumns.length; i++) {
               _currentColumns[i].setGenProperty(((BooleanNodeCheckBox)e.getSource()).isSelected());
            }
         }
      };


   /**
    * Describe what the JColumnSettingsPanel constructor does
    *
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for constructor
    */
   public JHibernateColumnSettingsPanel() {
      super();
      setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

      initComponents();
      addComponents();

      assignAllListeners();
   }


   /**
    * Sets the Column attribute of the JColumnSettingsPanel object
    *
    * @param columns The new Columns value
    */
   public void setColumns(Column[] columns) {

      deassignAllListeners();

      _currentColumns = new HibernateColumn[columns.length];
      for (int i = 0; i < columns.length; i++) {
         _currentColumns[i] = (HibernateColumn)columns[i];
      }

      boolean singleColumnSelected = _currentColumns.length == 1;

      if (_currentColumns.length >= 1) {
         _columnUpdateableCheckBox.setBooleanNode(_currentColumns[0].getUpdateableNode());
         _columnInsertableCheckBox.setBooleanNode(_currentColumns[0].getInsertableNode());
         _includeInTostringCheckBox.setBooleanNode(_currentColumns[0].getIncludeToStringNode());
         _genPropertyCheckBox.setBooleanNode(_currentColumns[0].getGenPropertyNode());
         _includeInEqualsCheckBox.setBooleanNode(_currentColumns[0].getIncludeEqualsNode());
         _fieldDescriptionField.setText(_currentColumns[0].getFieldDescription());
         _beanPropertyTypeCombo.setSelectedItem(_currentColumns[0].getBeanPropertyType());

         setAccessField();
         setFieldScope();
         setFieldGetScope();
         setFieldSetScope();
      }

      _columnNameField.setEnabled(singleColumnSelected);
      _genPropertyCheckBox.setEnabled(singleColumnSelected);

      if (singleColumnSelected) {
         // single selection
         _columnNameField.setText(_currentColumns[0].getVariableName());
      }
      else {
         _columnNameField.setText("");
      }

      setHibernateJavaTypeCombo();
      setHibernateSpecialtyCombo();

      assignAllListeners();
   }


   /**
    * Describe what the method does
    *
    * @todo-javadoc Write javadocs for method
    */
   protected void initComponents() {

      final String sitems[] = {HibernatePlugin.ColKeyId, HibernatePlugin.ColCompoundKeyId, HibernatePlugin.ColVersionId, HibernatePlugin.ColTimestampId, HibernatePlugin.ColPropertyId};
      DefaultComboBoxModel sModel = new DefaultComboBoxModel(sitems);
      _columnSpecialtyCombo.setModel(sModel);
      _columnSpecialtyCombo.setEditable(false);

      final String bitems[] = {"unbound", "bound", "constrained"};
      DefaultComboBoxModel cModel = new DefaultComboBoxModel(bitems);
      _beanPropertyTypeCombo.setModel(cModel);
      _beanPropertyTypeCombo.setEditable(false);
      _columnNameField.setEditable(false);
   }



   /**
    * Describe the method
    *
    * @todo-javadoc Describe the method
    */
   protected void addComponents() {
      JPanel firstGroup = new JPanel();
      firstGroup.setLayout(new BoxLayout(firstGroup, BoxLayout.Y_AXIS));
      firstGroup.setBorder(BorderFactory.createTitledBorder(LocalStrings.getMsg(LocalStrings.COLUMN_MAPPING)));
      JPanel secondGroup = new JPanel();
      secondGroup.setLayout(new BoxLayout(secondGroup, BoxLayout.Y_AXIS));
      secondGroup.setBorder(BorderFactory.createTitledBorder(LocalStrings.getMsg(LocalStrings.PROPERTY_META_ATTR)));

      JPanel firstSectionGroup1 = new JPanel();
      firstSectionGroup1.setLayout(new GridLayout(0, 2, 4, 2));
      JPanel secondSectionGroup1 = new JPanel();
      secondSectionGroup1.setLayout(new GridLayout(0, 2, 4, 2));
      JPanel thirdSectionGroup1 = new JPanel();
      thirdSectionGroup1.setLayout(new GridLayout(0, 2, 4, 2));

      JPanel firstSectionGroup2 = new JPanel();
      firstSectionGroup2.setLayout(new GridLayout(0, 2, 4, 2));
      JPanel secondSectionGroup2 = new JPanel();
      secondSectionGroup2.setLayout(new GridLayout(0, 2, 0, 2));
      JPanel thirdSectionGroup2 = new JPanel();
      thirdSectionGroup2.setLayout(new GridLayout(0, 2, 4, 2));
      JPanel fourthSectionGroup2 = new JPanel();
      fourthSectionGroup2.setLayout(new GridLayout(0, 3, 4, 2));

      firstSectionGroup1.add(_columnSpecialtyLabel);
      firstSectionGroup1.add(_columnSpecialtyCombo);
      firstSectionGroup1.add(_columnNameLabel);
      firstSectionGroup1.add(_columnNameField);
      firstSectionGroup1.add(_columnTypeLabel);
      firstSectionGroup1.add(_columnTypeCombo);

      secondSectionGroup1.add(_fieldAccessMethodLabel);
      secondSectionGroup1.add(BuildAccessPanel());

      thirdSectionGroup1.add(_columnUpdateableCheckBox);
      thirdSectionGroup1.add(_columnInsertableCheckBox);

      firstGroup.add(firstSectionGroup1);
      firstGroup.add(secondSectionGroup1);
      firstGroup.add(thirdSectionGroup1);

      firstSectionGroup2.add(_fieldDescriptionLabel);
      firstSectionGroup2.add(_fieldDescriptionField);
      secondSectionGroup2.add(_propertyGetScopeLabel);
      secondSectionGroup2.add(BuildTableGetScopePanel());
      secondSectionGroup2.add(_propertySetScopeLabel);
      secondSectionGroup2.add(BuildTableSetScopePanel());
      secondSectionGroup2.add(_fieldScopeLabel);
      secondSectionGroup2.add(BuildTableFieldScopePanel());
      thirdSectionGroup2.add(_beanPropertyTypeLabel);
      thirdSectionGroup2.add(_beanPropertyTypeCombo);
      fourthSectionGroup2.add(_genPropertyCheckBox);
      fourthSectionGroup2.add(_includeInTostringCheckBox);
      fourthSectionGroup2.add(_includeInEqualsCheckBox);

      secondGroup.add(firstSectionGroup2);
      secondGroup.add(secondSectionGroup2);
      secondGroup.add(thirdSectionGroup2);
      secondGroup.add(fourthSectionGroup2);

      add(firstGroup);
      add(secondGroup);
   }


   /**
    * Sets the HibernateJavaTypeCombo attribute of the
    * JHibernateColumnSettingsPanel object
    */
   private void setHibernateJavaTypeCombo() {
      _columnTypeCombo.setEnabled(true);
      _columnTypeCombo.removeAllItems();

      if (_currentColumns[0].isPk() && ((HibernateTable)_currentColumns[0].getTable()).isCompositeKey()) {
         if (_currentColumns.length > 1) {
            _columnTypeCombo.setEnabled(false);
         }
         else {
            _columnTypeLabel.setText(LocalStrings.getMsg(LocalStrings.JAVA_TYPE) + "   (" + ((HibernateTable)_currentColumns[0].getTable()).getCompoundKeyDestinationClassName() + ")");
         }
      }
      else {
         _columnTypeLabel.setText(LocalStrings.getMsg(LocalStrings.JAVA_TYPE));
      }

      String selectedJavaType = _currentColumns[0].getJavaType();

      String[] common = Sql2Java.getJavaTypes(_currentColumns[0].getSqlType());
      for (int i = 0; i < common.length; i++) {
         _columnTypeCombo.addItem(common[i]);
      }
      _columnTypeCombo.setSelectedItem(selectedJavaType);

   }


   /**
    * Sets the appropriate options for the hibernate specialty for the column.
    * If more than one currently selected column is active then no options are
    * available. The default setting for any columns is property unless it is a
    * primary key or part of one.</p> The logic for this method involves
    * determining if it a key, compound key, Foreign key, property, version, or
    * timestamp version.
    */
   private void setHibernateSpecialtyCombo() {
      _columnSpecialtyCombo.setEnabled(true);
      _columnSpecialtyCombo.removeAllItems();

      if (_currentColumns.length == 1) {
         if (_currentColumns[0].isPk()) {
            if (((HibernateTable)_currentColumns[0].getTable()).isCompositeKey()) {
               _columnSpecialtyCombo.addItem(HibernatePlugin.ColCompoundKeyId);
            }
            else {
               _columnSpecialtyCombo.addItem(HibernatePlugin.ColKeyId);
            }
         }
         else if (_currentColumns[0].isFk()) {
            // Involved in a relationship therefore do not need to
            // do anything here (just set the combo box).
            _columnSpecialtyCombo.addItem(HibernatePlugin.ColForeignKeyId);
         }
         else {
            // This is a property
            // Determine of it could be a specialty version property
            // Version can be either long, integer or short (or the Java object).
            // Timestamp can only be a timestamp.

            String ptype = _currentColumns[0].getJavaType();
            if (ptype == null) {
               JavaTypeMapper javaTypeMapper = _currentColumns[0].getJavaTypeMapper();
               if (javaTypeMapper == null) {
                  ptype = Sql2Java.getPreferredJavaType(_currentColumns[0].getSqlType(), 0, 0);
               }
               else {
                  ptype = javaTypeMapper.getPreferredJavaType(_currentColumns[0]);
               }
            }
            _columnSpecialtyCombo.addItem(HibernatePlugin.ColPropertyId);
            if (ptype.equals("long") || ptype.equals("int") || ptype.equals("short")) {
               _columnSpecialtyCombo.addItem(HibernatePlugin.ColVersionId);
            }
            else if (ptype.equals("java.sql.Timestamp")) {
               _columnSpecialtyCombo.addItem(HibernatePlugin.ColTimestampId);
            }
         }
         _columnSpecialtyCombo.setSelectedItem(_currentColumns[0].getColumnSpecialty());
         _currentColumns[0].setColumnSpecialty((String)_columnSpecialtyCombo.getSelectedItem());
      }
      else {
         // There is none or more than one column active
         _columnSpecialtyCombo.addItem(HibernatePlugin.ColPropertyId);
         _columnSpecialtyCombo.setEnabled(false);
      }
   }


   /**
    * Sets the FieldScope attribute of the JHibernateColumnsSettingsPanel GUI
    * object
    */
   private void setFieldScope() {
      _publicFieldRadioButton.setSelected(_currentColumns[0].getFieldScope().equals(HibernatePlugin.PublicId));
      _protectedFieldRadioButton.setSelected(_currentColumns[0].getFieldScope().equals(HibernatePlugin.ProtectedId));
      _privateFieldRadioButton.setSelected(_currentColumns[0].getFieldScope().equals(HibernatePlugin.PrivateId));
   }


   /**
    * Sets the AccessField attribute of the JHibernateColumnsSettingsPanel GUI
    * object
    */
   private void setAccessField() {
      _accessFieldRadioButton.setSelected(_currentColumns[0].getAccessField().equals(HibernatePlugin.FieldId));
      _accessPropertyRadioButton.setSelected(_currentColumns[0].getAccessField().equals(HibernatePlugin.PropertyId));
   }


   /**
    * Sets the FieldGetScope attribute of the JHibernateColumnsSettingsPanel GUI
    * object
    */
   private void setFieldGetScope() {
      _publicGetRadioButton.setSelected(_currentColumns[0].getFieldGetScope().equals(HibernatePlugin.PublicId));
      _protectedGetRadioButton.setSelected(_currentColumns[0].getFieldGetScope().equals(HibernatePlugin.ProtectedId));
      _privateGetRadioButton.setSelected(_currentColumns[0].getFieldGetScope().equals(HibernatePlugin.PrivateId));
   }


   /**
    * Sets the FieldSetScope attribute of the JHibernateColumnsSettingsPanel GUI
    * object
    */
   private void setFieldSetScope() {
      _publicSetRadioButton.setSelected(_currentColumns[0].getFieldSetScope().equals(HibernatePlugin.PublicId));
      _protectedSetRadioButton.setSelected(_currentColumns[0].getFieldSetScope().equals(HibernatePlugin.ProtectedId));
      _privateSetRadioButton.setSelected(_currentColumns[0].getFieldSetScope().equals(HibernatePlugin.PrivateId));
   }


   /**
    * Describe what the method does
    *
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for return value
    * @return Describe the return value
    */
   private JPanel BuildAccessPanel() {
      JPanel spanel = new JPanel();
      spanel.setLayout(new FlowLayout());
      ButtonGroup group = new ButtonGroup();
      group.add(_accessFieldRadioButton);
      group.add(_accessPropertyRadioButton);
      ActionListener radioAccessBtnAction =
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               for (int i = 0; i < _currentColumns.length; i++) {
                  _currentColumns[i].setAccessField(e.getActionCommand());
               }
            }
         };
      _accessFieldRadioButton.setActionCommand(HibernatePlugin.FieldId);
      _accessFieldRadioButton.addActionListener(radioAccessBtnAction);
      _accessPropertyRadioButton.addActionListener(radioAccessBtnAction);
      _accessPropertyRadioButton.setActionCommand(HibernatePlugin.PropertyId);
      spanel.add(_accessFieldRadioButton);
      spanel.add(_accessPropertyRadioButton);
      return spanel;
   }


   /**
    * Describe what the method does
    *
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for return value
    * @return Describe the return value
    */
   private JPanel BuildTableGetScopePanel() {
      JPanel spanel = new JPanel();
      spanel.setLayout(new FlowLayout());
      ButtonGroup group = new ButtonGroup();
      group.add(_publicGetRadioButton);
      group.add(_protectedGetRadioButton);
      group.add(_privateGetRadioButton);
      ActionListener radioGetBtnAction =
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               for (int i = 0; i < _currentColumns.length; i++) {
                  _currentColumns[i].setFieldGetScope(e.getActionCommand());
               }
            }
         };
      _publicGetRadioButton.setActionCommand(HibernatePlugin.PublicId);
      _publicGetRadioButton.addActionListener(radioGetBtnAction);
      _protectedGetRadioButton.addActionListener(radioGetBtnAction);
      _protectedGetRadioButton.setActionCommand(HibernatePlugin.ProtectedId);
      _privateGetRadioButton.addActionListener(radioGetBtnAction);
      _privateGetRadioButton.setActionCommand(HibernatePlugin.PrivateId);
      spanel.add(_publicGetRadioButton);
      spanel.add(_protectedGetRadioButton);
      spanel.add(_privateGetRadioButton);
      return spanel;
   }


   /**
    * Describe what the method does
    *
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for return value
    * @return Describe the return value
    */
   private JPanel BuildTableSetScopePanel() {
      JPanel spanel = new JPanel();
      spanel.setLayout(new FlowLayout());
      ButtonGroup group = new ButtonGroup();
      group.add(_publicSetRadioButton);
      group.add(_protectedSetRadioButton);
      group.add(_privateSetRadioButton);
      ActionListener radioSetBtnAction =
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               for (int i = 0; i < _currentColumns.length; i++) {
                  _currentColumns[i].setFieldSetScope(e.getActionCommand());
               }
            }
         };
      _publicSetRadioButton.setActionCommand(HibernatePlugin.PublicId);
      _publicSetRadioButton.addActionListener(radioSetBtnAction);
      _protectedSetRadioButton.addActionListener(radioSetBtnAction);
      _protectedSetRadioButton.setActionCommand(HibernatePlugin.ProtectedId);
      _privateSetRadioButton.addActionListener(radioSetBtnAction);
      _privateSetRadioButton.setActionCommand(HibernatePlugin.PrivateId);
      spanel.add(_publicSetRadioButton);
      spanel.add(_protectedSetRadioButton);
      spanel.add(_privateSetRadioButton);
      return spanel;
   }


   /**
    * Describe what the method does
    *
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for return value
    * @return Describe the return value
    */
   private JPanel BuildTableFieldScopePanel() {
      JPanel spanel = new JPanel();
      spanel.setLayout(new FlowLayout());
      ButtonGroup group = new ButtonGroup();
      group.add(_publicFieldRadioButton);
      group.add(_protectedFieldRadioButton);
      group.add(_privateFieldRadioButton);
      ActionListener radioFieldBtnAction =
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               for (int i = 0; i < _currentColumns.length; i++) {
                  _currentColumns[i].setFieldScope(e.getActionCommand());
               }
            }
         };
      _publicFieldRadioButton.setActionCommand(HibernatePlugin.PublicId);
      _publicFieldRadioButton.addActionListener(radioFieldBtnAction);
      _protectedFieldRadioButton.addActionListener(radioFieldBtnAction);
      _protectedFieldRadioButton.setActionCommand(HibernatePlugin.ProtectedId);
      _privateFieldRadioButton.addActionListener(radioFieldBtnAction);
      _privateFieldRadioButton.setActionCommand(HibernatePlugin.PrivateId);
      spanel.add(_publicFieldRadioButton);
      spanel.add(_protectedFieldRadioButton);
      spanel.add(_privateFieldRadioButton);
      return spanel;
   }


   /**
    * Describe what the method does
    *
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for return value
    * @param arrays Describe what the parameter does
    * @return Describe the return value
    */
   private Object[] common(Object[][] arrays) {
      HashMap union = new HashMap();
      for (int i = 0; i < arrays.length; i++) {
         Object[] array = arrays[i];
         for (int j = 0; j < array.length; j++) {
            Integer occurrances = (Integer)union.get(array[j]);
            if (occurrances == null) {
               occurrances = new Integer(0);
            }
            union.put(array[j], new Integer(occurrances.intValue() + 1));
         }
      }
      ArrayList common = new ArrayList();
      if (arrays.length > 1) {
         common.add("");
      }
      Iterator iterator = union.entrySet().iterator();
      while (iterator.hasNext()) {
         Map.Entry entry = (Map.Entry)iterator.next();
         Integer occurrances = (Integer)entry.getValue();
         if (occurrances.intValue() == arrays.length) {
            common.add(entry.getKey());
         }
      }
      Object[] result = common.toArray();
      Arrays.sort(result, Sql2Java.getTypeComparator());
      return result;
   }


   /** Allocates listeners to the main GUI components. */
   private void assignAllListeners() {
      // register listeners on text column and combo that will update borders
      _columnTypeCombo.addActionListener(_comboListener);
      _fieldDescriptionField.getDocument().addDocumentListener(_fieldDescriptionDocumentListener);
      _beanPropertyTypeCombo.addActionListener(_beanComboListener);
      _columnSpecialtyCombo.addActionListener(_columnSpecialtyComboListener);

      // Register listerner(s) on components
      _columnUpdateableCheckBox.addActionListener(toggleBtnUpdateableAction);
      _columnInsertableCheckBox.addActionListener(toggleBtnInsertableAction);
      _includeInTostringCheckBox.addActionListener(toggleBtnIncludeToStringAction);
      _genPropertyCheckBox.addActionListener(toggleBtnGenPropertyAction);
      _includeInEqualsCheckBox.addActionListener(toggleBtnIncludeEqualsAction);
   }


   /** Removes listeners from the main components. */
   private void deassignAllListeners() {
      // Deregister listeners from text column and combo that will update borders
      _columnTypeCombo.removeActionListener(_comboListener);
      _fieldDescriptionField.getDocument().removeDocumentListener(_fieldDescriptionDocumentListener);
      _beanPropertyTypeCombo.removeActionListener(_beanComboListener);
      _columnSpecialtyCombo.removeActionListener(_columnSpecialtyComboListener);

      // Deregister listerner(s) from components
      _columnUpdateableCheckBox.removeActionListener(toggleBtnUpdateableAction);
      _columnInsertableCheckBox.removeActionListener(toggleBtnInsertableAction);
      _includeInTostringCheckBox.removeActionListener(toggleBtnIncludeToStringAction);
      _genPropertyCheckBox.removeActionListener(toggleBtnGenPropertyAction);
      _includeInEqualsCheckBox.removeActionListener(toggleBtnIncludeEqualsAction);
   }
}
