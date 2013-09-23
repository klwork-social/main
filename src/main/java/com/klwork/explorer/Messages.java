/* Licensed under the Apache License, Version 2_0 (the "License");
 * you may not use this file except in compliance with the License_
 * You may obtain a copy of the License at
 * 
 *      http://www_apache_org/licenses/LICENSE-2_0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied_
 * See the License for the specific language governing permissions and
 * limitations under the License_
 */
package com.klwork.explorer;



/**
 * @author Joram Barrez
 */
public interface Messages {
  
  // General
  String APP_TITLE = "app_title";
  
  String CONFIRMATION_DIALOG_DEFAULT_TITLE = "confirmation_dialog_default_title";
  String CONFIRMATION_DIALOG_YES = "confirmation_dialog_yes";
  String CONFIRMATION_DIALOG_NO = "confirmation_dialog_no";
  String BUTTON_OK = "button_ok";
  String BUTTON_CREATE = "button_create";
  String BUTTON_CANCEL = "button_cancel";
  String BUTTON_SAVE = "button_save";
  String BUTTON_DELETE = "button_delete";
  String UNCAUGHT_EXCEPTION = "uncaught_exception";
  String SELECT_DEFAULT = "select_default";
  // Navigation
  String NAVIGATION_ERROR_NOT_INVOLVED_TITLE = "navigation_error_not_involved_title";
  String NAVIGATION_ERROR_NOT_INVOLVED = "navigation_error_not_involved";
  
  // Login
  String LOGIN_USERNAME = "login_username";
  String LOGIN_PASSWORD = "login_password";
  String LOGIN_BUTTON = "login_button";
  String LOGIN_FAILED_HEADER = "login_failed_header";
  String LOGIN_FAILED_INVALID = "login_failed_invalid";
  
  // Header
  String HEADER_SEARCHBOX = "header_searchbox";
  String HEADER_LOGOUT = "header_logout";
  
  // Footer
  String FOOTER_MESSAGE = "footer_message";

  // Main menu
  String MAIN_MENU_TASKS = "main_menu_tasks";
  String MAIN_MENU_PROCESS = "main_menu_process";
  String MAIN_MENU_MANAGEMENT = "main_menu_management";
  String MAIN_MENU_REPORTS = "main_menu_reports";
  String MAIN_MENU_PUBLIC_PROJECT = "main_menu_public_project";
  String MAIN_MENU_PUBLIC_TASK = "main_menu_public_task";
  String MAIN_MENU_TODOLIST = "main_menu_todolist";
  String MAIN_MENU_FEEDBACK = "main_menu_feedback";
  String MAIN_MENU_MY_PROJECT = "main_menu_my_project";
  
  
  //expand menu
  String EXPAND_MENU_INVITE = "expand_menu_invite";
  String EXPAND_MENU_MY_SCHEDULE = "expand_menu_my_schedule";//我的日程
  
  // Password
  String PASSWORD_CHANGE = "password_change";
  String PASSWORD_CHANGE_INPUT_REQUIRED = "password_change_input_required";
  String PASSWORD_CHANGE_INPUT_MATCH = "password_change_input_match";
  String PASSWORD_CHANGED_NOTIFICATION = "password_changed_notification";
  
  // Forms
  String FORM_USER_NO_USER_SELECTED = "form_user_no_user_selected";
  String FORM_USER_SELECT = "form_user_select";
  String FORM_FIELD_REQUIRED = "form_field_required";
  
  // Profile
  String PROFILE_ABOUT = "profile_about";
  String PROFILE_NAME = "profile_name";
  String PROFILE_FIRST_NAME = "profile_firstname";
  String PROFILE_LAST_NAME = "profile_lastname";
  String PROFILE_JOBTITLE = "profile_jobtitle";
  String PROFILE_BIRTHDATE = "profile_birthdate";
  String PROFILE_LOCATION = "profile_location";
  String PROFILE_CONTACT = "profile_contact";
  String PROFILE_EMAIL = "profile_email";
  String PROFILE_PHONE = "profile_phone";;
  String PROFILE_TWITTER = "profile_twitter";
  String PROFILE_SKYPE = "profile_skype";
  String PROFILE_ACCOUNTS = "profile_accounts";
  String PROFILE_SHOW = "profile_show";
  String PROFILE_EDIT = "profile_edit";
  String PROFILE_SAVE = "profile_save";
  String PROFILE_CHANGE_PICTURE = "profile_change_picture";
  String PROFILE_ACCOUNT_USER_NAME = "profile_account_username";
  String PROFILE_ACCOUNT_PASWORD = "profile_account_password";
  String PROFILE_DELETE_ACCOUNT_TITLE = "profile_delete_account_title";
  String PROFILE_DELETE_ACCOUNT_DESCRIPTION = "profile_delete_account_description";
  String PROFILE_ADD_ACCOUNT = "profile_add_account";
  String PROFILE_ACCOUNT_IMAP_DESCRIPTION = "profile_account_imap_description";
  String PROFILE_ACCOUNT_IMAP = "profile_account_imap";
  String PROFILE_ACCOUNT_ALFRESCO = "profile_account_alfresco";
  String PROFILE_NEW_PASSWORD = "profile_new_password";
  String PROFILE_CONFIRM_PASSWORD = "profile_confirm_password";
  
  // Imap
  String IMAP_SERVER = "imap_server";
  String IMAP_PORT = "imap_port";
  String IMAP_SSL = "imap_ssl";
  String IMAP_USERNAME = "imap_username";
  String IMAP_PASSWORD = "imap_password";
  String IMAP_DESCRIPTION = "imap_description";
  
  //Alfresco
  String ALFRESCO_SERVER = "alfresco_server";
  String ALFRESCO_USERNAME = "alfresco_username";
  String ALFRESCO_PASSWORD = "alfresco_password";
  String ALFRESCO_DESCRIPTION = "alfresco_description";
  
  // Case
  String TASK_CREATE_NEW = "task_create_new";
  String TASK_NEW = "task_new";
  String TASK_NAME_REQUIRED = "task_name_required";

  // Task menu
  String TASK_MENU_TASKS = "task_menu_tasks";
  String TASK_MENU_INBOX = "task_menu_inbox";
  String TASK_MENU_QUEUED = "task_menu_queued";
  String TASK_MENU_INVOLVED = "task_menu_involved";
  String TASK_MENU_ARCHIVED = "task_menu_archived";
  
  // Task details
  String TASK_ID = "task_id";
  String TASK_NAME = "task_name";
  String TASK_DESCRIPTION = "task_description";
  String TASK_NO_DESCRIPTION = "task_no_description";
  String TASK_OWNER = "task_owner";
  String TASK_OWNER_TRANSFER = "task_owner_transfer";
  String TASK_NO_OWNER = "task_no_owner";
  String TASK_ASSIGNEE = "task_assignee";
  String TASK_NO_ASSIGNEE = "task_no_assignee";
  String TASK_ASSIGNEE_REASSIGN = "task_assignee_reassign";
  String TASK_INVOLVED_REMOVE = "task_involved_remove";
  String TASK_INVOLVED_REMOVE_CONFIRMATION_TITLE = "task_involved_remove_confirmation_title";
  String TASK_INVOLVED_REMOVE_CONFIRMATION_DESCRIPTION = "task_involved_remove_confirmation_description";
  String TASK_CREATED_SHORT = "task_created_short";
  String TASK_DUEDATE = "task_duedate";
  String TASK_DUEDATE_UNKNOWN = "task_duedate_unknown";
  String TASK_DUEDATE_SHORT = "task_duedate_short";
  String TASK_COMPLETE = "task_complete";
  String TASK_COMPLETED = "task_task_completed";
  String TASK_RESET_FORM = "task_form_reset";
  String TASK_ADD_COMMENT = "task_comment_add";
  String TASK_COMMENT_POPUP_HEADER = "task_comment_popup_header";
  String TASK_CREATE_TIME = "task_create_time";
  String TASK_COMPLETE_TIME = "task_complete_time";
  String TASK_DURATION = "task_duration";
  String TASK_PRIORITY = "task_priority";
  String TASK_PRIORITY_LOW = "task_priority_low";
  String TASK_PRIORITY_MEDIUM = "task_priority_medium";
  String TASK_PRIORITY_HIGH = "task_priority_high";
  String TASK_NOT_FINISHED_YET = "task_not_finished_yet";
  String TASK_PART_OF_PROCESS = "task_part_of_process";
  String TASK_SUBTASK_OF_PARENT_TASK = "task_subtask_of_parent_task";
  String TASK_JUMP_TO_PROCESS = "task_jump_to_process";
  String TASK_CLAIM_FAILED = "task_claim_failed";
  String TASK_CLAIM_SUCCESS = "task_claim_success";
  String TASK_CLAIM = "task_claim";
  String TASK_RELATED_CONTENT = "task_related_content";
  String TASK_NO_RELATED_CONTENT = "task_no_related_content";
  String TASK_PEOPLE = "task_people";
  String TASK_FORM_HELP = "task_form_help";
  String TASK_FORM_UPLOAD_HELP = "task_form_upload_help";
  String TASK_FORM_GRADE_HELP = "task_form_grade_help";
  
  String TASK_FORM_HISTORY_HELP = "task_form_history_help";
  
  String TASK_SUBTASKS = "task_subtasks";
  String TASK_NO_SUBTASKS = "task_no_subtasks";
  String TASK_CONFIRM_DELETE_SUBTASK = "task_confirm_delete_subtask";
  
  // Task roles
  String TASK_ROLE_CONTRIBUTOR = "task_role_contributor";
  String TASK_ROLE_MANAGER = "task_role_manager";
  String TASK_ROLE_SPONSOR = "task_role_sponsor";
  String TASK_ROLE_IMPLEMENTER = "task_role_implementer";
        
  // Events
  String EVENT_ADD_USER_LINK = "event_add_user_link";
  String EVENT_DELETE_USER_LINK = "event_delete_user_link";
  String EVENT_ADD_GROUP_LINK = "event_add_group_link";
  String EVENT_DELETE_GROUP_LINK = "event_delete_group_link";
  String EVENT_ADD_ATTACHMENT = "event_add_attachment";
  String EVENT_DELETE_ATTACHMENT = "event_delete_attachment";
  String EVENT_COMMENT = "event_comment";
  String EVENT_DEFAULT = "event_default";
  String EVENT_TITLE = "event_title";
  
  String EVENT_COMPLETE_TASK = "event_complete_task";
  
  // Process menu
  String PROCESS_MENU_MY_INSTANCES = "process_menu_my_instances";
  String PROCESS_MENU_DEPLOYED_DEFINITIONS = "process_menu_deployed_definitions";
  String PROCESS_MENU_EDITOR_DEFINITIONS = "process_menu_editor_definitions";
  String PROCESS_MENU_INSTANCES = "process_menu_instances";
  
  // Process page
  String PROCESS_CATEGORY = "process_category";
  String PROCESS_VERSION = "process_version"; 
  String PROCESS_DEPLOY_TIME = "process_deploy_time";
  String PROCESS_HEADER_DIAGRAM = "process_header_diagram";
  String PROCESS_NO_DIAGRAM = "process_no_diagram";
  String PROCESS_HEADER_SUSPENSION_STATE = "process_header_suspension_state";
  String PROCESS_SCHEDULED_SUSPEND = "process_scheduled_suspend";
  String PROCESS_SCHEDULED_ACTIVATE = "process_scheduled_activate";
  String PROCESS_START = "process_start";
  String PROCESS_EDIT = "process_edit";
  String PROCESS_COPY = "process_copy";
  String PROCESS_NEW = "process_new";
  String PROCESS_IMPORT = "process_import";
  String PROCESS_DELETE = "process_delete";
  String PROCESS_DEPLOY = "process_deploy";
  String PROCESS_ACTIVATE = "process_activate";
  String PROCESS_ACTIVATE_POPUP = "process_activate_popup";
  String PROCESS_ACTIVATE_POPUP_TIME_DESCRIPTION = "process_activate_popup_time_description";
  String PROCESS_ACTIVATE_POPUP_INCLUDE_PROCESS_INSTANCES_DESCRIPTION = "process_activate_popup_process_instances_description";
  String PROCESS_SUSPEND = "process_suspend";
  String PROCESS_SUSPEND_POPUP = "process_suspend_popup";
  String PROCESS_SUSPEND_POPUP_TIME_DESCRIPTION = "process_suspend_popup_time_description";
  String PROCESS_SUSPEND_POPUP_TIME_NOW = "process_suspend_popup_time_now";
  String PROCESS_SUSPEND_POPUP_TIME_DATE = "process_suspend_popup_time_date";
  String PROCESS_SUSPEND_POPUP_INCLUDE_PROCESS_INSTANCES_DESCRIPTION = "process_suspend_popup_process_instances_description";
  String PROCESS_TOXML_FAILED = "process_toxml_failed";
  String PROCESS_CONVERT = "process_convert";
  String PROCESS_EXPORT = "process_export";
  String PROCESS_EDITOR_CHOICE = "process_editor_choice";
  String PROCESS_EDITOR_MODELER = "process_editor_modeler";
  String PROCESS_EDITOR_MODELER_DESCRIPTION = "process_editor_modeler_description";
  String PROCESS_EDITOR_CONVERSION_WARNING_MODELER = "process_editor_conversion_warning_modeler";
  String PROCESS_EDITOR_TABLE = "process_editor_table";
  String PROCESS_EDITOR_TABLE_DESCRIPTION = "process_editor_table_description";
  String PROCESS_EDITOR_CREATE_NEW = "process_editor_create_new";
  String PROCESS_EDITOR_CREATE_NEW_DEFAULT = "process_editor_create_new_default";
  String PROCESS_EDITOR_TITLE = "process_editor_title";
  String PROCESS_EDITOR_BPMN_PREVIEW = "process_editor_bpmn_preview";
  String PROCESS_EDITOR_SAVE = "process_editor_save";
  String PROCESS_EDITOR_NAME = "process_editor_name";
  String PROCESS_EDITOR_DESCRIPTION = "process_editor_description";
  String PROCESS_EDITOR_TASKS = "process_editor_tasks";
  String PROCESS_EDITOR_TASK_NAME = "process_editor_task_name";
  String PROCESS_EDITOR_TASK_ASSIGNEE = "process_editor_task_assignee";
  String PROCESS_EDITOR_TASK_GROUPS = "process_editor_task_groups";
  String PROCESS_EDITOR_TASK_DESCRIPTION = "process_editor_task_description";
  String PROCESS_EDITOR_TASK_CONCURRENCY = "process_editor_task_concurrency";
  String PROCESS_EDITOR_TASK_START_WITH_PREVIOUS = "process_editor_task_startwithprevious";
  String PROCESS_EDITOR_TASK_FORM_CREATE = "process_editor_task_form_create";
  String PROCESS_EDITOR_TASK_FORM_EDIT = "process_editor_task_form_edit";
  String PROCESS_EDITOR_ACTIONS = "process_editor_actions";
  String PROCESS_EDITOR_PROPERTY_NAME = "process_editor_property_name";
  String PROCESS_EDITOR_PROPERTY_TYPE = "process_editor_property_type";
  String PROCESS_EDITOR_PROPERTY_REQUIRED = "process_editor_property_required";
  String PROCESS_EDITOR_PROPERTY_TYPE_TEXT = "process_editor_property_type_text";
  String PROCESS_EDITOR_PROPERTY_TYPE_NUMBER = "process_editor_property_type_number";
  String PROCESS_EDITOR_PROPERTY_TYPE_DATE = "process_editor_property_type_date";
  String PROCESS_EDITOR_LOADING_ERROR = "process_editor_loading_error";
  
  String PROCESS_INSTANCE_DELETE = "process_instance_delete";
  String PROCESS_INSTANCE_DELETE_POPUP_TITLE = "process_instance_delete_popup_title";
  String PROCESS_INSTANCE_DELETE_POPUP_DESCRIPTION = "process_instance_delete_popup_description";
  String PROCESS_START_TIME = "process_start_time";
  String PROCESS_STARTED_NOTIFICATION = "process_started_notification";
  String PROCESS_INSTANCE_STARTED_ON = "process_instance_started_on";
  String PROCESS_INSTANCE_STARTED = "process_instance_started";
  String PROCESS_INSTANCE_HEADER_TASKS = "process_instance_header_tasks";
  String PROCESS_INSTANCE_NO_TASKS = "process_instance_no_tasks";
  String PROCESS_INSTANCE_HEADER_VARIABLES = "process_instance_header_variables";
  String PROCESS_INSTANCE_NO_VARIABLES = "process_instance_no_variables";
  String PROCESS_INSTANCES = "process_instances";
  String PROCESS_NO_INSTANCES = "process_no_instances";
  String PROCESS_ACTION_VIEW = "process_action_view";
  String PROCESS_INSTANCE_ID = "process_instance_id";
  String PROCESS_INSTANCE_NAME = "process_instance_name";
  String PROCESS_INSTANCE_BUSINESSKEY = "process_instance_businesskey";
  String PROCESS_INSTANCE_ACTIONS = "process_instance_actions";
  String PROCESS_INSTANCE_VARIABLE_NAME = "process_instance_variable_name";
  String PROCESS_INSTANCE_VARIABLE_VALUE = "process_instance_variable_value";
  String PROCESS_CONVERT_POPUP_CAPTION = "process_convert_popup_caption";
  String PROCESS_CONVERT_POPUP_MESSAGE = "process_convert_popup_message";
  String PROCESS_CONVERT_POPUP_CONVERT_BUTTON = "process_convert_popup_convert_button";
  String PROCESS_NEW_POPUP_CAPTION = "process_new_popup_caption";
  String PROCESS_NEW_POPUP_CREATE_BUTTON = "process_new_popup_create_button";
  String PROCESS_COPY_POPUP_CAPTION = "process_copy_popup_caption";
  String PROCESS_DELETE_POPUP_CAPTION = "process_delete_popup_caption";
  String PROCESS_DELETE_POPUP_MESSAGE = "process_delete_popup_message";
  String PROCESS_DELETE_POPUP_DELETE_BUTTON = "process_delete_popup_delete_button";
  
  // Reporting menu
  String REPORTING_MENU_RUN_REPORTS = "reporting_menu_run_reports";
  String REPORTING_MENU_SAVED_REPORTS = "reporting_menu_saved_reports";
  String REPORTING_SAVE_POPUP_CAPTION = "reporting_save_popup_caption";
  String REPORTING_ERROR_NOT_ENOUGH_DATA = "reporting_error_not_enough_data";
  String REPORTING_SAVE_POPUP_NAME = "reporting_save_popup_name";
  String REPORTING_SAVE_POPUP_NAME_EMPTY = "reporting_save_popup_name_empty";
  String REPORTING_SAVE_POPUP_NAME_EXISTS = "reporting_save_popup_name_exists";
  String REPORTING_SAVE_POPUP_NAME_TOO_LONG = "reporting_save_popup_name_too_long";
  String REPORTING_CREATE_TIME = "reporting_report_created";
  
  // Management menu
  String MGMT_MENU_DATABASE = "management_menu_database";
  String MGMT_MENU_DEPLOYMENTS = "management_menu_deployments";
  String MGMT_MENU_ACTIVE_PROCESS_DEFINITIONS = "management_menu_active_processdefinitions";
  String MGMT_MENU_SUSPENDED_PROCESS_DEFINITIONS = "management_menu_suspended_processdefinitions";
  String MGMT_MENU_JOBS = "management_menu_jobs";
  String MGMT_MENU_DEPLOYMENTS_SHOW_ALL = "management_menu_deployments_show_all";
  String MGMT_MENU_DEPLOYMENTS_UPLOAD = "management_menu_deployments_upload";
  String MGMT_MENU_USERS = "management_menu_users";
  String MGMT_MENU_GROUPS = "management_menu_groups";
  String MGMT_MENU_ADMINISTRATION = "management_menu_admin";
  
  // Job page
  String JOB_EXECUTE = "job_execute";
  String JOB_DELETE = "job_delete";
  String JOB_DELETED = "job_deleted";
  String JOB_HEADER_EXECUTION = "job_header_execution";
  String JOB_RETRIES = "job_retries";
  String JOB_NO_RETRIES = "job_no_retries";
  String JOB_DEFAULT_NAME = "job_default_name";
  String JOB_TIMER = "job_timer";
  String JOB_MESSAGE = "job_message";
  String JOB_DUEDATE = "job_duedate";
  String JOB_NO_DUEDATE = "job_no_dudedate";
  String JOB_ERROR = "job_error";
  String JOB_NOT_EXECUTED = "job_not_executed";
  String JOB_SUSPEND_PROCESSDEFINITION = "job_suspend_processdefinition";
  String JOB_ACTIVATE_PROCESSDEFINITION = "job_activate_processdefinition";
  
  // Deployment page
  String DEPLOYMENT_DELETE = "deployment_delete";
  String DEPLOYMENT_CREATE_TIME = "deployment_create_time";
  String DEPLOYMENT_HEADER_DEFINITIONS = "deployment_header_definitions";
  String DEPLOYMENT_HEADER_RESOURCES = "deployment_header_resources";
  String DEPLOYMENT_UPLOAD = "deployment_upload";
  String DEPLOYMENT_UPLOAD_DESCRIPTION = "deployment_upload_description";
  String DEPLOYMENT_UPLOAD_FAILED = "deployment_upload_failed";
  String DEPLOYMENT_UPLOAD_INVALID_FILE = "deployment_upload_invalid_file";
  String DEPLOYMENT_UPLOAD_INVALID_FILE_EXPLANATION = "deployment_upload_invalid_file_explanation";
  String DEPLOYMENT_UPLOAD_SERVER_ERROR = "deployment_upload_server_error";
  String DEPLOYMENT_DEPLOY_TIME = "deployment_deploy_time";
  String DEPLOYMENT_NO_NAME = "deployment_no_name";
  String DEPLOYMENT_NO_INSTANCES = "deployment_no_instances";
  String DEPLOYMENT_DELETE_POPUP_CAPTION = "deployment_delete_popup_caption";
  String DEPLOYMENT_DELETE_POPUP_WARNING = "deployment_delete_popup_warning";
  String DEPLOYMENT_DELETE_POPUP_DELETE_BUTTON = "deployment_delete_popup_delete_button";
  
  // Import to model workspace
  String MODEL_IMPORT = "model_import";
  String MODEL_IMPORT_DESCRIPTION = "model_import_description";
  String MODEL_IMPORT_FAILED = "model_import_failed";
  String MODEL_IMPORT_INVALID_FILE = "model_import_invalid_file";
  String MODEL_IMPORT_INVALID_FILE_EXPLANATION = "model_import_invalid_file_explanation";
  String MODEL_IMPORT_INVALID_BPMNDI = "model_import_invalid_bpmndi";
  String MODEL_IMPORT_INVALID_BPMNDI_EXPLANATION = "model_import_invalid_bpmndi_explanation";
  String MODEL_IMPORT_INVALID_BPMN_EXPLANATION = "model_import_invalid_bpmn_explanation";
  
  String MODEL_ACTION = "model_action";
  
  // Database page
  String DATABASE_NO_ROWS = "database_no_rows";
  
  // User page
  String USER_HEADER_DETAILS = "user_header_details";
  String USER_HEADER_GROUPS = "user_header_groups";
  String USER_ID = "user_id";
  String USER_ID_REQUIRED = "user_id_required";
  String USER_ID_UNIQUE = "user_id_unique";
  String USER_FIRSTNAME = "user_firstname";
  String USER_LASTNAME = "user_lastname";
  String USER_EMAIL = "user_email";
  String USER_PASSWORD = "user_password";
  String USER_PASSWORD_REQUIRED = "user_password_required";
  String USER_PASSWORD_MIN_LENGTH = "user_password_min_lenth";
  String USER_RESET_PASSWORD = "user_reset_password";
  String USER_CREATE = "user_create";
  String USER_EDIT = "user_edit";
  String USER_DELETE = "user_delete";
  String USER_SAVE = "user_save";
  String USER_NO_PICTURE = "user_no_picture";
  String USER_NO_GROUPS = "user_no_groups";
  String USER_CONFIRM_DELETE = "user_confirm_delete";
  String USER_CONFIRM_DELETE_GROUP = "user_confirm_delete_group";
  String USER_SELECT_GROUPS = "user_select_groups";
  String USER_SELECT_GROUPS_POPUP = "user_select_groups_popup";
  
  // Group page
  String GROUP_HEADER_DETAILS = "group_header_details";
  String GROUP_HEADER_USERS = "group_header_users";
  String GROUP_CREATE = "group_create";
  String GROUP_ID = "group_id";
  String GROUP_NAME = "group_name";
  String GROUP_TYPE = "group_type";
  String GROUP_CONFIRM_DELETE = "group_confirm_delete";
  String GROUP_ID_REQUIRED = "group_id_required";
  String GROUP_ID_UNIQUE = "group_id_unique";
  String GROUP_NO_MEMBERS = "group_no_members";
  String GROUP_SELECT_MEMBERS = "group_select_members";
  String GROUP_DELETE = "group_delete";
  
  // Running process instances page
  String ADMIN_MENU_RUNNING = "admin_menu_running";
  String ADMIN_MENU_COMPLETED = "admin_menu_completed";
  String ADMIN_MENU_DATABASE = "admin_menu_database";
  String ADMIN_RUNNING_TITLE = "admin_running_title";
  String ADMIN_RUNNING_NONE_FOUND = "admin_running_none_found";
  String ADMIN_COMPLETED_TITLE = "admin_completed_title";
  String ADMIN_COMPLETED_NONE_FOUND = "admin_completed_none_found";
  String ADMIN_DEFINITIONS = "admin_definitions";
  String ADMIN_NR_INSTANCES = "admin_nr_instances";
  String ADMIN_STARTED_BY = "admin_started_by";
  String ADMIN_START_ACTIVITY = "admin_start_activity";
  String ADMIN_FINISHED = "admin_finished";
  
  // Database settings page
  String DATABASE_TITLE = "database_title";
  String DATABASE_TYPE = "database_type";
  String DATABASE_UPDATE = "database_update";
  String DATABASE_CONFIG_TYPE = "database_config_type";
  String DATABASE_JNDI = "database_jndi";
  String DATABASE_DATASOURCE_CLASS = "database_datasource_class";
  String DATABASE_DATASOURCE_URL = "database_datasource_url";
  String DATABASE_JDBC_URL = "database_jdbc_url";
  
  // Upload
  String UPLOAD_SELECT = "upload_select";
  String UPLOAD_DROP = "upload_drop";
  String UPLOAD_FAILED = "upload_failed";
  String UPLOAD_LIMIT = "upload_limit";
  String UPLOAD_INVALID_MIMETYPE = "upload_invalid_mimetype";

  // Related content
  String RELATED_CONTENT_ADD = "related_content_add";
  String RELATED_CONTENT_NAME = "related_content_name";
  String RELATED_CONTENT_NAME_REQUIRED = "related_content_name_required";
  String RELATED_CONTENT_DESCRIPTION = "related_content_description";
  String RELATED_CONTENT_CREATE = "related_content_create";
  
  String RELATED_CONTENT_TYPE_URL = "related_content_type_url";
  String RELATED_CONTENT_TYPE_URL_URL = "related_content_type_url_url";
  String RELATED_CONTENT_TYPE_URL_URL_REQUIRED = "related_content_type_url_url_required";;
  String RELATED_CONTENT_TYPE_URL_HELP = "related_content_type_url_help";
  
  String RELATED_CONTENT_TYPE_FILE = "related_content_type_file";
  String RELATED_CONTENT_TYPE_FILE_HELP = "related_content_type_file_help";
  String RELATED_CONTENT_TYPE_FILE_UPLOADED = "related_content_type_file_uploaded";
  String RELATED_CONTENT_TYPE_FILE_REQUIRED = "related_content_type_file_required";
  String RELATED_CONTENT_CONFIRM_DELETE = "related_content_confirm_delete";
  String RELATED_CONTENT_SHOW_FULL_SIZE = "related_content_show_full_size";
  
  String RELATED_CONTENT_TYPE_EMAIL = "related_content_type_email";
  
  // People involvement
  String PEOPLE_SEARCH = "people_search";
  String PEOPLE_INVOLVE_POPUP_CAPTION = "people_involve_popup_caption";
  String PEOPLE_SELECT_MYSELF = "people_select_myself";

  String TASK_AUTHORISATION_ERROR_TITLE = "task_authorisation_error_title";
  String TASK_AUTHORISATION_MEMBERSHIP_ERROR = "task_authorisation_membership_error";
  String TASK_AUTHORISATION_INBOX_ERROR = "task_authorisation_inbox_error";

  String EMAIL_SUBJECT = "email_subject";
  String EMAIL_SENT_DATE = "email_sent_date";
  String EMAIL_RECEIVED_DATE = "email_received_date";
  String EMAIL_HTML_CONTENT = "email_html_content";
  String EMAIL_RECIPIENTS = "email_recipients";

  // Time formatting
  String TIME_UNIT_MOMENTS = "time_unit_moments";
  String TIME_UNIT_PAST = "time_unit_past";
  String TIME_UNIT_FUTURE = "time_unit_future";
  
  String TIME_UNIT_MINUTE = "time_unit_minute";
  String TIME_UNIT_MINUTES = "time_unit_minutes";
  String TIME_UNIT_HOUR = "time_unit_hour";
  String TIME_UNIT_HOURS = "time_unit_hours";
  String TIME_UNIT_DAY = "time_unit_day";
  String TIME_UNIT_DAYS = "time_unit_days";
  String TIME_UNIT_WEEK = "time_unit_week";
  String TIME_UNIT_WEEKS = "time_unit_weeks";
  String TIME_UNIT_MONTH = "time_unit_month";
  String TIME_UNIT_MONTHS = "time_unit_months";
  String TIME_UNIT_YEAR = "time_unit_year";
  String TIME_UNIT_YEARS = "time_unit_years";
  String TIME_UNIT_JUST_NOW = "time_unit_just_now";
  
  String MONTH_PREFIX = "month_";
  
//Time Organization
  String ORGANIZATION_TEAM_MANAGER = "organization_team_manager";
  String ORGANIZATION_TEAM_MEMBER = "organization_team_member";
  String ORGANIZATION_TEAM_MEMBER_MANAGER = "organization_team_member_manager";
  String ORGANIZATION_PARTICIPATION_TEAM_MANAGER = "organization_participation_team_manager";
  String TEAM_GROUP_TYPE_FORMAL = "team_group_type_formal";
  String TEAM_NO_ONES = "team_no_ones";
  String TEAM_NEW_CREATE = "team_new_create";
  String TEAM_GROUP_TABLE_TITLE = "team_group_table_title";
  
  String TEAM_CREATE = "team_create";
  String TEAM_NO_MEMBERS = "team_no_members";
  String TEAM_SELECT_MEMBERS = "team_select_members";
  String TEAM_DELETE = "team_delete";
  String TEAM_SELECT = "team_sign_select";
  String TEAM_MEMBER_SELECT = "team_mermber_select";
  
  //out project
  String OUTPROJECT_MY_PARTICIPATION = "outproject_my_participation";
  String OUTPROJECT_MY_PUBLISH = "outproject_my_publish";
  String OUTPROJECT_NO_PARTICIPATION_PROMPT = "outproject_no_participation_prompt";//项目没有参与
  String OUTPROJECT_NO_PUBLISH_PROMPT = "outproject_no_publish_prompt";//
  
  String OUTPROJECT_WINNER_NO_ONES = "outproject_winner_no_ones";
  String OUTPROJECT_PARTICIPANT_NO_PROJECTS = "outproject_participant_no_projects";
  
  String OUTPROJECT_PAGE_DETAIL="outproject_page_detail";
  String OUTPROJECT_PAGE_COMMENT="outproject_page_comment";
  String OUTPROJECT_PAGE_WINNERLIST="outproject_page_winnerlist";
  String OUTPROJECT_PAGE_STATUS="outproject_page_status";
  
  //social
  String SOCIAL_MAIN_MENU = "social_main_menu";
  
}
