package mixins;

import java.util.ArrayList;
import java.util.WeakHashMap;

interface Spatial {
    double getX();
    double getY();
    double getZ();
    void setX(double to);
    void setY(double to);
    void setZ(double to);
}

interface Mortal {
    double getHealth();
    double getMaxHealth();
    void setHealth(double to);

    default void heal(double amount) {
        setHealth(Math.min(getMaxHealth(), getHealth() + amount));
    }

    default void takeDamage(double amount) {
        setHealth(Math.max(0.0, getHealth() + amount));
    }
}

interface Locomotion extends Spatial {
    public static final double FRICTION = 0.9;

    static class State {
        public double velX;
        public double velZ;
    }

    static WeakHashMap<Locomotion, State> stateMap = new WeakHashMap<>();

    private State getState(Locomotion instance) {
        var state = stateMap.get(this);
        if (state == null) {
            state = new State();
            stateMap.put(this, state);
        }
        return state;
    }

    default void move(double dx, double dz) {
        var state = getState(this);
        state.velX = dx;
        state.velZ = dz;
    }

    default void tickLocomotion() {
        var state = getState(this);
        setX(getX() + state.velX);
        setZ(getZ() + state.velZ);
        state.velX *= FRICTION;
        state.velZ *= FRICTION;
    }
}

interface VoidDamage extends Spatial, Mortal {
    default void tickVoidDamage() {
        if (getY() < 0.0) {
            takeDamage(1.0);
        }
    }
}

class Player implements Locomotion, VoidDamage {
    private double x;
    private double y;
    private double z;
    private double health;
    private double maxHealth;
    @Override public double getX() { return x; }
    @Override public double getY() { return y; }
    @Override public double getZ() { return z; }
    @Override public void setX(double to) { x = to; }
    @Override public void setY(double to) { y = to; }
    @Override public void setZ(double to) { z = to; }
    @Override public double getHealth() { return health; }
    @Override public double getMaxHealth() { return maxHealth; }
    @Override public void setHealth(double to) { health = to; }
}

public class GameSample {
    public static void main(String[] args) {
        var locomotionComponents = new ArrayList<Locomotion>();
        var voidDamageComponents = new ArrayList<VoidDamage>();

        var player = new Player();
        locomotionComponents.add(player);
        voidDamageComponents.add(player);

        //
        // You get the idea...
        //

        for (var comp : locomotionComponents) {
            comp.tickLocomotion();
        }
        for (var comp : voidDamageComponents) {
            comp.tickVoidDamage();
        }
    }
}