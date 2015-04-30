package org.fjorum.model.repository;

import org.fjorum.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE c.sortOrder<:sortOrder ORDER BY c.sortOrder DESC")
    Category findAbove(int sortOrder);

    @Query("SELECT c FROM Category c WHERE c.sortOrder>:sortOrder ORDER BY c.sortOrder")
    Category findBelow(int sortOrder);
}
