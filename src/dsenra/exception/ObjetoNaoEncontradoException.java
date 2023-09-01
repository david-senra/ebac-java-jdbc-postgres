package dsenra.exception;

public class ObjetoNaoEncontradoException extends Exception {
    public ObjetoNaoEncontradoException(String msg) {
        this(msg, null);
    }

    public ObjetoNaoEncontradoException(String msg, Throwable erro) {
        super(msg, erro);
    }
}
