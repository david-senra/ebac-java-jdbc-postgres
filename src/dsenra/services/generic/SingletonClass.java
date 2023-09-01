package dsenra.services.generic;

public class SingletonClass {
    private static SingletonClass singletonClass;
    protected Class<?> classe;

    public SingletonClass () {
        classe = null;
    }

    public static SingletonClass getInstance() {
        if (singletonClass == null) {
            singletonClass = new SingletonClass();
        }
        return singletonClass;
    }

    public Class<?> getClasse() {
        return this.classe;
    }

    public void setClasse(Class classe) {
        this.classe = classe;
    }
}
