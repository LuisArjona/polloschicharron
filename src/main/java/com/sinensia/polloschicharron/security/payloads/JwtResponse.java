package com.sinensia.polloschicharron.security.payloads;

import java.io.Serializable;
import java.util.List;

public class JwtResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String token;
    
	public JwtResponse(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}


  
}
