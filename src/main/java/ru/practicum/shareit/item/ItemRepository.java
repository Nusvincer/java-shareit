package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwner(User owner);

    @Query("SELECT i FROM Item i JOIN FETCH i.owner")
    List<Item> findAllWithOwners();

    List<Item> findByOwnerId(Long ownerId);

        @Query("SELECT i FROM Item i WHERE (LOWER(i.name) LIKE LOWER(CONCAT('%', :text, '%')) " +
                "OR LOWER(i.description) LIKE LOWER(CONCAT('%', :text, '%'))) " +
                "AND i.available = TRUE")
        List<Item> searchAvailableItems(@Param("text") String text);
}
