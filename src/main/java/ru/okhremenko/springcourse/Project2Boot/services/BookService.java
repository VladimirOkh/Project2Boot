package ru.okhremenko.springcourse.Project2Boot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.okhremenko.springcourse.Project2Boot.models.Book;
import ru.okhremenko.springcourse.Project2Boot.models.Person;
import ru.okhremenko.springcourse.Project2Boot.repositories.BooksRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BooksRepository booksRepository;

    @Autowired
    public BookService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll(int page, int booksPerPage, boolean sort) {
        return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent();
    }

    public List<Book> getBooksStartingWith(String name) {
        return booksRepository.findBooksByNameStartingWith(name);
    }

    public Book findOne(int id) {
        Optional<Book> foundBook = booksRepository.findById(id);
        return foundBook.orElse(null);
    }

    public Optional<Person> getOwnerByBook(Book book) {
        return Optional.ofNullable(book.getOwner());
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        Book bookToBeUpdated = booksRepository.findById(id).get();

        updatedBook.setId(id);
        updatedBook.setOwner(bookToBeUpdated.getOwner());

        booksRepository.save(updatedBook);
    }

    @Transactional
    public void release(Book updatedBook) {
        updatedBook.setOwner(null);
        updatedBook.setTakenAt(null);
        booksRepository.save(updatedBook);
    }

    @Transactional
    public void setOwner(Book updatedBook, Person owner) {
        updatedBook.setOwner(owner);
        updatedBook.setTakenAt(new Date());
        booksRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }
}
