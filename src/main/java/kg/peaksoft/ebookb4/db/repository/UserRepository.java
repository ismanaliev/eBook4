package kg.peaksoft.ebookb4.db.repository;

import kg.peaksoft.ebookb4.db.models.entity.Book;
import kg.peaksoft.ebookb4.db.models.entity.User;
import kg.peaksoft.ebookb4.db.models.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Query("SELECT s FROM User s WHERE s.email = ?1")
    Optional<User> getUser(String username);

    //fin all vendors / admin panel
    @Query("SELECT u FROM User u WHERE u.role.name=?1")
    List<User> findAllVendors(ERole role);

    //fin all clients / admin panel
    @Query("SELECT u FROM User u WHERE u.role.name=?1")
    List<User> findAllClients(ERole role);

    @Query("SELECT c.likedBooks FROM User c WHERE c.id = ?1")
    List<Book> getAllLikedBooks(Long id);

    //change discountPromo to null if it is expired
    @Transactional
    @Modifying
    @Query("UPDATE Book b SET b.discountFromPromo = null WHERE b.user = ?1")
    void checkForPromos(User user);

    //We give here a promo
    @Transactional
    @Modifying
    @Query("UPDATE Book b SET b.discountFromPromo = ?2 WHERE b.user = ?1 AND b.discount is null ")
    void givePromo(User user, int discount);

    @Query("SELECT b FROM User b WHERE b.id = ?1 AND b.role.name = ?2")
    Optional<User> getUserById(Long id, ERole roleClient);

}
