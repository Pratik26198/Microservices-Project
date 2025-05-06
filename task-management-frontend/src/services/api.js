import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:5000'
});

// Request interceptor
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    
    // Skip authentication check for auth routes
    if (config.url.includes('/auth/')) {
      return config;
    }

    // Redirect to login if no token
    if (!token) {
      if (!window.location.pathname.includes('/login')) {
        window.location.href = '/login';
      }
      return Promise.reject(new Error('REDIRECTING_TO_LOGIN'));
    }

    // Add authorization header
    config.headers.Authorization = `Bearer ${token}`;
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor
api.interceptors.response.use(
  (response) => response,
  (error) => {
    // Handle 401 Unauthorized
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    
    // Handle network errors
    if (error.code === 'ERR_NETWORK') {
      console.error('Network Error:', error.message);
    }
    
    return Promise.reject(error);
  }
);

export default api;