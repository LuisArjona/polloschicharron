package com.sinensia.polloschicharron.security.integration.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sinensia.polloschicharron.security.integration.model.UsuarioPL;
import com.sinensia.polloschicharron.security.integration.repositories.UsuarioPLRepository;

@Service
public class UsuarioServices {

	@Autowired
    private UsuarioPLRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Comprueba que no exista ya un usuario por ese nombre, sino tirará
     * excepción que cazará el controlador, si no existe lo guarda en la 
     * base de datos encriptando la contraseña.
     * 
     * @param username
     * @param password
     * @return
     */
    public UsuarioPL registerUsuario(String username, String password) {
        if (usuarioRepository.existsByUsername(username)) {
            throw new IllegalStateException("El nombre de usuario ya existe");
        }
        UsuarioPL usuario = new UsuarioPL();
        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode(password));
        return usuarioRepository.save(usuario);
    }
}
