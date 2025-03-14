package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {

    private SelenideElement dashboardHeader = $("[data-test-id='dashboard']");
    private SelenideElement firstCardButton = $("[data-test-id='92df3f1c-a033-48e6-8390-206f6b1f56c0']> [data-test-id='action-deposit']");
    private SelenideElement sekondCardButton = $("[data-test-id='0f3f5c2a-249e-4c3d-8287-09f7a039391d']> [data-test-id='action-deposit']");

    private ElementsCollection cards = $$(".list__item div");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";



    public DashboardPage() {
        dashboardHeader.shouldBe(Condition.visible, Condition.text("Личный кабинет"));
    }

    public TransferPage selectToFirstСard () {
        firstCardButton.click();
        return new TransferPage();
    }

    public TransferPage selectToSecondСard () {
        sekondCardButton.click();
        return new TransferPage();
    }

//    public int checkingBalanceReplenishmentFirstCard () {
//
//
//    }


    public int getCardBalance() {
        val text = cards.first().text();
        return extractBalance(text);
    }

    private int extractBalance(String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }



}
