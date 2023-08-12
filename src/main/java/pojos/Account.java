package pojos;

import akka.http.javadsl.model.DateTime;
import interfaces.CborSerializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author jyd1124
 * @date 2023/7/24 17:04
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account implements CborSerializable {

    private static final long serialVersionUID = 1L;

    // 金额
    private long amount;

    // 用户 unique key
    private long userId;

    // 账户id
    private String accountId;

    public Account addAmount (AddAmountEvent event) {
        return Account.builder()
                .amount(amount + event.getValue())
                .userId(userId)
                .accountId(accountId)
                .build();
    }

    public Account subAmount (SubAmountEvent event) {
        return Account.builder()
                .amount(amount - event.getValue())
                .userId(userId)
                .accountId(accountId)
                .build();
    }

    public Account initStatus (UserAccountInfoEvent event) {
        return Account.builder()
                .amount(event.getInitAmount())
                .userId(event.getUserId())
                .accountId(event.getToAccountId())
                .build();
    }

    public Account copy () {
        return Account.builder()
                .amount(amount)
                .userId(userId)
                .accountId(accountId)
                .build();
    }
}
