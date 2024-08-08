package com.thai.profile.repository;

import fsa.cursus.user_service.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Stream<Report> findAllByUser_UserId(long userId);

    Stream<Report> findAllByUser_UserIdAndIsResolve(long userId, boolean isResolve);
}
