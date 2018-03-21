package ConquerSpace.util.scripts;

import java.nio.file.Paths;
import java.util.Properties;
import org.python.util.PythonInterpreter;

/**
 *
 * @author Zyun
 */
public class RunScript {
    private PythonInterpreter p;
    private String scriptName;
    
    public RunScript(String scriptName) {
        this.scriptName = scriptName;
        
        Properties props = new Properties();
        props.setProperty("python.path", Paths.get(scriptName).toAbsolutePath().getParent().normalize().toString());
        System.getProperties().setProperty("python.path", Paths.get(scriptName).toAbsolutePath().getParent().normalize().toString());
        p = new PythonInterpreter();
        PythonInterpreter.initialize(System.getProperties(), props, new String[] {""});
    }
    
    public void addVar(String name, Object o) {
        p.set(name, o);
    }
    
    public Object getObject(String name) {
        return (p.get(name));
    }
    
    public void exec() {
        p.execfile(scriptName);
    }
}
