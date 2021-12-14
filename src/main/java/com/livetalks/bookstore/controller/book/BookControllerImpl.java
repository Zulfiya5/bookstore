package com.livetalks.bookstore.controller.book;

import com.livetalks.bookstore.model.receive.book.BookAddModel;
import com.livetalks.bookstore.model.response.ApiResult;
import com.livetalks.bookstore.model.response.book.BookDisplayModel;
import com.livetalks.bookstore.model.response.book.CustomPage;
import com.livetalks.bookstore.service.book.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BookControllerImpl implements BookController{

    private final BookService bookService;

    @Override
    public ApiResult<BookDisplayModel> insertBook(@Valid BookAddModel bookAddModel) {
        return ApiResult.successResponse(bookService.insertBook(bookAddModel));
    }

    @Override
    public ApiResult<?> searchBooksByTitle(String bookTitle) {
        return ApiResult.successResponse(bookService.searchBooksByTitle(bookTitle)) ;
    }

    @Override
    public ApiResult<BookDisplayModel> updateBook(@Valid BookAddModel bookAddModel, UUID bookId) {
        return ApiResult.successResponse(bookService.updateBook(bookAddModel, bookId));
    }

    @Override
    public ApiResult<CustomPage<BookDisplayModel>> getAllBooks(int page, int size) {
        return ApiResult.successResponse(bookService.getAllBooks(page, size));
    }
}
