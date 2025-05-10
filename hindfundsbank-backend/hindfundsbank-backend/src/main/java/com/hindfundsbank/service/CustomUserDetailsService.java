package com.hindfundsbank.service;

import com.hindfundsbank.model.User;
import com.hindfundsbank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  @Autowired private UserRepository userRepo;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepo.findByEmail(email)
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    // all users get ROLE_USER
    List<GrantedAuthority> auths = List.of(new SimpleGrantedAuthority("ROLE_USER"));
    return new org.springframework.security.core.userdetails.User(
      user.getEmail(), user.getPassword(), auths);
  }
}
