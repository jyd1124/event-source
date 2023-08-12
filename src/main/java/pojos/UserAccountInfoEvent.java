package pojos;

import interfaces.AccountEvent;
import interfaces.CborSerializable;
import lombok.*;

/**
 * @author jyd1124
 * @date 2023/7/23 15:32
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserAccountInfoEvent implements CborSerializable, AccountEvent {

    private static final long serialVersionUID = 1L;

    private Long initAmount;

    private Long userId;

    private String toAccountId;

    public static UserAccountInfoEvent convertCommand (UserAccountInfoCommand command) {
        return UserAccountInfoEvent.builder()
                .initAmount(command.getInitAmount())
                .userId(command.getUserId())
                .toAccountId(command.getToAccountId())
                .build();
    }

}
