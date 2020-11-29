package api;

import java.util.Objects;

public class Geo_locationImpl implements geo_location {

    double _x, _y, _z;

    public Geo_locationImpl(double x, double y, double z) {
        _x = x;
        _y = y;
        _z = z;
    }

    public Geo_locationImpl(geo_location location) {
        this(location.x(), location.y(), location.z());
    }

    @Override
    public double x() {
        return _x;
    }

    @Override
    public double y() {
        return _y;
    }

    @Override
    public double z() {
        return _z;
    }

    @Override
    public double distance(geo_location g) {
        double tempDis = Math.pow((_x - g.x()), 2) + Math.pow((_y - g.y()), 2) + Math.pow((_z - g.z()), 2);
        return Math.sqrt(tempDis);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Geo_locationImpl that = (Geo_locationImpl) o;
        return Double.compare(that._x, _x) == 0 &&
                Double.compare(that._y, _y) == 0 &&
                Double.compare(that._z, _z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_x, _y, _z);
    }
}
