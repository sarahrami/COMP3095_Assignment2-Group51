package ca.gbc.postservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(value = "posts") // Specifies the name of the database table
public class Post {

    @Id
    private String id;
    private String content;
    private Long user_id;
    private String tag;
    private String image;

}