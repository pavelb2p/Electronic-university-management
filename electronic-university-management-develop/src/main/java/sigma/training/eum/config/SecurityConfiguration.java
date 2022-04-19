package sigma.training.eum.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
  private final UserDetailsService userDetailsService;

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(getEncoder());
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http
      .csrf()
      .disable()
      .httpBasic().and()
      .authorizeRequests()
      .antMatchers("/whoami").authenticated()
      .antMatchers("/students/**", "/tutors/**", "/courses/**", "/assignments/**", "/tasks/**",
        "/courses/{course-id}/remove-student", "/courses/{course-id}/add-student").access("hasAnyAuthority('admin')")
      .antMatchers("/my-courses/{id}/start", "/my-assignments/{id}/start", "/my-tasks/{id}/return", "/my-tasks/{id}/finish", "/my-assignments/{id}/tasks",
        "/my-assignments").access("hasAnyAuthority('tutor')")
      .antMatchers("/assignable-courses/**","/my-tasks/{id}/progress").access("hasAnyAuthority('student')")
      .antMatchers("/my-courses", "/my-tasks").access("hasAnyAuthority('tutor', 'student')");
  }
  @Bean
  public BCryptPasswordEncoder getEncoder() {
    return new BCryptPasswordEncoder();
  }
}
