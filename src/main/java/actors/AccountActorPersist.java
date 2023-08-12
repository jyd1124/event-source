package actors;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.cluster.sharding.typed.javadsl.ClusterSharding;
import akka.cluster.sharding.typed.javadsl.Entity;
import akka.cluster.sharding.typed.javadsl.EntityTypeKey;
import akka.persistence.typed.PersistenceId;
import akka.persistence.typed.javadsl.*;
import interfaces.AccountCommand;
import interfaces.AccountEvent;
import pojos.*;

/**
 * @author jyd1124
 * @date 2023/7/25 18:57
 */
public class AccountActorPersist extends EventSourcedBehavior<AccountCommand, AccountEvent, Account> {

    // 账户
    private final Account account;

    public static final EntityTypeKey<AccountCommand> TypeKey =
            EntityTypeKey.create(AccountCommand.class, "AccountActorPersist");

    public static Behavior<AccountCommand> create(String entityId, PersistenceId persistenceId) {
        return Behaviors.setup(context -> new AccountActorPersist(context, entityId, persistenceId));
    }

    public static void initSharding(ActorSystem<?> system) {
        ClusterSharding.get(system).init(
                Entity.of(TypeKey, entityContext ->
                    AccountActorPersist.create(
                            entityContext.getEntityId(),
                            PersistenceId.of(
                                    entityContext.getEntityTypeKey().name(),
                                    entityContext.getEntityId()))
        ));
    }

    private AccountActorPersist(
            ActorContext<AccountCommand> context, String entityId, PersistenceId persistenceId) {
        super(persistenceId);
        this.account = new Account();
        context.getLog().info("Starting generate AccountActorPersist {}", entityId);
    }

    @Override
    public Account emptyState() {
        return this.account;
    }

    @Override
    public CommandHandler<AccountCommand, AccountEvent, Account> commandHandler() {
        return newCommandHandlerBuilder()
                .forAnyState()
                .onCommand(UserAccountInfoCommand.class, this::onRegister)
                .onCommand(AddAmountCommand.class, this::onAdd)
                .onCommand(SubAmountCommand.class, this::onSub)
                .onCommand(QueryAmountCommand.class, this::onQuery)
                .build();
    }

    @Override
    public EventHandler<Account, AccountEvent> eventHandler() {
        return newEventHandlerBuilder()
                .forAnyState()
                .onEvent(UserAccountInfoEvent.class, Account::initStatus)
                .onEvent(AddAmountEvent.class, Account::addAmount)
                .onEvent(SubAmountEvent.class, Account::subAmount)
                .build();
    }

    public Effect<AccountEvent, Account> onRegister (Account state, UserAccountInfoCommand command) {
        return Effect()
                .persist(UserAccountInfoEvent.convertCommand(command))
                .thenRun(newAccount -> command.getToActor().tell(new AccountResult("accountId:" + command.getToAccountId())));
    }

    private Effect<AccountEvent, Account> onQuery(Account state, QueryAmountCommand command) {
        return Effect()
                .none()
                .thenRun(newAccount -> command.getToActor().tell(new AccountResult(newAccount.getAmount())));
    }

    public Effect<AccountEvent, Account> onSub (Account state, SubAmountCommand command) {
        return Effect()
                .persist(SubAmountEvent.convertCommand(command))
                .thenRun(newAccount -> command.getToActor().tell(new AccountResult("sub success")));
    }

    public Effect<AccountEvent, Account> onAdd (Account state, AddAmountCommand command) {
        return Effect()
                .persist(AddAmountEvent.convertCommand(command))
                .thenRun(newAccount -> command.getToActor().tell(new AccountResult("add success")));
    }

    @Override
    public RetentionCriteria retentionCriteria() {
        return RetentionCriteria.snapshotEvery(1000, 1);
    }
}
