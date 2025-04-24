package com.openclassrooms.starterjwt.unit.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class TeacherControllerTest {
	
	  @Mock
	  private TeacherService teacherService;

	  @Mock
	  private TeacherMapper teacherMapper;

	  @InjectMocks
	  private TeacherController teacherController;

	  @Test
	  void testFindById_TeacherExiste_ReturneTeacherDto() {
	    // Arrange
	    String teacherId = "1";
	    Teacher teacher = new Teacher();
	    when(teacherService.findById(anyLong())).thenReturn(teacher);
	    when(teacherMapper.toDto(teacher)).thenReturn(new TeacherDto());

	    // Act
	    ResponseEntity<?> responseEntity = teacherController.findById(teacherId);

	    // Assert
	    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
	    assertThat(responseEntity.getBody()).isInstanceOf(TeacherDto.class);
	  }
	  
	  @Test
	  void testFindById_TeacherExistePas_RetourneNotFound() {
	    // Arrange
	    String teacherId = "1";
	    when(teacherService.findById(anyLong())).thenReturn(null);

	    // Act
	    ResponseEntity<?> responseEntity = teacherController.findById(teacherId);

	    // Assert
	    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	  }
	  
	  @Test
	  void testFindById_FormatIdInvalid_ReturneBadRequest() {
	    // Arrange
	    String invalidTeacherId = "invalidId";

	    // Act
	    ResponseEntity<?> responseEntity = teacherController.findById(
	      invalidTeacherId
	    );

	    // Assert
	    assertThat(responseEntity.getStatusCode())
	      .isEqualTo(HttpStatus.BAD_REQUEST);
	  }

	  @Test
	  void testFindAll_ReturneListeDeTeacherDto() {
	    // Arrange
	    List<Teacher> teachers = Arrays.asList(new Teacher(), new Teacher());
	    when(teacherService.findAll()).thenReturn(teachers);
	    when(teacherMapper.toDto(teachers))
	      .thenReturn(Arrays.asList(new TeacherDto(), new TeacherDto()));

	    // Act
	    ResponseEntity<?> responseEntity = teacherController.findAll();

	    // Assert
	    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
	    assertThat(responseEntity.getBody()).isInstanceOf(List.class);
	    List<?> teacherDtos = (List<?>) responseEntity.getBody();
	    assertThat(teacherDtos)
	      .hasSize(2)
	      .allMatch(dto -> dto instanceof TeacherDto);
	  }



}
