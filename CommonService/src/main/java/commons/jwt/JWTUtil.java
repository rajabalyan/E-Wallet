package commons.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

public class JWTUtil {

    String secretKey = "jdhse7hegsyw63gdhy63gehy6swtg3ydwgwydthgwydtgwtere";

    public SecretKey getSecretKey(){
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(),"HmacSHA256");
        return secretKeySpec;
    }

    public String createToken(String username, String role){
        return Jwts.builder().setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+10*60*1000))
                .claim("role",role)
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token);
            return true;
        }
        catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

    public String fetchSubject(String token){
        return Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token).getBody().getSubject();
    }
}
