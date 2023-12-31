package kg.peaksoft.ebookb4.db.models.notEntities;

import kg.peaksoft.ebookb4.db.models.enums.BookType;
import kg.peaksoft.ebookb4.db.models.enums.Language;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class SortBooksGlobal {

    private Double min;

    private Double max;

    private List<String> genreName;

    private BookType bookType = BookType.PAPERBOOK;

    private List<Language> language = List.of(Language.KYRGYZ);

}
