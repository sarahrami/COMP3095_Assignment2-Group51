package ca.gbc.friendshipservice.repository;

import ca.gbc.friendshipservice.model.Friendship;
import ca.gbc.friendshipservice.model.Friendship.FriendshipStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FriendshipRepository extends MongoRepository<Friendship, String> {

    List<Friendship> findByUserId2AndStatus(String userId2, FriendshipStatus status);

    List<Friendship> findByUserId1AndStatusOrUserId2AndStatus(
            String userId1,
            FriendshipStatus status1,
            String userId2,
            FriendshipStatus status2
    );
}