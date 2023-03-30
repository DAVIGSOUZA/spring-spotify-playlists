package ada.spotify.backend.service;

import ada.spotify.backend.model.user.User;
import ada.spotify.backend.repository.UserRepository;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
