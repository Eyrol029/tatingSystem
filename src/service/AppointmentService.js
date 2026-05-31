import axios from 'axios';

const BASE_URL = 'http://localhost:8080/api/appointment';

export const AppointmentService = {

    async getAll() {
        const response = await axios.get(BASE_URL);
        return response.data;
    },

    async getById(id) {
        const response = await axios.get(`${BASE_URL}/${id}`);
        return response.data;
    },

    async add(appointment) {
        const response = await axios.post(BASE_URL, appointment);
        return response.data;
    },

    async update(appointment) {
        const response = await axios.put(BASE_URL, appointment);
        return response.data;
    },

    async delete(id) {
        const response = await axios.delete(`${BASE_URL}/${id}`);
        return response.data;
    }
};