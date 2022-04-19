package sigma.training.eum.user.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sigma.training.eum.user.exception.IllegalIdException;
import sigma.training.eum.user.exception.UserNotFoundException;
import sigma.training.eum.user.mapper.entity.UserEntityMapper;
import sigma.training.eum.user.persistence.repository.UserRepository;
import sigma.training.eum.user.service.type.User;
import sigma.training.eum.user.service.type.UserId;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
  private final UserRepository userRepository;
  private final UserEntityMapper userEntityMapper;

  @Override
  public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
    if (login == null) throw new UsernameNotFoundException("Login can't be null");
    Optional<UserDetails> userDetails = userRepository.findUserEntityByLogin(login).map(userEntityMapper::fromEntity);
    if(userDetails.isPresent() && userDetails.get().isEnabled()){
      return userDetails.get();
    }
    throw new UsernameNotFoundException(login + " was not found");
  }


  public String getCurrentUserRole() {
    return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
      .map(GrantedAuthority::getAuthority)
      .collect(Collectors.joining(","));
  }
  public User getCurrentUser() {
    return (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
  public User get(UserId id) throws UserNotFoundException {
    if (id == null) throw new IllegalIdException("Id cannot be null");
    return userRepository.findUserEntityById(id.value())
      .map(userEntityMapper::fromEntity).orElseThrow(() -> new UserNotFoundException("Incorrect user id"));
  }
  public User disableUser(UserId userId){
    User oldUser = get(userId);
    return userEntityMapper.fromEntity(
      userRepository.save(
        userEntityMapper.toEntity(new User(oldUser.userId(), oldUser.role(), oldUser.login(), oldUser.password(), false)
        )
      )
    );
  }
}
