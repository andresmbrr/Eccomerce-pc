package example.ms_categorias.exception;

public class CategoriaAlreadyExistsException extends RuntimeException {

    public CategoriaAlreadyExistsException(String message) {
        super(message);
    }
}