package sigma.training.eum.user.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import sigma.training.eum.user.dictionary.Role;
import sigma.training.eum.user.exception.IllegalIdException;
import sigma.training.eum.user.exception.UserNotFoundException;
import sigma.training.eum.user.mapper.entity.UserEntityMapper;
import sigma.training.eum.user.persistence.entity.UserEntity;
import sigma.training.eum.user.persistence.repository.UserRepository;
import sigma.training.eum.user.service.type.User;
import sigma.training.eum.user.service.type.UserId;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
  @Mock
  private UserRepository userRepository;
  @InjectMocks
  private UserService userService;
  @Mock
  private UserEntityMapper userEntityMapper;
  private static final SecurityContextImpl securityContext = new SecurityContextImpl();
  private static final UserEntity userEntrity = new UserEntity();
  private static final UserEntity userEntityDisabled = new UserEntity();
  static {
    userEntrity.setId(1L);
    userEntrity.setLogin("myLogin");
    userEntrity.setPassword("123456erge");
    userEntrity.setRole(Role.ADMIN);
    userEntrity.setEnabled(true);

    userEntityDisabled.setId(1L);
    userEntityDisabled.setLogin("myLogin");
    userEntityDisabled.setPassword("123456erge");
    userEntityDisabled.setRole(Role.ADMIN);
    userEntityDisabled.setEnabled(false);
    securityContext.setAuthentication(new Authentication() {
      @Override
      public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ADMIN"));
      }
      @Override
      public Object getCredentials() {
        return null;
      }
      @Override
      public Object getDetails() {
        return null;
      }
      @Override
      public Object getPrincipal() {
        return user;
      }
      @Override
      public boolean isAuthenticated() {
        return false;
      }
      @Override
      public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

      }
      @Override
      public String getName() {
        return null;
      }
    });
  }
  private static final List<UserEntity> USER_ENTITIES = Collections.singletonList(userEntrity);
  private static final List<UserEntity> USER_ENTITIES_DISABLED = Collections.singletonList(userEntityDisabled);
  private static final User user = new User(new UserId(1L), Role.ADMIN, "myLogin", "123456erge", true);
  private static final User disabledUser = new User(new UserId(1L), Role.ADMIN, "myLogin", "123456erge", false);
  @Test
  public void getUserDetailsByCorrectLogin(){
    when(userRepository.findUserEntityByLogin("myLogin")).thenReturn(USER_ENTITIES.stream().findFirst());
    when(userEntityMapper.fromEntity(USER_ENTITIES.get(0))).thenReturn(user);
    UserDetails userByLogin = userService.loadUserByUsername("myLogin");
    assertNotNull(userByLogin);
    assertEquals(userByLogin.getPassword(), user.getPassword());
    assertEquals(userByLogin.getUsername(), user.getUsername());
    verify(userRepository).findUserEntityByLogin("myLogin");
    verify(userEntityMapper).fromEntity(any());
  }
  @Test
  public void failToGetUserDetailsByNullLogin(){
    Exception exception = assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(null));
    assertEquals("Login can't be null", exception.getMessage());
  }
  @Test
  public void failToGetUserDetailsByIncorrectLogin(){
    Exception exception = assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("incorrect"));
    assertEquals("incorrect was not found", exception.getMessage());
    verify(userRepository).findUserEntityByLogin(any());
  }
  @Test
  @SneakyThrows({UserNotFoundException.class, IllegalIdException.class})
  public void getExistingUser(){
    when(userRepository.findUserEntityById(1L)).thenReturn(USER_ENTITIES.stream().findFirst());
    when(userEntityMapper.fromEntity(USER_ENTITIES.get(0))).thenReturn(user);
    User userById = userService.get(new UserId(1L));
    assertNotNull(user);
    assertEquals(userById, user);
    verify(userRepository).findUserEntityById(1L);
  }
  @Test
  public void failToGetUserByNotExistingId(){
    when(userRepository.findUserEntityById(10L)).thenReturn(Optional.empty());
    Exception exception = assertThrows(UserNotFoundException.class, () -> userService.get(new UserId(10L)));
    assertEquals("Incorrect user id", exception.getMessage());
    verify(userRepository).findUserEntityById(any());
    assertEquals(Optional.empty(),userRepository.findUserEntityById(10L));
  }
  @Test
  public void failToGetUserByNullId(){
    assertThrows(IllegalIdException.class, () -> userService.get(null));
  }
  @Test
  public void shouldObtainCurrentUserRole(){
    var mocked = mockStatic(SecurityContextHolder.class);
    mocked.when(SecurityContextHolder::getContext).thenReturn(securityContext);
    assertEquals("ADMIN", userService.getCurrentUserRole());
  }
  @Test
  public void shouldObtainCurrentUser(){
    var mocked = mockStatic(SecurityContextHolder.class);
    mocked.when(SecurityContextHolder::getContext).thenReturn(securityContext);
    assertEquals(user,userService.getCurrentUser());
    mocked.close();
  }
  @Test
  public void disableUserTest(){
    when(userRepository.findUserEntityById(1L)).thenReturn(USER_ENTITIES_DISABLED.stream().findFirst());
    when(userEntityMapper.fromEntity(USER_ENTITIES_DISABLED.get(0))).thenReturn(disabledUser);
    when(userEntityMapper.toEntity(disabledUser)).thenReturn(USER_ENTITIES_DISABLED.get(0));
    when(userRepository.save(USER_ENTITIES_DISABLED.get(0))).thenReturn(USER_ENTITIES_DISABLED.get(0));
    User expected = new User(new UserId(1L), Role.ADMIN, "myLogin", "123456erge", false);
    assertEquals(expected, userService.disableUser(new UserId(1L)));
  }
}
