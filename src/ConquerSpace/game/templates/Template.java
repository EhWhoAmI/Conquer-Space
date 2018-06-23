package ConquerSpace.game.templates;

import ConquerSpace.game.buildings.Buildable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author Zyun
 */
public class Template {

    private Class<?> c;
    private Object[] args;
    private String name;

    public Buildable create() throws ClassCastException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        Class[] types = new Class[args.length];
        for(int i = 0; i < args.length; i++) {
            
            types[i] = args[i].getClass();
        }
        Constructor<?> constructor = c.getConstructor(types);
        Object instance = constructor.newInstance(args);
        if(!(instance instanceof Buildable)) {
            throw new ClassCastException("Class " + c.getName() + " is not of class Buildable");
        }
        return ((Buildable) instance);
    }

    public Template(String className, Object[] args, String name) throws ClassNotFoundException {
        c = Class.forName(className);
        this.args = args;
        this.name = name;
    }

    public Class<?> getBuildableClass() {
        return c;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getName() {
        return name;
    }
}
