package ca.gbc.postservice;

import ca.gbc.postservice.dto.PostRequest;
import ca.gbc.postservice.dto.PostResponse;
import ca.gbc.postservice.model.Post;
import ca.gbc.postservice.repository.PostRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
@AutoConfigureMockMvc
class PostServiceApplicationTests extends AbstractContainerBaseTest{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    MongoTemplate mongoTemplate;


    private PostRequest getPostRequest(){
        return PostRequest.builder()
                .content("This product is great.")
                .build();
    }

    private List<Post> getPosts() {
        List<Post> posts = new ArrayList<>();
        UUID uuid = UUID.randomUUID();

        Post post = Post.builder()
                .id(uuid.toString())
                .content("This product is great")
                .build();
        posts.add(post);
        return posts;
    }

    private String convertObjectToString(List<PostResponse> postList) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(postList);
    }

    private List<PostResponse> convertStringToObject(String jsonString) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonString, new TypeReference<List<PostResponse>>() {});
    }

    // CREATING POST
    @Test
    void createPost() throws Exception{
        PostRequest postRequest = getPostRequest();
        String postRequestJsonString = objectMapper.writeValueAsString(postRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(postRequestJsonString))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Assertions.assertFalse(postRepository.findAll().isEmpty());
        Query query = new Query();
        query.addCriteria(Criteria.where("content").is("This product is great."));
        List<Post> post = mongoTemplate.find(query, Post.class);
        Assertions.assertFalse(post.isEmpty());

    }

    // RETRIEVING POSTS
    @Test
    void getAllPosts() throws Exception {

        postRepository.saveAll(getPosts());

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/post")
                .accept(MediaType.APPLICATION_JSON));


        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andDo(MockMvcResultHandlers.print());

        MvcResult result = response.andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode jsonNode = new ObjectMapper().readTree(jsonResponse);

        int actualSize = jsonNode.size();
        int expectedSize = getPosts().size();

        assertEquals(expectedSize, actualSize);
    }

    // UPDATING POST
    @Test
    void updatePost() throws Exception{

        Post savedPost = Post.builder()
                .id(UUID.randomUUID().toString())
                .content("Amazing Place, for real 10/10")
                .build();

        postRepository.save(savedPost);

        savedPost.setContent("Amazing Place, but I'd say 7/10");
        String postRequestString = objectMapper.writeValueAsString(savedPost);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/post/" + savedPost.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(postRequestString));

        response.andExpect(MockMvcResultMatchers.status().isNoContent());
        response.andDo(MockMvcResultHandlers.print());

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(savedPost.getId()));
        Post storedPost = mongoTemplate.findOne(query, Post.class);

        assertEquals(savedPost.getContent(), storedPost.getContent());
    }

    // DELETING POST
    @Test
    void deletePost() throws Exception{

        Post savedPost = Post.builder()
                .id(UUID.randomUUID().toString())
                .content("No One cares for this post.")
                .build();

        postRepository.save(savedPost);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/post/" + savedPost.getId())
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNoContent());
        response.andDo(MockMvcResultHandlers.print());

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(savedPost.getId()));
        Long postCount = mongoTemplate.count(query, Post.class);

        assertEquals(0, postCount);
    }


}
