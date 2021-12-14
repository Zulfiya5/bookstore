package com.livetalks.bookstore.model.receive.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookAddModel {

    @NotBlank(message = "Title of the book cannot be empty.")
    private String title;

    @NotBlank(message = "Author of the book cannot be empty.")
    private String author;

    @Digits(integer=10, fraction=0, message = "Published year should be integer value.")
    @JsonProperty("published_year")
    private int publishedYear;
}
