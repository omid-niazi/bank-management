package ir.bootcamp.bank.service;

import ir.bootcamp.bank.exceptions.CardExistsException;
import ir.bootcamp.bank.exceptions.InvalidCardPasswordException;
import ir.bootcamp.bank.exceptions.CardNotFoundException;
import ir.bootcamp.bank.model.Account;
import ir.bootcamp.bank.model.Card;
import ir.bootcamp.bank.repositories.CardRepository;

import java.sql.Date;
import java.sql.SQLException;

public class CardService {
    private CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Card find(String cardNumber) throws SQLException, CardNotFoundException {
        Card card = cardRepository.find(cardNumber);
        if (card == null) {
            throw new CardNotFoundException("there is no card with this number");
        }
        return card;
    }

    void createCard(String cardNumber, short cvv2, Date expireDate, Account account) throws SQLException, CardExistsException {
        if (cardRepository.find(cardNumber) != null) {
            throw new CardExistsException("a cad with this number already exists");
        }
        Card card = new Card(cardNumber, cvv2, expireDate, account, 1);
        cardRepository.add(card);
    }

    void changePassword(String cardNumber, String oldPassword, String newPassword) throws SQLException, CardNotFoundException, InvalidCardPasswordException {
        Card card = cardRepository.find(cardNumber);
        if (card == null) {
            throw new CardNotFoundException("there is no card with this card number");
        }

        if (card.password() == null || card.password().isEmpty()) {
            card.setPassword(newPassword);
            cardRepository.update(card);
            return;
        }

        if (!card.password().equals(oldPassword)) {
            throw new InvalidCardPasswordException("card password is wrong");
        }

        card.setPassword(newPassword);
        cardRepository.update(card);
        return;
    }

    void remove(String cardNumber) throws CardNotFoundException, SQLException {
        Card card = cardRepository.find(cardNumber);
        if (card == null) {
            throw new CardNotFoundException("there is no card with this card number");
        }
        cardRepository.delete(card.id());
    }

    void attemptFailed(Card card) throws SQLException {
        card.setFailedAttempt(card.failedAttempt() + 1);
        if (card.failedAttempt() > 2)
            card.setStatus(0);
        cardRepository.update(card);
    }

    void disableCard(Card inputCard) throws SQLException {
        Card card = inputCard;
        card.setStatus(0);
        cardRepository.update(card);
    }
    void enableCard(Card inputCard) throws SQLException {
        Card card = inputCard;
        card.setFailedAttempt(0);
        card.setStatus(1);
        cardRepository.update(card);
    }
}
