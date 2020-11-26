package ex2.src.api;

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
}
