package com.thai.profile.service.report;

import fsa.cursus.user_service.dto.course.ReportResponse;
import fsa.cursus.user_service.dto.course.UserInfo;
import jakarta.annotation.Nullable;

import java.util.List;

public interface ReportService {
    List<ReportResponse> getAllReport(long userId, @Nullable Boolean isResolve);

    UserInfo createCourseReport(long reportId, long userId, long courseId);

    void resolveReport(long reportId);

    void deleteCourseReport(long reportId);
}
