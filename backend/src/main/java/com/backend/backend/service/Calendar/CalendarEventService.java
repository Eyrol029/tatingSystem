package com.backend.backend.service.Calendar;

import java.time.LocalDate;
import java.util.List;

import com.backend.backend.model.CalendarEvent;
import com.backend.backend.model.CalendarEventDto;

public interface CalendarEventService {

    List<CalendarEventDto> getAllCalendarEvents();

    List<CalendarEventDto> getCalendarEventsInRange(LocalDate start, LocalDate end);

    List<CalendarEvent> getAllManualEvents();

    CalendarEvent addManualEvent(CalendarEvent event);

    CalendarEvent updateManualEvent(CalendarEvent event);

    void deleteManualEvent(Integer id);
}
