package ca.gbc.friendshipservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendshipResponse {
    private String id;
    private String userId1;
    private String userId2;
    private FriendshipStatus status;

    public enum FriendshipStatus {
        PENDING,
        APPROVED,
        DECLINED
    }
}