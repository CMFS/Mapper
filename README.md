# Mapper

利用注解简化对象转换。

使用`@ClassMapper`标记可转换目标对象类型，`@FieldMapper`标记目标对象字段名称。

> 对象的被标记字段set/get方法是必须的

```java
@ClassMapper(ModelB.class) // 标记可以转换为ModelB对象
public class ModelA {
  @FieldMapper("f1") // 标记转换为ModelB对象的f1字段
  private String field1;
  public String getField1() {
    return field1;
  }
  public void setField1(String field1) {
    this.field1 = field1;
  }
}

@ClassMapper(ModelA.class) // 标记可以转换为ModelA对象
public class ModelB {
  @FieldMapper("field1") // 标记转换为ModelA对象的field1字段
  private String f1;
  public String getF1() {
    return f1;
  }
  public void setF1(String f1) {
    this.f1 = f1;
  }
}

```
