 package com.prodapt.learningspring.rank.controller;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties.Cache.Connection;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prodapt.learningspring.rank.model.Classroom;
import com.prodapt.learningspring.rank.model.Student;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/classroom")
public class ClassroomController {
	 static {
	        try {
	            Class.forName("com.mysql.cj.jdbc.Driver");
	        } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	        }
	    }
  @Autowired
  private Classroom classroom;

  @GetMapping
  public String listAllStudents(Model model) {
    if (!model.containsAttribute("student"))
      model.addAttribute("student", new Student());
    model.addAttribute("students", classroom.getStudents());
    return "classroom";
  }

  @PostMapping("/add")
  public String addStudent(@Valid Student student, BindingResult bindingResult, RedirectAttributes attr) {
    if (bindingResult.hasErrors()) {
      attr.addFlashAttribute("org.springframework.validation.BindingResult.student", bindingResult);
      attr.addFlashAttribute("student", student);
      return "redirect:/classroom";
    }
   
    classroom.add(student);
    insertStudentIntoDatabase(student);
    return "redirect:/classroom";
     
  }

  @PostMapping("/delete")
  public String deleteStudent(@RequestParam int idx) {
    
   System.out.println(idx);
    classroom.remove(idx);
    System.out.println(idx);
    deleteStudentFromDatabase(idx);
    return "redirect:/classroom";
  }
 
  @GetMapping("/edit")
  public String editStudentForm(@RequestParam int id, Model model) {
    Student student;
    if (!model.containsAttribute("student")) {
      student = classroom.getById(id).get();
      model.addAttribute("student", student);
    }
    return "studentEditForm";
  }

  @PostMapping("/edit")
  public String editStudent(@Valid Student student,BindingResult bindingResult, RedirectAttributes attr) {
    if (bindingResult.hasErrors()) {
      attr.addFlashAttribute("org.springframework.validation.BindingResult.student", bindingResult);
      attr.addFlashAttribute("student", student);
      attr.addAttribute("id", student.getId());
      return "redirect:/classroom/edit";
    }
    else {
      classroom.replace(student.getId(), student);
      return "redirect:/classroom";
    }
  }
 
//  private List<Student> fetchStudentsFromDatabase() {
//      // Fetch students using JDBC SELECT query
//      List<Student> students = new ArrayList<>();
//      try {
//    	  cnx = DatabaseConnection.getConnection();
//      
//           stmt = cnx.prepareStatement("SELECT * FROM student_table")) {
//          ResultSet resultSet = statement.executeQuery();
//          while (resultSet.next()) {
//              Student student = new Student();
//              student.setId(resultSet.getInt("id"));
//              student.setName(resultSet.getString("name"));
//              // Set other properties
//              students.add(student);
//          }
//      } catch (SQLException e) {
//          e.printStackTrace();
//      }
//      return students;
//  }

  private void insertStudentIntoDatabase(Student student) {
     java.sql.Connection cnx = null;
		 PreparedStatement stmt = null;
      try {
    	  cnx= DriverManager.getConnection("jdbc:mysql://localhost:3306/spring","Database","atharva");
  	     stmt= cnx.prepareStatement("INSERT INTO student (name,score,ranks) VALUES (?,?,?)");
  	     System.out.println(student.getName());
  	    
          stmt.setString(1,student.getName());
          stmt.setInt(2,student.getScore());
          stmt.setInt(3,student.getRank());
          
          stmt.executeUpdate();
      } catch (SQLException e) {
          e.printStackTrace();
      }
  }

  private void deleteStudentFromDatabase(int id) {
      System.out.println(id);
	  java.sql.Connection cnx = null;
		 PreparedStatement stmt = null;
      try {
    	  cnx= DriverManager.getConnection("jdbc:mysql://localhost:3306/spring","Database","atharva");
        stmt = cnx.prepareStatement("DELETE FROM student WHERE id = ?");
          stmt.setInt(1, id);
          stmt.executeUpdate();
      } catch (SQLException e) {
          e.printStackTrace();
      }
  }
//
//  private Student fetchStudentByIdFromDatabase(int id) {
//      // Fetch student using JDBC SELECT query
//      Student student = new Student();
//      try (Connection connection = DatabaseConnection.getConnection();
//           PreparedStatement statement = connection.prepareStatement("SELECT * FROM student_table WHERE id = ?")) {
//          statement.setInt(1, id);
//          ResultSet resultSet = statement.executeQuery();
//          if (resultSet.next()) {
//              student.setId(resultSet.getInt("id"));
//              student.setName(resultSet.getString("name"));
//              // Set other properties
//          }
//      } catch (SQLException e) {
//          e.printStackTrace();
//      }
//      return student;
//  }
//
//  private void updateStudentInDatabase(Student student) {
//      // Update student using JDBC UPDATE query
//      try (Connection connection = DatabaseConnection.getConnection();
//           PreparedStatement statement = connection.prepareStatement("UPDATE student_table SET name = ? WHERE id = ?")) {
//          statement.setString(1, student.getName());
//          statement.setInt(2, student.getId());
//          statement.executeUpdate();
//      } catch (SQLException e) {
//          e.printStackTrace();
//      }
//  }


}