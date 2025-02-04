package com.sinensia.polloschicharron.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.sinensia.polloschicharron.security.integration.model.UsuarioPL;

public class UserDetailsImpl implements UserDetails{
	private static final long serialVersionUID = 1L;

	private final UsuarioPL usuario;
	
	public UserDetailsImpl(UsuarioPL usuario) {
        this.usuario = usuario;
    }
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList();
	}

	@Override
	public String getPassword() {
		return usuario.getPassword();
	}

	@Override
	public String getUsername() {
		return usuario.getUsername();
	}
	

    public Long getId() {
		return usuario.getId();
	}


	public String getEmail() {
		return usuario.getEmail();
	}



}
