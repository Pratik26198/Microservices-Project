package com.spd.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spd.model.Submission;
import com.spd.model.TaskDto;
import com.spd.repository.SubmissionRepository;

@Service
public class SubmissionServiceImplementation implements SubmissionService {
	
	@Autowired
	private SubmissionRepository submissionRepository;
	
	@Autowired
	private TaskService taskService;
	

	@Override
	public Submission submitTask(Long userId, Long taskId, String githubLink, String jwt) throws Exception {
		TaskDto task = taskService.getTaskById(taskId, jwt);
		if (task!=null) {
			Submission submission = new Submission();
			submission.setTaskId(taskId);
			submission.setUserId(userId);
			submission.setGithubLink(githubLink);
			submission.setSubmissionTime(java.time.LocalDateTime.now());
			return submissionRepository.save(submission);
		}
		throw new Exception("Task not found with Id :"+ taskId );
	}

	@Override
	public Submission getTaskSubmissionById(Long submissionId) throws Exception {
		return submissionRepository.findById(submissionId)
				.orElseThrow(() -> new Exception("Submission not found with Id :" + submissionId));
	}

	@Override
	public List<Submission> getAllTaskSubmissions() {
		return submissionRepository.findAll();
	}

	@Override
	public List<Submission> getTaskSubmissionsByTaskId(Long taskId) throws Exception {
		return submissionRepository.findByTaskId(taskId);
	}

	@Override
	public Submission acceptDeclineSubmission(Long id, String status) throws Exception {
	    Submission submission = getTaskSubmissionById(id); 
	    submission.setStatus(status);
	    if (status.equals("ACCEPTED")) {
	        taskService.completeTask(submission.getTaskId());
	    }
	    return submissionRepository.save(submission);
	}
}
