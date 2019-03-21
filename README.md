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
	...(Among the lines of about a million lines)...
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
| XML Object Model | XML Parsing | <https://xom.nu/> |
| Jython | Python Script engine. | <http://www.jython.org/> |
| CommonsIO | File operations. We don't use it for now.| <https://commons.apache.org/proper/commons-io/>|
| JFree Chart | Charts and graphs | <http://www.jfree.org/jfreechart/> |

### Helping
If you are excited to help, you need to know at least Java or Python. If you know both, great!

Check out the [projects page](https://github.com/EhWhoAmI/Conquer-Space/projects) to figure what we are doing next.

If you want to report an issue, just go to the [issues page](https://github.com/EhWhoAmI/Conquer-Space/issues) to report that. Just paste the stacktrace and what you did before everything went wrong.
