package com.example.demo.student;

import com.example.demo.student.exception.BadRequestException;
import com.example.demo.student.exception.StudentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    private StudentService studentService;


    @BeforeEach
    void setUp() {
        studentService = new StudentService(studentRepository);
    }

    @Test
    void getAllStudents() {
        //when
        studentService.getAllStudents();
        //then
        verify(studentRepository).findAll();
    }

    @Test
    @Disabled
    void willThrowWhenEmailIsTaken() {
        // given
        Student student = new Student(
                "Jamila",
                "jamila@gmail.com",
                Gender.FEMALE
        );

        given(studentRepository.selectExistsEmail(anyString()))
                .willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> studentService.addStudent(student))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email " + student.getEmail() + " taken");

        verify(studentRepository, never()).save(any());

    }
    @Test

    void canAddStudent() {
        //given
        Student student = new Student("Loki", "lokesh.murge@gmail.com",Gender.MALE);
        //when
        studentService.addStudent(student);
        //then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(studentArgumentCaptor.capture());

        Student value = studentArgumentCaptor.getValue();
        assertThat(value).isEqualTo(student);
    }

    @Test
    void canDeleteStudent() {
        //given
        Long id = 10L;
        given(studentRepository.existsById(id)).willReturn(true);
        //when
        studentService.deleteStudent(10L);
        //then
        verify(studentRepository).deleteById(10L);
    }

    @Test
    void canDeleteStudentIfNotPresent() {
        //given
        Long id = 10L;
        given(studentRepository.existsById(id)).willReturn(false);
        //when
        assertThatThrownBy(()->studentService.deleteStudent(id))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessage( "Student with id " + id + " does not exists");
        //then
        verify(studentRepository,never()).deleteById(any());
    }
}