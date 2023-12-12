package ca.gbc.commentservice.services;

import ca.gbc.commentservice.dto.CommentRequest;
import ca.gbc.commentservice.dto.CommentResponse;

import java.util.List;

public interface CommentService {

    CommentResponse createComment(CommentRequest commentRequest);

    CommentResponse getCommentById(Long id);

    List<CommentResponse> getAllCommentsByPostId(String postId);

    CommentResponse updateComment(Long id, CommentRequest commentRequest);

    void deleteComment(Long id);
}

