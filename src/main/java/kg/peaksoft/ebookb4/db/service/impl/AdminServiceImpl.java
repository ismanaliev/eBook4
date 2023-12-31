package kg.peaksoft.ebookb4.db.service.impl;

import kg.peaksoft.ebookb4.dto.response.BookResponse;
import kg.peaksoft.ebookb4.dto.response.ClientResponse;
import kg.peaksoft.ebookb4.dto.response.CountForAdmin;
import kg.peaksoft.ebookb4.dto.response.VendorResponse;
import kg.peaksoft.ebookb4.db.models.entity.Book;
import kg.peaksoft.ebookb4.db.models.entity.User;
import kg.peaksoft.ebookb4.db.models.enums.ERole;
import kg.peaksoft.ebookb4.db.models.enums.RequestStatus;
import kg.peaksoft.ebookb4.db.models.mappers.ClientMapper;
import kg.peaksoft.ebookb4.db.models.mappers.VendorMapper;
import kg.peaksoft.ebookb4.dto.request.CustomPageRequest;
import kg.peaksoft.ebookb4.dto.request.RefuseBookRequest;
import kg.peaksoft.ebookb4.db.repository.BookRepository;
import kg.peaksoft.ebookb4.db.repository.UserRepository;
import kg.peaksoft.ebookb4.db.service.AdminService;
import kg.peaksoft.ebookb4.exceptions.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static kg.peaksoft.ebookb4.db.models.enums.RequestStatus.ACCEPTED;
import static kg.peaksoft.ebookb4.db.models.enums.RequestStatus.REFUSED;

@Slf4j
@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private BookRepository bookRepository;
    private UserRepository userRepository;
    private VendorMapper vendorMapper;
    private ClientMapper clientMapper;
    private ModelMapper modelMapper;

    @Override
    public List<BookResponse> getBooksFromBasket(Long clientId) {
        return bookRepository.findBasketByClientIdAdmin(clientId).stream().map(book ->
                modelMapper.map(book, BookResponse.class)).collect(Collectors.toList());
    }

    @Override
    public List<VendorResponse> findAllVendors() {
        List<User> users = userRepository.findAllVendors(ERole.ROLE_VENDOR);
        List<VendorResponse> vendorResponses = new ArrayList<>();
        log.info("Get all vendors works");
        for (User i : users) {
            vendorResponses.add(vendorMapper.createVendorDto(i));
        }
        return vendorResponses;
    }

    @Override
    public ResponseEntity<?> deleteById(String email) {
        User userById = userRepository.findByEmail(email).orElseThrow(() ->
                new BadRequestException("User not Found!"));
        if (userById.getRole().getName().equals(ERole.ROLE_ADMIN)) {
            return ResponseEntity.badRequest().body("User with id " + " not found");
        }
        log.info("Successfully deleter");
        userRepository.deleteById(userById.getId());
        return ResponseEntity.ok("Successfully deleted");
    }

    @Override
    public ResponseEntity<?> deleteByIdAdmin(Long userId) {
        User userById = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("User not Found!"));
        if (userById.getRole().getName().equals(ERole.ROLE_ADMIN)) {
            return ResponseEntity.badRequest().body("User with id " + " not found");
        }
        log.info("Successfully deleter");
        userRepository.deleteById(userById.getId());
        return ResponseEntity.ok("Successfully deleted");
    }

    @Override
    public ResponseEntity<?> deleteBookById(Long id) {
        if (!bookRepository.existsById(id)) {
            log.error("Delete book by id works");
            throw new BadRequestException("Book with id " + id + " does not exists");
        }
        bookRepository.deleteById(id);
        return ResponseEntity.ok().body("Successfully deleter");
    }

    @Override
    public List<ClientResponse> findAllClient() {
        List<User> users = userRepository.findAllClients(ERole.ROLE_CLIENT);
        List<ClientResponse> clientResponses = new ArrayList<>();
        for (User i : users) {
            clientResponses.add(clientMapper.createClientDto(i));
        }
        log.info("Find all Clients works");
        return clientResponses;
    }

    @Override
    public VendorResponse getVendorById(Long id) {
        User user = userRepository.getUserById(id, ERole.ROLE_VENDOR).orElseThrow(() -> {
            log.error("Vendor with id ={} does not exists", id);
            throw new BadRequestException(String.format("Vendor with id %s doesn't exist!", id));
        });
        log.info("Get all vendors works");
        return vendorMapper.createVendorDto(user);
    }

    @Override
    public ClientResponse getClientById(Long id) {
        User user = userRepository.getUserById(id, ERole.ROLE_CLIENT).orElseThrow(() -> {
            log.error("Client with id ={} does not exists", id);
            throw new BadRequestException(String.format("Client with id %s doesn't exist!", id));
        });
        log.info("Get client by id works");
        return clientMapper.createClientDto(user);
    }

    @Override
    @Transactional
    public ResponseEntity<?> acceptBookRequest(Long bookId) {
        Book book = bookRepository.findBookInProgress(bookId, RequestStatus.INPROGRESS).orElseThrow(() -> {
            log.error("Book with id ={} does not exists", bookId);
            throw new BadRequestException(String.format("Book with id %s does not exists or it is already went through admin-check", bookId));
        });
        log.info("admin accept books ");
        book.setRequestStatus(ACCEPTED);
        return ResponseEntity.status(HttpStatus.OK).body("Accepted successfully book with id = " + book.getTitle());
    }

    @Override
    @Transactional
    public ResponseEntity<?> refuseBookRequest(RefuseBookRequest request, Long id) {
        Book book = bookRepository.findBookInProgress(id, RequestStatus.INPROGRESS).orElseThrow(() -> {
            log.error("Book with id ={} does not exists", id);
            throw new BadRequestException(String.format("Book with id %s does not exists or it is already went through admin-check", id));
        });
        book.setRequestStatus(REFUSED);
        log.info("admin refuse book request");
        return ResponseEntity.ok().body(request.getReason());
    }

    @Override
    @Transactional
    public Book getBookById(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> {
            log.error("Book with id ={} does not exists", bookId);
            throw new BadRequestException(String.format("The book with the id %s was not found", bookId));
        });
        if (book.getAdminWatch().equals(false)) {
            book.setAdminWatch(true);
        }
        log.info("Get book by id works");
        return book;
    }

    @Override
    public List<Book> findBooksFromVendor(Integer offset, int pageSize, Long vendorId) {
        User user = userRepository.findById(vendorId).orElseThrow(() -> {
            log.error("Vendor with id ={} does not exists", vendorId);
            throw new BadRequestException(String.format("Vendor with id %s doesn't exist!", vendorId));
        });
        List<Book> books = bookRepository.findBooksFromVendor(user.getEmail());
        Pageable paging = PageRequest.of(offset, pageSize);
        int start = Math.min((int) paging.getOffset(), books.size());
        int end = Math.min((start + paging.getPageSize()), books.size());
        Page<Book> pages = new PageImpl<>(books.subList(start, end), paging, books.size());
        System.out.println(new CustomPageRequest<>(pages).getContent().size());
        log.info("Vendor books=s%" + new CustomPageRequest<>(pages).getContent().size());
        return new CustomPageRequest<>(pages).getContent();
    }

    @Override
    public List<Book> findBooksFromVendorInFavorites(Integer offset, int pageSize, Long vendorId) {
        User user = userRepository.findById(vendorId).orElseThrow(() -> {
            log.error("Vendor with id ={} does not exists", vendorId);
            throw new BadRequestException(String.format("Vendor with id %s doesn't exist!", vendorId));
        });
        List<Book> likedBooksFromVendor = bookRepository.findLikedBooksFromVendor(user.getEmail());
        Pageable paging = PageRequest.of(offset, pageSize);
        int start = Math.min((int) paging.getOffset(), likedBooksFromVendor.size());
        int end = Math.min((start + paging.getPageSize()), likedBooksFromVendor.size());
        Page<Book> pages = new PageImpl<>(likedBooksFromVendor.subList(start, end), paging, likedBooksFromVendor.size());
        return new CustomPageRequest<>(pages).getContent();
    }

    @Override
    public List<Book> findBooksFromVendorAddedToBasket(Integer offset, int pageSize, Long vendorId) {
        User user = userRepository.findById(vendorId).orElseThrow(() -> {
            log.error("Vendor with id ={} does not exists", vendorId);
            throw new BadRequestException(String.format("Vendor with id %s doesn't exist!", vendorId));
        });
        List<Book> booksWithBasket = bookRepository.findBooksFromVendorAddedToBasket(user.getEmail());
        Pageable paging = PageRequest.of(offset, pageSize);
        int start = Math.min((int) paging.getOffset(), booksWithBasket.size());
        int end = Math.min((start + paging.getPageSize()), booksWithBasket.size());
        Page<Book> pages = new PageImpl<>(booksWithBasket.subList(start, end), paging, booksWithBasket.size());
        return new CustomPageRequest<>(pages).getContent();
    }

    @Override
    public List<Book> findBooksFromVendorWithDiscount(Integer offset, int pageSize, Long vendorId) {
        User user = userRepository.findById(vendorId).orElseThrow(() -> new BadRequestException(String.format("Vendor with id %s doesn't exist!", vendorId)));
        List<Book> booksWithDiscount = bookRepository.findBooksFromVendorWithDiscount(user.getEmail());
        Pageable paging = PageRequest.of(offset, pageSize);
        int start = Math.min((int) paging.getOffset(), booksWithDiscount.size());
        int end = Math.min((start + paging.getPageSize()), booksWithDiscount.size());
        Page<Book> pages = new PageImpl<>(booksWithDiscount.subList(start, end), paging, booksWithDiscount.size());
        return new CustomPageRequest<>(pages).getContent();
    }

    @Override
    public List<Book> findBooksFromVendorCancelled(Integer offset, int pageSize, Long vendorId, RequestStatus status) {
        User user = userRepository.findById(vendorId).orElseThrow(() -> new BadRequestException(String.format("Vendor with id %s doesn't exist!", vendorId)));
        List<Book> booksWithCancel = bookRepository.findBooksFromVendorWithCancel(user.getEmail(), status);
        Pageable paging = PageRequest.of(offset, pageSize);
        int start = Math.min((int) paging.getOffset(), booksWithCancel.size());
        int end = Math.min((start + paging.getPageSize()), booksWithCancel.size());
        Page<Book> pages = new PageImpl<>(booksWithCancel.subList(start, end), paging, booksWithCancel.size());
        return new CustomPageRequest<>(pages).getContent();
    }

    @Override
    public List<Book> findBooksFromVendorInProcess(Integer offset, int pageSize, Long vendorId, RequestStatus status) {
        User user = userRepository.findById(vendorId).orElseThrow(() -> new BadRequestException(String.format("Vendor with id %s doesn't exist!", vendorId)));
        List<Book> booksInProgress = bookRepository.findBooksFromVendorInProgress(user.getEmail(), status);
        Pageable paging = PageRequest.of(offset, pageSize);
        int start = Math.min((int) paging.getOffset(), booksInProgress.size());
        int end = Math.min((start + paging.getPageSize()), booksInProgress.size());
        Page<Book> pages = new PageImpl<>(booksInProgress.subList(start, end), paging, booksInProgress.size());
        return new CustomPageRequest<>(pages).getContent();
    }

    @Override
    public List<Book> getBooksInPurchased(Long clientId) {
        return bookRepository.getBooksInPurchased(clientId);
    }

    @Override
    public List<BookResponse> getAllLikedBooks(Long clientId) {
        return userRepository.getAllLikedBooks(clientId).stream().map(book ->
                modelMapper.map(book, BookResponse.class)).collect(Collectors.toList());
    }

    @Override
    public CountForAdmin getCountOfInProgressAlsoDontWatched() {
        List<Book> bookList = bookRepository.findAll();
        for (Book book : bookList) {
            if (book.getRequestStatus().equals(ACCEPTED)) {
                book.setAdminWatch(true);
            }
            bookRepository.save(book);
        }
        Integer all = bookRepository.getCountOfBooksInProgress(RequestStatus.INPROGRESS);
        Integer countOfPages = countOfPages(all);
        Integer unread = getCountOfDidNotWatched(bookList);
        CountForAdmin counts = new CountForAdmin();
        counts.setCountOfPages(countOfPages);
        counts.setAll(all);
        counts.setUnread(unread);
        return counts;
    }

    public Integer countOfPages(Integer books) {
        int count = 1;
        int size = books;
        for (int i = 0; i < size; i++) {
            if (size - 8 >= 0) {
                size -= 8;
                count++;
            }
        }
        return count;
    }

    @Override
    public Integer getCountOfDidNotWatched(List<Book> bookList) {
        Integer count = 0;
        for (Book book : bookList) {
            if (book.getAdminWatch().equals(false)) {
                count++;
            }
        }
        return count;
    }

}