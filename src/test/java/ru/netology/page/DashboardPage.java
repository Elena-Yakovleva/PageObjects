package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {

    private SelenideElement dashboardHeader = $("[data-test-id='dashboard']");

    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    private ElementsCollection cards = $$(".list__item div");

    private SelenideElement reloadButton = $("[data-test-id='action-reload']");

    // проверка перехода на страницу
    public DashboardPage() {
        dashboardHeader.shouldBe(Condition.visible, Condition.text("Личный кабинет"));
    }

    // обновление страницы через кнопку "Обновить"
    public void reloadDashboardPage() {
        reloadButton.click();
        dashboardHeader.shouldBe(Condition.visible);
    }

    // пополнить карту
    public TransferPage selectCard(DataHelper.UserСard userСard) {
       getCard(userСard).$("button").click();
        return new TransferPage();
    }

    // Получение баланса карты

    // 1. метод выводит элемент коллекции осуществляя поиск по тестовой метке нужной карты
    private SelenideElement getCard(DataHelper.UserСard userСard) {
        return cards.findBy(Condition.attribute("data-test-id", userСard.getCardId()));
    }
    // 2. метод обрабатывает полученный из коллекции элемент и выделяет из него текст
    //    затем с помощью доп метода extractBalance обрабатывает его и выводит информацию о балансе карты в форме числа
    public int getCardBalance(DataHelper.UserСard userСard) {
        val text = getCard(userСard).text();
        return  extractBalance(text);
    }

    // метод берет полученный текст и обрезает его по указанному диапазону.
    private int extractBalance(String text) {
        val start = (text.indexOf(balanceStart) + balanceStart.length()); // вычисляет индекс последнего элемента через сумму начально индекса и длинны подстроки
        val finish = text.indexOf(balanceFinish); // получает индекс первого элемента в подстроке: " р."
        val value = text.substring(start, finish); //обрезает текст, оставляя промежуток от индекса последнего элемента подстроки старт до первого элемента подстроки финиш

        return Integer.parseInt(value); // переводит полученное значение в int
    }

    // проверка видимости баланса
    public void checkCardBalance(DataHelper.UserСard userСard, int extractBalance ) {
        getCard(userСard).shouldBe(Condition.visible, Condition.text(balanceStart + extractBalance + balanceFinish));
    }

}
