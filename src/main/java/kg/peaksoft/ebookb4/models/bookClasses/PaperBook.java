package kg.peaksoft.ebookb4.models.bookClasses;

import kg.peaksoft.ebookb4.models.others.FileSources;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.File;

/**
 * Author: Zhanarbek Abdurasulov
 * Date: 12/4/22
 */

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaperBook {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
    @SequenceGenerator(name = "generator", sequenceName = "generator", allocationSize = 1)
    private Long paperBookId;

    private String publishingHouse;
    @OneToOne
    private FileSources aboutBook;
    @OneToOne
    private FileSources fragmentOfBook;
    private Integer numberOfPages;
    private Integer numberOfSelected;

}
