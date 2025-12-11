package id.kai.eraport.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtUserInfo {
    private UUID id;
    private String name;
    private String email;
    private UUID role;
}
