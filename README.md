<!-- Plugin description -->

# POJO to Json

[![][idea-img]][plugin]
[![][license-img]][github]
[![][release-img]][plugin]
[![][download-img]][plugin]

[idea-img]: https://img.shields.io/badge/IntelliJ%20IDEA%20Plugins-000000?logo=IntelliJ-idea&logoColor=white
[license-img]: https://img.shields.io/github/license/organics2016/pojo2json
[release-img]: https://img.shields.io/jetbrains/plugin/v/12066
[download-img]: https://img.shields.io/jetbrains/plugin/d/12066

[github]: https://github.com/organics2016/pojo2json
[plugin]: https://plugins.jetbrains.com/plugin/12066-pojo-to-json


A simple plugin for converting POJO to JSON in IntelliJ IDEA

- Support BigDecimal and other Numeric objects.
- Support Java8 time type.
- Support Enum.
- Partial support Jackson and Fastjson annotations.

## Support JVM platform languages

- Java - full support
- Kotlin - beta, but full support
- Scala - beta, but full support

## Screenshot

![Image text](https://raw.githubusercontent.com/organics2016/pojo2json/master/screenshot/pojo2json.gif)

## Installation

- **Install in IDEA:**
    - <kbd>Preferences(Settings)</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search<b>"POJO to Json"</b></kbd> > <kbd>Install</kbd>

- **Manual Install:**
    - [plugin] -> <kbd>Preferences(Settings)</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd> -> Select the plug-in package and install（No need to unzip）

<iframe frameborder="none" width="384px" height="319px" src="https://plugins.jetbrains.com/embeddable/card/12066"> </iframe>

## Usage

- <kbd>Open the target class file</kbd> > <kbd>Right click</kbd> > <kbd>POJO To Json</kbd> > <kbd>Json result will copy to clipboard</kbd>

## Q&A

- Why always report errors when use it?
```
This class reference level exceeds maximum limit or has nested references!
```
When the program throws this warning there are only two possibilities.

1. This class reference level > 500 

eg:
```
{
 {
  {
    .......501
  }
 }
}
```
2. This class or parent class has nested references

eg:
```
{
 "a":{
  "b":{
   "a":{
     "b":{
        .........
      }
    }
   }
 }
}
```
or
```
{
 "a":{
  "a":{
   "a":{
     "a":{
        .........
      }
    }
   }
 }
}
```
Perhaps both will happen for entity but this entity are not suitable for JSON.<br>
So you can try to serialize your POJO using Jackson to see what happens.<br>
What is a POJO? I think least it's not a single model.

- But how to solve this problem?

You can try the following methods.

## Support Annotations and Javadoc

### @JsonProperty and @JSONField

```java
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    @JsonProperty("name")
    private String username;
    
    @JSONField(name = "pass")
    private String password;
}
```
paste result:
```json
{
  "name": "",
  "pass": ""
}
```

### @JsonIgnore

```java
import com.fasterxml.jackson.annotation.JsonIgnore;

public class User {
    
    @JsonIgnore
    private String username;
    private String password;
}
```
paste result:
```json
{
  "password": ""
}
```

### @JsonIgnoreProperties or Javadoc tags JsonIgnoreProperties

```java
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

public class User {

    private String username;
    @JsonIgnoreProperties({"users", "aaa", "bbb"})
    private List<Role> roles;

    public class Role {

        private String roleName;
        private List<User> users;
    }
}
```
paste result:
```json
{
  "username": "",
  "roles": [
    {
      "roleName": ""
    }
  ]
}
```
or when there is no jackson library

```java
import java.util.List;

public class User {

    private String username;
    /**
     * @JsonIgnoreProperties users, aaa, bbb
     */
    private List<Role> roles;

    public class Role {

        private String roleName;
        private List<User> users;
    }
}
```
paste result:
```json
{
  "username": "",
  "roles": [
    {
      "roleName": ""
    }
  ]
}
```

You may encounter this problem during use.
```
This class reference level exceeds maximum limit or has nested references!
```
The above method can solve the nested reference problem well.

### @JsonIgnoreType

```java
import com.fasterxml.jackson.annotation.JsonIgnoreType;

import java.util.List;

public class User {

    private String username;
    private List<Role> roles;

    @JsonIgnoreType
    public class Role {
        private String roleName;
        private List<User> users;
    }
}
```
paste result:
```json
{
  "username": "",
  "roles": []
}
```
<!-- Plugin description end -->

## Contributors

Ideas and partial realization from
[![](https://avatars.githubusercontent.com/u/12984934?s=28)linsage](https://github.com/linsage)