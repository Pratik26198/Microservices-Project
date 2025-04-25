package com.spd.service;

import java.util.List;

import com.spd.model.Submission;

public interface SubmissionService {

    Submission submitTask(Long userId, Long taskId, String githubLink, String jwt) throws Exception;
    Submission getTaskSubmissionById(Long submissionId) throws Exception;
    List<Submission> getAllTaskSubmissions();
    List <Submission> getTaskSubmissionsByTaskId(Long taskId) throws Exception;
    Submission acceptDeclineSubmission(Long id, String status) throws Exception;
}
