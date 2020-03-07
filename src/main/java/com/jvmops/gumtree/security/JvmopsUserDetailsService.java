package com.jvmops.gumtree.security;

import com.jvmops.gumtree.user.JvmopsUser;
import com.jvmops.gumtree.user.JvmopsUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JvmopsUserDetailsService implements UserDetailsService {
    private final JvmopsUserRepository jvmopsUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return jvmopsUserRepository.findByEmail(username)
                .map(JvmopsUserDetailsService::convertToUserDetails)
                .orElseThrow(() -> new IllegalStateException(String.format("User %s not found", username)));
    }

    private static UserDetails convertToUserDetails(JvmopsUser jvmopsUser) {
        Set<SimpleGrantedAuthority> grantedAuthorities = jvmopsUser.getAuthorities().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        return User.builder()
                .username(jvmopsUser.getEmail())
                .password(jvmopsUser.getPassword())
                .authorities(grantedAuthorities)
                .build();
    }
}
