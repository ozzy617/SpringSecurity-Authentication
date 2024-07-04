package sandbox;

import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
public class GreetingsRestController {

    //1 способ получения юзера из контекста
    //Взятие аутентификации из SecurityContext после чего взятин Principal и каст в UserDetails
    @GetMapping("/api/v1/greetings")
    public ResponseEntity<Map<String, String>> getGreetings() {
        UserDetails  userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("greeting", "Hello %s!".formatted(userDetails.getUsername())));

    }

    //2 способ получения юзера из контекста
    //Извлечение из запроса и каст в Auth после чего каст в UserDeatils
    @GetMapping("/api/v2/greetings")
    public ResponseEntity<Map<String, String>> getGreetingsV2(HttpServletRequest request) {
        UserDetails  userDetails = (UserDetails) ((Authentication)request.getUserPrincipal()).getPrincipal();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("greeting", "Hello %s!".formatted(userDetails.getUsername())));

    }

    //3 способ получения юзера из контекста
    //Извлечение из Secutiry COntext Principal и его каст в UserDetails
    @GetMapping("/api/v3/greetings")
    public ResponseEntity<Map<String, String>> getGreetingsV3(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("greeting", "Hello %s!".formatted(userDetails.getUsername())));

    }

    //5 способ получения юзера из контекста
    //Получение в параметрах Principal
    @GetMapping("/api/v5/greetings")
    public ResponseEntity<Map<String, String>> getGreetingsV5(Principal principal) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("greeting", "Hello, %s".formatted(principal.getName())));
    }
}
