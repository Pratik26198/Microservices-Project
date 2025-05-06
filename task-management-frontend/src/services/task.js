// src/services/task.js
import api from './api';

const handleError = (error) => {
  // Skip handling for redirect errors
  if (error.message === 'REDIRECTING_TO_LOGIN') {
    return Promise.reject(error);
  }

  if (error.response) {
    console.error(
      `API Error - Status: ${error.response.status}`,
      error.response.data
    );
  } else if (error.request) {
    console.error('Network Error:', error.message);
  } else {
    console.error('Request Error:', error.message);
  }
  
  return Promise.reject(error);
};

// Unified async handler
const apiHandler = async (fn) => {
  try {
    const response = await fn();
    return response.data;
  } catch (error) {
    return handleError(error);
  }
};

// Task Operations
export const createTask = (taskData) => 
  apiHandler(() => api.post('/api/tasks', taskData));

export const getTaskById = (taskId) => 
  apiHandler(() => api.get(`/api/tasks/${taskId}`));

export const getAssignedTasks = (status) => 
  apiHandler(() => api.get('/api/tasks/user', { params: { status } }));

export const getAllTasks = (status) => 
  apiHandler(() => api.get('/api/tasks', { params: { status } }));

export const assignTaskToUser = (taskId, userId) => 
  apiHandler(() => api.put(`/api/tasks/${taskId}/user/${userId}/assigned`));

export const updateTask = (taskId, taskData) => 
  apiHandler(() => api.put(`/api/tasks/${taskId}`, taskData));

export const completeTask = (taskId) => 
  apiHandler(() => api.put(`/api/tasks/${taskId}/complete`));

export const deleteTask = (taskId) => 
  apiHandler(() => api.delete(`/api/tasks/${taskId}`));

// Submission Operations
export const submitTask = (taskId, githubLink) => 
  apiHandler(() => api.post('/api/submissions', null, {
    params: { task_id: taskId, github_link: githubLink }
  }));

export const getTaskSubmissions = (taskId) => 
  apiHandler(() => api.get(`/api/submissions/task/${taskId}`));

export const updateSubmissionStatus = (submissionId, status) => 
  apiHandler(() => api.put(`/api/submissions/${submissionId}`, { status }));