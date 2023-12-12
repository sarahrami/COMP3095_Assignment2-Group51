package ca.gbc.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Friendship {
    private String userId1;
    private String userId2;
    private Friendship.FriendshipStatus status;

    public enum FriendshipStatus {
        PENDING,
        APPROVED,
        DECLINED
    }
}
