package ex2.src.api;

public class geo_locationImpl implements geo_location {
    double _x,_y,_z;
    public geo_locationImpl( double x,double y,double z){
        _x=x;
        _y=y;
        _z=z;
    }

    @Override
    public double x() { return _x; }

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
        double tempDis =  Math.pow((_x-g.x()), 2) + Math.pow((_y-g.y()), 2) +Math.pow((_z-g.z()), 2);
        return Math.sqrt(tempDis);
    }
}
