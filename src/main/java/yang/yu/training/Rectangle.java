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
