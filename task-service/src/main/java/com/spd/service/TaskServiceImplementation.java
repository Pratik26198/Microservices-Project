package com.spd.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spd.model.Task;
import com.spd.model.TaskStatus;
import com.spd.repository.TaskRepository;

@Service
public class TaskServiceImplementation implements TaskService {

	@Autowired
	private TaskRepository taskRepository;
	
	@Override
	public Task createTask(Task task, String requestorRole) throws Exception {
		if(!requestorRole.equals("ROLE_ADMIN")) {
			throw new Exception("Only admin can create task");
		}
		task.setStatus(TaskStatus.PENDING);
		task.setCreatedAt(LocalDateTime.now());
		return taskRepository.save(task);
	}

	@Override
	public Task getTaskById(Long id) throws Exception {
		return taskRepository.findById(id).orElseThrow(() -> new Exception("Task not found with id: " + id));
	}

	@Override
	public List<Task> getAllTask(TaskStatus status) {
	    List<Task> allTask = taskRepository.findAll();
	    List<Task> filteredTasks = allTask.stream()
	            .filter(task -> status == null || task.getStatus().name().equalsIgnoreCase(status.toString()))
	            .collect(Collectors.toList());
	    return filteredTasks;
	}


	@Override
	public Task updateTask(Long id, Task updatedTask, Long userId) throws Exception {
		Task existingTask = getTaskById(id);

		if (updatedTask.getTitle() != null) {
			existingTask.setTitle(updatedTask.getTitle());
		}
		if (updatedTask.getDescription() != null) {
			existingTask.setDescription(updatedTask.getDescription());
		}
		if (updatedTask.getImage() != null) {
			existingTask.setImage(updatedTask.getImage());
		}
		if (updatedTask.getStatus() != null) {
			existingTask.setStatus(updatedTask.getStatus());
		}
		if (updatedTask.getDeadline() != null) {
			existingTask.setDeadline(updatedTask.getDeadline());
		}
		return taskRepository.save(existingTask);
	}

	@Override
	public void deleteTask(Long id) throws Exception{

		getTaskById(id);
		taskRepository.deleteById(id);
	}

	@Override
	public Task assignedToUser(Long userId, Long taskId) throws Exception {

		Task task = getTaskById(taskId);
		task.setAssignedUserId(userId);
		task.setStatus(TaskStatus.DONE);

		return taskRepository.save(task);
	}

	@Override
	public List<Task> assignedUsersTask(Long userId, TaskStatus status) {
		List <Task> allTask = taskRepository.findByAssignedUserId(userId);
		List<Task> filteredTasks = allTask.stream()
				.filter(task -> status == null || task.getStatus().name().equalsIgnoreCase(status.toString()))
				.collect(Collectors.toList());
		return filteredTasks;
	}

	@Override
	public Task completeTask(Long taskId) throws Exception {
		Task task = getTaskById(taskId);
		task.setStatus(TaskStatus.DONE);
		return taskRepository.save(task);
	}

}
