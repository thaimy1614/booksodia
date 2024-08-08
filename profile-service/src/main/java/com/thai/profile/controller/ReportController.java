package com.thai.profile.controller;

import fsa.cursus.user_service.dto.ResponseObject;
import fsa.cursus.user_service.dto.course.ReportResponse;
import fsa.cursus.user_service.dto.course.UserInfo;
import fsa.cursus.user_service.service.report.ReportService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${spring.application.api-prefix}")
public class ReportController {
    private final ReportService reportService;

    @Operation(summary = "Student: Get all course report of the logged in user")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('SCP_STUDENT_PERMS')")
    @GetMapping("/course-report")
    public ResponseObject<List<ReportResponse>> getAllReport(
            @RequestParam(required = false) Boolean isResolve,
            @Autowired JwtAuthenticationToken token
    ) {
        final long currentUser = Long.parseLong(token.getName());
        final List<ReportResponse> response = reportService.getAllReport(currentUser, isResolve);
        return ResponseObject.success(response);
    }

    @Hidden
    @PreAuthorize("hasAuthority('SCP_ADMIN_PERMS')")
    @PutMapping("/course-report/{reportId}")
    public void markResolve(
            @PathVariable Long reportId
    ) {
        reportService.resolveReport(reportId);
    }

    @Hidden
    @PreAuthorize("hasAnyAuthority('SCP_STUDENT_PERMS', 'SCP_ADMIN_PERMS')")
    @DeleteMapping("/course-report/{reportId}")
    public void deleteCourseReport(
            @PathVariable Long reportId
    ) {
        reportService.deleteCourseReport(reportId);
    }

    @Hidden
    @PreAuthorize("hasAuthority('SCP_STUDENT_PERMS')")
    @PostMapping("/{userId}/course-report/{reportId}")
    public UserInfo createCourseReport(
            @PathVariable Long reportId,
            @PathVariable Long userId,
            @RequestParam Long courseId
    ) {
        return reportService.createCourseReport(reportId, userId, courseId);
    }
}
