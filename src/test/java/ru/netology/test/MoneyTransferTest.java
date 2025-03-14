package ru.netology.test;

import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.DataHelper.*;

public class MoneyTransferTest {

    // java -jar .\artifacts\app-ibank-build-for-testers.jar

    @Test
    void shouldTransferMoneyFromSecondCardToFirstCard() {
        var user = getAuthInfo();
        var code = getVerificationCodeFor(user);
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var verifiloginPage = loginPage.validLogin(user);
        var dashboardPage = verifiloginPage.validVerify(code);
        var transferPage = dashboardPage.selectToFirstСard();
        var sumTransfer = "200";
        var secondCard = getSecondCard(user);
        dashboardPage = transferPage.moneyTransfer(sumTransfer, secondCard);



    }


}
