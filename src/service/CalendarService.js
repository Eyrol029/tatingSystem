import axios from 'axios';

const BASE_URL = 'http://localhost:8080/api/calendar';

export const CalendarService = {

    async getEvents(start, end) {
        const params = {};
        if (start) params.start = start;
        if (end) params.end = end;
        const response = await axios.get(`${BASE_URL}/events`, { params });
        return response.data;
    },

    async addManualEvent(event) {
        const response = await axios.post(`${BASE_URL}/manual`, event);
        return response.data;
    },

    async updateManualEvent(event) {
        const response = await axios.put(`${BASE_URL}/manual`, event);
        return response.data;
    },

    async deleteManualEvent(id) {
        const response = await axios.delete(`${BASE_URL}/manual/${id}`);
        return response.data;
    }
};
