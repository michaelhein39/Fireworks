/* This class creates a firework ADT made up of many particles. It contains
   functions that draw particles, launch particles together, and make them explode
   together. There are also some other functions, such as updating the position of
   particles. */

public class Firework {
    private Particle[] firework; // array of particles to form one burst
    private int quantity; // number of particles in the firework

    // Creates firework using arguments for number of particles, initial position,
    // velocity, acceleration, size, and color
    public Firework(int number, double px, double py, double vx, double vy,
                    double ax, double ay, double size, float r, float g,
                    float b) {
        // initialize an array of particle objects
        quantity = number;
        firework = new Particle[number];

        // initialize identical particle objects for each array element
        for (int i = 0; i < quantity; i++)
            firework[i] = new Particle(px, py, vx, vy, ax, ay, size, r, g, b);
    }

    // Change velocities of particles in firework to burst outwards in bloom shape
    // after hitting burst point
    public void prepareBurst() {
        double radians = 0; // initial value
        double totalRadians = 2 * Math.PI; // represents a full circle (2 pi)

        // set up the incrementation to move evenly around the circle
        double radianInc = totalRadians / quantity;
        double scaleDown = 0.3; // small number used to scale velocity down lower than 1

        // loop through each particle to give them different velocities
        // sets up the circular burst motion
        for (int i = 0; i < quantity; i++) {
            double rand = StdRandom.uniform(-0.02, 0.02); // create small random variance
            double xVel = Math.cos(radians) * scaleDown + rand; // update x velocity
            double yVel = Math.sin(radians) * scaleDown + rand; // update y velocity
            firework[i].setVx(xVel); // replace particle's velocity
            firework[i].setVy(yVel); // replace particle's velocity
            radians += radianInc; // move to next position in circle
        }
    }

    // Update position of all firework particles
    public void moveParticles() {
        for (int i = 0; i < quantity; i++) {
            firework[i].updatePosition();
        }
    }

    // Draws all individual particles of firework
    public void draw() {
        for (int i = 0; i < quantity; i++) {
            firework[i].draw();
        }
    }

    // Returns a string showing the positions of every particle in the firework
    public String positionsString() {
        StringBuilder string = new StringBuilder("Particle Positions: \n");
        for (int i = 0; i < quantity; i++) {
            string.append("x: " + firework[i].getPx() + "\n");
            string.append("y: " + firework[i].getPy() + "\n");
        }
        return string.toString();
    }

    // Returns a string showing the velocities of every particle in the firework
    public String velocitiesString() {
        StringBuilder string = new StringBuilder("Particle Velocities: \n");
        for (int i = 0; i < quantity; i++) {
            string.append("x: " + firework[i].getVx() + "\n");
            string.append("y: " + firework[i].getVy() + "\n");
        }
        return string.toString();
    }

    // Draws firework launching upwards with initial velocities before burst
    public void launch(String image) {
        // loops as long as firework has not reached burst point
        for (int i = 0; i < Double.POSITIVE_INFINITY; i++) {
            // create small random variation in the burst point
            double burstPoint = StdRandom.gaussian(0, 0.15);
            // break from launch when y velocity is below burst point
            if (firework[0].getVy() <= burstPoint)
                break;
            draw();
            StdDraw.show();
            moveParticles(); // update positions
            StdDraw.pause(30); // pause briefly between time steps
            StdDraw.picture(0, 0, image); // clear to background image
        }
    }

    // Draw exploding firework in bloom shape with new velocities
    public void burst(String image) {
        prepareBurst(); // update all velocities to burst
        StdAudio.play("explosion.wav"); // bursting sound
        int counter = 0; // counts number of time steps
        // loops until firework is nearly transparent
        while (firework[0].getColorAlpha() > 0.01) {
            // display firework
            draw();
            StdDraw.show();
            moveParticles();
            StdDraw.pause(30); // pause briefly between steps
            if (counter % 5 == 0) // clear every five time steps to create tails
                StdDraw.picture(0, 0, image); // clear to image
            for (int j = 0; j < quantity; j++) {
                firework[j].updateColor(0.9); // update colors and fade
            }
            counter++; // move on to next time step and keep track
        }
        StdDraw.picture(0, 0, image); // clear to image
    }

    // Return the number of particles in the firework
    public int getQuantity() {
        return quantity;
    }

    // Return a specific particle from the firework given an integer index
    public Particle getParticle(int index) {
        return firework[index];
    }

    // main method to test
    public static void main(String[] args) {
        // set up StdDraw
        StdDraw.setCanvasSize(750, 750); // 750 x 750 canvas size
        StdDraw.setXscale(-10, 10);
        StdDraw.setYscale(-10, 10);
        StdDraw.enableDoubleBuffering();
        StdAudio.play("rainbowSong.wav"); // test using rainbow song
        StdDraw.picture(0, 0, "skyline.jpeg"); // test background image

        // create firework object
        Firework test = new Firework(10, -10, -10, 0.2, 0.57, 0, -0.0098,
                                     0.2, 0.501F, 0.847F, 0.996F);
        // launch
        test.launch("skyline.jpeg");

        // burst
        test.burst("skyline.jpeg");

        // second firework object
        Firework test2 = new Firework(200, 0, -10, -0.14, 0.6, 0, -0.0098, 0.05,
                                      0.486F, 0.992F, 0.466F);
        // launch
        test2.launch("skyline.jpeg");

        // burst
        test2.burst("skyline.jpeg");

        // test other methods

        // test move particles
        StdOut.println(test.positionsString());
        test.moveParticles();
        StdOut.println(test.positionsString());

        // test prepare burst
        StdOut.println(test.velocitiesString());
        test.prepareBurst();
        StdOut.println(test.velocitiesString());

        // test draw
        test.draw();
        StdDraw.show();
    }
}