package com.luv2code.spring_boot_library.dao;

import com.luv2code.spring_boot_library.entity.Checkout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CheckoutRepository extends JpaRepository<Checkout, Long> {

    // Find a checkout by user email and book ID
    Checkout findByUserEmailAndBookId(String userEmail, Long bookId);

    // Find all books checked out by a user using their email
    List<Checkout> findBooksByUserEmail(String userEmail);

    // Delete all checkout entries for a specific book ID
    @Modifying
    @Transactional
    @Query("delete from Checkout c where c.bookId = :book_id")
    void deleteAllByBookId(@Param("book_id") Long bookId);

}
