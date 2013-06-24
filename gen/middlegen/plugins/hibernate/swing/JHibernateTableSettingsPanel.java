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

import java.util.ArrayList;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;

import middlegen.Table;
import middlegen.plugins.hibernate.HibernateTable;
import middlegen.plugins.hibernate.HibernatePlugin;
import middlegen.swing.BooleanNodeCheckBox;
import middlegen.swing.JTableSettingsPanel;
import middlegen.util.BooleanNode;

/**
 * This panel renders and modifies columns
 *
 * @author David Channon and Gavin King
 * @created 1 January 2003
 * @version 2.1
 */
public class JHibernateTableSettingsPanel extends JTableSettingsPanel {

   /**
    * @todo-javadoc Describe the field
    */
   private final JLabel _beanKeyGeneratorLabel = new JLabel(LocalStrings.getMsg(LocalStrings.KEY_GENERATOR));
   /**
    * @todo-javadoc Describe the field
    */
   private final JLabel _beanNameLabel = new JLabel(LocalStrings.getMsg(LocalStrings.DOMAIN_CLASS_NAME));
   /**
    * @todo-javadoc Describe the field
    */
   private final JTextField _beanNameField = new JTextField();
   /**
    * @todo-javadoc Describe the field
    */
   private final JLabel _schemaNameLabel = new JLabel(LocalStrings.getMsg(LocalStrings.SCHEMA_NAME));
   /**
    * @todo-javadoc Describe the field
    */
   private final JTextField _schemaNameField = new JTextField();
   /**
    * @todo-javadoc Describe the field
    */
   private final JLabel _persisterLabel = new JLabel(LocalStrings.getMsg(LocalStrings.PERSISTER));
   /**
    * @todo-javadoc Describe the field
    */
   private final JTextField _persisterField = new JTextField();
   /**
    * @todo-javadoc Describe the field
    */
   private final JLabel _classDescriptionLabel = new JLabel(LocalStrings.getMsg(LocalStrings.CLASS_DESCRIPTION));
   /**
    * @todo-javadoc Describe the field
    */
   private final JTextField _classDescriptionField = new JTextField();
   /**
    * @todo-javadoc Describe the field
    */
   private final JLabel _extendsLabel = new JLabel(LocalStrings.getMsg(LocalStrings.EXTENDS));
   /**
    * @todo-javadoc Describe the field
    */
   private final JTextField _extendsField = new JTextField();
   /**
    * @todo-javadoc Describe the column
    */
   private final BooleanNodeCheckBox _implementLifecycleCheckBox = new BooleanNodeCheckBox(LocalStrings.getMsg(LocalStrings.LIFECYCLE), null);
   /**
    * @todo-javadoc Describe the column
    */
   private final BooleanNodeCheckBox _implementValidatableCheckBox = new BooleanNodeCheckBox(LocalStrings.getMsg(LocalStrings.VALIDATABLE), null);

   /**
    * @todo-javadoc Describe the field
    */
   private final BooleanNodeCheckBox _pcKeyCheckBox = new BooleanNodeCheckBox(LocalStrings.getMsg(LocalStrings.COMPOUNDKEY), null);
   /**
    * @todo-javadoc Describe the field
    */
   private final BooleanNodeCheckBox _proxyCheckBox = new BooleanNodeCheckBox(LocalStrings.getMsg(LocalStrings.PROXY), null);
   /**
    * @todo-javadoc Describe the field
    */
   private final BooleanNodeCheckBox _mutableCheckBox = new BooleanNodeCheckBox(LocalStrings.getMsg(LocalStrings.MUTABLE), null);
   /**
    * @todo-javadoc Describe the field
    */
   private final BooleanNodeCheckBox _dynamicUpdateCheckBox = new BooleanNodeCheckBox(LocalStrings.getMsg(LocalStrings.DYNAMIC_UPDATE), null);
   /**
    * @todo-javadoc Describe the field
    */
   private final BooleanNodeCheckBox _dynamicInsertCheckBox = new BooleanNodeCheckBox(LocalStrings.getMsg(LocalStrings.DYNAMIC_INSERT), null);
   /**
    * @todo-javadoc Describe the field
    */
   private final BooleanNodeCheckBox _selectBeforeUpdateCheckBox = new BooleanNodeCheckBox(LocalStrings.getMsg(LocalStrings.SELECT_BEFORE_UPDATE), null);
   /**
    * @todo-javadoc Describe the field
    */
   private final JRadioButton _publicRadioButton = new JRadioButton(HibernatePlugin.PublicId);
   /**
    * @todo-javadoc Describe the field
    */
   private final JRadioButton _protectedRadioButton = new JRadioButton(HibernatePlugin.ProtectedId);
   /**
    * @todo-javadoc Describe the field
    */
   private final JRadioButton _privateRadioButton = new JRadioButton(HibernatePlugin.PrivateId);
   /**
    * @todo-javadoc Describe the field
    */
   private final JLabel _classScopeLabel = new JLabel(LocalStrings.getMsg(LocalStrings.CLASS_SCOPE));
   /**
    * @todo-javadoc Describe the field
    */
   private final JLabel _implementsLabel = new JLabel(LocalStrings.getMsg(LocalStrings.EXTERNAL_INTERFACE));
   /**
    * @todo-javadoc Describe the field
    */
   private final JTextField _sourceTextField = new JTextField();
   /**
    * @todo-javadoc Describe the column
    */
   private final DefaultComboBoxModel _implementsComboModel = new DefaultComboBoxModel();
   /**
    * @todo-javadoc Describe the column
    */
   private final JComboBox _implementsComboBox = new JComboBox(_implementsComboModel);
   /**
    * @todo-javadoc Describe the field
    */
   private final JComboBox _beanKeyGeneratorCombo = new JComboBox();
   /**
    * @todo-javadoc Describe the field
    */
   private final JTextField _beanKeyGeneratorField = new JTextField();
   /**
    * @todo-javadoc Describe the field
    */
   private final JLabel _batchSizeLabel = new JLabel(LocalStrings.getMsg(LocalStrings.BATCH_SIZE));
   /**
    * @todo-javadoc Describe the field
    */
   private final JTextField _batchSizeField = new JTextField();
   /**
    * @todo-javadoc Describe the field
    */
   private final JLabel _whereLabel = new JLabel(LocalStrings.getMsg(LocalStrings.WHERE));
   /**
    * @todo-javadoc Describe the field
    */
   private final JTextField _whereField = new JTextField();
   /**
    * @todo-javadoc Describe the field
    */
   private final BooleanNodeCheckBox _implementEqualsHashcode = new BooleanNodeCheckBox(LocalStrings.getMsg(LocalStrings.GENERATE_EQUALS_HASH), null);

   /**
    * @todo What is this used for - clean it up by removing it - maybe.
    * @todo-javadoc Describe the column
    */
   private final DocumentListener _identityClassListener =
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
            updateTable(_beanNameField.getText());
         }
      };

   /**
    * @todo-javadoc Describe the column
    */
   private final DocumentListener _schemaDocumentListener =
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
            _currentTable.setDatabaseSchema(_schemaNameField.getText());
         }
      };

   /**
    * @todo-javadoc Describe the column
    */
   private final DocumentListener _persisterDocumentListener =
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
            _currentTable.setPersister(_persisterField.getText());
         }
      };

   /**
    * @todo-javadoc Describe the column
    */
   private final DocumentListener _batchSizeDocumentListener =
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
            _currentTable.setBatchSize(_batchSizeField.getText());
         }
      };

   /**
    * @todo-javadoc Describe the column
    */
   private final DocumentListener _whereDocumentListener =
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
            _currentTable.setWhere(_whereField.getText());
         }
      };

   /**
    * @todo-javadoc Describe the column
    */
   private final DocumentListener _classDescriptionDocumentListener =
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
            _currentTable.setClassDescription(_classDescriptionField.getText());
         }
      };

   /**
    * @todo-javadoc Describe the column
    */
   private final DocumentListener _extendsDocumentListener =
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
            _currentTable.setExtends(_extendsField.getText());
         }
      };

   /**
    * @todo-javadoc Describe the column
    */
   private final DocumentListener _keyGeneratorArgListener =
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
            _currentTable.setKeyGeneratorArg(_beanKeyGeneratorField.getText());
         }
      };

   /**
    * @todo-javadoc Describe the field
    */
   private final ActionListener toggleBtnMutableAction =
      new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            _currentTable.setMutable(((BooleanNodeCheckBox)e.getSource()).isSelected());
         }
      };

   /**
    * @todo-javadoc Describe the field
    */
   private final ActionListener toggleBtnProxyAction =
      new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            _currentTable.setProxy(((BooleanNodeCheckBox)e.getSource()).isSelected());
         }
      };

   /**
    * @todo-javadoc Describe the field
    */
   private final ActionListener togglePlainCompoundKeyAction =
      new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            _currentTable.setPlainCompoundKey(((BooleanNodeCheckBox)e.getSource()).isSelected());
         }
      };

   /**
    * @todo-javadoc Describe the field
    */
   private final ActionListener toggleBtnDynamicUpdateAction =
      new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            _currentTable.setDynamicUpdate(((BooleanNodeCheckBox)e.getSource()).isSelected());
         }
      };

   /**
    * @todo-javadoc Describe the field
    */
   private final ActionListener toggleBtnDynamicInsertAction =
      new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            _currentTable.setDynamicInsert(((BooleanNodeCheckBox)e.getSource()).isSelected());
         }
      };

   /**
    * @todo-javadoc Describe the field
    */
   private final ActionListener toggleBtnSelectBeforeUpdateAction =
      new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            _currentTable.setSelectBeforeUpdate(((BooleanNodeCheckBox)e.getSource()).isSelected());
         }
      };

   /**
    * @todo-javadoc Describe the field
    */
   private final ActionListener toggleBtnLifecycleAction =
      new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            _currentTable.setImplementLifecycle(((BooleanNodeCheckBox)e.getSource()).isSelected());
         }
      };

   /**
    * @todo-javadoc Describe the field
    */
   private final ActionListener toggleBtnEqualsHashcodeAction =
      new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            _currentTable.setEqualsHashcode(((BooleanNodeCheckBox)e.getSource()).isSelected());
         }
      };

   /**
    * @todo-javadoc Describe the field
    */
   private final ActionListener toggleBtnValidatableAction =
      new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            _currentTable.setImplementValidatable(((BooleanNodeCheckBox)e.getSource()).isSelected());
         }
      };

   /**
    * @todo-javadoc Describe the field
    */
   private final ActionListener keyGeneratorComboAction =
      new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            _currentTable.setKeyGenerator((String)_beanKeyGeneratorCombo.getSelectedItem());
         }
      };

   /**
    * @todo-javadoc Describe the column
    */
   private HibernateTable _currentTable;


   /**
    * Build the GUI as presented to the user. The layout manager and the GUI
    * components are setup.
    *
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for constructor
    */
   public JHibernateTableSettingsPanel() {
      super();
      setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

      JPanel firstGroup = new JPanel();
      firstGroup.setLayout(new BoxLayout(firstGroup, BoxLayout.Y_AXIS));
      firstGroup.setBorder(BorderFactory.createTitledBorder(LocalStrings.getMsg(LocalStrings.MAPPING_ATTRIBUTE)));
      JPanel secondGroup = new JPanel();
      secondGroup.setLayout(new BoxLayout(secondGroup, BoxLayout.Y_AXIS));
      secondGroup.setBorder(BorderFactory.createTitledBorder(LocalStrings.getMsg(LocalStrings.CLASS_ATTRIBUTE)));
      JPanel firstSectionGroup1 = new JPanel();
      firstSectionGroup1.setLayout(new GridLayout(0, 2, 4, 2));
      JPanel secondSectionGroup1 = new JPanel();
      secondSectionGroup1.setLayout(new GridLayout(0, 3, 4, 2));
      JPanel firstSectionGroup2 = new JPanel();
      firstSectionGroup2.setLayout(new GridLayout(0, 2, 4, 2));
      JPanel secondSectionGroup2 = new JPanel();
      secondSectionGroup2.setLayout(new GridLayout(0, 2, 4, 2));
      JPanel thirdSectionGroup2 = new JPanel();
      thirdSectionGroup2.setLayout(new GridLayout(0, 2, 4, 2));
      JPanel fourthSectionGroup2 = new JPanel();
      fourthSectionGroup2.setLayout(new GridLayout(0, 3, 4, 2));

      firstSectionGroup1.add(_beanNameLabel);
      firstSectionGroup1.add(_beanNameField);
      firstSectionGroup1.add(_beanKeyGeneratorLabel);
      firstSectionGroup1.add(BuildKeyGeneratorPanel());
      firstSectionGroup1.add(_schemaNameLabel);
      firstSectionGroup1.add(_schemaNameField);
      firstSectionGroup1.add(_persisterLabel);
      firstSectionGroup1.add(_persisterField);
      firstSectionGroup1.add(BuildBatchSizePanel());
      firstSectionGroup1.add(BuildWherePanel());

      secondSectionGroup1.add(_pcKeyCheckBox);
      secondSectionGroup1.add(_proxyCheckBox);
      secondSectionGroup1.add(_mutableCheckBox);
      secondSectionGroup1.add(_dynamicUpdateCheckBox);
      secondSectionGroup1.add(_dynamicInsertCheckBox);
      secondSectionGroup1.add(_selectBeforeUpdateCheckBox);

      firstGroup.add(firstSectionGroup1);
      firstGroup.add(secondSectionGroup1);

      firstSectionGroup2.add(_classDescriptionLabel);
      firstSectionGroup2.add(_classDescriptionField);
      firstSectionGroup2.add(_extendsLabel);
      firstSectionGroup2.add(_extendsField);
      secondSectionGroup2.add(_classScopeLabel);
      secondSectionGroup2.add(BuildTableScopePanel());
      thirdSectionGroup2.add(_implementsLabel);
      thirdSectionGroup2.add(BuildImplementsPanel());
      fourthSectionGroup2.add(_implementLifecycleCheckBox);
      fourthSectionGroup2.add(_implementValidatableCheckBox);
      fourthSectionGroup2.add(_implementEqualsHashcode);

      secondGroup.add(firstSectionGroup2);
      secondGroup.add(secondSectionGroup2);
      secondGroup.add(thirdSectionGroup2);
      secondGroup.add(fourthSectionGroup2);

      add(firstGroup);
      add(secondGroup);

      // register listeners on text column and combo that will update borders
      _schemaNameField.getDocument().addDocumentListener(_schemaDocumentListener);
      _persisterField.getDocument().addDocumentListener(_persisterDocumentListener);
      _classDescriptionField.getDocument().addDocumentListener(_classDescriptionDocumentListener);
      _extendsField.getDocument().addDocumentListener(_extendsDocumentListener);
      _beanKeyGeneratorField.getDocument().addDocumentListener(_keyGeneratorArgListener);
      _batchSizeField.getDocument().addDocumentListener(_batchSizeDocumentListener);
      _whereField.getDocument().addDocumentListener(_whereDocumentListener);

      // register listeners for Check boxes and Combos
      _pcKeyCheckBox.addActionListener(togglePlainCompoundKeyAction);
      _mutableCheckBox.addActionListener(toggleBtnMutableAction);
      _proxyCheckBox.addActionListener(toggleBtnProxyAction);
      _dynamicUpdateCheckBox.addActionListener(toggleBtnDynamicUpdateAction);
      _dynamicInsertCheckBox.addActionListener(toggleBtnDynamicInsertAction);
      _selectBeforeUpdateCheckBox.addActionListener(toggleBtnSelectBeforeUpdateAction);
      _implementLifecycleCheckBox.addActionListener(toggleBtnLifecycleAction);
      _implementValidatableCheckBox.addActionListener(toggleBtnValidatableAction);
      _implementEqualsHashcode.addActionListener(toggleBtnEqualsHashcodeAction);
      _beanKeyGeneratorCombo.addActionListener(keyGeneratorComboAction);

      _beanNameField.setEditable(false);
   }


   /**
    * @param table The new Table value
    */
   public void setTable(Table table) {
      _currentTable = (HibernateTable)table;

      _implementEqualsHashcode.setBooleanNode(_currentTable.getEqualsHashcodeNode());
      _implementLifecycleCheckBox.setBooleanNode(_currentTable.getLifecycleNode());
      _implementValidatableCheckBox.setBooleanNode(_currentTable.getValidatableNode());
      _pcKeyCheckBox.setBooleanNode(_currentTable.getPlainCompoundKeyNode());
      _proxyCheckBox.setBooleanNode(_currentTable.getProxyNode());
      _mutableCheckBox.setBooleanNode(_currentTable.getMutableNode());
      _dynamicUpdateCheckBox.setBooleanNode(_currentTable.getDynamicUpdateNode());
      _dynamicInsertCheckBox.setBooleanNode(_currentTable.getDynamicInsertNode());
      _selectBeforeUpdateCheckBox.setBooleanNode(_currentTable.getSelectBeforeUpdateNode());
      _beanNameField.setText(_currentTable.getDestinationClassName());
      _schemaNameField.setText(_currentTable.getDatabaseSchema());
      _persisterField.setText(_currentTable.getPersister());
      _batchSizeField.setText(_currentTable.getBatchSize());
      _whereField.setText(_currentTable.getWhere());
      _classDescriptionField.setText(_currentTable.getClassDescription());
      _extendsField.setText(_currentTable.getExtends());
      setKeyGenerator();
      setClassScope();
      setMultipleImplements();
   }


   /**
    * Gets the PreferredSize attribute of the JTableSettingsPanel object
    *
    * @return The PreferredSize value
    */
   public Dimension getPreferredSize() {
      return getMinimumSize();
   }


   /**
    * Sets the KeyGenerator attributes of the JHibernateTableSettingsPanel GUI
    * object
    */
   private void setKeyGenerator() {
      final String aKeyGenItem[] = {"assigned", "native", "sequence", "identity", "increment", "hilo", "seqhilo", "uuid.hex", "uuid.string", "foreign"};
      final String cKeyGenItem[] = {"assigned"};

      DefaultComboBoxModel cModel = null;
      if (_currentTable.isCompositeKey()) {
         cModel = new DefaultComboBoxModel(cKeyGenItem);
      }
      else {
         cModel = new DefaultComboBoxModel(aKeyGenItem);
      }
      String keyGeneratorValue = _currentTable.getKeyGenerator();
      _beanKeyGeneratorCombo.setModel(cModel);
      _beanKeyGeneratorField.setEditable(!_currentTable.isCompositeKey());
      _beanKeyGeneratorField.setText(_currentTable.getKeyGeneratorArg());
      _beanKeyGeneratorCombo.setSelectedItem(keyGeneratorValue);
   }


   /**
    * Sets the ClassScope attribute of the JHibernateTableSettingsPanel GUI
    * object
    */
   private void setClassScope() {
      _publicRadioButton.setSelected(_currentTable.getClassScope().equals(HibernatePlugin.PublicId));
      _protectedRadioButton.setSelected(_currentTable.getClassScope().equals(HibernatePlugin.ProtectedId));
      _privateRadioButton.setSelected(_currentTable.getClassScope().equals(HibernatePlugin.PrivateId));
   }


   /**
    * Sets the multiple interface attributes of the JHibernateTableSettingsPanel
    * GUI object
    */
   private void setMultipleImplements() {
      _implementsComboBox.removeAllItems();
      ArrayList aList = _currentTable.getImplements();
      for (int i = 0; i < aList.size(); i++) {
         _implementsComboBox.addItem(aList.get(i));
      }
   }


   /**
    * Build the class scope section of the GUI. This is a sub layout of the
    * primary interface.
    *
    * @todo-javadoc Write javadocs for return value
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for constructor
    * @return Describe the return value
    */
   private JPanel BuildTableScopePanel() {
      JPanel spanel = new JPanel();
      spanel.setLayout(new FlowLayout());
      ButtonGroup group = new ButtonGroup();
      group.add(_publicRadioButton);
      group.add(_protectedRadioButton);
      group.add(_privateRadioButton);
      ActionListener radioBtnAction =
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               _currentTable.setClassScope(e.getActionCommand());
            }
         };
      _publicRadioButton.setActionCommand(HibernatePlugin.PublicId);
      _publicRadioButton.addActionListener(radioBtnAction);
      _protectedRadioButton.addActionListener(radioBtnAction);
      _protectedRadioButton.setActionCommand(HibernatePlugin.ProtectedId);
      _privateRadioButton.addActionListener(radioBtnAction);
      _privateRadioButton.setActionCommand(HibernatePlugin.PrivateId);
      spanel.add(_publicRadioButton);
      spanel.add(_protectedRadioButton);
      spanel.add(_privateRadioButton);
      return spanel;
   }


   /**
    * Build the implements section of the GUI. This is a sub layout of the
    * primary interface.
    *
    * @todo-javadoc Write javadocs for return value
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for constructor
    * @return Describe the return value
    */
   private JPanel BuildImplementsPanel() {
      JPanel ipanel = new JPanel();
      ipanel.setLayout(new BoxLayout(ipanel, BoxLayout.Y_AXIS));
      JButton addButton = new JButton(LocalStrings.getMsg(LocalStrings.ADD));
      JButton removeButton = new JButton(LocalStrings.getMsg(LocalStrings.REMOVE));
      ActionListener addBtnAction =
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               String txt = _sourceTextField.getText().trim();
               if (txt.length() > 0) {
                  _implementsComboBox.addItem(txt);
                  _sourceTextField.setText("");
                  ArrayList aList = _currentTable.getImplements();
                  aList.add(txt);
                  _currentTable.setImplements(aList);
               }
            }
         };
      ActionListener removeBtnAction =
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               int idx = _implementsComboBox.getSelectedIndex();
               if (idx >= 0) {
                  _implementsComboBox.removeItemAt(idx);
                  ArrayList aList = _currentTable.getImplements();
                  aList.remove(idx);
                  _currentTable.setImplements(aList);
               }
            }
         };
      addButton.addActionListener(addBtnAction);
      removeButton.addActionListener(removeBtnAction);
      Dimension btnSize = new Dimension(80, 20);
      addButton.setPreferredSize(btnSize);
      removeButton.setPreferredSize(btnSize);
      Box firstRow = Box.createHorizontalBox();
      Box secondRow = Box.createHorizontalBox();
      firstRow.add(_sourceTextField);
      firstRow.add(addButton);
      secondRow.add(_implementsComboBox);
      secondRow.add(removeButton);
      ipanel.add(firstRow);
      ipanel.add(secondRow);
      return ipanel;
   }


   /**
    * Build the Batch Size section of the GUI. This is a sub layout of the
    * primary interface.
    *
    * @todo-javadoc Write javadocs for return value
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for constructor
    * @return Describe the return value
    */
   private JPanel BuildBatchSizePanel() {
      JPanel ipanel = new JPanel();
      ipanel.setLayout(new BoxLayout(ipanel, BoxLayout.Y_AXIS));
      Box firstRow = Box.createHorizontalBox();
      firstRow.add(_batchSizeLabel);
      firstRow.add(_batchSizeField);
      firstRow.add(_whereLabel);
      ipanel.add(firstRow);
      return ipanel;
   }


   /**
    * Build the Batch Size section of the GUI. This is a sub layout of the
    * primary interface. <br/>
    * Note: Moved label to the BatchSize panel.
    *
    * @todo-javadoc Write javadocs for return value
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for constructor
    * @return Describe the return value
    */
   private JPanel BuildWherePanel() {
      JPanel ipanel = new JPanel();
      ipanel.setLayout(new BoxLayout(ipanel, BoxLayout.Y_AXIS));
      Box firstRow = Box.createHorizontalBox();
      firstRow.add(_whereField);
      ipanel.add(firstRow);
      return ipanel;
   }


   /**
    * Build the key generator section of the GUI. This is a sub layout of the
    * primary interface.
    *
    * @todo-javadoc Write javadocs for return value
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for constructor
    * @return Describe the return value
    */
   private JPanel BuildKeyGeneratorPanel() {
      JPanel ipanel = new JPanel();
      ipanel.setLayout(new BoxLayout(ipanel, BoxLayout.Y_AXIS));
      _beanKeyGeneratorCombo.setEditable(false);
      Dimension btnSize = new Dimension(90, 20);
      _beanKeyGeneratorCombo.setPreferredSize(btnSize);
      Box firstRow = Box.createHorizontalBox();
      firstRow.add(_beanKeyGeneratorCombo);
      firstRow.add(_beanKeyGeneratorField);
      ipanel.add(firstRow);
      return ipanel;
   }


   /**
    * Describe what the method does
    *
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for method
    * @param beanName Describe what the parameter does
    */
   private void updateTable(String beanName) {
      _currentTable.setDestinationClassName(beanName);
   }

}
