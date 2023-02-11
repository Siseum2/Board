package com.Study.Board.Model;

import com.Study.Board.Model.Enum.UserRole;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class User extends BaseEntity {

    @Column(nullable = false, length = 30, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Email
    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Builder.Default
    private String role = UserRole.USER.getValue();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Post> postList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Comment> commentList = new ArrayList<>();

}
