package com.feelow.Feelow.repository;

import com.feelow.Feelow.domain.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByTeacherId(Long teacherId);
    Optional<Teacher> findByMember_memberId(Long memberId);
}
