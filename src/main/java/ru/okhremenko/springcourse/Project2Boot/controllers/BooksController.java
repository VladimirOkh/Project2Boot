package ru.okhremenko.springcourse.Project2Boot.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.okhremenko.springcourse.Project2Boot.models.Book;
import ru.okhremenko.springcourse.Project2Boot.models.Person;
import ru.okhremenko.springcourse.Project2Boot.services.BookService;
import ru.okhremenko.springcourse.Project2Boot.services.PeopleService;

import java.util.Optional;


@Controller
@RequestMapping("/books")
public class BooksController {

    private final BookService bookService;
    private final PeopleService peopleService;

    @Autowired
    public BooksController(BookService bookService, PeopleService peopleService) {
        this.bookService = bookService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(Model model,
                        @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                        @RequestParam(name = "books_per_page", required = false, defaultValue = "10") int booksPerPage,
                        @RequestParam(name = "sort_by_year", required = false, defaultValue = "true") boolean sort_by_year) {
        model.addAttribute("books", bookService.findAll(page, booksPerPage, sort_by_year));

        return "books/index";
    }

    @GetMapping("/search")
    public String search() {
        return "books/search";
    }

    @PostMapping("/search")
    public String makeSearch(Model model, @RequestParam(value = "query", required = false) String query) {
        model.addAttribute("books", bookService.getBooksStartingWith(query));
        return "books/search";
    }


    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, @ModelAttribute("person") Person person, Model model) {
        model.addAttribute("book", bookService.findOne(id));
        Optional<Person> holder = bookService.getOwnerByBook(bookService.findOne(id));
        if (holder.isPresent())
            model.addAttribute("holder", holder.get());
        else
            model.addAttribute("people", peopleService.findAll());
        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(Model model) {
        model.addAttribute("book", new Book());
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book) {

        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookService.findOne(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, @PathVariable("id") int id) {

        bookService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("{id}/release")
    public String release(@PathVariable("id") int id) {
        bookService.release(bookService.findOne(id));
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person person) {
        bookService.setOwner(bookService.findOne(id), person);
        return "redirect:/books/" + id;
    }

}
