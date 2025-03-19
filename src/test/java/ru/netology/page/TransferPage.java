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
    private SelenideElement cancelButton = $("[data-test-id='action-cancel']");
    private SelenideElement errorMessage = $("[data-test-id='error-notification']");

    // метод проверки перехода на страницу
    public TransferPage() {
        transferHeader.shouldBe(Condition.visible, Condition.text("Пополнение карты"));
    }

    // метод перевода любой суммы
    public void moneyTransfer (String amountTransfer, DataHelper.UserСard card) {
        amount.click();
        amount.setValue(amountTransfer);
        from.setValue(card.getCardNumber());
        transferButton.click();
    }

    // метод перевода валидной суммы
    public DashboardPage moneyValidTransfer(String amountTransfer, DataHelper.UserСard card) {

        moneyTransfer(amountTransfer, card);
        return new DashboardPage();
    }

    // метод отмены перевода и возврата на страницу с картами

    public DashboardPage cancelTransfer() {
        cancelButton.click();
        return new DashboardPage();
    }

    // метод нажатия на кнопку
    public TransferPage transferButtonClick() {
        transferButton.click();
        return new TransferPage();
    }


    //  метод проверки наличия сообщения об ошибке
    public void findErrorMessage(String expectedText) {
        errorMessage.should(Condition.and("Проверка сообщения об ошибке", Condition.text(expectedText), Condition.visible));
    }


}
