package com.livetalks.bookstore.controller.book;

import com.livetalks.bookstore.constants.RestConstant;
import com.livetalks.bookstore.model.receive.book.BookAddModel;
import com.livetalks.bookstore.model.response.ApiResult;
import com.livetalks.bookstore.model.response.book.BookDisplayModel;
import com.livetalks.bookstore.model.response.book.CustomPage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static com.livetalks.bookstore.constants.AppConstant.DEFAULT_PAGE_NUMBER;
import static com.livetalks.bookstore.constants.AppConstant.DEFAULT_PAGE_SIZE;

@RequestMapping(RestConstant.BOOK_CONTROLLER)
public interface BookController {

    String INSERT_BOOK = "/insert";
    String SEARCH_BOOKS_BY_TITLE = "/search-by-title";
    String UPDATE_BOOK = "/update";
    String GET_ALL_BOOKS = "/list";

    @PostMapping(INSERT_BOOK)
    ApiResult<BookDisplayModel> insertBook(@RequestBody @Valid BookAddModel bookAddModel);

    @GetMapping(SEARCH_BOOKS_BY_TITLE + "/{bookTitle}")
    ApiResult<?> searchBooksByTitle(@PathVariable String bookTitle);

    @PutMapping(UPDATE_BOOK + "/{bookId}")
    ApiResult<BookDisplayModel> updateBook(@RequestBody @Valid BookAddModel bookAddModel, @PathVariable UUID bookId);

    @GetMapping(GET_ALL_BOOKS)
    ApiResult<CustomPage<BookDisplayModel>> getAllBooks(
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size);
}
