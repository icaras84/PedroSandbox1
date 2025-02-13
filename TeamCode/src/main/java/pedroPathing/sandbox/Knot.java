package pedroPathing.sandbox;

import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.Point;
import com.pedropathing.pathgen.Vector;

/**
 * While this is not a mathematical knot, this class stores the position, heading at the knot position, and the tangent vector
 * @author icaras84 - 7462 (Alumni)
 */
public class Knot {

    /**
     * Pose to store the position of the knot and the heading
     */
    private final Pose pose;

    /**
     * Tangent vector to store the tangents relative to the knot pose
     */
    private final Vector tangent;

    /**
     * This constructor takes an arbitrary pose and tangent vector to construct a knot.
     * @param pose pose
     * @param tangent tangent vector
     */
    public Knot(Pose pose, Vector tangent){
        this.pose = pose;
        this.tangent = tangent;
    }

    /**
     * This constructor creates a Knot with the tangent specifically aligned with the heading vector with a specified length
     * @param pose pose
     * @param tangentLength tangent vector length
     */
    public Knot(Pose pose, double tangentLength){
        this(pose, new Vector(tangentLength, pose.getHeading()));
    }

    /**
     * This method returns the tangent's point in the coordinate space.
     * @return tangent vector + pose position
     */
    public Point getTangentPoint(){
        return new Point(this.pose.getX() + this.tangent.getXComponent(), this.pose.getY() + this.tangent.getYComponent(), Point.CARTESIAN);
    }

    public Point getReflectedTangentPoint(){
        return new Point(this.pose.getX() - this.tangent.getXComponent(), this.pose.getY() - this.tangent.getYComponent(), Point.CARTESIAN);
    }

    public void reflectTangent(){
        this.tangent.rotateVector(Math.PI);
    }

    /**
     * This method returns a tangent vector which indicates the direction and magnitude of the tangent relative to the knot pose
     * @return tangent vector
     */
    public Vector getTangentVector(){
        return this.tangent;
    }

    /**
     * This method returns the knot's pose (a position in space and heading)
     * @return pose
     */
    public Pose getPose() {
        return this.pose;
    }

    /**
     * This returns the position of the knot as a point
     * @return position
     */
    public Point getPosPoint(){
        return new Point(this.pose.getX(), this.pose.getY(), Point.CARTESIAN);
    }
}
