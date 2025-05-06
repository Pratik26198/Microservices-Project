import api from './api';

export const signup = (userData) => api.post('/auth/signup', userData);

export const signin = (credentials) => api.post('/auth/signin', credentials);

export const getProfile = () => api.get('/api/users/profile');