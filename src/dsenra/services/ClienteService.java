package dsenra.services;

import dsenra.domain.Cliente;
import dsenra.services.generic.GenericService;
import dsenra.services.generic.SingletonClass;

public class ClienteService extends GenericService<Cliente> implements IClienteService {
    public ClienteService() {
        super();
        Class classeInterna = SingletonClass.getInstance().getClasse();
        if (classeInterna == null) {
            SingletonClass.getInstance().setClasse(ClienteService.class);
        }
    }
}
