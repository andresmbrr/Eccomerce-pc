package example.ms_stock.exception;

public class InsufficientStockException
        extends RuntimeException {

    public InsufficientStockException(String message) {
        super(message);
    }
}
