package com.backend.backend.model;

import java.util.List;

/**
 * Single DTO returned by GET /api/dashboard/summary
 * Contains all data the frontend dashboard needs in one request.
 */
public class DashboardSummaryDTO {

    // ── Stats cards ───────────────────────────────────────────────────────────
    private long totalPatients;
    private long totalDeliveries;
    private long totalAppointments;
    private long totalPrenatalRecords;

    // ── Financial overview ────────────────────────────────────────────────────
    private double totalRevenue;
    private double totalExpenses;
    private double netIncome;

    // ── Recent activity feed ──────────────────────────────────────────────────
    private List<ActivityItem> recentActivities;

    // ── Pending appointments ──────────────────────────────────────────────────
    private List<PendingItem> pendingAppointments;

    // ──────────────────────────────────────────────────────────────────────────
    //  Inner classes
    // ──────────────────────────────────────────────────────────────────────────

    public static class ActivityItem {
        private String text;
        private String time;
        private String type; // "patient" | "prenatal" | "appointment" | "payment"

        public ActivityItem() {}
        public ActivityItem(String text, String time, String type) {
            this.text = text; this.time = time; this.type = type;
        }

        public String getText()  { return text; }
        public void   setText(String text)  { this.text = text; }
        public String getTime()  { return time; }
        public void   setTime(String time)  { this.time = time; }
        public String getType()  { return type; }
        public void   setType(String type)  { this.type = type; }
    }

    public static class PendingItem {
        private String type;       // "Appointment Request"
        private String name;       // patient full name
        private String subtitle;   // service type
        private String status;     // "Pending"
        private Integer referenceId;

        public PendingItem() {}
        public PendingItem(String type, String name, String subtitle, String status, Integer referenceId) {
            this.type = type; this.name = name; this.subtitle = subtitle;
            this.status = status; this.referenceId = referenceId;
        }

        public String  getType()        { return type; }
        public void    setType(String type) { this.type = type; }
        public String  getName()        { return name; }
        public void    setName(String name) { this.name = name; }
        public String  getSubtitle()    { return subtitle; }
        public void    setSubtitle(String subtitle) { this.subtitle = subtitle; }
        public String  getStatus()      { return status; }
        public void    setStatus(String status) { this.status = status; }
        public Integer getReferenceId() { return referenceId; }
        public void    setReferenceId(Integer referenceId) { this.referenceId = referenceId; }
    }

    // ── Getters / Setters ─────────────────────────────────────────────────────

    public long   getTotalPatients()        { return totalPatients; }
    public void   setTotalPatients(long v)  { this.totalPatients = v; }

    public long   getTotalDeliveries()      { return totalDeliveries; }
    public void   setTotalDeliveries(long v){ this.totalDeliveries = v; }

    public long   getTotalAppointments()    { return totalAppointments; }
    public void   setTotalAppointments(long v){ this.totalAppointments = v; }

    public long   getTotalPrenatalRecords() { return totalPrenatalRecords; }
    public void   setTotalPrenatalRecords(long v){ this.totalPrenatalRecords = v; }

    public double getTotalRevenue()         { return totalRevenue; }
    public void   setTotalRevenue(double v) { this.totalRevenue = v; }

    public double getTotalExpenses()        { return totalExpenses; }
    public void   setTotalExpenses(double v){ this.totalExpenses = v; }

    public double getNetIncome()            { return netIncome; }
    public void   setNetIncome(double v)    { this.netIncome = v; }

    public List<ActivityItem> getRecentActivities()          { return recentActivities; }
    public void               setRecentActivities(List<ActivityItem> v){ this.recentActivities = v; }

    public List<PendingItem>  getPendingAppointments()       { return pendingAppointments; }
    public void               setPendingAppointments(List<PendingItem> v){ this.pendingAppointments = v; }
}