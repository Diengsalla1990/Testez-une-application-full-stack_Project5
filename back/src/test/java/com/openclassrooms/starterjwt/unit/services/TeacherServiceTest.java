package com.openclassrooms.starterjwt.unit.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;

@SpringBootTest
public class TeacherServiceTest {
	
	  @InjectMocks
	  private TeacherService teacherService;

	  @Mock
	  private TeacherRepository teacherRepository;

	  private Long testTeacherId = 1L;
	  private LocalDateTime fixedDateTime = LocalDateTime.of(2025, 1, 1, 1, 1, 1);

	  private Teacher testTeacher;
	  private List<Teacher> testTeachersList;

	  @BeforeEach
	  public void setUp() {
	    testTeacher = new Teacher();
	    testTeacher.setId(testTeacherId);
	    testTeacher.setFirstName("firstName");
	    testTeacher.setLastName("lastName");
	    testTeacher.setCreatedAt(fixedDateTime);
	    testTeacher.setUpdatedAt(fixedDateTime);

	    testTeachersList = new ArrayList<Teacher>();
	    testTeachersList.add(testTeacher);
	  }

	  @Test
	  public void testerFindAllTeachers() {
	    // Arrange
	    List<Teacher> teachersListUnderTest = testTeachersList;
	    when(teacherRepository.findAll()).thenReturn(teachersListUnderTest);

	    // Act
	    List<Teacher> TeachersList = teacherService.findAll();

	    // Assert
	    assertThat(TeachersList).isEqualTo(teachersListUnderTest);
	    verify(teacherRepository).findAll();
	  }

	  @Test
	  public void testerFindTeacherById_IdExiste() {
	    // Arrange
	    Long teacherIdUnderTest = testTeacherId;
	    Teacher teacherUnderTest = testTeacher;
	    when(teacherRepository.findById(teacherIdUnderTest))
	      .thenReturn(Optional.of(teacherUnderTest));

	    // Act
	    Teacher retrievedTeacher = teacherService.findById(teacherIdUnderTest);

	    // Assert
	    assertThat(retrievedTeacher).isEqualTo(teacherUnderTest);
	    verify(teacherRepository).findById(teacherIdUnderTest);
	  }

	  @Test
	  public void testerFindTeacherParId_IdnonExiste() {
	    // Arrange
	    Long nonExistingTeacherId = 99L;
	    when(teacherRepository.findById(nonExistingTeacherId))
	      .thenReturn(Optional.empty());

	    // Act
	    Teacher retrievedTeacher = teacherService.findById(nonExistingTeacherId);

	    // Assert
	    assertThat(retrievedTeacher).isNull();
	    verify(teacherRepository).findById(nonExistingTeacherId);
	  }

}
