package kg.peaksoft.ebookb4.db.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import kg.peaksoft.ebookb4.db.models.booksClasses.Basket;
import kg.peaksoft.ebookb4.db.models.booksClasses.ClientOperations;
import kg.peaksoft.ebookb4.db.models.booksClasses.FileInformation;
import kg.peaksoft.ebookb4.db.models.enums.BookType;
import kg.peaksoft.ebookb4.db.models.enums.Language;
import kg.peaksoft.ebookb4.db.models.enums.RequestStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REFRESH;

@Getter
@Setter
@RequiredArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_seq")
    @SequenceGenerator(name = "hibernate_seq", sequenceName = "book_seq", allocationSize = 1)
    @Column(name = "book_id")
    private Long bookId;

    @Column(length = 10000000)
    private String title;

    @Column(length = 10000000)
    private String authorFullName;

    @Column(length = 10000000)
    private String aboutBook;

    @Column(length = 10000000)
    private String publishingHouse;

    private int yearOfIssue;

    private Double price;

    private Boolean isBestSeller;

    @Enumerated(value = EnumType.STRING)
    private Language language;

    @Enumerated(value = EnumType.STRING)
    private BookType bookType;

    @Enumerated(value = EnumType.STRING)
    private RequestStatus requestStatus = RequestStatus.INPROGRESS;

    private Boolean adminWatch = false;

    private int baskets;

    private int likes;

    private Integer discount;

    private LocalDate dateOfRegister;

    private LocalDate endOfTheNewTerm;

    private Boolean isNew = true;

    private Integer discountFromPromo;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "books_basket",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "basket_id"))
    private List<Basket> basket;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "liked_books",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> likedBooks;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = {REFRESH, MERGE, PERSIST, DETACH})
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "file_id")
    private FileInformation fileInformation;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "audiobook_id")
    private AudioBook audioBook;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ebook_id")
    private ElectronicBook electronicBook;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "paperbook_id")
    private PaperBook paperBook;

    @ManyToOne(fetch = FetchType.EAGER)
    private Genre genre;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "operation_books",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "operation_id"))
    private List<ClientOperations> operations;

    @JsonIgnore
    public void setOperation(ClientOperations courseId) {
        if (operations == null) {
            operations = new ArrayList<>();
        }
        operations.add(courseId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return baskets == book.baskets && likes == book.likes &&
                Objects.equals(bookId, book.bookId) &&
                Objects.equals(title, book.title) &&
                Objects.equals(authorFullName, book.authorFullName) &&
                Objects.equals(aboutBook, book.aboutBook) &&
                Objects.equals(publishingHouse, book.publishingHouse) &&
                Objects.equals(yearOfIssue, book.yearOfIssue) &&
                Objects.equals(price, book.price) &&
                Objects.equals(isBestSeller, book.isBestSeller) && language == book.language && bookType == book.bookType && requestStatus == book.requestStatus &&
                Objects.equals(adminWatch, book.adminWatch) &&
                Objects.equals(discount, book.discount) &&
                Objects.equals(dateOfRegister, book.dateOfRegister) &&
                Objects.equals(endOfTheNewTerm, book.endOfTheNewTerm) &&
                Objects.equals(isNew, book.isNew) &&
                Objects.equals(discountFromPromo, book.discountFromPromo) &&
                Objects.equals(basket, book.basket) &&
                Objects.equals(likedBooks, book.likedBooks) &&
                Objects.equals(user, book.user) &&
                Objects.equals(fileInformation, book.fileInformation) &&
                Objects.equals(audioBook, book.audioBook) &&
                Objects.equals(electronicBook, book.electronicBook) &&
                Objects.equals(paperBook, book.paperBook) &&
                Objects.equals(genre, book.genre) &&
                Objects.equals(operations, book.operations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, title, authorFullName, aboutBook, publishingHouse, yearOfIssue, price, isBestSeller, language, bookType, requestStatus, adminWatch, baskets, likes, discount, dateOfRegister, endOfTheNewTerm, isNew, discountFromPromo, basket, likedBooks, user, fileInformation, audioBook, electronicBook, paperBook, genre, operations);
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", title='" + title
                + ", price=" + price + "}";
    }

}