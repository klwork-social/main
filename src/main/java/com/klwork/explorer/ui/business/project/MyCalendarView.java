/**
 * Copyright 2013 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.klwork.explorer.ui.business.project;

import java.text.DateFormatSymbols;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import com.klwork.business.domain.model.DictDef;
import com.klwork.business.domain.model.MyCalendarEvent;
import com.klwork.business.domain.model.MyCalendarEventQuery;
import com.klwork.business.domain.service.MyCalendarEventService;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.handler.CommonFieldHandler;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.Calendar.TimeFormat;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.DateClickEvent;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClick;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClickHandler;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.RangeSelectEvent;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.RangeSelectHandler;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.WeekClick;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.WeekClickHandler;
import com.vaadin.ui.components.calendar.event.BasicEventProvider;
import com.vaadin.ui.components.calendar.event.CalendarEvent;
import com.vaadin.ui.components.calendar.handler.BasicDateClickHandler;
import com.vaadin.ui.components.calendar.handler.BasicWeekClickHandler;

/** Calendar component test application */
public class MyCalendarView extends Panel {

    private static final long serialVersionUID = -5436777475398410597L;

    private static final String DEFAULT_ITEMID = "DEFAULT";

    private enum Mode {
        MONTH, WEEK, DAY;
    }

    /**
     * This Gregorian calendar is used to control dates and time inside of this
     * test application.
     */
    private GregorianCalendar calendar;

    /** Target calendar component that this test application is made for. */
    private Calendar calendarComponent;

    private Date currentMonthsFirstDate;

    private final Label captionLabel = new Label("");

    private Button monthButton;

    private Button weekButton;

    private Button nextButton;

    private Button prevButton;

    private ComboBox timeZoneSelect;

    private ComboBox formatSelect;

    private ComboBox localeSelect;

    private CheckBox hideWeekendsButton;

    private CheckBox readOnlyButton;

    private TextField captionField;

    private Window scheduleEventPopup;

    private final FormLayout scheduleEventFieldLayout = new FormLayout();
    private FieldGroup scheduleEventFieldGroup = new FieldGroup();

    private Button deleteEventButton;

    private Button applyEventButton;

    private Mode viewMode = Mode.MONTH;

    private BasicEventProvider dataSource;

    private Button addNewEvent;

    /*
     * When testBench is set to true, CalendarTest will have static content that
     * is more suitable for Vaadin TestBench testing. Calendar will use a static
     * date Mon 10 Jan 2000. Enable by starting the application with a
     * "testBench" parameter in the URL.
     */
    private boolean testBench = false;

    private String calendarHeight = null;

    private String calendarWidth = null;

    private CheckBox disabledButton;

    private Integer firstHour;

    private Integer lastHour;

    private Integer firstDay;

    private Integer lastDay;

    private Locale defaultLocale = Locale.US;

    private boolean showWeeklyView;

    private boolean useSecondResolution;

    private DateField startDateField;
    private DateField endDateField;
    protected I18nManager i18nManager;
    MyCalendarEventService myCalendarEventService;
    
	public MyCalendarView() {
		this.i18nManager = ViewToolManager.getI18nManager();
		this.myCalendarEventService = ViewToolManager.getBean("myCalendarEventService");
		init();
	}

    public void init() {
    	//this.setCaption("我的时间视图");
        GridLayout layout = new GridLayout();
        layout.setSizeFull();
        layout.setMargin(true);
        setContent(layout);

        //handleURLParams(request.getParameterMap());

        initContent();
    }

    private void handleURLParams(Map<String, String[]> parameters) {
        testBench = parameters.containsKey("testBench")
                || parameters.containsKey("?testBench");

        if (parameters.containsKey("width")) {
            calendarWidth = parameters.get("width")[0];
        }

        if (parameters.containsKey("height")) {
            calendarHeight = parameters.get("height")[0];
        }

        if (parameters.containsKey("firstDay")) {
            firstDay = Integer.parseInt(parameters.get("firstDay")[0]);
        }

        if (parameters.containsKey("lastDay")) {
            lastDay = Integer.parseInt(parameters.get("lastDay")[0]);
        }

        if (parameters.containsKey("firstHour")) {
            firstHour = Integer.parseInt(parameters.get("firstHour")[0]);
        }

        if (parameters.containsKey("lastHour")) {
            lastHour = Integer.parseInt(parameters.get("lastHour")[0]);
        }

        if (parameters.containsKey("locale")) {
            String localeArray[] = parameters.get("locale")[0].split("_");
            defaultLocale = new Locale(localeArray[0], localeArray[1]);
            setLocale(defaultLocale);
        }

        if (parameters.containsKey(("secondsResolution"))) {
            useSecondResolution = true;
        }

        showWeeklyView = parameters.containsKey("weekly");

    }

    public void initContent() {
        // Set default Locale for this application
        if (testBench) {
            setLocale(defaultLocale);

        } else {
            setLocale(Locale.getDefault());
        }

        // Initialize locale, timezone and timeformat selects.
        localeSelect = createLocaleSelect();
        timeZoneSelect = createTimeZoneSelect();
        formatSelect = createCalendarFormatSelect();

        initCalendar();
        initLayoutContent();
        
        //测试数据
        addInitialEvents();
    }

    private Date resolveFirstDateOfWeek(Date today,
            java.util.Calendar currentCalendar) {
        int firstDayOfWeek = currentCalendar.getFirstDayOfWeek();
        currentCalendar.setTime(today);
        while (firstDayOfWeek != currentCalendar
                .get(java.util.Calendar.DAY_OF_WEEK)) {
            currentCalendar.add(java.util.Calendar.DATE, -1);
        }
        return currentCalendar.getTime();
    }

    private Date resolveLastDateOfWeek(Date today,
            java.util.Calendar currentCalendar) {
        currentCalendar.setTime(today);
        currentCalendar.add(java.util.Calendar.DATE, 1);
        int firstDayOfWeek = currentCalendar.getFirstDayOfWeek();
        // Roll to weeks last day using firstdayofweek. Roll until FDofW is
        // found and then roll back one day.
        while (firstDayOfWeek != currentCalendar
                .get(java.util.Calendar.DAY_OF_WEEK)) {
            currentCalendar.add(java.util.Calendar.DATE, 1);
        }
        currentCalendar.add(java.util.Calendar.DATE, -1);
        return currentCalendar.getTime();
    }

    private void addInitialEvents() {
        Date originalDate = calendar.getTime();
        calendar.setTime(originalDate);
        initData();
        
    }
    
    public void reflashData() {
        dataSource = new BasicEventProvider();
        calendarComponent.setEventProvider(dataSource);
        initData();
        
    }
    
    
    
    public void initData() {
    	MyCalendarEventQuery query = new MyCalendarEventQuery();
    	//dataSource.
        query.setOwnUser(getCurrentUserId());
		List<MyCalendarEvent> list = myCalendarEventService.findMyCalendarEventByQueryCriteria(query , null);
		//dataSource.
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			MyCalendarEvent myCalendarEvent = (MyCalendarEvent) iterator.next();
			if(!dataSource.containsEvent(myCalendarEvent)){
			 dataSource.addEvent(myCalendarEvent);
			}
		}
    }

	public String getCurrentUserId() {
		return LoginHandler.getLoggedInUser().getId();
	}

    private void initLayoutContent() {
        initNavigationButtons();
        initHideWeekEndButton();
        initReadOnlyButton();
        initDisabledButton();
        initAddNewEventButton();

        HorizontalLayout hl = new HorizontalLayout();
        hl.setWidth("100%");
        hl.setSpacing(true);
        hl.setMargin(new MarginInfo(false, false, true, false));
        hl.addComponent(prevButton);
        hl.addComponent(captionLabel);
        hl.addComponent(monthButton);
        hl.addComponent(weekButton);
        hl.addComponent(nextButton);
        hl.setComponentAlignment(prevButton, Alignment.MIDDLE_LEFT);
        hl.setComponentAlignment(captionLabel, Alignment.MIDDLE_CENTER);
        hl.setComponentAlignment(monthButton, Alignment.MIDDLE_CENTER);
        hl.setComponentAlignment(weekButton, Alignment.MIDDLE_CENTER);
        hl.setComponentAlignment(nextButton, Alignment.MIDDLE_RIGHT);

        monthButton.setVisible(viewMode == Mode.WEEK);
        weekButton.setVisible(viewMode == Mode.DAY);

        HorizontalLayout controlPanel = new HorizontalLayout();
        controlPanel.setSpacing(true);
        controlPanel.setMargin(new MarginInfo(false, false, true, false));
        controlPanel.setWidth("100%");
        //controlPanel.addComponent(localeSelect);
        //controlPanel.addComponent(timeZoneSelect);
        controlPanel.addComponent(formatSelect);
        controlPanel.addComponent(hideWeekendsButton);
        //controlPanel.addComponent(readOnlyButton);
        //controlPanel.addComponent(disabledButton);
        controlPanel.addComponent(addNewEvent);

        /*controlPanel.setComponentAlignment(timeZoneSelect,
                Alignment.MIDDLE_LEFT);*/
        controlPanel.setComponentAlignment(formatSelect, Alignment.MIDDLE_LEFT);
        /*controlPanel.setComponentAlignment(localeSelect, Alignment.MIDDLE_LEFT);*/
        controlPanel.setComponentAlignment(hideWeekendsButton,
                Alignment.MIDDLE_LEFT);
       /* controlPanel.setComponentAlignment(readOnlyButton,
                Alignment.MIDDLE_LEFT);
        controlPanel.setComponentAlignment(disabledButton,
                Alignment.MIDDLE_LEFT);*/
        controlPanel.setComponentAlignment(addNewEvent, Alignment.MIDDLE_LEFT);

        GridLayout layout = (GridLayout) getContent();
        layout.addComponent(controlPanel);
        layout.addComponent(hl);
        layout.addComponent(calendarComponent);
        layout.setRowExpandRatio(layout.getRows() - 1, 1.0f);
    }

    private void initNavigationButtons() {
        monthButton = new Button("月视图", new ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                switchToMonthView();
            }
        });

        weekButton = new Button("周视图", new ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                // simulate week click
                WeekClickHandler handler = (WeekClickHandler) calendarComponent
                        .getHandler(WeekClick.EVENT_ID);
                handler.weekClick(new WeekClick(calendarComponent, calendar
                        .get(GregorianCalendar.WEEK_OF_YEAR), calendar
                        .get(GregorianCalendar.YEAR)));
            }
        });

        nextButton = new Button("下月", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                handleNextButtonClick();
            }
        });
       

        prevButton = new Button("上月", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                handlePreviousButtonClick();
            }
        });
    
    }

    private void initHideWeekEndButton() {
        hideWeekendsButton = new CheckBox("隐藏周末");
        hideWeekendsButton.setImmediate(true);
        hideWeekendsButton
                .addValueChangeListener(new Property.ValueChangeListener() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        setWeekendsHidden(hideWeekendsButton.getValue());
                    }
                });
    }

    private void setWeekendsHidden(boolean weekendsHidden) {
        if (weekendsHidden) {
            int firstToShow = (GregorianCalendar.MONDAY - calendar
                    .getFirstDayOfWeek()) % 7;
            calendarComponent.setFirstVisibleDayOfWeek(firstToShow + 1);
            calendarComponent.setLastVisibleDayOfWeek(firstToShow + 5);
        } else {
            calendarComponent.setFirstVisibleDayOfWeek(1);
            calendarComponent.setLastVisibleDayOfWeek(7);
        }

    }

    private void initReadOnlyButton() {
        readOnlyButton = new CheckBox("Read-only mode");
        readOnlyButton.setImmediate(true);
        readOnlyButton
                .addValueChangeListener(new Property.ValueChangeListener() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        calendarComponent.setReadOnly(readOnlyButton.getValue());
                    }
                });
    }

    private void initDisabledButton() {
        disabledButton = new CheckBox("Disabled");
        disabledButton.setImmediate(true);
        disabledButton
                .addValueChangeListener(new Property.ValueChangeListener() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        calendarComponent.setEnabled(!disabledButton.getValue());
                    }
                });
    }

    public void initAddNewEventButton() {
        addNewEvent = new Button("增加事件到今天");
        addNewEvent.addClickListener(new Button.ClickListener() {

            private static final long serialVersionUID = -8307244759142541067L;

            @Override
            public void buttonClick(ClickEvent event) {
                Date start = getToday();
                start.setHours(0);
                start.setMinutes(0);
                start.setSeconds(0);

                Date end = getEndOfDay(calendar, start);

                showEventPopup(createNewEvent(start, end), true);
            }
        });
    }

    private void initFormFields(Layout formLayout,
            Class<? extends CalendarEvent> eventClass) {
    	//WW_TODO 日期表单初始化
        startDateField = CommonFieldHandler.createDateField("开始时间",useSecondResolution);
        endDateField = CommonFieldHandler.createDateField("结束时间",useSecondResolution);

        final CheckBox allDayField = CommonFieldHandler.createCheckBox("全天");
        allDayField.addValueChangeListener(new Property.ValueChangeListener() {

            private static final long serialVersionUID = -7104996493482558021L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                Object value = event.getProperty().getValue();
                if (value instanceof Boolean && Boolean.TRUE.equals(value)) {
                    setFormDateResolution(Resolution.DAY);

                } else {
                    setFormDateResolution(Resolution.MINUTE);
                }
            }

        });

        captionField = CommonFieldHandler.createTextField("标题");
        //final TextField whereField = CommonFieldHandler.createTextField("地点");
        final TextArea descriptionField = CommonFieldHandler.createTextArea("描述");
        descriptionField.setRows(3);
        //颜色
        final ComboBox styleNameField = createStyleNameComboBox();

        formLayout.addComponent(startDateField);
        formLayout.addComponent(endDateField);
        formLayout.addComponent(allDayField);
        formLayout.addComponent(captionField);
  
        formLayout.addComponent(descriptionField);
        formLayout.addComponent(styleNameField);

        scheduleEventFieldGroup.bind(startDateField, "start");
        scheduleEventFieldGroup.bind(endDateField, "end");
        scheduleEventFieldGroup.bind(captionField, "caption");
        scheduleEventFieldGroup.bind(descriptionField, "description");
     
        scheduleEventFieldGroup.bind(styleNameField, "styleName");
        scheduleEventFieldGroup.bind(allDayField, "allDay");
    }

    

    private ComboBox createStyleNameComboBox() {
    	List<DictDef> list = DictDef.queryDictsByType(DictDef
				.dict("color"));
        ComboBox s = CommonFieldHandler.createComBox("颜色", list, "blue");
        return s;
    }

    private void initCalendar() {
        dataSource = new BasicEventProvider();

        calendarComponent = new Calendar(dataSource);
        calendarComponent.setLocale(getLocale());
        calendarComponent.setWeeklyCaptionFormat("yyyy-MM-dd");
       
        calendarComponent.setImmediate(true);

        if (calendarWidth != null || calendarHeight != null) {
            if (calendarHeight != null) {
                calendarComponent.setHeight(calendarHeight);
            }
            if (calendarWidth != null) {
                calendarComponent.setWidth(calendarWidth);
            }
        } else {
            calendarComponent.setSizeFull();
        }

        if (firstHour != null && lastHour != null) {
            calendarComponent.setFirstVisibleHourOfDay(firstHour);
            calendarComponent.setLastVisibleHourOfDay(lastHour);
        }

        if (firstDay != null && lastDay != null) {
            calendarComponent.setFirstVisibleDayOfWeek(firstDay);
            calendarComponent.setLastVisibleDayOfWeek(lastDay);
        }
        
        //设置今天
        Date today = getToday();
        calendar = new GregorianCalendar(getLocale());
        calendar.setTime(today);

        updateCaptionLabel();
        
        //一个月的时间，开始和结束
        if (!showWeeklyView) {
            int rollAmount = calendar.get(GregorianCalendar.DAY_OF_MONTH) - 1;
            calendar.add(GregorianCalendar.DAY_OF_MONTH, -rollAmount);
            resetTime(false);
            currentMonthsFirstDate = calendar.getTime();
            calendarComponent.setStartDate(currentMonthsFirstDate);
            calendar.add(GregorianCalendar.MONTH, 1);
            calendar.add(GregorianCalendar.DATE, -1);
            calendarComponent.setEndDate(calendar.getTime());
        }

        addCalendarEventListeners();
    }

    private Date getToday() {
        if (testBench) {
            GregorianCalendar testDate = new GregorianCalendar();
            testDate.set(GregorianCalendar.YEAR, 2000);
            testDate.set(GregorianCalendar.MONTH, 0);
            testDate.set(GregorianCalendar.DATE, 10);
            testDate.set(GregorianCalendar.HOUR_OF_DAY, 0);
            testDate.set(GregorianCalendar.MINUTE, 0);
            testDate.set(GregorianCalendar.SECOND, 0);
            testDate.set(GregorianCalendar.MILLISECOND, 0);
            return testDate.getTime();
        }
        return new Date();
    }

    @SuppressWarnings("serial")
    private void addCalendarEventListeners() {
        // Register week clicks by changing the schedules start and end dates.
        calendarComponent.setHandler(new BasicWeekClickHandler() {

            @Override
            public void weekClick(WeekClick event) {
                // let BasicWeekClickHandler handle calendar dates, and update
                // only the other parts of UI here
                super.weekClick(event);
                updateCaptionLabel();
                switchToWeekView();
            }
        });

        calendarComponent.setHandler(new EventClickHandler() {

            @Override
            public void eventClick(EventClick event) {
            	MyCalendarEvent calendarEvent = (MyCalendarEvent)event.getCalendarEvent();
				showEventPopup(calendarEvent, false);
            }
        });

        calendarComponent.setHandler(new BasicDateClickHandler() {

            @Override
            public void dateClick(DateClickEvent event) {
                // let BasicDateClickHandler handle calendar dates, and update
                // only the other parts of UI here
                super.dateClick(event);
                switchToDayView();
            }
        });

        calendarComponent.setHandler(new RangeSelectHandler() {

            @Override
            public void rangeSelect(RangeSelectEvent event) {
                handleRangeSelect(event);
            }
        });
    }

    private ComboBox createTimeZoneSelect() {
        ComboBox s = new ComboBox("时区");
        s.addContainerProperty("caption", String.class, "");
        s.setItemCaptionPropertyId("caption");
        s.setFilteringMode(FilteringMode.CONTAINS);

        Item i = s.addItem(DEFAULT_ITEMID);
        i.getItemProperty("caption").setValue(
                "Default (" + TimeZone.getDefault().getID() + ")");
        for (String id : TimeZone.getAvailableIDs()) {
            if (!s.containsId(id)) {
                i = s.addItem(id);
                i.getItemProperty("caption").setValue(id);
            }
        }

        if (testBench) {
            s.select("America/New_York");
        } else {
            s.select(DEFAULT_ITEMID);
        }
        s.setImmediate(true);
        s.addValueChangeListener(new ValueChangeListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(ValueChangeEvent event) {

                updateCalendarTimeZone(event.getProperty().getValue());
            }
        });

        return s;
    }

    private ComboBox createCalendarFormatSelect() {
        ComboBox s = new ComboBox("日历格式");
        s.addContainerProperty("caption", String.class, "默认");
        s.setItemCaptionPropertyId("caption");

        Item i = s.addItem(DEFAULT_ITEMID);
        i.getItemProperty("caption").setValue("默认");
        i = s.addItem(TimeFormat.Format12H);
        i.getItemProperty("caption").setValue("12H");
        i = s.addItem(TimeFormat.Format24H);
        i.getItemProperty("caption").setValue("24H");

        s.select(DEFAULT_ITEMID);
        s.setImmediate(true);
        s.addValueChangeListener(new ValueChangeListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                updateCalendarFormat(event.getProperty().getValue());
            }
        });

        return s;
    }

    private ComboBox createLocaleSelect() {
        ComboBox s = new ComboBox("地区/国家");
        s.addContainerProperty("caption", String.class, "");
        s.setItemCaptionPropertyId("caption");
        s.setFilteringMode(FilteringMode.CONTAINS);

        for (Locale l : Locale.getAvailableLocales()) {
            if (!s.containsId(l)) {
                Item i = s.addItem(l);
                i.getItemProperty("caption").setValue(getLocaleItemCaption(l));
            }
        }

        s.select(getLocale());
        s.setImmediate(true);
        s.addValueChangeListener(new ValueChangeListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                updateCalendarLocale((Locale) event.getProperty().getValue());
            }
        });

        return s;
    }

    private void updateCalendarTimeZone(Object timezoneId) {
        TimeZone tz = null;
        if (!DEFAULT_ITEMID.equals(timezoneId)) {
            tz = TimeZone.getTimeZone((String) timezoneId);
        }

        // remember the week that was showing, so we can re-set it later
        Date startDate = calendarComponent.getStartDate();
        calendar.setTime(startDate);
        int weekNumber = calendar.get(java.util.Calendar.WEEK_OF_YEAR);
        calendarComponent.setTimeZone(tz);
        calendar.setTimeZone(calendarComponent.getTimeZone());

        if (viewMode == Mode.WEEK) {
            calendar.set(java.util.Calendar.WEEK_OF_YEAR, weekNumber);
            calendar.set(java.util.Calendar.DAY_OF_WEEK,
                    calendar.getFirstDayOfWeek());

            calendarComponent.setStartDate(calendar.getTime());
            calendar.add(java.util.Calendar.DATE, 6);
            calendarComponent.setEndDate(calendar.getTime());
        }
    }

    private void updateCalendarFormat(Object format) {
        TimeFormat calFormat = null;
        if (format instanceof TimeFormat) {
            calFormat = (TimeFormat) format;
        }

        calendarComponent.setTimeFormat(calFormat);
    }

    private String getLocaleItemCaption(Locale l) {
        String country = l.getDisplayCountry(getLocale());
        String language = l.getDisplayLanguage(getLocale());
        StringBuilder caption = new StringBuilder(country);
        if (caption.length() != 0) {
            caption.append(", ");
        }
        caption.append(language);
        return caption.toString();
    }

    private void updateCalendarLocale(Locale l) {
        int oldFirstDayOfWeek = calendar.getFirstDayOfWeek();
        setLocale(l);
        calendarComponent.setLocale(l);
        calendar = new GregorianCalendar(l);
        int newFirstDayOfWeek = calendar.getFirstDayOfWeek();

        // we are showing 1 week, and the first day of the week has changed
        // update start and end dates so that the same week is showing
        if (viewMode == Mode.WEEK && oldFirstDayOfWeek != newFirstDayOfWeek) {
            calendar.setTime(calendarComponent.getStartDate());
            calendar.add(java.util.Calendar.DAY_OF_WEEK, 2);
            // starting at the beginning of the week
            calendar.set(GregorianCalendar.DAY_OF_WEEK, newFirstDayOfWeek);
            Date start = calendar.getTime();

            // ending at the end of the week
            calendar.add(GregorianCalendar.DATE, 6);
            Date end = calendar.getTime();

            calendarComponent.setStartDate(start);
            calendarComponent.setEndDate(end);

            // Week days depend on locale so this must be refreshed
            setWeekendsHidden(hideWeekendsButton.getValue());
        }

    }

    private void handleNextButtonClick() {
        switch (viewMode) {
        case MONTH:
            nextMonth();
            break;
        case WEEK:
            nextWeek();
            break;
        case DAY:
            nextDay();
            break;
        }
    }

    private void handlePreviousButtonClick() {
        switch (viewMode) {
        case MONTH:
            previousMonth();
            break;
        case WEEK:
            previousWeek();
            break;
        case DAY:
            previousDay();
            break;
        }
    }

    private void handleRangeSelect(RangeSelectEvent event) {
        Date start = event.getStart();
        Date end = event.getEnd();

        /*
         * If a range of dates is selected in monthly mode, we want it to end at
         * the end of the last day.
         */
        if (event.isMonthlyMode()) {
            end = getEndOfDay(calendar, end);
        }

        showEventPopup(createNewEvent(start, end), true);
    }

    private void showEventPopup(MyCalendarEvent event, boolean newEvent) {
        if (event == null) {
            return;
        }

        updateCalendarEventPopup(newEvent);
        //Form数据初始化
        updateCalendarEventForm(event);
        //WW_TODO 日期弹出窗口
       if (!UI.getCurrent().getWindows().contains(scheduleEventPopup)) {
    	   UI.getCurrent().addWindow(scheduleEventPopup);
        }
    }

    /* Initializes a modal window to edit schedule event. */
    private void createCalendarEventPopup() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.addStyleName("social");

        scheduleEventPopup = new Window(null, layout);
        scheduleEventPopup.setWidth("400px");
        scheduleEventPopup.setModal(true);
        scheduleEventPopup.center();

        layout.addComponent(scheduleEventFieldLayout);

        applyEventButton = new Button("保存", new ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    commitCalendarEvent();
                } catch (CommitException e) {
                    e.printStackTrace();
                }
            }
        });
        Button cancel = new Button("取消", new ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                discardCalendarEvent();
            }
        });
        deleteEventButton = new Button("删除", new ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                deleteCalendarEvent();
            }
        });
        scheduleEventPopup.addCloseListener(new Window.CloseListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void windowClose(Window.CloseEvent e) {
                discardCalendarEvent();
            }
        });

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        buttons.addComponent(deleteEventButton);
        buttons.addComponent(applyEventButton);
        buttons.addComponent(cancel);
        layout.addComponent(buttons);
        layout.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);
    }

    private void updateCalendarEventPopup(boolean newEvent) {
        if (scheduleEventPopup == null) {
            createCalendarEventPopup();
        }

        if (newEvent) {
            scheduleEventPopup.setCaption("新事件");
        } else {
            scheduleEventPopup.setCaption("编辑");
        }

        deleteEventButton.setVisible(!newEvent);
        deleteEventButton.setEnabled(!calendarComponent.isReadOnly());
        applyEventButton.setEnabled(!calendarComponent.isReadOnly());
    }

    private void updateCalendarEventForm(MyCalendarEvent event) {
        BeanItem<MyCalendarEvent> item = new BeanItem<MyCalendarEvent>(event);
        scheduleEventFieldLayout.removeAllComponents();
        scheduleEventFieldGroup = new FieldGroup();
        initFormFields(scheduleEventFieldLayout, event.getClass());
        scheduleEventFieldGroup.setBuffered(true);
        scheduleEventFieldGroup.setItemDataSource(item);
    }

    private void setFormDateResolution(Resolution resolution) {
        if (startDateField != null && endDateField != null) {
            startDateField.setResolution(resolution);
            endDateField.setResolution(resolution);
        }
    }

    private MyCalendarEvent createNewEvent(Date startDate, Date endDate) {

    	MyCalendarEvent event = new MyCalendarEvent();
        event.setCaption("");
        event.setAllDay(true);
        event.setStart(startDate);
        event.setEnd(endDate);
        event.setStyleName("blue");
        return event;
    }

    /* Removes the event from the data source and fires change event. */
    private void deleteCalendarEvent() {
    	MyCalendarEvent event = getFormCalendarEvent();
        if (dataSource.containsEvent(event)) {
            dataSource.removeEvent(event);
        }
        myCalendarEventService.deleteMyCalendarEvent(event);
        removeWindow(scheduleEventPopup);
    }

    /* Adds/updates the event in the data source and fires change event. */
    private void commitCalendarEvent() throws CommitException {
    	//WW_TODO 日期进行提交
        scheduleEventFieldGroup.commit();
        MyCalendarEvent event = getFormCalendarEvent();
        if (event.getEnd() == null) {
            event.setEnd(event.getStart());
        }
        if (!dataSource.containsEvent(event)) {
            dataSource.addEvent(event);
        }
        event.setOwnUser(getCurrentUserId());
		myCalendarEventService.createMyCalendarEvent(event);
        removeWindow(scheduleEventPopup);
    }

    private void removeWindow(Window w) {
		UI.getCurrent().removeWindow(w);
	}

	private void discardCalendarEvent() {
        scheduleEventFieldGroup.discard();
        removeWindow(scheduleEventPopup);
    }

    @SuppressWarnings("unchecked")
    private MyCalendarEvent getFormCalendarEvent() {
        BeanItem<MyCalendarEvent> item = (BeanItem<MyCalendarEvent>) scheduleEventFieldGroup
                .getItemDataSource();
        MyCalendarEvent event = item.getBean();
        return  event;
    }

    private void nextMonth() {
        rollMonth(1);
    }

    private void previousMonth() {
        rollMonth(-1);
    }

    private void nextWeek() {
        rollWeek(1);
    }

    private void previousWeek() {
        rollWeek(-1);
    }

    private void nextDay() {
        rollDate(1);
    }

    private void previousDay() {
        rollDate(-1);
    }

    private void rollMonth(int direction) {
        calendar.setTime(currentMonthsFirstDate);
        calendar.add(GregorianCalendar.MONTH, direction);
        resetTime(false);
        currentMonthsFirstDate = calendar.getTime();
        calendarComponent.setStartDate(currentMonthsFirstDate);

        updateCaptionLabel();

        calendar.add(GregorianCalendar.MONTH, 1);
        calendar.add(GregorianCalendar.DATE, -1);
        resetCalendarTime(true);
    }

    private void rollWeek(int direction) {
        calendar.add(GregorianCalendar.WEEK_OF_YEAR, direction);
        calendar.set(GregorianCalendar.DAY_OF_WEEK,
                calendar.getFirstDayOfWeek());
        resetCalendarTime(false);
        resetTime(true);
        calendar.add(GregorianCalendar.DATE, 6);
        calendarComponent.setEndDate(calendar.getTime());
    }

    private void rollDate(int direction) {
        calendar.add(GregorianCalendar.DATE, direction);
        resetCalendarTime(false);
        resetCalendarTime(true);
    }
    
    //显示标题
    private void updateCaptionLabel() {
        DateFormatSymbols s = new DateFormatSymbols(getLocale());
        String month = s.getShortMonths()[calendar.get(GregorianCalendar.MONTH)];
        captionLabel.setValue(calendar.get(GregorianCalendar.YEAR) + " "
                + month);
        
        if (viewMode == Mode.MONTH) {
        	if(nextButton != null){
        	nextButton.setCaption("下月");
        	prevButton.setCaption("上月");
        	}
        }else {
        	if(nextButton != null){
        	nextButton.setCaption("下周");
        	prevButton.setCaption("上周");
        	}
        }
    }

    private MyCalendarEvent getNewEvent(String caption, Date start, Date end) {
    	MyCalendarEvent event = new MyCalendarEvent();
        event.setCaption(caption);
        event.setStart(start);
        event.setEnd(end);

        return event;
    }

    /*
     * Switch the view to week view.
     */
    public void switchToWeekView() {
        viewMode = Mode.WEEK;
        weekButton.setVisible(false);
        monthButton.setVisible(true);
    }

    /*
     * Switch the Calendar component's start and end date range to the target
     * month only. (sample range: 01.01.2010 00:00.000 - 31.01.2010 23:59.999)
     */
    public void switchToMonthView() {
        viewMode = Mode.MONTH;
        monthButton.setVisible(false);
        weekButton.setVisible(false);

        calendar.setTime(currentMonthsFirstDate);
        calendarComponent.setStartDate(currentMonthsFirstDate);

        updateCaptionLabel();

        calendar.add(GregorianCalendar.MONTH, 1);
        calendar.add(GregorianCalendar.DATE, -1);
        resetCalendarTime(true);
    }

    /*
     * Switch to day view (week view with a single day visible).
     */
    public void switchToDayView() {
        viewMode = Mode.DAY;
        monthButton.setVisible(true);
        weekButton.setVisible(true);
    }

    private void resetCalendarTime(boolean resetEndTime) {
        resetTime(resetEndTime);
        if (resetEndTime) {
            calendarComponent.setEndDate(calendar.getTime());
        } else {
            calendarComponent.setStartDate(calendar.getTime());
            updateCaptionLabel();
        }
    }

    /*
     * Resets the calendar time (hour, minute second and millisecond) either to
     * zero or maximum value.
     */
    private void resetTime(boolean max) {
        if (max) {
            calendar.set(GregorianCalendar.HOUR_OF_DAY,
                    calendar.getMaximum(GregorianCalendar.HOUR_OF_DAY));
            calendar.set(GregorianCalendar.MINUTE,
                    calendar.getMaximum(GregorianCalendar.MINUTE));
            calendar.set(GregorianCalendar.SECOND,
                    calendar.getMaximum(GregorianCalendar.SECOND));
            calendar.set(GregorianCalendar.MILLISECOND,
                    calendar.getMaximum(GregorianCalendar.MILLISECOND));
        } else {
            calendar.set(GregorianCalendar.HOUR_OF_DAY, 0);
            calendar.set(GregorianCalendar.MINUTE, 0);
            calendar.set(GregorianCalendar.SECOND, 0);
            calendar.set(GregorianCalendar.MILLISECOND, 0);
        }
    }

    private static Date getEndOfDay(java.util.Calendar calendar, Date date) {
        java.util.Calendar calendarClone = (java.util.Calendar) calendar
                .clone();

        calendarClone.setTime(date);
        calendarClone.set(java.util.Calendar.MILLISECOND,
                calendarClone.getActualMaximum(java.util.Calendar.MILLISECOND));
        calendarClone.set(java.util.Calendar.SECOND,
                calendarClone.getActualMaximum(java.util.Calendar.SECOND));
        calendarClone.set(java.util.Calendar.MINUTE,
                calendarClone.getActualMaximum(java.util.Calendar.MINUTE));
        calendarClone.set(java.util.Calendar.HOUR,
                calendarClone.getActualMaximum(java.util.Calendar.HOUR));
        calendarClone.set(java.util.Calendar.HOUR_OF_DAY,
                calendarClone.getActualMaximum(java.util.Calendar.HOUR_OF_DAY));

        return calendarClone.getTime();
    }

    private static Date getStartOfDay(java.util.Calendar calendar, Date date) {
        java.util.Calendar calendarClone = (java.util.Calendar) calendar
                .clone();

        calendarClone.setTime(date);
        calendarClone.set(java.util.Calendar.MILLISECOND, 0);
        calendarClone.set(java.util.Calendar.SECOND, 0);
        calendarClone.set(java.util.Calendar.MINUTE, 0);
        calendarClone.set(java.util.Calendar.HOUR, 0);
        calendarClone.set(java.util.Calendar.HOUR_OF_DAY, 0);

        return calendarClone.getTime();
    }
}
