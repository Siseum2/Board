package com.Study.Board.Model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class Comment extends BaseEntity {
    @Lob
    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}
