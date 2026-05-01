package catchorcrash;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class HighScoreManager {

    private File file;

    public HighScoreManager() {
        file = new File("highscore.txt");
    }

    public int getHighScore() {
        if (!file.exists()) {
            return 0;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            reader.close();

            if (line != null) {
                return Integer.parseInt(line.trim());
            }
        } catch (Exception e) {
            return 0;
        }

        return 0;
    }

    public void saveIfHigher(int score) {
        if (score <= getHighScore()) {
            return;
        }

        try {
            FileWriter writer = new FileWriter(file);
            writer.write(String.valueOf(score));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
