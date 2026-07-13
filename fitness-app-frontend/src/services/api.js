import axios from "axios";

// Use relative path — works with both Vite dev proxy and nginx in Docker
const API_URL = '/api';

const api = axios.create({
    baseURL: API_URL
});

api.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    const userId = localStorage.getItem('userId');

    if (token) {
        config.headers['Authorization'] = `Bearer ${token}`;
    }

    if (userId) {
        config.headers['X-User-ID'] = userId;
    }
    
    return config;
}, (error) => {
    return Promise.reject(error);
});

export const getActivities = () => api.get('/activities');
export const addActivity = (activity) => api.post('/activities', activity);
export const getActivityDetail = (id) => api.get(`/recommendations/activity/${id}`);
