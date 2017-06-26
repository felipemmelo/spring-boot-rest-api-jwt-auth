package services.security.jwt;

import java.util.Base64;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import beans.jwt.MinimalJWTUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
 
/**
 * This class defines methods for coping with JWT.
 *
 */
@Service
public class JWTService {
 
    @Value("${jwt.expire.hours}")
    private Long expireHours;
 
    @Value("${jwt.secret}")
    private String plainSecret;
    
    private String encodedSecret;
 
    public MinimalJWTUser getUser(String token) {
        return getUser(this.encodedSecret, token);
    }
 
    public String getToken(MinimalJWTUser jwtUser) {
        return getToken(this.encodedSecret, jwtUser);
    }
    
    @PostConstruct
    protected void init() {
        this.encodedSecret = generateEncodedSecret(this.plainSecret);
    }
 
    private String generateEncodedSecret(String plainSecret) {
    	
        if (StringUtils.isEmpty(plainSecret)) {
            throw new IllegalArgumentException("JWT secret cannot be null or empty.");
        }
        return Base64
                .getEncoder()
                .encodeToString(this.plainSecret.getBytes());
    }
 
    private Date getExpirationTime() {
    	
        Date now = new Date();
        Long expireInMilis = TimeUnit.HOURS.toMillis(expireHours);
        return new Date(expireInMilis + now.getTime());
    }
 
    private MinimalJWTUser getUser(String encodedSecret, String token) {
    	
        Claims claims = Jwts.parser()
                .setSigningKey(encodedSecret)
                .parseClaimsJws(token)
                .getBody();
        
        String userName = claims.getSubject();
        @SuppressWarnings("unchecked")
		List<String> roleList = (List<String>) claims.get("roles");
        
        Set<String> roles = new HashSet<String>();
        roles.addAll(roleList);
        
        MinimalJWTUser securityUser = new MinimalJWTUser();
        securityUser.setUsername(userName);
        securityUser.setRoles(roles);
        
        return securityUser;
    }
    
    private String getToken(String encodedSecret, MinimalJWTUser jwtUser) {
    	
        Date now = new Date();
        
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(jwtUser.getUsername())
                .claim("roles", jwtUser.getRoles())
                .setIssuedAt(now)
                .setExpiration(getExpirationTime())
                .signWith(SignatureAlgorithm.HS512, encodedSecret)
                .compact();
    }
}
