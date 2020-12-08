
public class Vector {

    private float magnitude;
    private float theta;

    public Vector(float magnitude, float theta) {
        this.magnitude = magnitude;
        this.theta = theta;
    }

    public float getMagnitude() {
        return magnitude;
    }

    public float getAngle() {
        return theta;
    }

    public float getY() {
        return (float) (this.magnitude * Math.sin(theta));

    }

    public float getX() {
        return (float) (this.magnitude * Math.cos(theta));
    }

    public Vector deepCopy() {
        return new Vector(magnitude, theta);
    }

    public void add(Vector v, float maxMagnitude) {

        float x = this.getX() + v.getX();
        float y = this.getY() + v.getY();

        float newMagnitude = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        float newAngle = (float) Math.atan2(y, x);
        
        newMagnitude = Math.min(newMagnitude, maxMagnitude);

        magnitude = newMagnitude;
        theta = newAngle;

    }

    public void add(Vector v) {
        add(v, Float.MAX_VALUE);
    }

}
