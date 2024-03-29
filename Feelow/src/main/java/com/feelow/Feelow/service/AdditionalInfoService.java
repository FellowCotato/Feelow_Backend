package com.feelow.Feelow.service;

import com.feelow.Feelow.S3.S3ImageService;
import com.feelow.Feelow.domain.entity.*;
import com.feelow.Feelow.domain.dto.AdditionalInfoRequestDto;
import com.feelow.Feelow.domain.dto.ResponseDto;
import com.feelow.Feelow.repository.ClassroomRepository;
import com.feelow.Feelow.repository.MemberRepository;
import com.feelow.Feelow.repository.StudentRepository;
import com.feelow.Feelow.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
@AllArgsConstructor
public class AdditionalInfoService {

    private final StudentRepository studentRepository;

    private final TeacherRepository teacherRepository;

    private final ClassroomRepository classroomRepository;

    private final MemberRepository memberRepository;

    private final S3ImageService s3ImageService;

    // 멤버 타입 업데이트
    @Transactional
    public ResponseDto<?> updateMemberType(Long memberId, String member_type) {
        try {
            Optional<Member> optionalMember = memberRepository.findByMemberId(memberId);

            if (optionalMember.isPresent()) {
                Member loginMember = optionalMember.get();
                loginMember.setMemberType(member_type);
                return ResponseDto.success("멤버 타입이 업데이트 되었습니다.", loginMember);
            } else {
                // 회원을 찾지 못한 경우에 대한 응답 반환
                return ResponseDto.failed(HttpStatus.NOT_FOUND, "해당 ID의 회원을 찾을 수 없습니다.", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 예외 메시지를 포함한 실패 응답 반환
            return ResponseDto.failed(HttpStatus.INTERNAL_SERVER_ERROR, "멤버타입 업데이트에 실패했습니다: " + e.getMessage(), null);
        }
    }

    // 학생 / 선생님 추가 정보 입력
    @Transactional
    public ResponseDto<?> addAdditionalInfo(Long member_id, AdditionalInfoRequestDto infoRequestDto) {
        try {
            Optional<Member> optionalMember = memberRepository.findByMemberId(member_id);

            if (optionalMember.isPresent()) {
                Member member = optionalMember.get();

                // 이미 등록된 학급인지 확인
                Classroom existingClassroom = classroomRepository.findBySchoolAndGradeAndClassNum(
                        infoRequestDto.getSchool(),
                        infoRequestDto.getGrade(),
                        infoRequestDto.getClass_num()
                ).orElse(null);

                // 이미 등록된 학급이 없으면 새로운 학급 등록
                if (existingClassroom == null) {
                    Classroom classroom = new Classroom();
                    classroom.setSchool(infoRequestDto.getSchool());
                    classroom.setGrade(infoRequestDto.getGrade());
                    classroom.setClassNum(infoRequestDto.getClass_num());
                    classroomRepository.save(classroom);
                    existingClassroom = classroom;
                }

                // 멤버 타입이 student면 student에 추가 정보 저장
                if ("student".equals(member.getMemberType())) {
                    // 주어진 memberId로 이미 존재하는 Student 확인
                    Optional<Student> existingStudent = studentRepository.findByMember_memberId(member_id);
                    System.out.println("Is existingStudent present? " + existingStudent.isPresent());
                    // 이미 존재하는 경우
                    if (existingStudent.isPresent()) {
                        return ResponseDto.failed("이미 저장된 학생입니다.", null);
                    } else {
                        // 새로운 학생 정보 저장
                        System.out.println("새로운 학생 정보 저장");
                        Student student = new Student();
                        student.setNickname(infoRequestDto.getNickname());
                        student.setStudentNumber(infoRequestDto.getStudent_number());
                        student.setStudentName(infoRequestDto.getName());
                        student.setClassroom(existingClassroom);
                        student.setMember(member);
                        String imageUrl = s3ImageService.getImageUrl("feelow");
                        student.setCharacterImagePath(imageUrl);
                        student.setPoint(0);
                        studentRepository.save(student);

                        return ResponseDto.success(HttpStatus.OK, "Student 정보가 저장되었습니다.", student);
                    }
                    // 멤버 타입이 teacher이면 teacher에 추가 정보 저장
                } else if ("teacher".equals(member.getMemberType())) {
                    // 주어진 memberId로 이미 존재하는 Teacher 확인
                    Optional<Teacher> existingTeacher = teacherRepository.findByMember_memberId(member_id);
                    System.out.println("Is existingTeacher present?" + existingTeacher.isPresent());
                    // 이미 존재하는 경우
                    if (existingTeacher.isPresent()) {
                        return ResponseDto.failed(HttpStatus.CONFLICT,"이미 저장된 선생님입니다.", null);
                    } else {
                        // teacher 정보 저장
                        Teacher teacher = Teacher.builder()
                                .teacherName(infoRequestDto.getName())
                                .classroom(existingClassroom)
                                .member(member)
                                .approvalStatus(ApprovalStatus.PENDING)
                                .build();

                        teacherRepository.save(teacher);

                        return ResponseDto.success("Teacher 정보가 저장되었습니다.", teacher);
                    }
                } else {
                    return ResponseDto.failed(HttpStatus.BAD_REQUEST, "유효하지 않은 멤버 타입입니다.", null);
                }

            } else {
                return ResponseDto.failed(HttpStatus.NOT_FOUND, "해당 ID의 회원을 찾을 수 없습니다.", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.failed(HttpStatus.INTERNAL_SERVER_ERROR, "멤버 정보 저장에 실패했습니다." + e.getMessage(), null);
        }
    }




}