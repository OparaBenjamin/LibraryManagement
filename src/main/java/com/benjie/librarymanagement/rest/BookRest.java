package com.benjie.librarymanagement.rest;

/*
 * Created by OPARA BENJAMIN
 * On 12/14/2020 - 10:19 PM
 */

import com.benjie.librarymanagement.entity.Book;
import com.benjie.librarymanagement.service.BookService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

@Path("books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authz
public class BookRest {

    @Inject
    private BookService bookService;
    private int requestCode;

    @Admin
    @POST
    @Path("create")
    public Response createBook(Book book) {
        if (bookService.exists(book.getIsbn())) {
            return Response.status(/*Response.Status.CONFLICT*/ 409,
                    "A book with same ISBN already exists").build();
        }
        bookService.createBook(book);
        URI location = UriBuilder.fromResource(BookRest.class)
                .queryParam("isbn", book.getIsbn())
                .build();
        return Response.created(location).build();
    }

    @Admin
    @PUT
    @Path("update")
    public Response updateBook(@QueryParam("isbn") String isbn, Book book) {
        if (bookService.updateBook(isbn, book) != null) {
            return Response.ok(book).build();
        }
        return Response.status(404,
                "Book with the following ISBN: " + isbn + " does not exit.").build();
    }

    @PUT
    @Path("borrow")
    @Produces("text/plain")
    public Response borrowBook(@QueryParam("isbn") String isbn) {
        requestCode = bookService.borrowBook(isbn);

        if (requestCode == 2) {
            return Response.ok("Sorry this book is currently restricted").build();
        } else if (requestCode == 0) {
            return Response.status(404,
                    "Sorry the book is currently not available").build();
        } else if (requestCode == -1) {
            return Response.status(404,
                    "Book with the following ISBN: " + isbn + " does not exit.").build();
        } else if (requestCode == 3) {
            return Response.status(403,
                    "Sorry you are not allowed to borrow this book.\nKindly contact the admin.").build();
        }
        return Response.ok("Successfully Borrowed").build();
    }

    @PUT
    @Path("return")
    @Produces("text/plain")
    public Response returnBook(@QueryParam("isbn") String isbn) {
        requestCode = bookService.returnBook(isbn);
        if (requestCode == 1) {
            return Response.ok("Successfully Returned").build();
        } else if (requestCode == -1) {
            return Response.status(404,
                    "Book with the following ISBN: " + isbn + " does not exit.").build();
        } else if (requestCode == 0) {
            return Response.status(304,
                    "Sorry you did not borrow this book.").build();
        }
        return Response.noContent().build();
    }

    @Admin
    @DELETE
    @Path("remove")
    @Produces("text/plain")
    public Response removeBook(@QueryParam("isbn") String isbn) {
        requestCode = bookService.removeBook(isbn);
        if (requestCode == 1) {
            return Response.ok("Successfully removed").build();
        } else if (requestCode == -1) {
            return Response.status(404,
                    "Book with the following ISBN: " + isbn + " does not exit.").build();
        }
        return Response.noContent().build();
    }

    @Admin
    @PUT
    @Path("restrict")
    @Produces("text/plain")
    public Response restrictBook(@QueryParam("isbn") String isbn,
                                 @QueryParam("value") boolean state) {
        requestCode = bookService.restrictBook(isbn, state);
        if (requestCode == 1) {
            return Response.ok("Successfully restricted").build();
        } else if (requestCode == 0) {
            return Response.ok("Successfully restored").build();
        } else if (requestCode == -1) {
            return Response.status(404,
                    "Book with the following ISBN: " + isbn + " does not exit.").build();
        }
        return Response.noContent().build();
    }

    @GET
    @Path("list")
    public Response findAllBooks() {
        return Response.ok(bookService.findAllBooks()).build();
    }

    @GET
    public Response findBooksByQuery(@QueryParam("author") String author,
                                     @QueryParam("isbn") String isbn,
                                     @QueryParam("title") String title) {

        if (author != null && !author.isEmpty()) {
            return Response.ok(bookService.findBooksByAuthor(author)).build();
        } else if (isbn != null && !isbn.isEmpty()) {
            return Response.ok(bookService.findBooksByISBN(isbn)).build();
        } else if (title != null && !title.isEmpty()) {
            return Response.ok(bookService.findBooksByTitle(title)).build();
        }
        return Response.status(400, "Invalid Query Param").build();
    }
}
