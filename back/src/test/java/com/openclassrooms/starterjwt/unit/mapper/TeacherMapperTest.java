package com.openclassrooms.starterjwt.unit.mapper;


import static org.assertj.core.api.Assertions.assertThat;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.mapper.TeacherMapperImpl;
import com.openclassrooms.starterjwt.models.Teacher;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

public class TeacherMapperTest {

  private final TeacherMapper teacherMapper = new TeacherMapperImpl();

  private LocalDateTime fixedDateTime = LocalDateTime.of(2025, 1, 1, 1, 1, 1);

  @Test
  void testTeacherDto_ToTeacherEntity() {
    // Arrange
    TeacherDto teacherDto = createTeacherDto(1L, "firstname", "lastname");

    // Act
    Teacher teacher = teacherMapper.toEntity(teacherDto);

    // Assert
    assertThat(teacher.getId()).isEqualTo(1L);
    assertThat(teacher.getFirstName()).isEqualTo("firstname");
    assertThat(teacher.getLastName()).isEqualTo("lastname");
    assertThat(teacher.getCreatedAt()).isEqualTo(fixedDateTime);
    assertThat(teacher.getUpdatedAt()).isEqualTo(fixedDateTime);
  }

  @Test
  void testerNullTeacherDto_Avec_TeacherEntity_ReturneNull() {
    // Arrange
    TeacherDto teacherDto = null;

    // Act
    Teacher result = teacherMapper.toEntity(teacherDto);

    // Assert
    assertThat(result).isEqualTo(null);
  }

  @Test
  void testerTeacherEntity_Avec_TeacherDto() {
    // Arrange
    Teacher teacher = createTeacher(1L, "firstname", "lastname");

    // Act
    TeacherDto teacherDto = teacherMapper.toDto(teacher);

    // Assert
    assertThat(teacherDto.getId()).isEqualTo(1L);
    assertThat(teacherDto.getFirstName()).isEqualTo("firstname");
    assertThat(teacherDto.getLastName()).isEqualTo("lastname");
    assertThat(teacherDto.getCreatedAt()).isEqualTo(fixedDateTime);
    assertThat(teacherDto.getUpdatedAt()).isEqualTo(fixedDateTime);
  }

  @Test
  void testerNullTeacherEntity_Avec_TeacherDto_RetouirneNull() {
    // Arrange
    Teacher teacher = null;

    // Act
    TeacherDto result = teacherMapper.toDto(teacher);

    // Assert
    assertThat(result).isEqualTo(null);
  }

  @Test
  void testerListeTeacherDto_Avec_ListeTeacherEntity() {
    // Arrange
    List<TeacherDto> teacherDtoList = Arrays.asList(
      createTeacherDto(1L, "firstname1", "lastname1"),
      createTeacherDto(2L, "firstname2", "lastname2")
    );

    // Act
    List<Teacher> teacherList = teacherMapper.toEntity(teacherDtoList);

    // Assert
    assertThat(teacherList).hasSize(2);
    assertThat(teacherList.get(0).getId()).isEqualTo(1L);
    assertThat(teacherList.get(0).getFirstName()).isEqualTo("firstname1");
    assertThat(teacherList.get(0).getLastName()).isEqualTo("lastname1");
    assertThat(teacherList.get(1).getId()).isEqualTo(2L);
    assertThat(teacherList.get(1).getFirstName()).isEqualTo("firstname2");
    assertThat(teacherList.get(1).getLastName()).isEqualTo("lastname2");
  }

  @Test
  void testerListeNullTeacherDto_Avec_ListTeacherEntity_RetourneNull() {
    // Arrange
    List<TeacherDto> teacherDtoList = null;

    // Act
    List<Teacher> result = teacherMapper.toEntity(teacherDtoList);

    // Assert
    assertThat(result).isEqualTo(null);
  }

  @Test
  void testerListTeacher_Avec_ListTeacherDto() {
    // Arrange
    List<Teacher> teacherList = Arrays.asList(
      createTeacher(1L, "firstname1", "lastname1"),
      createTeacher(2L, "firstname2", "lastname2")
    );

    // Act
    List<TeacherDto> teacherDtoList = teacherMapper.toDto(teacherList);

    // Assert
    assertThat(teacherDtoList).hasSize(2);
    assertThat(teacherDtoList.get(0).getId()).isEqualTo(1L);
    assertThat(teacherDtoList.get(0).getFirstName()).isEqualTo("firstname1");
    assertThat(teacherDtoList.get(0).getLastName()).isEqualTo("lastname1");
    assertThat(teacherDtoList.get(1).getId()).isEqualTo(2L);
    assertThat(teacherDtoList.get(1).getFirstName()).isEqualTo("firstname2");
    assertThat(teacherDtoList.get(1).getLastName()).isEqualTo("lastname2");
  }

  @Test
  void testerNullListeTeacher_Avec_ListTeacherDto_RetourneNull() {
    // Arrange
    List<Teacher> teacherList = null;

    // Act
    List<TeacherDto> result = teacherMapper.toDto(teacherList);

    // Assert
    assertThat(result).isEqualTo(null);
  }

  private TeacherDto createTeacherDto(
    Long id,
    String firstName,
    String lastName
  ) {
    TeacherDto teacherDto = new TeacherDto();
    teacherDto.setId(id);
    teacherDto.setFirstName(firstName);
    teacherDto.setLastName(lastName);
    teacherDto.setCreatedAt(fixedDateTime);
    teacherDto.setUpdatedAt(fixedDateTime);
    return teacherDto;
  }

  private Teacher createTeacher(Long id, String firstName, String lastName) {
    Teacher teacher = new Teacher();
    teacher.setId(id);
    teacher.setFirstName(firstName);
    teacher.setLastName(lastName);
    teacher.setCreatedAt(fixedDateTime);
    teacher.setUpdatedAt(fixedDateTime);
    return teacher;
  }
}