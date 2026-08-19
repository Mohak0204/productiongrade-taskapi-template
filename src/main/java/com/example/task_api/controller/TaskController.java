package com.example.task_api.controller;

import com.example.task_api.dto.TaskCreateRequest;
import com.example.task_api.dto.TaskPageResponse;
import com.example.task_api.dto.TaskResponse;
import com.example.task_api.service.TaskService;
import com.example.task_api.dto.TaskUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.PageRequest;
import java.util.List;

@RestController
public class TaskController {

    private final TaskService taskService;

    // Constructor injection.
    // Spring automatically provides TaskService here.
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // GET /api/tasks
    @GetMapping("/api/tasks")
    public ResponseEntity<TaskPageResponse> getTasks(@RequestParam(required = false) String title, @PageableDefault(
                                                                 size = 10,
                                                                 sort = "id",
                                                                 direction = Sort.Direction.ASC
                                                         )
                                                          Pageable pageable
    ) {
        Pageable safePageable = PageRequest.of(
                pageable.getPageNumber(),
                Math.min(pageable.getPageSize(), 100),
                pageable.getSort()
        );
        TaskPageResponse tasks = taskService.getTasks(title ,safePageable);

        return ResponseEntity.ok(tasks);
    }
    @GetMapping("/api/tasks/{id}")
    public ResponseEntity<TaskResponse> getTaskById(
            @PathVariable Long id
    ) {

        TaskResponse response = taskService.getTaskById(id);

        return ResponseEntity.ok(response);
    }
        // POST /api/tasks
    @PostMapping("/api/tasks")
    public ResponseEntity<TaskResponse> createTask(
            @Valid@RequestBody TaskCreateRequest request
    ) {

        TaskResponse response = taskService.createTask(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @PutMapping("/api/tasks/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid@RequestBody TaskUpdateRequest request
    ) {

        TaskResponse response = taskService.updateTask(id, request);

        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/api/tasks/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id
    ) {

        taskService.deleteTask(id);

        return ResponseEntity.noContent().build();
    }

}