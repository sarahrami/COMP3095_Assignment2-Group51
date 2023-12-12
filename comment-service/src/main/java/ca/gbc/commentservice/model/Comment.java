package ca.gbc.commentservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity // Marks this class as a JPA entity
@Table(name = "comments") // Specifies the name of the database table
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Specifies auto-generation of primary key
    @Column(name = "id")
    private Long id;

    @Column(name = "post_id")
    private String postId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "content")
    private String content;
}
