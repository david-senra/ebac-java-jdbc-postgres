package dsenra.fabricas;

import dsenra.domain.Cliente;
import dsenra.fabricas.generic.GenericFactory;
import dsenra.services.ProdutoService;
import dsenra.services.generic.SingletonClass;

public class FactoryClientes extends GenericFactory<Cliente> implements IFactoryClientes {
    public FactoryClientes() {
        super();
        Class classeInterna = SingletonClass.getInstance().getClasse();
        if (classeInterna == null) {
            SingletonClass.getInstance().setClasse(ProdutoService.class);
        }
    }
}
