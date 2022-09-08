package recipes.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import recipes.Recipe;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@ToString(exclude = "recipes")
@AllArgsConstructor
@Data
public class User {

    @Id
    @JsonProperty("email")
    @Email
    @NotBlank
    private String username;
    @NotBlank
    @Size(min = 8)
    private String password;
    @JsonIgnore
    private String role;
    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Recipe> recipes;
}
