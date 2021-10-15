<!-- Plugin description -->

# POJO to JSON

A simple plugin for converting POJO to JSON in IntelliJ IDEA

## Support

- Support BigDecimal and other Numeric objects.
- Support Java8 time type.
- Support Enum.
- Partial support Jackson and Fastjson annotations.

## Screenshot

![Image text](https://raw.githubusercontent.com/organics2016/pojo2json/master/screenshot/pojo2json.gif)

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


## Plugin

https://plugins.jetbrains.com/plugin/12066-pojo-to-json

<!-- Plugin description end -->

## Contributors

Ideas and partial realization from
[![linsage](https://avatars.githubusercontent.com/u/12984934?s=28)linsage](https://github.com/linsage)