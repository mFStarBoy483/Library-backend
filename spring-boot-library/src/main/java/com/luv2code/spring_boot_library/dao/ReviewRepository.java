package com.luv2code.spring_boot_library.dao;

import com.luv2code.spring_boot_library.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Method to find reviews by book ID with pagination
    Page<Review> findByBookId(Long bookId, Pageable pageable);

    // Method to find a review by user email and book ID
    Review findByUserEmailAndBookId(String userEmail, Long bookId);

    // Custom query to delete all reviews for a specific book
    @Transactional
    @Modifying
    @Query("delete from Review where bookId = :bookId")
    void deleteAllByBookId(@Param("bookId") Long bookId);
}
