package com.revshop.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.revshop.demo.entity.Favorite;
import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(Long userId); 
    Optional<Favorite> findByUserIdAndProductId(Long userId, Long productId); 
    void deleteByUserIdAndProductId(Long userId, Long productId); 
}
