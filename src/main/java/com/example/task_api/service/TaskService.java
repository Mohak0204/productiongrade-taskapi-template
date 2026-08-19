package com.example.task_api.service;

import com.example.task_api.dto.TaskCreateRequest;
import com.example.task_api.dto.TaskResponse;
import com.example.task_api.entity.Task;
import com.example.task_api.exception.TaskNotFoundException;
import com.example.task_api.repository.TaskRepository;
import org.springframework.stereotype.Service;
import com.example.task_api.dto.TaskUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // GET /api/tasks
    public Page<TaskResponse> getTasks(String title,Pageable pageable) {

        Page<Task> taskPage ;
        if (title == null || title.isBlank()) {

            // No filter → get all tasks
            taskPage = taskRepository.findAll(pageable);

        } else {

            // Filter by title
            taskPage = taskRepository
                    .findByTitleContainingIgnoreCase(title, pageable);
        }

        return taskPage.map(task ->
                new TaskResponse(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription()
                )
        );
    }

    // POST /api/tasks
    public TaskResponse createTask(TaskCreateRequest request) {

        Task task = new Task(
                request.getTitle(),
                request.getDescription()
        );

        Task savedTask = taskRepository.save(task);

        return new TaskResponse(
                savedTask.getId(),
                savedTask.getTitle(),
                savedTask.getDescription()
        );
    }
    public TaskResponse getTaskById(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription()
        );
    }
    public TaskResponse updateTask(Long id, TaskUpdateRequest request) {

        // Find the existing task in the database
        Task task = taskRepository.findById(id)
                .orElseThrow(()-> new TaskNotFoundException(id));

        // Update the existing entity
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());

        // Save the updated entity
        Task updatedTask = taskRepository.save(task);

        // Convert Entity -> Response DTO
        return new TaskResponse(
                updatedTask.getId(),
                updatedTask.getTitle(),
                updatedTask.getDescription()
        );
    }
    public void deleteTask(Long id) {

        // Check whether the task exists
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }

        // Delete the task
        taskRepository.deleteById(id);
    }
}