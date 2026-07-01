package com.backend.backend.repository.Calendar;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.backend.model.CalendarEvent;

public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Integer> {
}
