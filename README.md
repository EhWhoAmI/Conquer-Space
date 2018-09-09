Conquer Space
=======

The general idea: Conquer Space!

We are still coding this thingy, but we hope that this will be rather complex.

We hope to include:
 - Turn based gameplay
 - Espionage
 - Extremely detailed gameplay
### Compiling
This uses [Netbeans](https://netbeans.org/) to compile, but [Apache Ant](http://ant.apache.org/) will do.

`git clone https://github.com/EhWhoAmI/Conquer-Space.git` to clone this repo.

Note when compiling code, especially python code. You need to clean, then build before running it, as running just builds it, and if you refractored it, or added a new method,
the new build does not record that. It will fail, pretty badly with something in the lines of `java.lang.NoSuchMethodError: java.lang.NoSuchMethodError` or something like that.
Here's an example:
```
Traceback (most recent call last):
  File "Conquer-Space/assets/scripts/python/universeGen/main.py", line 143, in <module>
    centerSize = sector.getSize()
	at ConquerSpace.game.universe.spaceObjects.Sector.getSize(Sector.java:111)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)

java.lang.NoSuchMethodError: java.lang.NoSuchMethodError: ConquerSpace.game.universe.GalaticLocation.getDistance()F

	at org.python.core.Py.JavaError(Py.java:546)
	at org.python.core.Py.JavaError(Py.java:537)
	at org.python.core.PyReflectedFunction.__call__(PyReflectedFunction.java:188)
	at org.python.core.PyReflectedFunction.__call__(PyReflectedFunction.java:204)
	at org.python.core.PyObject.__call__(PyObject.java:461)
	at org.python.core.PyObject.__call__(PyObject.java:465)
	at org.python.core.PyMethod.__call__(PyMethod.java:126)
	at org.python.pycode._pyx0.f$0(Conquer-Space/assets/scripts/python/universeGen/main.py:229)
	at org.python.pycode._pyx0.call_function(Conquer-Space/assets/scripts/python/universeGen/main.py)
	at org.python.core.PyTableCode.call(PyTableCode.java:167)
	at org.python.core.PyCode.call(PyCode.java:18)
	at org.python.core.Py.runCode(Py.java:1386)
	at org.python.core.__builtin__.execfile_flags(__builtin__.java:535)
	at org.python.util.PythonInterpreter.execfile(PythonInterpreter.java:286)
	at ConquerSpace.util.scripts.RunScript.exec(RunScript.java:34)
	at ConquerSpace.start.gui.NewGame.actionPerformed(NewGame.java:240)
	at javax.swing.AbstractButton.fireActionPerformed(AbstractButton.java:2022)
	at javax.swing.AbstractButton$Handler.actionPerformed(AbstractButton.java:2348)
	at javax.swing.DefaultButtonModel.fireActionPerformed(DefaultButtonModel.java:402)
	at javax.swing.DefaultButtonModel.setPressed(DefaultButtonModel.java:259)
	at javax.swing.plaf.basic.BasicButtonListener.mouseReleased(BasicButtonListener.java:252)
	at java.awt.Component.processMouseEvent(Component.java:6533)
	at javax.swing.JComponent.processMouseEvent(JComponent.java:3324)
	at java.awt.Component.processEvent(Component.java:6298)
	at java.awt.Container.processEvent(Container.java:2236)
	at java.awt.Component.dispatchEventImpl(Component.java:4889)
	at java.awt.Container.dispatchEventImpl(Container.java:2294)
	at java.awt.Component.dispatchEvent(Component.java:4711)
	at java.awt.LightweightDispatcher.retargetMouseEvent(Container.java:4888)
	at java.awt.LightweightDispatcher.processMouseEvent(Container.java:4525)
	at java.awt.LightweightDispatcher.dispatchEvent(Container.java:4466)
	at java.awt.Container.dispatchEventImpl(Container.java:2280)
	at java.awt.Window.dispatchEventImpl(Window.java:2746)
	at java.awt.Component.dispatchEvent(Component.java:4711)
	at java.awt.EventQueue.dispatchEventImpl(EventQueue.java:758)
	at java.awt.EventQueue.access$500(EventQueue.java:97)
	at java.awt.EventQueue$3.run(EventQueue.java:709)
	at java.awt.EventQueue$3.run(EventQueue.java:703)
	at java.security.AccessController.doPrivileged(Native Method)
	at java.security.ProtectionDomain$JavaSecurityAccessImpl.doIntersectionPrivilege(ProtectionDomain.java:80)
	at java.security.ProtectionDomain$JavaSecurityAccessImpl.doIntersectionPrivilege(ProtectionDomain.java:90)
	at java.awt.EventQueue$4.run(EventQueue.java:731)
	at java.awt.EventQueue$4.run(EventQueue.java:729)
	at java.security.AccessController.doPrivileged(Native Method)
	at java.security.ProtectionDomain$JavaSecurityAccessImpl.doIntersectionPrivilege(ProtectionDomain.java:80)
	at java.awt.EventQueue.dispatchEvent(EventQueue.java:728)
	at java.awt.EventDispatchThread.pumpOneEventForFilters(EventDispatchThread.java:201)
	at java.awt.EventDispatchThread.pumpEventsForFilter(EventDispatchThread.java:116)
	at java.awt.EventDispatchThread.pumpEventsForHierarchy(EventDispatchThread.java:105)
	at java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:101)
	at java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:93)
	at java.awt.EventDispatchThread.run(EventDispatchThread.java:82)
Caused by: java.lang.NoSuchMethodError: ConquerSpace.game.universe.GalaticLocation.getDistance()F
	at ConquerSpace.game.universe.spaceObjects.Sector.getSize(Sector.java:111)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.python.core.PyReflectedFunction.__call__(PyReflectedFunction.java:186)
	... 49 more
```
Trust me, that is not nice.

REMEMBER: BUILD AND CLEAN BEFORE RUNNING

Unless you are doing something you know is not related to scripting. That is if you are experienced enough.

#### For Netbeans
Just open the project folder, and `F6` or the green arrow to compile.

#### For Ant
`cd Conquer-Space` when done with cloning.

run `ant` to compile.

run `ant jar` for making the jar.

`ant run` to run.

### Libraries that we use
| Library Name | Description | Website |
| ------------ | ----------- | ------- |
| Log4J2       | General Logging | <https://logging.apache.org/log4j/2.x/> |
| WEB Laf      | Formatting, look and feel, etc. | <http://weblookandfeel.com/> |
| XML Object Model | XML Parsing (We don't need that yet, but we have used it before, so it deserves a place here.) And we might use it in the future | <https://xom.nu/> |
| Jython | Python Script engine. | <http://www.jython.org/> |
| CommonsIO | File operations. We don't use it for now.| <https://commons.apache.org/proper/commons-io/>|
| JFree Chart | Charts and graphs | <http://www.jfree.org/jfreechart/> |

### Helping
If you are excited to help, you need to know at least Java or Python. If you know both, great!

Check out the [projects page](https://github.com/EhWhoAmI/Conquer-Space/projects) to figure what we are doing next.

If you want to report an issue, just go to the [issues page](https://github.com/EhWhoAmI/Conquer-Space/issues) to report that. Just paste the stacktrace and what you did before everything went wrong.