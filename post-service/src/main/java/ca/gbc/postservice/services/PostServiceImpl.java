package ca.gbc.postservice.services;

import ca.gbc.postservice.dto.CommentResponse;
import ca.gbc.postservice.dto.PostRequest;
import ca.gbc.postservice.dto.PostResponse;
import ca.gbc.postservice.dto.UserResponse;
import ca.gbc.postservice.model.Post;
import ca.gbc.postservice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final MongoTemplate mongoTemplate;
    private final WebClient webClient;

    @Value("${user.service.url}")
    private String userURI;

    @Value("${comment.service.url}")
    private String commentURL;

    @Override
    public void createPost(PostRequest postRequest) {
        Post post = Post.builder()
                .content(postRequest.getContent())
                .tag(postRequest.getTag())
                .image(postRequest.getImage())
                .user_id(postRequest.getUserId())
                .build();
        post = postRepository.save(post);
        log.info("{0}", post);
    }

    @Override
    public String updatePost(String postId, PostRequest postRequest) {
        Query query = new Query();

        query.addCriteria(Criteria.where("id").is(postId));
        Post post = mongoTemplate.findOne(query, Post.class);

        if (post != null) {
            post.setContent(postRequest.getContent());

            return postRepository.save(post).getId();
        }
        return postId.toString();
    }

    @Override
    public void deletePost(String postId) {
        log.info("Post {} is selected", postId);
        postRepository.deleteById(postId);
    }

    @Override
    public List<PostResponse> getAllPosts() {
        log.info("Returning all posts");
        List<Post> posts = postRepository.findAll();
        List<PostResponse> responses = posts.stream().map(this::mapToPostResponse).toList();

        for (PostResponse response : responses) {
            List<CommentResponse> comments = webClient.get()
                    .uri(commentURL + "/post/" + response.getId())
                    .retrieve()
                    .bodyToFlux(CommentResponse.class)
                    .collectList()
                    .block();
            response.setComments(comments);

            if (response.getUserId() != null) {
                UserResponse user = webClient.get()
                        .uri(userURI + "/" + response.getUserId())
                        .retrieve()
                        .bodyToFlux(UserResponse.class)
                        .blockFirst();
                response.setUser(user);
            }
        }

        return responses;
    }

    private PostResponse mapToPostResponse(Post post) {
        return PostResponse.builder()
                .Id(post.getId())
                .userId(post.getUser_id())
                .tag(post.getTag())
                .image(post.getImage())
                .content(post.getContent()).build();
    }

    /*
     * @Override
     * public List<HashMap<String, String>> getPostsForUser(String userId){
     * Query query = new Query();
     * query.addCriteria(Criteria.where("userId").is(userId));
     * 
     * List<Post> posts = mongoTemplate.find(query, Post.class);
     * return mapToHashMapList(posts);
     * }
     * 
     * private List<HashMap<String, String>> mapToHashMapList(List<Post> posts) {
     * return posts.stream()
     * .map(post -> {
     * HashMap<String, String> postMap = new HashMap<>();
     * postMap.put("postId", post.getId());
     * postMap.put("userId", post.getUserId());
     * postMap.put("content", post.getContent());
     * return postMap;
     * })
     * .collect(Collectors.toList());
     * }
     */

}
