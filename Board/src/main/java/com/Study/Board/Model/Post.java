package com.Study.Board.Model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
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

    @OneToMany(mappedBy = "post")
    private List<Comment> commentList = new ArrayList<>();

}
