package kg.peaksoft.ebookb4.db.models.mappers;

import kg.peaksoft.ebookb4.db.models.dto.ClientRegisterDTO;
import kg.peaksoft.ebookb4.db.models.entity.User;
import org.springframework.stereotype.Component;

/**
 * Author: Zhanarbek Abdurasulov
 * Date: 24/4/22
 */
@Component
public class ClientRegisterMapper {

    public ClientRegisterDTO createDTO(User user){
        ClientRegisterDTO dto = new ClientRegisterDTO();
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        return dto;
    }

}