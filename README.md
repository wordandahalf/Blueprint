<p align="center"><img src="docs/blueprint_logo.png" alt="Blueprint Logo" height="100"></p>

---

**Blueprint** is a simple, annotation-driven library for modifying Java code at runtime

# Usage
Currently, Blueprint is not hosted on any Maven repositories, so you must download the source and manually compile it. This README will be updated to reflect whether or not that changes.

To include it as a dependency, either directly add the JAR or add the following dependecy to your POM.
```xml
<dependency>
    <groupId>io.github.wordandahalf</groupId>
    <artifactId>Blueprint</artifactId>
    <version>0.1.0</version>
</dependency>
```

Here is a simple example of how to utilize Blueprint:
```java
package blueprintTest;

public class Foo {
    private String text;
    
    public Foo() { this.text = "Hello, foo!"; }
    
    public void getFoo() { System.out.println(this.text); }
}
```

```java
package blueprintTest;

@Blueprint(target = "blueprintTest.Foo")
public class BlueprintTest {
    public static void main(String[] args) throws Exception {
        Blueprints.add(BlueprintTest.class);
        Blueprints.apply();
        
        Foo foo = new Foo();
        foo.getFoo();
    }
    
    @Inject(target = "getFoo", at = @At(location = At.Location.TAIL))
    public void getFoo() {
        System.out.println("Hello, world!");
    }
}
```

When ran, this program will have the output:
```
Hello, foo!
Hello, world!
```

For more extensive documentation and examples, please see the [Wiki](https://github.com/wordandahalf/Blueprint/wiki)
