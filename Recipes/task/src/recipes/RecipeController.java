package recipes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.business.RecipeService;
import recipes.persistence.UserRepository;
import recipes.user.User;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class RecipeController {
    @Autowired
    RecipeService recipeService;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    UserRepository userRepo;

    @GetMapping("/api/recipe/{id}")
    public Recipe getRecipe(@PathVariable long id) {
        Recipe r = recipeService.findRecipeById(id);
        if (r == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return r;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class})
    public void handleValidation() {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/api/register")
    public void register(@Valid @RequestBody User user) {
        // input validation omitted for brevity

        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(user.getUsername());
        if (!matcher.matches())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        if (userRepo.findById(user.getUsername()).isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepo.save(user);

    }

    @PostMapping("/api/recipe/new")
    public Map<String, Long> setRecipe(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody Recipe recipe) {

        if (userDetails == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        User user = userRepo.findById(userDetails.getUsername()).get();
        Recipe r = recipeService.save(new Recipe(recipe.getName(), recipe.getDescription(), recipe.ingredients, recipe.directions, recipe.getCategory(), LocalDateTime.now(), user));

        return Map.of("id", r.getId());
    }


    //@ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/api/recipe/{id}")
    public void deleteRecipe(@AuthenticationPrincipal UserDetails userDetails, @PathVariable long id) {
        if (userDetails == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        Recipe recipe = recipeService.findRecipeById(id);
        if (recipe == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        if (recipe.getAuthor().getUsername().equals(userDetails.getUsername())) {
            recipeService.deleteRecipeById(id);

            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/api/recipe/search")
    public List<Recipe> searchRecipes(@RequestParam(required = false) String category, @RequestParam(required = false) String name) {
        if (category != null)
            return recipeService.searchByCategory(category);
        else if (name != null)
            return recipeService.searchByName(name);
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/api/recipe/{id}")
    public Recipe updateRecipe(@AuthenticationPrincipal UserDetails userDetails, @PathVariable long id, @Valid @RequestBody Recipe recipe) {
        if (userDetails == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        Recipe r = recipeService.findRecipeById(id);

        if (r == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        if (!r.getAuthor().getUsername().equals(userDetails.getUsername()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        r.setName(recipe.getName());
        r.setDate(LocalDateTime.now());
        r.setCategory(recipe.getCategory());
        r.setDirections(recipe.getDirections());
        r.setIngredients(recipe.getIngredients());
        r.setDescription(recipe.getDescription());
        recipeService.save(r);
        return recipe;
    }
}
