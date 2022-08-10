/* This client creates the three features of the project: the firework show,
learning stage, and firework quiz. */

public class FireworkDisplay {

    // Returns maximum alpha value (transparency) among all fireworks in a symbol
    // table of fireworks.
    public static float maxAlpha(ST<Integer, Firework> group) {
        // starts at low value, tracks max alpha value throughout symbol table
        double max = 0.0;

        // goes through symbol table and updates max alpha value
        for (int i = 0; i < group.size(); i++) {
            // gets first particle of firework in symbol table
            Firework temp = group.get(i);
            Particle singleParticle = temp.getParticle(0);

            double currentAlpha = singleParticle.getColorAlpha();

            // updates max alpha if current firework's particles have higher alpha
            if (currentAlpha > max) max = currentAlpha;
        }
        // returns max alpha
        return (float) max;
    }

    // Draws a group of fireworks launching all together and bursting.
    // Needs symbol table of fireworks and a background image to clear to.
    public static void multipleLaunchBurst(ST<Integer, Firework> group, String image) {
        // tracks how many times the fireworks have been drawn
        int n = 0;

        // tracks whether one of the fireworks in the groups exploded already
        boolean hasOneBurst = false;

        // draws motion of firework group until all fireworks are nearly transparent
        while (maxAlpha(group) > 0.01F) {
            // iterates through firework group and draws/updates them as necessary
            for (int i = 0; i < group.size(); i++) {
                // randomizes burst point
                double burstPoint = StdRandom.gaussian(0, 0.15);

                // gets first particle of current firework
                Firework temp = group.get(i);
                Particle singleParticle = temp.getParticle(0);

                // draws firework when it has exploded (alpha has been decremented)
                if (singleParticle.getColorAlpha() < 1.0F) {
                    (group.get(i)).draw(); // draws firework
                    (group.get(i)).moveParticles(); // updates position of particles

                    // updates color of every particle in firework
                    for (int j = 0; j < temp.getQuantity(); j++) {
                        Particle singleParticleTwo = temp.getParticle(j);
                        // multiplies alpha values by 0.9 so that they decrease
                        singleParticleTwo.updateColor(0.9);
                    }
                }

                // updates color and plays sound as firework first passes burst point
                else if (singleParticle.getVy() <= burstPoint) {
                    // decrements alpha values so that it will enter previous if
                    // statement in next loop around
                    for (int j = 0; j < (group.get(i)).getQuantity(); j++) {
                        Particle tempParticle = (group.get(i)).getParticle(j);
                        tempParticle.updateColor(0.999);
                    }

                    // plays explosion sound
                    StdAudio.play("explosion.wav");

                    // updates velocities in preparation of explosion
                    (group.get(i)).prepareBurst();

                    hasOneBurst = true;
                }

                // draws firework and updates position while it is being launched up
                else {
                    (group.get(i)).draw();
                    (group.get(i)).moveParticles();
                }
            }
            // displays fireworks after they are all drawn
            StdDraw.show();
            StdDraw.pause(30); // pause briefly

            // clears to picture every step when no firework has burst yet
            if (!hasOneBurst) StdDraw.picture(0, 0, image);

            // creates layered bloom when fireworks burst by clearing every fifth step
            else if (n % 5 == 0) StdDraw.picture(0, 0, image);

            n++;
        }
        // clear to picture after fireworks are nearly transparent
        StdDraw.picture(0, 0, image);
    }

    // Simulate show mode to create a timed show with fireworks and music.
    private static void show() {
        while (true) {
            // display options
            StdDraw.picture(0, 0, "ShowMenu.jpeg");
            StdDraw.show();

            // keeps track of selected choice
            String fileName = "";

            if (StdDraw.isKeyPressed('1')) { // choice 1 - fourth of july
                fileName = "FourthOfJuly.txt";
            }
            else if (StdDraw.isKeyPressed('2')) { // choice 2 - EDM
                fileName = "EDMdance.txt";
            }
            else if (StdDraw.isKeyPressed('3')) { // choice 3 - Rainbow
                fileName = "Rainbow.txt";
            }
            else if (StdDraw.isKeyPressed('Z')) // user wants to exit mode
            {
                return; // end function
            }
            else {
                continue; // choose no option if wrong key is pressed
            }

            // initialize reader for correct file
            In reader = new In(fileName);
            String songName = reader.readString(); // get song
            String pictureName = reader.readString(); // get background

            // display show
            StdDraw.picture(0, 0, pictureName); // show background image
            StdDraw.show();
            StdAudio.play(songName); // start song

            while (!reader.isEmpty()) {
                // identifies single firework or firework group
                int loops = reader.readInt();

                // displays a firework group based on text file input
                if (loops > 1) {
                    ST<Integer, Firework> group = new ST<Integer, Firework>();

                    // creates fireworks from input and enters them into symbol table
                    for (int i = 0; i < loops; i++) {
                        int numP = reader.readInt(); // number of particles
                        double px = reader.readDouble(); // get x position
                        double py = reader.readDouble(); // get y position
                        double vx = reader.readDouble(); // get x velocity
                        double vy = reader.readDouble(); // get y velocity
                        double ax = reader.readDouble(); // get x acceleration
                        double ay = reader.readDouble(); // get y acceleration
                        double size = reader.readDouble(); // get size of particles
                        float r = reader.readFloat(); // get red value
                        float g = reader.readFloat(); // get blue value
                        float b = reader.readFloat(); // get green value
                        Firework temp = new Firework(numP, px, py, vx, vy, ax, ay, size,
                                                     r, g, b);
                        group.put(i, temp);
                    }
                    // launch and explode firework group over background
                    multipleLaunchBurst(group, pictureName);
                }

                // displays a single firework based on text file input
                else {
                    int numP = reader.readInt(); // number of particles
                    double px = reader.readDouble(); // get x position
                    double py = reader.readDouble(); // get y position
                    double vx = reader.readDouble(); // get x velocity
                    double vy = reader.readDouble(); // get y velocity
                    double ax = reader.readDouble(); // get x acceleration
                    double ay = reader.readDouble(); // get y acceleration
                    double size = reader.readDouble(); // get size of particles
                    float r = reader.readFloat(); // get red value
                    float g = reader.readFloat(); // get blue value
                    float b = reader.readFloat(); // get green value
                    Firework fw = new Firework(numP, px, py, vx, vy, ax, ay, size,
                                               r, g, b); // create firework
                    fw.launch(pictureName); // launch firework over background
                    fw.burst(pictureName); // explode firework over background
                }
            }
            return; // get back to main menu after text file is empty
        }
    }

    // Simulate learning mode for fireworks to be launched based on user choice.
    private static void learn() {
        while (true) {
            // prevent double clicking issue
            StdDraw.pause(200);

            // display menu
            StdDraw.picture(0, 0, "LearnMenu.jpeg");
            StdDraw.show();

            // check that user wants to keep playing
            // adapted from XOXOStdDraw program in class materials
            if (StdDraw.isKeyPressed('1')) {
                return; // exit learn mode if 1 is pressed
            }

            // initialize color values to 0
            float r = 0.0F;
            float g = 0.0F;
            float b = 0.0F;

            // match mouse click to chemical choice
            // adapted from XOXOStdDraw program in class materials
            if (StdDraw.isMousePressed()) {
                // tracks coordinates of mouse click
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();

                if (x >= -8 && x <= 8) {
                    if (y >= 6.5 && y <= 8.5) { // choice 1 - red
                        r = 1.0F;
                    }
                    else if (y >= 3.5 && y <= 5.5) { // choice 2 - orange
                        r = 1.0F;
                        g = 0.6F;
                    }
                    else if (y >= 0.5 && y <= 2.5) { // choice 3 - yellow
                        r = 1.0F;
                        g = 1.0F;
                    }
                    else if (y >= -2.5 && y <= -0.5) { // choice 4 - green
                        r = 0.5F;
                        g = 0.9F;
                        b = 0.2F;
                    }
                    else if (y >= -5.5 && y <= -3.5) { // choice 5 - blue
                        r = 0.364F;
                        g = 0.501F;
                        b = 0.976F;
                    }
                    else if (y >= -8.5 && y <= -6.5) { // choice 6 - purple
                        r = 0.8F;
                        g = 0.509F;
                        b = 0.972F;
                    }
                    else continue; // account for wrong click

                    // create firework
                    Firework fw = new Firework(100, 0, -5, 0, 0.5,
                                               0, -0.0098, 0.1, r, g, b);
                    String pictureName = "learnSky.jpeg"; // background to be sent

                    // display single firework launching and exploding
                    fw.launch(pictureName);
                    fw.burst(pictureName);
                }
            }
        }
    }

    // Simulate quiz mode for player to match chemicals to firework colors.
    private static void quiz() {
        // pause shortly so that mouse does not double click when choosing quiz
        StdDraw.pause(200);

        // create symbol table that associates an integer with a String array
        // of questions and answers
        ST<Integer, String[]> questions = new ST<Integer, String[]>();
        questions.put(0, new String[] { "Strontium Salts", "RED" });
        questions.put(1, new String[] { "Calcium Salts", "ORANGE" });
        questions.put(2, new String[] { "Sodium Salts", "YELLOW" });
        questions.put(3, new String[] { "Barium Salts", "GREEN" });
        questions.put(4, new String[] { "Copper Salts", "BLUE" });
        questions.put(5, new String[] { "Copper and Strontium Salts", "PURPLE" });

        // create probability array where each index has equal probability
        double[] probabilities = new double[6];
        for (int i = 0; i < questions.size(); i++) {
            probabilities[i] = 1.0 / questions.size();
        }

        // start with the score at 0
        int score = 0;

        // plays game until all chemicals are matched with their colors
        while (!questions.isEmpty()) {
            // display quiz board with score before picking an answer each time
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.picture(0, 0, "GameMenu.jpeg");
            StdDraw.text(8, 8, "" + score);
            StdDraw.show();

            // get random integer that matches a key that is still in the symbol table
            int random = StdRandom.discrete(probabilities);

            // get a question and answer pair from the symbol table using random key
            String[] pairedQA = questions.get(random);

            // prompts user to match a color with the given chemical
            StdDraw.text(-2, 7, "Which color is produced by "
                    + pairedQA[0] + "?");
            StdDraw.show();

            // used to keep track of color user selects
            String selectedAnswer = "";

            // waits for user to click a color
            while (true) {
                // exits quiz if you press X
                if (StdDraw.isKeyPressed('X')) {
                    return;
                }

                // tracks which color is clicked
                if (StdDraw.isMousePressed()) {
                    // coordinates of mouse click
                    double x = StdDraw.mouseX();
                    double y = StdDraw.mouseY();

                    // for colors in left column
                    if (x >= -8 && x <= -2) {
                        // recognizes user clicked in red box
                        if (y >= 0 && y <= 2) {
                            selectedAnswer = "RED";
                        }
                        // orange box
                        else if (y >= -4 && y <= -2) {
                            selectedAnswer = "ORANGE";
                        }
                        // yellow box
                        else if (y >= -8 && y <= -6) {
                            selectedAnswer = "YELLOW";
                        }
                    }

                    // for colors in right column
                    else if (x >= 2 && x <= 8) {
                        // recognizes user clicked in green box
                        if (y >= 0 && y <= 2) {
                            selectedAnswer = "GREEN";
                        }
                        // blue box
                        else if (y >= -4 && y <= -2) {
                            selectedAnswer = "BLUE";
                        }
                        // purple box
                        else if (y >= -8 && y <= -6) {
                            selectedAnswer = "PURPLE";
                        }
                    }

                    // loops again and waits for new click if no color was selected
                    if (selectedAnswer.matches("")) continue;

                    // user clicked correct color
                    else if (selectedAnswer.matches(pairedQA[1])) {
                        score++; // increment score

                        // remove key-value pair of that chemical in symbol table
                        questions.remove(random);

                        // makes it impossible to be prompted with same chemical
                        probabilities[random] = 0.0;

                        // give remaining chemicals equal probabilities of occurring
                        for (int j = 0; j < 6; j++) {
                            if (questions.contains(j)) {
                                probabilities[j] = 1.0 / questions.size();
                            }
                        }

                        // mark as correct and change score before next question
                        StdDraw.setPenColor(8, 207, 7); // green color
                        StdDraw.picture(0, 0, "GameMenu.jpeg");
                        StdDraw.text(-2, 6, "Correct!");
                        StdDraw.text(8, 8, "" + score);
                        StdDraw.show();
                        StdDraw.pause(2200); // pause briefly after choosing correctly
                    }

                    // user clicked incorrect color
                    else {
                        score--; // decrement score

                        // mark as incorrect and change score before next question
                        StdDraw.setPenColor(208, 6, 11); // red color
                        StdDraw.picture(0, 0, "GameMenu.jpeg");
                        StdDraw.text(-2, 6, "Incorrect :(");
                        StdDraw.text(8, 8, "" + score);
                        StdDraw.show();
                        StdDraw.pause(2200); // pause after choosing incorrectly
                    }

                    // goes back into original while loop to get new random chemical
                    break;
                }
            }
        }

        // gives game over screen and feedback on quiz performance
        StdDraw.picture(0, 0, "GameMenu.jpeg");
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(8, 8, "" + score);
        StdDraw.text(-2, 7, "Game Over");
        // perfect score feedback
        if (score == 6) StdDraw.text(-2, 6, "Great Job!");
        // mediocre score feedback
        else if (score < 6 && score >= 0) StdDraw.text(-2, 6,
                                                       "Try again to get a perfect score!");
        // bad score feedback
        else StdDraw.text(-2, 6, "Consider going back to learning mode.");
        StdDraw.show();
        StdDraw.pause(4000); // pause once quiz is done and return to main menu
    }

    public static void main(String[] args) {
        // set up canvas to draw
        StdDraw.setCanvasSize(750, 750); // 750 x 750 canvas size
        StdDraw.setXscale(-10, 10);
        StdDraw.setYscale(-10, 10);

        StdDraw.enableDoubleBuffering();

        // welcome screen
        StdDraw.picture(0, 0, "StartMenu.jpeg");
        StdDraw.show();
        StdDraw.pause(3000); // wait before moving on to game

        boolean programOver = false;
        while (!programOver) { // start game to be repeated
            // display menu
            StdDraw.picture(0, 0, "MainMenu.jpeg"); // menu screen
            StdDraw.show();
            // check that user wants to keep playing
            // adapted from XOXOStdDraw program in class materials
            if (StdDraw.isKeyPressed('9')) {
                programOver = true; // end loop and exit to goodbye screen
            }

            // adapted from XOXOStdDraw program in class materials
            // check user choice of mode
            if (StdDraw.isMousePressed()) {
                // tracks coordinates of mouse click
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();

                if (x >= -8 && x <= 8) {
                    if (y >= 4 && y <= 8) { // choice 1 - Show
                        show();
                    }
                    else if (y >= -2 && y <= 2) { // choice 2 - Learn
                        learn();
                    }
                    else if (y >= -8 && y <= -4) { // choice 3 - Quiz
                        quiz();
                    }
                }
            }
        }
        StdDraw.picture(0, 0, "EndScreen.jpeg"); // draw goodbye screen
        StdDraw.show();
        StdDraw.pause(4000); // wait before closing game
        System.exit(0); // closes StdDraw window and ends program
    }
}