package com.example.demo.controller;

import com.example.demo.model.Student;
import com.example.demo.repository.JDBCRepository.JDBCStudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = {("*")})
@RestController
public class StudentRestController {
    @Autowired
    private JDBCStudentRepository studentRepository;

    @RequestMapping(value = "/students", method = RequestMethod.GET)
    public ResponseEntity<List<Student>> findAll() {
        List<Student> students = studentRepository.findAll();
        if (students.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @RequestMapping(value = "/students/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Student> getStudentById(
            @PathVariable("id") Long id) throws ParseException {
        Optional<Student> student = Optional.ofNullable(studentRepository.findById(id));
        if (!student.isPresent()) {
            return new ResponseEntity<>(student.get(),
                    HttpStatus.NO_CONTENT);
        }
//        String date =new SimpleDateFormat("yyyy-MM-dd").format(student.get().getBirthDate());
//        Date date1= new SimpleDateFormat("yyyy-MM-dd").parse(date);
//        System.out.println(date1);
//        student.get().setBirthDate(date1);
        return new ResponseEntity<>(student.get(), HttpStatus.OK);
    }

    @RequestMapping(value = "/students",
            method = RequestMethod.POST)
    public ResponseEntity<Student> createStudent(
            @RequestBody Student student,
            UriComponentsBuilder builder) {
        studentRepository.save(student);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("students/{id}")
                .buildAndExpand(student.getId()).toUri());
        return new ResponseEntity<>(student, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/students/{id}",
            method = RequestMethod.PUT)
    public ResponseEntity<Student> updateStudent(
            @PathVariable("id") Long id,
            @RequestBody Student student) {
        Optional<Student> currentStudent = Optional.ofNullable(studentRepository.findById(id));
        if (!currentStudent.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        currentStudent.get().setName(student.getName());
        currentStudent.get().setBirthDate(student.getBirthDate());
        currentStudent.get().setGender(student.getGender());
        currentStudent.get().setId_class(student.getId_class());
        currentStudent.get().setStudentCode(student.getStudentCode());
        studentRepository.update(currentStudent.get());
        return new ResponseEntity<>(currentStudent.get(), HttpStatus.OK);
    }

    @RequestMapping(value = "/students/delete/{id}",
            method = RequestMethod.PUT)
    public ResponseEntity<Student> deleteStudent(
            @PathVariable("id") Long id) {
        Optional<Student> student = Optional.ofNullable(studentRepository.findById(id));
        if (!student.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        studentRepository.deleteById(student.get().getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/classes/{id}/students/", method = RequestMethod.GET)
    public ResponseEntity<List<Student>> findAllByIdClass(@PathVariable("id") Long id) {
        List<Student> students = studentRepository.findAllByIdClass(id);
        if (students.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(students, HttpStatus.OK);
    }
}
