package pedroPathing.sandbox;

import com.pedropathing.follower.Follower;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathBuilder;
import com.pedropathing.pathgen.PathChain;

import java.util.ArrayList;
import java.util.function.BiConsumer;

/**
 * PathWeaver is a class that operates on a PathBuilder (given by a follower) to automatically create paths between specified knots
 * @author icaras84 - 7462 (Alumni)
 */
public class PathWeaver {

    public enum HEADING_MODE{
        CONSTANT,
        LINEAR,
        TANGENT
    }

    /**
     * The PathBuilder this class works on
     */
    private PathBuilder builder;
    /**
     * A history of the path chains that have been built
     */
    private ArrayList<PathChain> wovenChains;

    /**
     * This constructor takes in a follower to set the underlying PathBuilder this class operates on
     * @param follower follower
     */
    public PathWeaver(Follower follower){
        this.builder = follower.pathBuilder();
        this.wovenChains = new ArrayList<>();
    }

    /**
     * This methods weaves the specified knots together with cubic bezier curves or lines
     * @param headingMode heading interpolation mode (set for all intermediate paths constructed)
     * @param areLines specifies if the intermediate curves are lines instead of cubic beziers
     * @param knots an array of knots
     * @return this object (to be able to chain method calls)
     * @throws RuntimeException this method expects two or more knots to be declared
     */
    public PathWeaver weave(HEADING_MODE headingMode, boolean areLines, Knot... knots) throws RuntimeException{
        if (knots.length < 2){
            throw new RuntimeException("PathWeaver weave() method expects at least two knots.");
        }

        int maxIter = knots.length - 1;
        Path[] paths = new Path[maxIter];

        for (int i = 0; i < maxIter; i++) {
            Knot current = knots[i];
            Knot next = knots[i + 1];
            BezierCurve curve;
            if (areLines){
                curve = new BezierLine(current.getPosPoint(), next.getPosPoint());
            } else {
                curve = new BezierCurve(current.getPosPoint(), current.getTangentPoint(), next.getReflectedTangentPoint(), next.getPosPoint());
            }
            paths[i] = new Path(curve);
            switch (headingMode){
                case CONSTANT:
                    paths[i].setConstantHeadingInterpolation(current.getPose().getHeading());
                    break;
                case LINEAR:
                    paths[i].setLinearHeadingInterpolation(current.getPose().getHeading(), next.getPose().getHeading());
                    break;
                case TANGENT:
                    paths[i].setTangentHeadingInterpolation();
                    break;
            }
        }

        this.wovenChains.add(new PathChain(paths));
        return this;
    }

    /**
     * Weaves a line between two points
     * @param headingMode heading interpolation mode
     * @param startKnot start knot
     * @param endKnot end knot
     * @return this object (to be able to chain method calls)
     */
    public PathWeaver weaveLine(HEADING_MODE headingMode, Knot startKnot, Knot endKnot){
        this.weave(headingMode, true, startKnot, endKnot);
        return this;
    }

    /**
     * Weaves a cubic bezier curve between two points
     * @param headingMode heading interpolation mode
     * @param startKnot start knot
     * @param endKnot end knot
     * @return this object (to be able to chain method calls)
     */
    public PathWeaver weaveCubic(HEADING_MODE headingMode, Knot startKnot, Knot endKnot){
        this.weave(headingMode, false, startKnot, endKnot);
        return this;
    }

    /**
     * This methods accepts a bi-consumer and repeats it for a specified amount of time
     * @param maxIter max amount of iterations
     * @param biConsumer a bi-consumer that accepts the iteration number (starting from zero) and a PathWeaver reference (to keep the weaver within the consumer's scope)
     * @return this object (to be able to chain method calls)
     */
    public PathWeaver repeat(int maxIter, BiConsumer<Integer, PathWeaver> biConsumer){
        for (int i = 0; i < maxIter; i++) {
            biConsumer.accept(i, this);
        }
        return this;
    }

    /**
     * Gets the underlying PathBuilder that PathWeaver operates on
     * @return PathBuilder
     */
    public PathBuilder getBuilder() {
        return builder;
    }

    /**
     * Sets the underlying PathBuilder that PathWeaver will operate on
     * @param builder PathBuilder
     * @return this object (to be able to chain method calls)
     */
    public PathWeaver setBuilder(PathBuilder builder) {
        this.builder = builder;
        return this;
    }

    /**
     * Gets the PathChains that this class has constructed
     * @return this object (to be able to chain method calls)
     */
    public ArrayList<PathChain> getWovenChains() {
        return wovenChains;
    }

    /**
     * Sets the PathChains that this class has constructed
     * @param wovenChains ArrayList of PathChains
     * @return this object (to be able to chain method calls)
     */
    public PathWeaver setWovenChains(ArrayList<PathChain> wovenChains) {
        this.wovenChains = wovenChains;
        return this;
    }
}
