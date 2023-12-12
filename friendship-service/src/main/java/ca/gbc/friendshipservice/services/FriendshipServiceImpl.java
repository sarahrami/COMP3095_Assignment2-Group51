package ca.gbc.friendshipservice.services;

import ca.gbc.friendshipservice.model.Friendship;
import ca.gbc.friendshipservice.model.Friendship.FriendshipStatus;
import ca.gbc.friendshipservice.repository.FriendshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendshipServiceImpl implements ca.gbc.friendshipservice.service.FriendshipService {

    private final FriendshipRepository friendshipRepository;

    @Autowired
    public FriendshipServiceImpl(FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    @Override
    public List<Friendship> findAll() {
        return friendshipRepository.findAll();
    }

    @Override
    public Friendship findById(String id) {
        return friendshipRepository.findById(id).orElse(null);
    }

    @Override
    public Friendship save(Friendship friendship) {
        return friendshipRepository.save(friendship);
    }

    @Override
    public Friendship update(String id, Friendship friendship) {
        if (!id.equals(friendship.getId())) {
            return null; // The ID from the path should match the ID of the object.
        }
        Friendship existingFriendship = findById(id);
        if (existingFriendship != null) {
            existingFriendship.setStatus(friendship.getStatus());
            // You can update other attributes here as needed.
            return friendshipRepository.save(existingFriendship);
        }
        return null;
    }

    @Override
    public boolean delete(String id) {
        Friendship existingFriendship = findById(id);
        if (existingFriendship != null) {
            friendshipRepository.delete(existingFriendship);
            return true;
        }
        return false;
    }

    @Override
    public Friendship sendFriendRequest(Friendship friendshipRequest) {
        friendshipRequest.setStatus(FriendshipStatus.PENDING);
        return friendshipRepository.save(friendshipRequest);
    }

    @Override
    public Friendship approveFriendRequest(String id) {
        Friendship friendship = findById(id);
        if (friendship != null && friendship.getStatus() == FriendshipStatus.PENDING) {
            friendship.setStatus(FriendshipStatus.APPROVED);
            return friendshipRepository.save(friendship);
        }
        return null;
    }

    @Override
    public Friendship declineFriendRequest(String id) {
        Friendship friendship = findById(id);
        if (friendship != null && friendship.getStatus() == FriendshipStatus.PENDING) {
            friendship.setStatus(FriendshipStatus.DECLINED);
            return friendshipRepository.save(friendship);
        }
        return null;
    }

    @Override
    public List<Friendship> findPendingRequests(String userId) {
        return friendshipRepository.findByUserId2AndStatus(userId, FriendshipStatus.PENDING);
    }

    @Override
    public List<Friendship> findFriendsForUser(String userId) {
        return friendshipRepository.findByUserId1AndStatusOrUserId2AndStatus(userId, FriendshipStatus.APPROVED, userId, FriendshipStatus.APPROVED);
    }

    @Override
    public boolean removeFriend(String id) {
        Friendship friendship = findById(id);
        if (friendship != null && friendship.getStatus() == FriendshipStatus.APPROVED) {
            friendshipRepository.delete(friendship);
            return true;
        }
        return false;
    }
}