package ada.spotify.backend.Controller;

import ada.spotify.backend.model.user.DuplicatedException;
import ada.spotify.backend.model.user.User;
import ada.spotify.backend.model.user.UserDTO;
import ada.spotify.backend.model.user.UserRequest;
import ada.spotify.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@Log4j2
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO cadastrar(@RequestBody @Valid UserRequest userRequest) {
        Optional<User> optionalUserModel = this.userRepository.findByEmail(userRequest.getEmail());
        if (optionalUserModel.isPresent()) {
            throw new DuplicatedException("E-mail já cadastrado");
        }

        User user = User.builder()
                .email(userRequest.getEmail())
                .username(userRequest.getUsername())
                .role(userRequest.getRole())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .build();

        User userModel = this.userRepository.save(user);

        return new UserDTO(userModel);

    }


}