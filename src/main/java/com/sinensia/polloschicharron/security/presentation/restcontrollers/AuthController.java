package com.sinensia.polloschicharron.security.presentation.restcontrollers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sinensia.polloschicharron.presentation.config.PresentationException;
import com.sinensia.polloschicharron.security.JwtUtils;
import com.sinensia.polloschicharron.security.integration.services.UsuarioServices;
import com.sinensia.polloschicharron.security.payloads.JwtResponse;
import com.sinensia.polloschicharron.security.payloads.LoginRequest;
import com.sinensia.polloschicharron.security.payloads.RegisterRequest;

import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private UsuarioServices usuarioServices;
    
    /**
     * Intenta autenticar al usuario con su usuario y password (ejecuta
     * automaticamente el loadByUsername de UserDetailsService y hace uso del Encoder para comparar contraseñas)
     * si lo autentica establece la autenticacion en el contexto de seguridad y genera el token con la autenticacion
     * 
     * @param usuario y password
     * @return token
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {

    	Authentication authentication = null;
    	
    	try {
    		authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
    	} catch(Exception e) {
    		throw new PresentationException("bad credentials", HttpStatus.UNAUTHORIZED);
    	}
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generarJwt(authentication);
        
        return ResponseEntity.ok(new JwtResponse(jwt));
    }
    
    /**
     * Si el servicio puede registrar al usuario devuelve un 200 ok
     * si no un 400 bad request
     * 
     * @param register credentials
     * @return status
     */
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody RegisterRequest registerRequest){
    	
    	try {
    		usuarioServices.registerUsuario(registerRequest.getUsername(), registerRequest.getPassword());
    	}catch(IllegalStateException e) {
    		throw new PresentationException(e.getMessage(), HttpStatus.BAD_REQUEST);
    	}
    	
    	return ResponseEntity.ok().build();
    }
}
