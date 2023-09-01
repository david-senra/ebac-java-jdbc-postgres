package dsenra.exception;

public class ObjetoJaCadastradoException extends Exception {
    public ObjetoJaCadastradoException(String msg) {
        this(msg, null);
    }

    public ObjetoJaCadastradoException(String msg, Throwable erro) {
        super(msg, erro);
    }
}
