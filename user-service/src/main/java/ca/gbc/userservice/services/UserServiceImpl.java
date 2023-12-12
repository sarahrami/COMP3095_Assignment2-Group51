package ca.gbc.userservice.services;

import ca.gbc.userservice.dto.FriendshipResponse;
import ca.gbc.userservice.dto.UserResponse;
import ca.gbc.userservice.model.User;
import ca.gbc.userservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final WebClient webClient;

    @Value("${friendship.service.uri}")
    private String friendshipURI;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserResponse> findAll() {
        List<User> users = userRepository.findAll();
        List<UserResponse> response = new ArrayList<>();
        for (User user : users) {
            FriendshipResponse friendshipResponse = webClient.get()
                    .uri(friendshipURI + "/friends/" + user.getId())
                    .retrieve()
                    .bodyToFlux(FriendshipResponse.class)
                    .blockFirst();
            UserResponse userResponse = UserResponse.builder()
                    .Id(user.getId().toString())
                    .username(user.getUsername())
                    .friendshipResponse(friendshipResponse).build();
            response.add(userResponse);
        }
        return response;
    }

    @Override
    public UserResponse findById(Long id) {  // Note the change in parameter type to String
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty() == true) {
            return null;
        }

        User realUser = user.get();
        FriendshipResponse friendshipResponse = webClient.get()
                .uri(friendshipURI + "/friends/" + realUser.getId())
                .retrieve()
                .bodyToFlux(FriendshipResponse.class)
                .blockFirst();
        UserResponse userResponse = UserResponse.builder()
                .Id(realUser.getId().toString())
                .username(realUser.getUsername())
                .friendshipResponse(friendshipResponse).build();

        return userResponse;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(Long id, User user) {
        if (userRepository.existsById(id)) {
            return userRepository.save(user); // This will update if the ID already exists
        }
        return null;
    }

    @Override
    public boolean delete(Long id) { // Note the change in parameter type to String
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
