package dsenra.services;

import dsenra.domain.Produto;
import dsenra.services.generic.GenericService;
import dsenra.services.generic.SingletonClass;

public class ProdutoService extends GenericService<Produto> implements IProdutoService {

    public ProdutoService() {
        super();
        Class classeInterna = SingletonClass.getInstance().getClasse();
        if (classeInterna == null) {
            SingletonClass.getInstance().setClasse(ProdutoService.class);
        }
    }

}
