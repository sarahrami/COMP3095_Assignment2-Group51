package ca.gbc.commentservice.services;

import ca.gbc.commentservice.dto.CommentRequest;
import ca.gbc.commentservice.dto.CommentResponse;
import ca.gbc.commentservice.dto.UserResponse;
import ca.gbc.commentservice.model.Comment;
import ca.gbc.commentservice.repository.CommentRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final WebClient webClient;

    @Autowired
    private CommentRepository commentRepository;

    @Value("${user.service.url}")
    private String userURI;

    @Override
    public CommentResponse createComment(CommentRequest commentRequest) {
        Comment comment = Comment.builder()
                .postId(commentRequest.getPostId())
                .userId(commentRequest.getUserId())
                .content(commentRequest.getContent())
                .build();

        comment = commentRepository.save(comment);

        return new CommentResponse(comment.getId(), comment.getPostId(), comment.getUserId(), comment.getContent(), null);
    }

    @Override
    public CommentResponse getCommentById(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
        UserResponse user = webClient.get()
                .uri(userURI + "/" + comment.getUserId())
                .retrieve()
                .bodyToFlux(UserResponse.class)
                .blockFirst();
        return new CommentResponse(comment.getId(), comment.getPostId(), comment.getUserId(), comment.getContent(), user);
    }

    @Override
    public List<CommentResponse> getAllCommentsByPostId(String postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(comment -> new CommentResponse(comment.getId(), comment.getPostId(), comment.getUserId(), comment.getContent(), null))
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponse updateComment(Long id, CommentRequest commentRequest) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));

        comment.setContent(commentRequest.getContent()); // You can add other fields to update as well.

        comment = commentRepository.save(comment);

        return new CommentResponse(comment.getId(), comment.getPostId(), comment.getUserId(), comment.getContent(), null);
    }

    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}

