package com.Study.Board.Model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class Post extends BaseEntity {

    @Column(nullable = false, length = 30)
    private String subject;
    @Lob
    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 수정

    @Builder.Default
    @OneToMany(mappedBy = "post")
    private List<Comment> commentList = new ArrayList<>();

}
