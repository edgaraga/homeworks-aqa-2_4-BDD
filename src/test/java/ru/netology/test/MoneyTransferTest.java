package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPageV2;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {

    @BeforeEach
    void auth() {
        open("http://localhost:9999", LoginPageV2.class);
        val loginPageV2 = new LoginPageV2();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPageV2.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferFromFirstToSecondCard() {
        val dashboardPage = new DashboardPage();
        int amount = 7777;
        int firstCardBalance = dashboardPage.getFirstCardBalance();
        int secondCardBalance = dashboardPage.getSecondCardBalance();
        var transferPage = dashboardPage.secondCard();
        var cardInfo = DataHelper.getFirstCardInfo();
        transferPage.transferCard(cardInfo, amount);
        int firstCardAfterTransferBalance = DataHelper.decreaseBalance(firstCardBalance, amount);
        int secondCardAfterTransferBalance = DataHelper.increaseBalance(secondCardBalance, amount);
        int firstCardBalanceAfter = dashboardPage.getFirstCardBalance();
        int secondCardBalanceAfter = dashboardPage.getSecondCardBalance();
        assertEquals(firstCardAfterTransferBalance, firstCardBalanceAfter);
        assertEquals(secondCardAfterTransferBalance, secondCardBalanceAfter);
    }

    @Test
    void shouldTransferFromSecondToFirstCard() {
        val dashboardPage = new DashboardPage();
        int amount = 3333;
        int firstCardBalance = dashboardPage.getFirstCardBalance();
        int secondCardBalance = dashboardPage.getSecondCardBalance();
        var transferPage = dashboardPage.firstCard();
        var cardInfo = DataHelper.getSecondCardInfo();
        transferPage.transferCard(cardInfo, amount);
        int firstCardAfterTransferBalance = DataHelper.increaseBalance(firstCardBalance, amount);
        int secondCardAfterTransferBalance = DataHelper.decreaseBalance(secondCardBalance, amount);
        int firstCardBalanceAfter = dashboardPage.getFirstCardBalance();
        int secondCardBalanceAfter = dashboardPage.getSecondCardBalance();
        assertEquals(firstCardAfterTransferBalance, firstCardBalanceAfter);
        assertEquals(secondCardAfterTransferBalance, secondCardBalanceAfter);
    }

    @Test
    void shouldNotTransferMoreThanRestOfBalanceAndShowErrorInThisCase() {
        val dashboardPage = new DashboardPage();
        int amount = 10001;
        dashboardPage.getFirstCardBalance();
        dashboardPage.getSecondCardBalance();
        var transferPage = dashboardPage.firstCard();
        var cardInfo = DataHelper.getSecondCardInfo();
        transferPage.transferCard(cardInfo, amount);
        transferPage.moneyTransferError();
    }
}