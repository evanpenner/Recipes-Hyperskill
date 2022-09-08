package recipes.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import recipes.Recipe;
import recipes.persistence.RecipeRepository;

import java.util.List;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe findRecipeById(Long id) {
        return recipeRepository.findRecipeById(id);
    }

    public Recipe save(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public void deleteRecipeById(Long id) {
        System.out.println("Deleting id: " + id);
        Recipe recipe = findRecipeById(id);

        if (recipe == null) {
            System.out.println("Couldn't find " + id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        System.out.println("Deleting for reals now" + id);
        try {
            recipeRepository.delete(recipe);
            System.out.println("Here's the recipe: " + findRecipeById(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Recipe> searchByCategory(String category) {
        List<Recipe> recipes = recipeRepository.findByCategoryIgnoreCase(category);
        recipes.sort((t, a) -> {
            if (t.getDate().isEqual(a.getDate())) return 0;
            return t.getDate().isBefore(a.getDate()) ? 1 : -1;
        });

        return recipes;
    }

    public List<Recipe> searchByName(String name) {
        List<Recipe> recipes = recipeRepository.findByNameContainingIgnoreCase(name);
        recipes.sort((t, a) -> {
            if (t.getDate().isEqual(a.getDate())) return 0;
            return t.getDate().isBefore(a.getDate()) ? 1 : -1;
        });

        return recipes;
    }
}
