package com.orion.DigiWallet.repository;

import com.orion.DigiWallet.model.Category;
import com.orion.DigiWallet.model.Transaction;
import com.orion.DigiWallet.model.User;
import com.orion.DigiWallet.model.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
// FIRST SEE THE APPLICATION.PROPERTIES IN TEST RESOURCES FOLDER
// ALSO LOOK AT THE DBSCIPT.SQL AND DATAINSERT.SQL FILES IN MAIN FOLDER
//RUN THE SHELL SCRIPT TO CREATE THE TABLES IN TEST DATABASE BEFORE RUNNING THE TESTS
//TODO: 3.6.1: REMOVE @Disabled TO ENABLE THE TESTS
@Disabled
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private User user;
    private Wallet wallet;
    private Category category;

    @BeforeEach
    void setUp() {
        // GIVEN
        // Create and persist User
        //TODO: 3.6.2: SET OTHER MANDATORY FIELDS IF ANY IN USER ENTITY
        // create user with dummy data eg username, email etc
        // username: "Test User", email: "txn.user@test.com
        // Save the user to the repository and assign to this.user
        User user = new User();
        user.setUsername("Test User");
        user.setEmail("txn.user@test.com");
        user.setRole("USER");
        user.setStatus("ACTIVE");
        this.user = userRepository.save(user);
        // ...existing code...

        // Create and persist Wallet linked to User
        //TODO: 3.6.3: Create wallet with dummy data eg balance, currency, status etc
        // balance: 1000, currency: "INR", status: "ACTIVE"
        // Link the wallet to the user created above
        // Balance is BigDecimal.valueOf(1000)
        // Save the wallet to the repository and assign to this.wallet
        Wallet wallet = new Wallet();
        wallet.setUser(this.user);
        wallet.setBalance(BigDecimal.valueOf(1000));
        wallet.setCurrency("INR");
        wallet.setStatus("ACTIVE");
        this.wallet = walletRepository.save(wallet);

        // Create and persist Category
        //TODO: 3.6.4: Create category with type "EXPENSE"
        // Save the category to the repository and assign to this.category
        Category category = new Category();
        category.setName("General");
        category.setType("EXPENSE");
        this.category = categoryRepository.save(category);

    }

    //TODO: 3.6.5: READ ONLY
    // Write a test to verify:
    // - Transaction can be saved successfully
    // - Transaction ID is generated
    @Disabled
    @Test
    void shouldSaveTransactionSuccessfully() {
        // GIVEN
        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setCategory(category);
        transaction.setAmount(250.0);
        transaction.setTransactionType("DEBIT");
        transaction.setReferenceId("TXN-TEST-001");

        // WHEN
        Transaction savedTransaction = transactionRepository.save(transaction);

        // THEN
        assertThat(savedTransaction.getId()).isNotNull();
    }

    // TODO: 3.6.6:
    // Write a test to verify:
    // - Transactions can be fetched by User ID
    @Test
    void shouldFindTransactionsByUserId() {
        // GIVEN
        // Create and save multiple Transactions for the Wallet
        Transaction transaction1 = new Transaction();
        transaction1.setWallet(wallet);
        transaction1.setCategory(category);
        transaction1.setAmount(250.0);
        transaction1.setTransactionType("DEBIT");
        transaction1.setReferenceId("TXN-TEST-001");
        transactionRepository.save(transaction1);

        Transaction transaction2 = new Transaction();
        transaction2.setWallet(wallet);
        transaction2.setCategory(category);
        transaction2.setAmount(500.0);
        transaction2.setTransactionType("CREDIT");
        transaction2.setReferenceId("TXN-TEST-002");
        transactionRepository.save(transaction2);

        // WHEN
        List<Transaction> transactions = transactionRepository.findByWallet_User_Id(user.getId());

        // THEN
        assertThat(transactions).isNotEmpty();
        assertThat(transactions).hasSize(2);
        assertThat(transactions.get(0).getWallet().getUser().getId()).isEqualTo(user.getId());
        assertThat(transactions.get(1).getWallet().getUser().getId()).isEqualTo(user.getId());
    }

    // TODO: 3.6.7
    // Write a test to verify:
    // - Empty list is returned when no transactions exist for user
    @Test
    void shouldReturnEmptyListWhenNoTransactionsForUser() {
        // GIVEN
        // Create a new user without any transactions
        User newUser = new User();
        newUser.setUsername("No Transaction User");
        newUser.setEmail("notxn.user@test.com");
        newUser.setRole("USER");
        newUser.setStatus("ACTIVE");
        User savedNewUser = userRepository.save(newUser);

        // WHEN
        List<Transaction> transactions = transactionRepository.findByWallet_User_Id(savedNewUser.getId());

        // THEN
        assertThat(transactions).isEmpty();
    }
}
