package com.openclassrooms.starterjwt.unit.mapper;


import static org.assertj.core.api.Assertions.assertThat;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.mapper.UserMapperImpl;
import com.openclassrooms.starterjwt.models.User;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

public class UserMapperTest {

  private final UserMapper userMapper = new UserMapperImpl();

  private LocalDateTime fixedDateTime = LocalDateTime.of(2025, 1, 1, 1, 1, 1);

  @Test
  void testreUserDto_AvecUserEntity() {
    // Arrange
    UserDto userDto = createUserDto(
      1L,
      "test@email.com",
      "lastname",
      "firstname",
      true,
      "password"
    );

    // Act
    User user = userMapper.toEntity(userDto);

    // Assert
    assertThat(user.getId()).isEqualTo(1L);
    assertThat(user.getEmail()).isEqualTo("test@email.com");
    assertThat(user.getLastName()).isEqualTo("lastname");
    assertThat(user.getFirstName()).isEqualTo("firstname");
    assertThat(user.isAdmin()).isEqualTo(true);
    assertThat(user.getPassword()).isEqualTo("password");
    assertThat(user.getCreatedAt()).isEqualTo(fixedDateTime);
    assertThat(user.getUpdatedAt()).isEqualTo(fixedDateTime);
  }

  @Test
  void testerNullUserDto_AvecUserEntity_RetournNull() {
    // Arrange
    UserDto userDto = null;

    // Act
    User result = userMapper.toEntity(userDto);

    // Assert
    assertThat(result).isEqualTo(null);
  }

  @Test
  void testerUserEntity_ToUserDto() {
    // Arrange
    User user = createUser(
      1L,
      "test@test.com",
      "lastname",
      "firstname",
      true,
      "password"
    );

    // Act
    UserDto userDto = userMapper.toDto(user);

    // Assert
    assertThat(userDto.getId()).isEqualTo(1L);
    assertThat(userDto.getEmail()).isEqualTo("test@test.com");
    assertThat(userDto.getLastName()).isEqualTo("lastname");
    assertThat(userDto.getFirstName()).isEqualTo("firstname");
    assertThat(userDto.isAdmin()).isEqualTo(true);
    assertThat(userDto.getPassword()).isEqualTo("password");
    assertThat(userDto.getCreatedAt()).isEqualTo(fixedDateTime);
    assertThat(userDto.getUpdatedAt()).isEqualTo(fixedDateTime);
  }

  @Test
  void testerNullUserEntity_ToUserDto_retourneNull() {
    // Arrange
    User user = null;

    // Act
    UserDto result = userMapper.toDto(user);

    // Assert
    assertThat(result).isEqualTo(null);
  }

  @Test
  void testUserDtoList_ToUserEntityList() {
    // Arrange
    List<UserDto> userDtoList = Arrays.asList(
      createUserDto(
        1L,
        "test1@test.com",
        "lastname1",
        "firstname1",
        true,
        "password1"
      ),
      createUserDto(
        2L,
        "test2@test.com",
        "lastname2",
        "firstname2",
        false,
        "password2"
      )
    );

    // Act
    List<User> userList = userMapper.toEntity(userDtoList);

    // Assert
    assertThat(userList).hasSize(2);
    assertThat(userList.get(0).getId()).isEqualTo(1L);
    assertThat(userList.get(0).getEmail()).isEqualTo("test1@test.com");
    assertThat(userList.get(0).getLastName()).isEqualTo("lastname1");
    assertThat(userList.get(0).getFirstName()).isEqualTo("firstname1");
    assertThat(userList.get(0).isAdmin()).isEqualTo(true);
    assertThat(userList.get(0).getPassword()).isEqualTo("password1");
    assertThat(userList.get(0).getCreatedAt()).isEqualTo(fixedDateTime);
    assertThat(userList.get(0).getUpdatedAt()).isEqualTo(fixedDateTime);
    assertThat(userList.get(1).getId()).isEqualTo(2L);
    assertThat(userList.get(1).getEmail()).isEqualTo("test2@test.com");
    assertThat(userList.get(1).getLastName()).isEqualTo("lastname2");
    assertThat(userList.get(1).getFirstName()).isEqualTo("firstname2");
    assertThat(userList.get(1).isAdmin()).isEqualTo(false);
    assertThat(userList.get(1).getPassword()).isEqualTo("password2");
    assertThat(userList.get(1).getCreatedAt()).isEqualTo(fixedDateTime);
    assertThat(userList.get(1).getUpdatedAt()).isEqualTo(fixedDateTime);
  }

  @Test
  void testNullUserDtoList_ToUserEntityList_ShouldReturnNull() {
    // Arrange
    List<UserDto> userDtoList = null;

    // Act
    List<User> result = userMapper.toEntity(userDtoList);

    // Assert
    assertThat(result).isEqualTo(null);
  }

  @Test
  void testerListeUser_AvecListeUserDto() {
    // Arrange
    List<User> userList = Arrays.asList(
      createUser(
        1L,
        "test1@test.com",
        "lastname1",
        "firstname1",
        true,
        "password1"
      ),
      createUser(
        2L,
        "test2@test.com",
        "lastname2",
        "firstname2",
        false,
        "password2"
      )
    );

    // Act
    List<UserDto> userDtoList = userMapper.toDto(userList);

    // Assert
    assertThat(userDtoList).hasSize(2);
    assertThat(userDtoList.get(0).getId()).isEqualTo(1L);
    assertThat(userDtoList.get(0).getEmail()).isEqualTo("test1@test.com");
    assertThat(userDtoList.get(0).getLastName()).isEqualTo("lastname1");
    assertThat(userDtoList.get(0).getFirstName()).isEqualTo("firstname1");
    assertThat(userDtoList.get(0).isAdmin()).isEqualTo(true);
    assertThat(userDtoList.get(0).getPassword()).isEqualTo("password1");
    assertThat(userDtoList.get(0).getCreatedAt()).isEqualTo(fixedDateTime);
    assertThat(userDtoList.get(0).getUpdatedAt()).isEqualTo(fixedDateTime);
    assertThat(userDtoList.get(1).getId()).isEqualTo(2L);
    assertThat(userDtoList.get(1).getEmail()).isEqualTo("test2@test.com");
    assertThat(userDtoList.get(1).getLastName()).isEqualTo("lastname2");
    assertThat(userDtoList.get(1).getFirstName()).isEqualTo("firstname2");
    assertThat(userDtoList.get(1).isAdmin()).isEqualTo(false);
    assertThat(userDtoList.get(1).getPassword()).isEqualTo("password2");
    assertThat(userDtoList.get(1).getCreatedAt()).isEqualTo(fixedDateTime);
    assertThat(userDtoList.get(1).getUpdatedAt()).isEqualTo(fixedDateTime);
  }

 

  private UserDto createUserDto(
    Long id,
    String email,
    String lastName,
    String firstName,
    boolean admin,
    String password
  ) {
    UserDto userDto = new UserDto();
    userDto.setId(id);
    userDto.setEmail(email);
    userDto.setLastName(lastName);
    userDto.setFirstName(firstName);
    userDto.setAdmin(admin);
    userDto.setPassword(password);
    userDto.setCreatedAt(fixedDateTime);
    userDto.setUpdatedAt(fixedDateTime);
    return userDto;
  }

  private User createUser(
    Long id,
    String email,
    String lastName,
    String firstName,
    boolean admin,
    String password
  ) {
    User user = new User();
    user.setId(id);
    user.setEmail(email);
    user.setLastName(lastName);
    user.setFirstName(firstName);
    user.setAdmin(admin);
    user.setPassword(password);
    user.setCreatedAt(fixedDateTime);
    user.setUpdatedAt(fixedDateTime);
    return user;
  }
}