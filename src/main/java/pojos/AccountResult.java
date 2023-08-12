package pojos;

import interfaces.AccountCommand;
import interfaces.CborSerializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jyd1124
 * @date 2023/7/26 23:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResult implements CborSerializable, AccountCommand {

    private Long amount;

    private String msg;

    public AccountResult (Long amount) {
        this.amount = amount;
    }

    public AccountResult (String msg) {
        this.msg = msg;
    }

}
