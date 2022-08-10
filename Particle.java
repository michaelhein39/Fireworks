/* This file creates a single particle to be part of a firework burst. There are
   functions for updating color, updating position, drawing the particle, and
   getting some of its attributes. */

import java.awt.Color;

public class Particle {
    /* Instance variables adapted from Ball.java in precept 12 to represent the
    projectile motion of a single particle along with the color of the particle
    and its radius */
    private double px, py; // positions in x- and y-coordinates
    private double vx, vy; // velocities in x- and y-directions
    private final double ax, ay; // acceleration in x- and y-directions
    private final double size; // radius of particle
    private float r; // red value of color
    private float g; // green value of color
    private float b; // blue value of color
    private float a; // opacity of color
    private Color color; // color of particle

    // Initializes one particle with arguments for every instance variable.
    public Particle(double px, double py, double vx, double vy, double ax,
                    double ay, double size, float r, float g, float b) {
        this.px = px;
        this.py = py;
        this.vx = vx;
        this.vy = vy;
        this.ax = ax;
        this.ay = ay;
        this.size = size;

        // add slight randomness to each r g b value so that particles are all different
        // Standard Deviation of 0.05 to add  variation based on gaussian distribution
        this.a = 1; // initialize alpha 1 (full opacity)
        float rand1 = (float) StdRandom.gaussian(0, 0.05);
        if (r + rand1 <= 1.0 && r + rand1 >= 0.0) this.r = r + rand1;
        else this.r = r;
        float rand2 = (float) StdRandom.gaussian(0, 0.05);
        if (g + rand2 <= 1.0 && g + rand2 >= 0.0) this.g = g + rand2;
        else this.g = g;
        float rand3 = (float) StdRandom.gaussian(0, 0.05);
        if (b + rand3 <= 1.0 && b + rand3 >= 0.0) this.b = b + rand3;
        else this.b = b;

        // creates color with rgb values between 0 and 1, and alpha value for opacity
        this.color = new Color(this.r, this.g, this.b, this.a);
    }

    // Updates x- and y-positions according to the velocity and acceleration.
    // Inspired by Ball.java, with additional acceleration component.
    public void updatePosition() {
        vx = vx + ax; // updates x-velocity using x-acceleration
        px = px + vx; // updates x-position using new x-velocity

        vy = vy + ay; // updates y-velocity using y-acceleration
        py = py + vy; // updates y-position using new y-velocity
    }

    /* Updates color to make it more transparent using the alpha feature of
    the Color constructor from the Color API
     */
    public void updateColor(double decrementer) {
        // check that the decrementer is valid to approach transparency in color
        if (decrementer >= 1 || decrementer == 0)
            throw new IllegalArgumentException(
                    "Decrementer must be less than 1 but greater than 0");
        // reduces opacity by multiplying alpha by a value 0 < x < 1
        a *= decrementer;

        // add randomness to each r b g value to change color slightly as the
        // firework moves
        // Standard dev: 0.05 for slight variation
        float rand1 = (float) StdRandom.gaussian(0, 0.05);
        if (r + rand1 <= 1.0 && r + rand1 >= 0.0) r += rand1;
        float rand2 = (float) StdRandom.gaussian(0, 0.05);
        if (g + rand2 <= 1.0 && g + rand2 >= 0.0) g += rand2;
        float rand3 = (float) StdRandom.gaussian(0, 0.05);
        if (b + rand3 <= 1.0 && b + rand3 >= 0.0) b += rand3;
        color = new Color(r, g, b, a); // reinitialize color
    }

    // Draws filled circle using instance variables for size, position, and color
    public void draw() {
        StdDraw.setPenColor(color);
        StdDraw.filledCircle(px, py, size);
    }

    // Returns color opacity of particle
    public float getColorAlpha() {
        return a;
    }

    // Returns y-position of particle
    public double getPx() {
        return px;
    }

    // Returns y-position of particle
    public double getPy() {
        return py;
    }

    // Returns x-velocity of particle
    public double getVx() {
        return vx;
    }

    // Returns y-velocity of particle
    public double getVy() {
        return vy;
    }

    // updates x velocity to new value from argument
    public void setVx(double xvel) {
        vx = xvel;
    }

    // updates y velocity to new value from argument
    public void setVy(double yvel) {
        vy = yvel;
    }

    public static void main(String[] args) {
        // set up StdDraw
        StdDraw.setXscale(-1, 1);
        StdDraw.setYscale(-1, 1);
        StdDraw.enableDoubleBuffering();

        // create a particle at (0,0) that moves and fades
        Particle practice = new Particle(0, 0, 0.01, 0.01, -0.000005,
                                         -0.00091, 0.1, 0.0F, 0.5F, 0.5F);
        // simulate 10 time steps
        for (int i = 0; i < 100; i++) {
            practice.draw();
            StdDraw.show();
            StdDraw.pause(10);
            practice.updatePosition();
            practice.updateColor(0.95);
            StdDraw.clear();
        }

        // test other functions
        Particle tester = new Particle(0, 0, 0.1, 0.1, 0.01,
                                       0.01, 0.1, 0.0F, 0.5F, 0.5F);
        // print initial positions
        StdOut.println("Test Using Update Positions:");
        StdOut.println("Test Step 0:");
        StdOut.println("X Position: " + tester.getPx());
        StdOut.println("Y Position: " + tester.getPy());
        // should print 0 and 0

        // print initial velocities
        StdOut.println("X Velocity: " + tester.getVx());
        StdOut.println("Y Velocity: " + tester.getVy());
        // should print 0.1 and 0.1

        // update initial positions and velocities
        tester.updatePosition();  // step 1
        StdOut.println("Test Step 1:");
        StdOut.println("X Position: " + tester.getPx());
        StdOut.println("Y Position: " + tester.getPy());
        // should print 0.11 and 0.11
        StdOut.println("X Velocity: " + tester.getVx());
        StdOut.println("Y Velocity: " + tester.getVy());
        // should print 0.11 and 0.11

        tester.updatePosition();  // step 2
        StdOut.println("Test Step 2:");
        StdOut.println("X Position: " + tester.getPx());
        StdOut.println("Y Position: " + tester.getPy());
        // should print 0.23 and 0.23
        StdOut.println("X Velocity: " + tester.getVx());
        StdOut.println("Y Velocity: " + tester.getVy());
        // should print 0.12 and 0.12


        // change positions, velocities, and color:

        StdOut.println("Test of Set and Update Color");
        Particle tester2 = new Particle(0, 0, 0.1, 0.1, 0.01,
                                        0.01, 0.1, 0.0F, 0.5F, 0.5F);
        tester2.setVx(0.5);
        tester2.setVy(0.5);
        StdOut.println("New X Velocity: " + tester2.getVx());
        StdOut.println("New Y Velocity: " + tester2.getVy());
        // should print 0.5, 0.5
        tester2.updateColor(0.8);
        StdOut.println("New Color Opacity: " + tester2.getColorAlpha());
        // should print previous alpha times 0.8 --> 0.8

        tester2.setVx(0.7);
        tester2.setVy(0.7);
        StdOut.println("New X Velocity: " + tester2.getVx());
        StdOut.println("New Y Velocity: " + tester2.getVy());
        // should print 0.7, 0.7
        tester2.updateColor(0.5);
        StdOut.println("New Color Opacity: " + tester2.getColorAlpha());
        // should print previous alpha times 0.8 --> -0.4
    }
}