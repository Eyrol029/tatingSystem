import axios from 'axios';

const BASE_URL = 'http://localhost:8080/api/familyplanning/records';

export async function createFamilyPlanningRecord(record) {
    const response = await axios.post(BASE_URL, record);
    return response.data;
}
