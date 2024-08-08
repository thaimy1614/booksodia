package com.thai.profile.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "course_report")
public class Report {
    @Id
    @Column(name = "report_id")
    private Long reportId;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Builder.Default
    @Column(name = "is_resolve", nullable = false)
    private Boolean isResolve = false;

    @JoinColumn(name = "user_id", updatable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
}
