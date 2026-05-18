package example.ms_carrito.exception;

public class CartNotFoundException
        extends RuntimeException {

    public CartNotFoundException(String message) {
        super(message);
    }
}