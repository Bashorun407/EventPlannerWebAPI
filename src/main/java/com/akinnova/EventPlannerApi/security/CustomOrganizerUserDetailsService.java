package com.akinnova.EventPlannerApi.security;

import com.akinnova.EventPlannerApi.entity.Organizer;
import com.akinnova.EventPlannerApi.repository.OrganizerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomOrganizerUserDetailsService implements UserDetailsService {

    @Autowired
    private OrganizerRepository organizerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Organizer organizer = organizerRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Organizer with this username not found" + username));

        Set<GrantedAuthority> authorities = organizer.getRoles().stream()
                .map((role) -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toSet());

        return new User(organizer.getUsername(), organizer.getPassword(), authorities);
    }
}
