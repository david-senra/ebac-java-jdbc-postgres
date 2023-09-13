package dsenra.domain.mock;

import dsenra.domain.IPersistente;
import dsenra.domain.Venda;

import java.time.Instant;

public class MockVenda extends Venda implements IPersistente {
    private Venda venda01;
    private Venda venda02;

    public MockVenda() {
        this.venda01 = new Venda();
        this.venda01.setId(1L);
        this.venda01.setCodigo("10");
        this.venda01.setDataVenda(Instant.now());
        this.venda01.setStatus(Status.INICIADA);

        this.venda02 = new Venda();
        this.venda02.setId(2L);
        this.venda02.setCodigo("20");
        this.venda02.setDataVenda(Instant.now());
        this.venda02.setStatus(Status.INICIADA);
    }

    public Venda getVenda01() {
        return venda01;
    }

    public Venda getVenda02() {
        return venda02;
    }
}
