package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class TransferPage {

    private SelenideElement transferHeader = $("h1");
    private SelenideElement amount = $("[data-test-id='amount'] input");
    private SelenideElement from = $("[data-test-id='from'] input");
    private SelenideElement transferButton = $("[data-test-id='action-transfer']");
    private String sumTransfer;

    public TransferPage() {
        transferHeader.shouldBe(Condition.visible, Condition.text("Пополнение карты"));
    }

    public DashboardPage moneyTransfer (String sumTransfer, DataHelper.UserСard card) {
        amount.click();
        amount.setValue(sumTransfer);
        from.setValue(card.getCardNumber());
        transferButton.click();
        return new DashboardPage();
    }

}
