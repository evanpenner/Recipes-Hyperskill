package recipes.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recipes.Recipe;

import java.util.List;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Long> {

    Recipe findRecipeById(Long id);

    List<Recipe> findByCategoryIgnoreCase(String category);

    List<Recipe> findByNameContainingIgnoreCase(String recipeName);

}

