package kg.peaksoft.ebookb4.db.models.bookClasses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Author: Zhanarbek Abdurasulov
 * Date: 12/4/22
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ElectronicBook {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
    @SequenceGenerator(name = "generator", sequenceName = "generator", allocationSize = 1)
    private Long ebookId;
    private String publishingHouse;
    private String fragmentOfBook;
    private Integer numberOfPages;
    private String urlOfBookFromCloud;


}