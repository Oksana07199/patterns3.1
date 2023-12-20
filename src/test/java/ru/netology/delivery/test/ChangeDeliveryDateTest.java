package ru.netology.delivery.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class ChangeDeliveryDateTest {
    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        // TODO: добавить логику теста в рамках которого будет выполнено планирование и перепланирование встречи.
        // Для заполнения полей формы можно использовать пользователя validUser и строки с датами в переменных
        // firstMeetingDate и secondMeetingDate. Можно также вызывать методы generateCity(locale),
        // generateName(locale), generatePhone(locale) для генерации и получения в тесте соответственно города,
        // имени и номера телефона без создания пользователя в методе generateUser(String locale) в датагенераторе
        $("[data-test-id='city'] input").setValue(validUser.getCity());
        String choiceDate = LocalDateTime.now().plusDays(4).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(firstMeetingDate);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $(".notification_status_ok .notification__content").shouldBe(visible, Duration.ofSeconds(20)).shouldHave(text("Встреча успешно запланирована на "+firstMeetingDate));
        $(".notification_status_ok .notification__title").shouldBe(visible, Duration.ofSeconds(20)).shouldHave(text("Успешно!"));
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(secondMeetingDate);
        $(".button").click();
        $(".notification_status_error .notification__title").shouldBe(visible, Duration.ofSeconds(20)).shouldHave(text("Необходимо подтверждение"));
        $(".notification_status_error .notification__content").shouldBe(visible, Duration.ofSeconds(20)).shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $$(".button").findBy(text("Перепланировать")).click();
        $(".notification_status_ok .notification__content").shouldBe(visible, Duration.ofSeconds(20)).shouldHave(text("Встреча успешно запланирована на "+secondMeetingDate));
        $(".notification_status_ok .notification__title").shouldBe(visible, Duration.ofSeconds(20)).shouldHave(text("Успешно!"));

    }
}