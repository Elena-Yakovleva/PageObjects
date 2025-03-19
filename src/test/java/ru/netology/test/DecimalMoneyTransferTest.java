package ru.netology.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertAll;
import static ru.netology.data.DataHelper.*;
import static ru.netology.data.DataHelper.generateValidAmount;

public class DecimalMoneyTransferTest {

    DashboardPage dashboardPage;
    DataHelper.UserСard userFirstCardInfo;
    DataHelper.UserСard userSecondCardInfo;
    double firstCardBalance;
    double secondCardBalance;




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
    void shouldTransferDecimalSumFromSecondCardToFirstCard() {

        var amount = "12,56";
        var amountDecimalSum = rublesToKopecks(Double.parseDouble(amount.replace(",", ".")));
        var expectedBalanceFirstCard = rublesToKopecks(firstCardBalance) + amountDecimalSum;
        var expectedBalanceSecondCard = rublesToKopecks(secondCardBalance) - amountDecimalSum;
        var transferPage = dashboardPage.selectCard(userFirstCardInfo);
        System.out.println("сумма перевода " + amount +
                ",\nсумма в копейках " + amountDecimalSum +
                ",\n баланс карты firstCard до начисления " + firstCardBalance +
                ",\n баланс карты secondCard до списания " + secondCardBalance);
        dashboardPage = transferPage.moneyValidTransfer(String.valueOf(amount), userSecondCardInfo);
        dashboardPage.reloadDashboardPage();

        var firstCard = kopecksToRubles(expectedBalanceFirstCard);
        var secondCard = kopecksToRubles(expectedBalanceSecondCard);
        assertAll(() -> dashboardPage.checkCardBalance(userFirstCardInfo, firstCard ),
                () -> dashboardPage.checkCardBalance( userSecondCardInfo, secondCard ));
    }


}
