package hello.springtransaction.order;

public class NoEnoughMoneyException extends Exception{

    public NoEnoughMoneyException(String message) {
        super(message);
    }
}
