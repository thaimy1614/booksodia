package com.thai.profile.service.report;

import fsa.cursus.user_service.dto.course.ReportResponse;
import fsa.cursus.user_service.dto.course.UserInfo;
import fsa.cursus.user_service.exception.white.ReportNotFound;
import fsa.cursus.user_service.exception.white.UserNotFoundException;
import fsa.cursus.user_service.invoker.CourseServiceInvoker;
import fsa.cursus.user_service.model.Report;
import fsa.cursus.user_service.model.User;
import fsa.cursus.user_service.repository.ReportRepository;
import fsa.cursus.user_service.repository.UserRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final UserRepository userRepository;
    private final ReportRepository reportRepository;
    private final CourseServiceInvoker courseServiceInvoker;

    @Override
    @Transactional(readOnly = true)
    public List<ReportResponse> getAllReport(long userId, @Nullable Boolean isResolve) {
        final List<Long> reportIds;
        if (isResolve == null) {    // Both resolved and not resolved
            reportIds = reportRepository.findAllByUser_UserId(userId)
                    .map(Report::getReportId)
                    .toList();
        } else if (isResolve) {     // Only resolved
            reportIds = reportRepository.findAllByUser_UserIdAndIsResolve(userId, true)
                    .map(Report::getReportId)
                    .toList();
        } else {                    // Only not resolved
            reportIds = reportRepository.findAllByUser_UserIdAndIsResolve(userId, false)
                    .map(Report::getReportId)
                    .toList();
        }

        return courseServiceInvoker.getAllReportByIds(reportIds);
    }

    @Override
    public UserInfo createCourseReport(long reportId, long userId, long courseId) {
        // Looking for user
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        // Create new report and save it
        final Report report = Report.builder()
                .reportId(reportId)
                .courseId(courseId)
                .isResolve(false)
                .user(user)
                .build();
        reportRepository.save(report);

        return UserInfo.builder()
                .userId(user.getUserId())
                .image(user.getImage())
                .isActive(user.getIsActive())
                .fullName(user.getFullName())
                .build();
    }

    @Override
    public void resolveReport(long reportId) {
        final Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportNotFound(reportId));
        report.setIsResolve(true);
        reportRepository.save(report);
    }

    @Override
    public void deleteCourseReport(long reportId) {
        final Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportNotFound(reportId));
        reportRepository.delete(report);
    }
}
