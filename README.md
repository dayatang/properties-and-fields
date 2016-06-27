# 正确区分属性和字段

在进行Java软件开发的时候，很多人都没有搞清Java对象中属性（Property）和字段（Field）的区别，以为属性就是字段。本文试图对这两个概念作一个澄清。

## 1 范例

首先上一个例子：平面直角坐标系中的矩形。

一个矩形的形状可以用宽（width）和高（height）来表示，而它的位置可以用左下角坐标lowerLeftCoordinate（例如(10, 5)）来表示。平面直角坐标系中任何一个点（Point）可以用这个点的坐标(x, y)来表示。

点Point的定义是这样的：

```java
package yang.yu.training;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class Point implements Serializable {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Point)) {
            return false;
        }
        Point that = (Point) other;
        return this.x == that.x &&
                this.y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Point(" + x + ", " + y + ")";
    }

    public static Point of(int x, int y) {
        return new Point(x, y);
    }
}

```

而矩形Rectangle的定义是这样的：

```java
package yang.yu.training;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Rectangle implements Serializable {

    @Id
    private String id = UUID.randomUUID().toString();

    private Point lowerLeftCoordinate;

    private int _width;

    private int _height;

    public Rectangle(int width, int height) {
        this._width = width;
        this._height = height;
        lowerLeftCoordinate = new Point(0, 0);
    }

    public String getId() {
        return id;
    }

    public int getWidth() {
        return _width;
    }

    public int getHeight() {
        return _height;
    }

    public int getArea() {
        return _width * _height;
    }

    public boolean isSquare() {
        return _width == _height;
    }

    public Point getLowerLeftCoordinate() {
        return lowerLeftCoordinate;
    }

    public Point getLoweRightCoordinate() {
        return new Point(lowerLeftCoordinate.getX() + _width, lowerLeftCoordinate.getY());
    }

    public Point getUpperLeftCoordinate() {
        return new Point(lowerLeftCoordinate.getX(), lowerLeftCoordinate.getY() + _height);
    }

    public Point getUpperRightCoordinate() {
        return new Point(lowerLeftCoordinate.getX() + _width, lowerLeftCoordinate.getY() + _height);
    }

    public void moveHorizontally(int delta) {
        lowerLeftCoordinate = new Point(lowerLeftCoordinate.getX() + delta, lowerLeftCoordinate.getY());
    }

    public void moveVertically(int delta) {
        lowerLeftCoordinate = new Point(lowerLeftCoordinate.getX(), lowerLeftCoordinate.getY() + delta);
    }

    public void moveTo(Point point) {
        lowerLeftCoordinate = new Point(lowerLeftCoordinate.getX() + point.getX(), lowerLeftCoordinate.getY() + point.getY());
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Rectangle)) {
            return false;
        }
        Rectangle that = (Rectangle) other;
        return this._width == that._width &&
                this._height == that._height;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_width, _height);
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "_width=" + _width +
                ", _height=" + _height +
                '}';
    }
}
```

为了方便，矩形宽度、高度和点坐标都用整数int而不是浮点数double来表示。

## 2 属性和字段的定义
在很多情况下，在一个类中，字段和属性往往一一对应，例如Point类中，我们有两个字段x和y，也有两个属性x和y与之一一对应：

```java

    private int x;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
```

```java

    private int y;

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
```

下面一行：

```java
    private int x;
```

在Point类中定义了一个字段（Field），名为x。

根据JavaBean规范，下面的代码：

```java
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
```

定义了一个可读（因为有getX()方法）、可写（因为有setX()方法）的属性（Property）x。属性的名字来自getX()或setX()。根据JavaBean规范，属性的名字是方法名getX/setX去掉get/set前缀之后，剩下的部分首字母改为小写来定义。因此，getX去掉get之后剩下X，再将它的首字母（在这里是唯一的字母）改为小写就是x。这就是属性名。同样地，在Rectangle类中，getWidth方法的存在意味着Rectangle类中存在一个名为width的属性。

## 3 属性和字段之间没有必然的一一对应关系
请牢记：

	在一个类之中，属性和字段没有必然的一一对应关系。
	
具体表现在：

### 3.1 属性和字段的名称可以是不同的

例如在Rectangle类中，字段**_width**对应的属性名是**width**：

```java

    private int _width;			//定义字段_width

    public int getWidth() {		//定义属性width
        return _width;
    }
```

### 3.2 有的字段没有对应的属性，有的属性没有对应的字段
在Rectangle类中，我们有一个area属性（由getArea()方法定义）：

```java
    public int getArea() {
        return _width * _height;
    }
```
却没有一个相应的area字段存在。这个属性是通过计算（width * height）得来的。

在Rectangle类中，只有左下角的坐标点，字段和属性一一对应：

```java
    private Point lowerLeftCoordinate;

    public Point getLowerLeftCoordinate() {
        return lowerLeftCoordinate;
    }
```

而右下角、左上角、右上角的坐标点，只有相应的属性（loweRightCoordinate、upperLeftCoordinate和upperRightCoordinate），没有相应的字段。它们都是计算属性：

```java
    public Point getLoweRightCoordinate() {
        return new Point(lowerLeftCoordinate.getX() + _width, lowerLeftCoordinate.getY());
    }

    public Point getUpperLeftCoordinate() {
        return new Point(lowerLeftCoordinate.getX(), lowerLeftCoordinate.getY() + _height);
    }

    public Point getUpperRightCoordinate() {
        return new Point(lowerLeftCoordinate.getX() + _width, lowerLeftCoordinate.getY() + _height);
    }
```

### 3.3 有的属性是多个字段综合表现的结果
例如判断矩形是不是正方形的属性（通过isSquare()方法定义），是比较_width和_height两个字段的结果：

```java
    public boolean isSquare() {
        return _width == _height;
    }
```

面积属性也是这样：

```java
    public int getArea() {
        return _width * _height;
    }
```

### 3.4 属性可以是只读、只写或读写的，而字段都是可读写的

如果一个类中同时存在getAbc()（或isAbc()，如果属性类型是boolean类型）和setAbc()方法，那么属性abc就是可读可写的。如果没有相应的setAbc()方法，该属性就是只读的。如果没有相应的getAbc()（或isAbc()，如果属性类型是boolean类型）方法，该属性就是只写的。

下面的Rectangle类代码中，width和height属性都是只读的，它们通过Rectangle类的构造函数设置，然后就永远不可以改变了，因为类中没有定义setWidth()和setHeight()方法：

```java
    private int _width;

    private int _height;

    public Rectangle(int width, int height) {
        this._width = width;
        this._height = height;
        lowerLeftCoordinate = new Point(0, 0);
    }

    public int getWidth() {
        return _width;
    }

    public int getHeight() {
        return _height;
    }
```

## 4 属性是类外部接口的一部分，字段是类内部实现的一部分

其实，上面那些区别都是浮云，真正重要的是要记住：

	属性是类外部接口的一部分；
	字段是类内部实现的一部分。
	
因此：

	属性是你的代码和你代码的客户的协议的一部分，必须精心设计，不可随意改动；
	而字段属于你的自由王国，你可以随意添加、删除和修改，只要不影响类的外部接口。
	
无论是在设计一个系统还是一个类，区分它们的外部接口和内部实现都是非常有意义的。在系统的层级,外部接口主要包含接口和抽象基类，而内部实现包含上述接口的实现类和和基类的具体子类。在类的层级，外部接口包含属性和公有(public)方法签名，而内部实现包括字段、私有方法和公有方法的方法体。接口和实现的区分比起字段和方法的区分具有更重大的意义。

	外部接口的“形诸外”是内部实现的“动于内”的结果。
	
字段代表内部实现的静态部分，属性代表外部接口的静态部分。方法属于动态部分。
	
## 5 重要推论
在软件开发领域，有一条基本原则：

	面向接口编程，不要面向实现编程。
	
因此我们可以得到这样的推论：

### 5.1 先定义属性而不是字段

	先定义属性（因为它是类接口的一部分），然后根据需要定义字段，而不是相反。

例如我在设计Rectangle类的时候，是根据外界对矩形的期待设计出它的长度、宽度、四个角的坐标、面积、是否正方形等属性，为了支撑这几个属性，我定义长度、宽度和左下角坐标三个字段；而不是先定义那几个字段，再通过IDE自动创建对应的属性（getter和setter）。

### 5.2 不要为每个字段默认生成属性

	千万不要、不要、不要不假思索地为每个字段添加getter和setter方法生成属性。

我发现大多数程序员都是不假思索地先定义字段，然后利用IDE为所有的字段自动生成对应的读写属性。这是一种非常有害的习惯，会生成一个臃肿的、充满无用代码的类。直接将所有的字段设置为public的，或者封装为public属性，都违反了封装性原则，将大量的内部实现细节暴露给外界，加重了你的代码用户的认知负担，同时限制了你未来重构代码的自由度。

在很多时候，设计一个不可变类（或部分属性不可变）是非常有益的。也就是说，只有getter，没有setter。字段初始值通过构造函数参数传入。
	
### 5.3 尽量通过方法而不是setter来修改字段值

	尽量通过真正的方法而不是通过setter修改字段值。

本质上，getter和setter不是真正的方法，而是属性访问器。Java不像C#那样拥有专门的属性访问器语法，只得用getter和setter方法来代替。在面向对象的设计中，方法和字段是相互作用的：字段可以影响方法调用的结果，而方法可以修改字段的取值。

在Rectangle类中，我是通过三个move方法来修改lowerLeftCoordinate字段的值的：

```java

    public void moveHorizontally(int delta) {
        lowerLeftCoordinate = new Point(lowerLeftCoordinate.getX() + delta, lowerLeftCoordinate.getY());
    }

    public void moveVertically(int delta) {
        lowerLeftCoordinate = new Point(lowerLeftCoordinate.getX(), lowerLeftCoordinate.getY() + delta);
    }

    public void moveTo(Point point) {
        lowerLeftCoordinate = new Point(lowerLeftCoordinate.getX() + point.getX(), lowerLeftCoordinate.getY() + point.getY());
    }
```


而不是通过属性修改器来重新设置左下角坐标:

```java
	public void setLowerLeftCoordinate(Point newCoordinate) {
		this.lowerLeftCoordinate = lowerLeftCoordinate;
	}
```

这样做的原因是：

1. 隐藏内部实现。不必将“通过修改左下角坐标来实现矩形的移动”这样的内部实现细节呈现给代码的用户。
2. 面向意图编程。因为用户的意图就是将矩形移动到一个新的位置，而不是重新设置它的左下角坐标。


## 6 JPA/Hibernate中区分属性和字段持久化

在JPA和Hibernate这样的ORM软件中，我们可以根据属性/字段对实体进行查询。这时候必须区分是按属性还是按字段进行持久化。

JPA/Hibernate中，如果@Id注解如果定义在属性上，就是根据属性进行持久化的；如果是定义在字段上，就是根据字段来进行持久化的。

有什么不同？

我们先看字段持久化（@Id定义在id字段上）：

```java
@Entity
public class Rectangle {

    @Id
    private String id;

    private int _width;


    public String getId() {
        return id;
    }

    public int getWidth() {
        return _width;
    }
```

在进行jpql或hql查询的时候，应该根据字段名**_width**而不是属性名**width**:

```sql
	SELECT o FROM Rectangle o WHERE o._width > 100
```
	
或者
	
```sql
	SELECT o._width, o._height WHERE o._width > 100
```

下面是属性持久化（@Id定义在getId()方法上）：

```java
@Entity
public class Rectangle {

    private String id;

    private int _width;


    @Id
    public String getId() {
        return id;
    }

    public int getWidth() {
        return _width;
    }
```

在进行jpql或hql查询的时候，应该根据属性名**width**而不是字段名**_width**:

```sql
	SELECT o FROM Rectangle o WHERE o.width > 100
```

或者

```sql
	SELECT o.width, o.height WHERE o.width > 100
```

@Id字段定义在字段还是属性上决定了整个类的所有属性/字段的持久化策略。

范例代码可以从[https://git.oschina.net/yyang/properties-and-fields](https://git.oschina.net/yyang/properties-and-fields)下载。

