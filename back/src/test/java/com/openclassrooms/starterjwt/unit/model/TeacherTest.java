package com.openclassrooms.starterjwt.unit.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.models.Teacher;

@SpringBootTest
public class TeacherTest {
	private final Validator validator = Validation
		    .buildDefaultValidatorFactory()
		    .getValidator();
		  private Long testTeacherId = 1L;
		  private LocalDateTime fixedDateTime = LocalDateTime.of(2025, 1, 1, 1, 1, 1);

		  private Teacher testTeacher;

		  @BeforeEach
		  public void setUp() {
		    testTeacher =
		      Teacher
		        .builder()
		        .id(testTeacherId)
		        .firstName("Ibra")
		        .lastName("Dieng")
		        .createdAt(fixedDateTime)
		        .updatedAt(fixedDateTime)
		        .build();
		  }
		  
		  @Test
		  public void testeTeacherModel() {
		    Teacher teacherTest = testTeacher;

		    assertThat(teacherTest.getId()).isEqualTo(1L);
		    assertThat(teacherTest.getFirstName()).isEqualTo("Ibra");
		    assertThat(teacherTest.getLastName()).isEqualTo("Dieng");
		    assertThat(teacherTest.getCreatedAt()).isEqualTo(fixedDateTime);
		    assertThat(teacherTest.getUpdatedAt()).isEqualTo(fixedDateTime);
		  }
		  
		  @Test
		  @SuppressWarnings("ConstantConditions")
		  public void testerTeacherModel_Avec_ChampsVides() {
		    Teacher teacherUnderTest = Teacher
		      .builder()
		      .id(null)
		      .firstName(null)
		      .lastName(null)
		      .createdAt(null)
		      .updatedAt(null)
		      .build();

		    Set<ConstraintViolation<Teacher>> violations = validator.validate(
		      teacherUnderTest
		    );

		    assertThat(violations).isNotEmpty();
		  }
		  
		  @Test
		  public void testerTeacherModel_ToString() {
		    Teacher teacherTest = testTeacher;

		    String expectedToString =
		      "Teacher(id=" +
		      testTeacherId +
		      ", lastName=Dieng, firstName=Ibra, createdAt=" +
		      fixedDateTime +
		      ", updatedAt=" +
		      fixedDateTime +
		      ")";

		    assertEquals(expectedToString, teacherTest.toString());
		  }
		  
		  @Test
		  public void testerEqualsEtHashCode() {
		    Teacher teacherTest1 = Teacher.builder().id(testTeacherId).build();
		    Teacher teacherTest2 = Teacher.builder().id(testTeacherId).build();
		    Teacher teacherTest3 = Teacher.builder().id(2L).build();

		    assertEquals(teacherTest1, teacherTest2);
		    assertNotEquals(teacherTest1, teacherTest3);

		    assertEquals(teacherTest1.hashCode(), teacherTest2.hashCode());
		    assertNotEquals(teacherTest1.hashCode(), teacherTest3.hashCode());
		  }

}
