package ir.bootcamp.bank.service;

import ir.bootcamp.bank.model.Card;
import ir.bootcamp.bank.repositories.CardRepository;

import java.sql.SQLException;

import static ir.bootcamp.bank.util.ConsoleMessageType.error;
import static ir.bootcamp.bank.util.ConsoleMessageType.success;
import static ir.bootcamp.bank.util.ConsoleUtil.print;

public class CardService {
    private CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Card find(String cardNumber) throws SQLException {
        return cardRepository.find(cardNumber);
    }

    public void changePassword(String cardNumber, String oldPassword, String newPassword) throws SQLException {
        Card card = cardRepository.find(cardNumber);
        if (card == null) {
            print("card number is wrong", error);
            return;
        }

        if (card.password() == null || card.password().isEmpty()) {
            card.setPassword(newPassword);
            cardRepository.update(card);
            print("password changed", success);
            return;
        }

        if (!card.password().equals(oldPassword)) {
            print("your password is wrong", error);
            return;
        }

        card.setPassword(newPassword);
        cardRepository.update(card);
        print("password changed", success);
        return;
    }
}
