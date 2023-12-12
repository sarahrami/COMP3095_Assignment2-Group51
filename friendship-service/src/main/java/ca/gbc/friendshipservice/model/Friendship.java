package ca.gbc.friendshipservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Friendship model representing a friendship entity for MongoDB.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "friendships")
public class Friendship {

    @Id
    private String id; // Unique identifier for each friendship

    private String userId1; // User ID of the first user in the friendship
    private String userId2; // User ID of the second user in the friendship

    private FriendshipStatus status; // Status of the friendship (e.g., pending, approved, declined)

    public enum FriendshipStatus {
        PENDING,
        APPROVED,
        DECLINED
    }

    /**
     * Send a friend request from user1 to user2.
     * @param userId1 ID of the user sending the request
     * @param userId2 ID of the user receiving the request
     * @return A new Friendship instance with PENDING status
     */
    public static Friendship sendFriendRequest(String userId1, String userId2) {
        return Friendship.builder()
                .userId1(userId1)
                .userId2(userId2)
                .status(FriendshipStatus.PENDING)
                .build();
    }

    /**
     * Approve a friend request, changing its status to APPROVED.
     */
    public void approveFriendRequest() {
        if (this.status == FriendshipStatus.PENDING) {
            this.status = FriendshipStatus.APPROVED;
        } else {
            throw new IllegalStateException("Friendship request is not pending.");
        }
    }

    /**
     * Decline a friend request, changing its status to DECLINED.
     */
    public void declineFriendRequest() {
        if (this.status == FriendshipStatus.PENDING) {
            this.status = FriendshipStatus.DECLINED;
        } else {
            throw new IllegalStateException("Friendship request is not pending.");
        }
    }
}