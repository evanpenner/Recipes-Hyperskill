package recipes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import recipes.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Entity
@Table(name = "recipes")
@Data
@ToString(exclude = "author")
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String category;
    @NotBlank
    String description;
    @NotEmpty
    String[] ingredients;
    @NotEmpty
    String[] directions;
    @LastModifiedDate
    LocalDateTime date;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    @JsonIgnore
    User author;

    public Recipe(String name, String description, String[] ingredients, String[] directions, String category, User user) {
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.directions = directions;
        this.category = category.toLowerCase();
        this.author = user;
    }

    public Recipe(String name, String description, String[] ingredients, String[] directions, String category, LocalDateTime date, User user) {
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.directions = directions;
        this.category = category;
        this.date = date;
        this.author = user;
    }
}
