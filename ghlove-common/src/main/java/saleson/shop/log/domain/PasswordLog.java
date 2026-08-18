package saleson.shop.log.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordLog {

    private final int limit = 2;

    private long userId;

    private String password;

    private String createdDate;

}
