package com.sinensia.polloschicharron.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthTokenFilter extends OncePerRequestFilter{

	 @Autowired
	 private JwtUtils jwtUtils;
	 
	 /**
	  * Realiza el filtro del token JWT 1. Lo parsea 2. Comprueba que existe y valida su firma
	  * 3. Si es valido obtiene el id del usuario y establece un token de autenticacion
	  * 4. Establece la autenticación en el contexto de seguridad y ya valid al usuario
	  * 5. Finalmente indica a la cadena de filtros que siga.
	  * 
	  */
	 @Override
	 protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		 
		 String jwt = parsearJwt(request);
		 try {
			 
			 if (jwt != null && jwtUtils.validarJwt(jwt)) {
				 Long userId = jwtUtils.getUserIdFromToken(jwt);

				 UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
				 authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				 SecurityContextHolder.getContext().setAuthentication(authentication);
			 } else {
	                logger.warn("Token JWT no presente o inválido para la solicitud: {}" + request.getRequestURI());
	            }
		 } catch (Exception e) {
			 logger.error("Cannot set user authentication: {}", e);
		 }
		 filterChain.doFilter(request, response);
	 }
	 
	 /**
	  * Comprueba que existe el header de authorization y que
	  * empieza por "Bearer " si es asi parsea el token sin el Bearer
	  * , en caso contrario devuelve null.
	  * 
	  * @param request
	  * @return Token JWT parseado
	  */
	 private String parsearJwt(HttpServletRequest request) {
	    	
		String headerAuth = request.getHeader("Authorization"); 
	        
		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			 return headerAuth.substring(7);
		}
		
		return null;
	}
}
