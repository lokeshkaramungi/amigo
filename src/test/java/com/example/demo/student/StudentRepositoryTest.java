package com.example.demo.student;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StudentRepositoryTest {

    @Autowired
    private  StudentRepository studentRepository;

    @Test
    void shouldCheckSelectExistsEmail() {
        //given
        String email = "lokesh.murge@gmail.com";
        Student student = new Student("Loki", email,Gender.MALE);

        studentRepository.save(student);

        //when
        Boolean isEmail = studentRepository.selectExistsEmail(email);
        //then
        assertThat(isEmail).isTrue();

    }
}