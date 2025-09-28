package com.example.demo.Service;

import com.example.demo.Model.Student;
import com.example.demo.Repository.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class myUserDetailsService implements UserDetailsService {

    @Autowired
    private StudentRepo studentRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Student student = studentRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("student not found for email"));

        return User.builder()
                .username(student.getEmail())
                .password(student.getPassword())
                .roles("STUDENT")
                .build();

    }
}
