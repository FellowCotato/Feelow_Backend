package com.feelow.Feelow.repository;

import com.feelow.Feelow.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    // ID로 member 찾기
    Optional<Member> findMemberById(Long id);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByMemberId(Long memberId);


}
