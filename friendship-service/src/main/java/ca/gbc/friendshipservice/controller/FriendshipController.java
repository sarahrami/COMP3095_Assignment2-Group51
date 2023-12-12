package ca.gbc.friendshipservice.controller;

import ca.gbc.friendshipservice.model.Friendship;
import ca.gbc.friendshipservice.service.FriendshipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/friendships")
public class FriendshipController {

    private final FriendshipService friendshipService;
    private static final Logger logger = LoggerFactory.getLogger(FriendshipController.class);

    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    @GetMapping
    public ResponseEntity<List<Friendship>> getAllFriendships() {
        try {
            return ResponseEntity.ok(friendshipService.findAll());
        } catch (Exception e) {
            logger.error("Error fetching friendships", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching friendships");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Friendship> getFriendshipById(@PathVariable String id) {
        try {
            Friendship friendship = friendshipService.findById(id);
            if (friendship != null) {
                return new ResponseEntity<>(friendship, HttpStatus.OK);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Friendship not found");
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching friendship by id", e);
        }
    }

    @PostMapping
    public ResponseEntity<Friendship> createFriendship(@Validated @RequestBody Friendship friendship) {
        try {
            Friendship newFriendship = friendshipService.save(friendship);
            return new ResponseEntity<>(newFriendship, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating friendship", e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Friendship> updateFriendship(@PathVariable String id, @Validated @RequestBody Friendship friendship) {
        try {
            Friendship updatedFriendship = friendshipService.update(id, friendship);
            if (updatedFriendship != null) {
                return new ResponseEntity<>(updatedFriendship, HttpStatus.OK);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to update: Friendship not found");
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating friendship", e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFriendship(@PathVariable String id) {
        try {
            if (friendshipService.delete(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to delete: Friendship not found");
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting friendship", e);
        }
    }

    @PostMapping("/send-request")
    public ResponseEntity<Friendship> sendFriendRequest(@Validated @RequestBody Friendship friendshipRequest) {
        try {
            Friendship newFriendship = friendshipService.sendFriendRequest(friendshipRequest);
            return new ResponseEntity<>(newFriendship, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error sending friend request", e);
        }
    }

    @PutMapping("/approve/{id}")
    public ResponseEntity<Friendship> approveFriendRequest(@PathVariable String id) {
        try {
            Friendship approvedFriendship = friendshipService.approveFriendRequest(id);
            if (approvedFriendship != null) {
                return new ResponseEntity<>(approvedFriendship, HttpStatus.OK);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to approve: Friendship request not found");
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error approving friend request", e);
        }
    }

    @PutMapping("/decline/{id}")
    public ResponseEntity<Friendship> declineFriendRequest(@PathVariable String id) {
        try {
            Friendship declinedFriendship = friendshipService.declineFriendRequest(id);
            if (declinedFriendship != null) {
                return new ResponseEntity<>(declinedFriendship, HttpStatus.OK);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to decline: Friendship request not found");
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error declining friend request", e);
        }
    }

    @GetMapping("/pending-requests/{userId}")
    public ResponseEntity<List<Friendship>> getPendingFriendRequests(@PathVariable String userId) {
        try {
            List<Friendship> pendingRequests = friendshipService.findPendingRequests(userId);
            return new ResponseEntity<>(pendingRequests, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching pending friend requests", e);
        }
    }

    @GetMapping("/friends/{userId}")
    public ResponseEntity<List<Friendship>> getFriendsForUser(@PathVariable String userId) {
        try {
            List<Friendship> friends = friendshipService.findFriendsForUser(userId);
            return new ResponseEntity<>(friends, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching friends for user", e);
        }
    }

    @DeleteMapping("/remove-friend/{id}")
    public ResponseEntity<Void> removeFriend(@PathVariable String id) {
        try {
            if (friendshipService.removeFriend(id)) {
                return ResponseEntity.noContent().build();
            } else {
                logger.warn("Unable to remove: Friend not found with ID {}", id);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to remove: Friend not found");
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error removing friend with ID {}", id, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error removing friend");
        }
    }
}