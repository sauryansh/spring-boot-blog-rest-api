package com.springboot.blog.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "posts"
        ,uniqueConstraints = {
        @UniqueConstraint(columnNames = {"title"})
})
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", nullable = false, length = 100)
    private String title;
    @Column(name = "description", nullable = false, length = 500)
    private String description;
    @Column(name = "content", nullable = false, length = 1000)
    private String content;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL,orphanRemoval = true  )
    private Set<Comment> comments= new HashSet<>();
}
