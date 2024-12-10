package com.luv2code.spring_boot_library.controller;

import com.luv2code.spring_boot_library.entity.Book;
import com.luv2code.spring_boot_library.responsemodels.ShelfCurrentLoansResponse;
import com.luv2code.spring_boot_library.service.BookService;
import com.luv2code.spring_boot_library.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "https://localhost:3000")
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Get current loans for the authenticated user
    @GetMapping("/secure/currentloans")
    public ResponseEntity<List<ShelfCurrentLoansResponse>> currentLoans(@RequestHeader(value = "Authorization") String token) {
        try {
            String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
            List<ShelfCurrentLoansResponse> loans = bookService.currentLoans(userEmail);
            return new ResponseEntity<>(loans, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get the count of books currently loaned by the user
    @GetMapping("/secure/currentloans/count")
    public ResponseEntity<Integer> currentLoansCount(@RequestHeader(value = "Authorization") String token) {
        try {
            String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
            int count = bookService.currentLoansCount(userEmail);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(0, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Check if a book is checked out by the user
    @GetMapping("/secure/ischeckedout/byuser")
    public ResponseEntity<Boolean> checkoutBookByUser(@RequestHeader(value = "Authorization") String token,
                                                      @RequestParam Long bookId) {
        try {
            String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
            Boolean isCheckedOut = bookService.checkoutBookByUser(userEmail, bookId);
            return new ResponseEntity<>(isCheckedOut, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Checkout a book
    @PutMapping("/secure/checkout")
    public ResponseEntity<Book> checkoutBook(@RequestHeader(value = "Authorization") String token,
                                             @RequestParam Long bookId) {
        try {
            String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
            Book checkedOutBook = bookService.checkoutBook(userEmail, bookId);
            return new ResponseEntity<>(checkedOutBook, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Return a book
    @PutMapping("/secure/return")
    public ResponseEntity<String> returnBook(@RequestHeader(value = "Authorization") String token,
                                             @RequestParam Long bookId) {
        try {
            String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
            bookService.returnBook(userEmail, bookId);
            return new ResponseEntity<>("Book returned successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Renew a loan
    @PutMapping("/secure/renew/loan")
    public ResponseEntity<String> renewLoan(@RequestHeader(value = "Authorization") String token,
                                            @RequestParam Long bookId) {
        try {
            String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
            bookService.renewLoan(userEmail, bookId);
            return new ResponseEntity<>("Loan renewed successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
