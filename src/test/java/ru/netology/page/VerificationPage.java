package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {

    private SelenideElement verifyHeader = $("h2");
    private SelenideElement verifyButton = $("[data-test-id='action-verify']");
    private SelenideElement verifyCode = $("[data-test-id='code'] input");


    public VerificationPage() {
        verifyHeader.shouldBe(Condition.visible, Condition.text("Интернет Банк"));
    }

    public DashboardPage validVerify(DataHelper.VerificationCode verificationCode) {
        verifyCode.setValue(verificationCode.getCode());
        verifyButton.click();
        return new DashboardPage();
    }
}
