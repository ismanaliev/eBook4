package kg.peaksoft.ebookb4.db.service;

import kg.peaksoft.ebookb4.dto.ClientOperationDTO;
import kg.peaksoft.ebookb4.dto.ClientRegisterDTO;
import kg.peaksoft.ebookb4.dto.ClientUpdateDTO;
import kg.peaksoft.ebookb4.db.models.entity.Book;
import kg.peaksoft.ebookb4.dto.response.BookResponse;
import kg.peaksoft.ebookb4.dto.response.CardResponse;
import org.springframework.http.ResponseEntity;

import javax.transaction.Transactional;
import java.util.List;

public interface ClientService {

    ResponseEntity<?> register(ClientRegisterDTO clientRegisterDTO, Long number);

    ResponseEntity<?> likeABook(Long bookId, String username);

    ResponseEntity<?> addBookToBasket(Long bookId, String username);

    List<BookResponse> getBooksFromBasket(String name);

    @Transactional
    ResponseEntity<?> update(ClientUpdateDTO newClientDTO, String username);

    ClientRegisterDTO getClientDetails(String username);

    ResponseEntity<?> deleteBookFromBasket(Long id, String authentication);

    void cleanBasketOfClientByEmail(String clientEmail);

    ClientOperationDTO sumAfterPromo(String name);

    ResponseEntity<?> placeOrder(String name);

    List<Book> operationBook(String name);

    List<CardResponse> plusOrMinus(String name, String plusOrMinus, Long bookId, String promoCode);

}
