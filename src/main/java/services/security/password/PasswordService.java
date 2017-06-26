package services.security.password;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    @Value("${jwt.secret}")
    private String secret;
	
    private PasswordEncoder passwordEncoder;
    
	@PostConstruct
    public void init() {
    	this.passwordEncoder = new StandardPasswordEncoder(this.secret);
	}	
	
	public String encode(String rawPassword) {
		return this.passwordEncoder.encode(rawPassword);
	}
	
	public boolean equals(String rawPassword, String encodedPassword) {
		
		return this.passwordEncoder.matches(rawPassword, encodedPassword);
	}
}
