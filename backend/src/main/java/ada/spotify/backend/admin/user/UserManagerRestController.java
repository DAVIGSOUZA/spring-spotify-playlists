package ada.spotify.backend.admin.user;

import ada.spotify.backend.model.user.UserDTO;
import ada.spotify.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class UserManagerRestController {

    private final UserRepository userRepository;

    @GetMapping
    public List<UserDTO> listar() {
        return this.userRepository.findAll().stream()
                .map(UserDTO::new)
                .toList();
    }

}