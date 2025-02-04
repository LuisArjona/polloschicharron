package com.sinensia.polloschicharron.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sinensia.polloschicharron.security.integration.model.UsuarioPL;
import com.sinensia.polloschicharron.security.integration.repositories.UsuarioPLRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UsuarioPLRepository usuarioPLRepository;
	
	/**
	 * Método que se ejecuta cuando el AuthenticationManager quiera autenticar
	 * de manera automatica, simplemente devuelve los UserDetails del usuario
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UsuarioPL usuario = usuarioPLRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username " + username));
	
		return new UserDetailsImpl(usuario);
	}

}
