package kg.peaksoft.ebookb4.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CountForAdmin {

    private Integer all;

    private Integer unread;

    private Integer countOfPages;

}
