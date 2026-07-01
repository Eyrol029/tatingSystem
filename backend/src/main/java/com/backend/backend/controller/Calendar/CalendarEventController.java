package com.backend.backend.controller.Calendar;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.backend.model.CalendarEvent;
import com.backend.backend.model.CalendarEventDto;
import com.backend.backend.service.Calendar.CalendarEventService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/calendar")
public class CalendarEventController {

    @Autowired
    private CalendarEventService service;

    @GetMapping("/events")
    public List<CalendarEventDto> getEvents(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return service.getCalendarEventsInRange(start, end);
    }

    @GetMapping("/manual")
    public List<CalendarEvent> getManualEvents() {
        return service.getAllManualEvents();
    }

    @PostMapping("/manual")
    public CalendarEvent addManualEvent(@RequestBody CalendarEvent event) {
        return service.addManualEvent(event);
    }

    @PutMapping("/manual")
    public CalendarEvent updateManualEvent(@RequestBody CalendarEvent event) {
        return service.updateManualEvent(event);
    }

    @DeleteMapping("/manual/{id}")
    public String deleteManualEvent(@PathVariable Integer id) {
        service.deleteManualEvent(id);
        return "Manual calendar event deleted successfully.";
    }
}
