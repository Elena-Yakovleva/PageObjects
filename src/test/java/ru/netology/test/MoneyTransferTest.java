package ru.netology.test;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.netology.data.DataHelper.*;

public class MoneyTransferTest {

    // java -jar .\artifacts\app-ibank-build-for-testers.jar
    DashboardPage dashboardPage;
    UserСard userFirstCardInfo;
    UserСard userSecondCardInfo;
    int firstCardBalance;
    int secondCardBalance;


    @BeforeAll
    public static void setup() {
        given()
                .baseUri("http://localhost:9999")
                .contentType("text/html; charset=UTF-8")
                .when()
                .get("/")
                .then()
                .statusCode(200);
    }

    @BeforeEach
    void setupAll() {
        open("http://localhost:9999");
        var user = getAuthInfo();
        var code = getVerificationCodeFor(user);
        var loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(user);
        dashboardPage = verificationPage.validVerify(code);
        userFirstCardInfo = DataHelper.getFirstCard(user);
        firstCardBalance = dashboardPage.getCardBalance(userFirstCardInfo);
        userSecondCardInfo = DataHelper.getSecondCard(user);
        secondCardBalance= dashboardPage.getCardBalance(userSecondCardInfo);

    }
    @Test
    void shouldNotHandleEmptyForm() {
        var transferPage = dashboardPage.selectCard(userFirstCardInfo);
        transferPage.transferButtonClick();
        transferPage.findErrorMessage("Ошибка! ");
    }

    @Test
    void shouldTransferMoneyFromSecondCardToFirstCard() {
        var amount = generateValidAmount(secondCardBalance);
        var expectedBalanceFirstCard = firstCardBalance + amount;
        var expectedBalanceSecondCard = secondCardBalance - amount;
        var transferPage = dashboardPage.selectCard(userFirstCardInfo);
        dashboardPage = transferPage.moneyValidTransfer(String.valueOf(amount), userSecondCardInfo);
        dashboardPage.reloadDashboardPage();
        assertAll(() -> dashboardPage.checkCardBalance(userFirstCardInfo, expectedBalanceFirstCard),
                () -> dashboardPage.checkCardBalance(userSecondCardInfo, expectedBalanceSecondCard));
    }

    @Test
    void shouldTransferMoneyFromFirstCardToSecondCard() {
        var amount = generateValidAmount(firstCardBalance);
        var expectedBalanceFirstCard = firstCardBalance - amount;
        var expectedBalanceSecondCard = secondCardBalance + amount;
        var transferPage = dashboardPage.selectCard(userSecondCardInfo);
        dashboardPage = transferPage.moneyValidTransfer(String.valueOf(amount), userFirstCardInfo);
        dashboardPage.reloadDashboardPage();
        assertAll(() -> dashboardPage.checkCardBalance(userFirstCardInfo, expectedBalanceFirstCard),
                () -> dashboardPage.checkCardBalance(userSecondCardInfo, expectedBalanceSecondCard));
    }

    @Test
    void shouldCancelTransferMoneyFromSecondCardToFirstCard() {
        var amount = generateValidAmount(secondCardBalance);
        var expectedBalanceFirstCard = firstCardBalance;
        var expectedBalanceSecondCard = secondCardBalance;
        var transferPage = dashboardPage.selectCard(userFirstCardInfo);
        dashboardPage=transferPage.cancelTransfer();
        assertAll(() -> dashboardPage.checkCardBalance(userFirstCardInfo, expectedBalanceFirstCard),
                () -> dashboardPage.checkCardBalance(userSecondCardInfo, expectedBalanceSecondCard));
    }

    @Test
    void shouldCancelTransferMoneyFromFirstCardToSecondCard() {
        var amount = generateValidAmount(firstCardBalance);
        var expectedBalanceFirstCard = firstCardBalance;
        var expectedBalanceSecondCard = secondCardBalance;
        var transferPage = dashboardPage.selectCard(userSecondCardInfo);
        dashboardPage = transferPage.cancelTransfer();
        assertAll(() -> dashboardPage.checkCardBalance(userFirstCardInfo, expectedBalanceFirstCard),
                () -> dashboardPage.checkCardBalance(userSecondCardInfo, expectedBalanceSecondCard));
    }




    @Test
    void shouldNotTransferInvalidAmountFromSecondCardToFirstCard(){
        var amount = generateInvalidAmount(secondCardBalance);
        var expectedBalanceFirstCard = firstCardBalance;
        var expectedBalanceSecondCard = secondCardBalance;
        var transferPage = dashboardPage.selectCard(userFirstCardInfo);
        System.out.println("shouldNotTransferInvalidAmountFromSecondCardToFirstCard:" + "сумма перевода " + amount +
                ",\n баланс карты secondCard до списания " + secondCardBalance +
                ",\n баланс карты firstCard до начисления " + firstCardBalance);
        transferPage.moneyTransfer(String.valueOf(amount), userSecondCardInfo);
        assertAll(() ->transferPage.findErrorMessage("Ошибка"),
                () -> dashboardPage.reloadDashboardPage(),
                () -> dashboardPage.checkCardBalance(userFirstCardInfo, expectedBalanceFirstCard),
                () -> dashboardPage.checkCardBalance(userSecondCardInfo, expectedBalanceSecondCard));

    }

    @Test
    void shouldNotTransferInvalidAmountFromFirstCardToSecondCard() {
        var amount = generateInvalidAmount(firstCardBalance);
        var expectedBalanceFirstCard = firstCardBalance;
        var expectedBalanceSecondCard = secondCardBalance;
        System.out.println("shouldNotTransferInvalidAmountFromFirstCardToSecondCard:" + "сумма перевода " + amount +
                ",\n баланс карты firstCard до списания " + firstCardBalance +
                ",\n баланс карты secondCard до начисления " + secondCardBalance);
        var transferPage = dashboardPage.selectCard(userSecondCardInfo);
        transferPage.moneyTransfer(String.valueOf(amount), userFirstCardInfo);
        assertAll(() ->transferPage.findErrorMessage("Ошибка"),
                () -> dashboardPage.reloadDashboardPage(),
                () -> dashboardPage.checkCardBalance(userFirstCardInfo, expectedBalanceFirstCard),
                () -> dashboardPage.checkCardBalance(userSecondCardInfo, expectedBalanceSecondCard));
    }




}
