package com.klwork.explorer.ui.form;

import java.util.Map;

import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Event;

/**
 * Event indicating a form has been submitted or cancelled. When submitted,
 * the values of the form-properties are available.
 * 
 * @author Frederik Heremans
 */
public class FormPropertiesEvent extends Event {

  private static final long serialVersionUID = -410814526942034125L;
  
  public static final String TYPE_SUBMIT = "SUBMIT";
  public static final String TYPE_CANCEL = "CANCEL";
  
  private String type;
  private Map<String, String> formProperties;
  
  public FormPropertiesEvent(Component source, String type) {
    super(source);
    this.type = type;
  }
  
  public FormPropertiesEvent(Component source, String type, Map<String, String> formProperties) {
    this(source, type);
    this.formProperties = formProperties;
  }
  
  public String getType() {
    return type;
  }
  
  public Map<String, String> getFormProperties() {
    return formProperties;
  }
}
