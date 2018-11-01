package uet.oop.bomberman.audio;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Audio {
    private class AudioPlayer extends Thread {
        @Override
        public void run() {
            super.run();
        }
    }
    public static final String bg = "/audio/bg.mp3";
    public static final String boom = "/audio/boom.mp3";
    public static final String won = "/audio/won.mp3";
    public static final String end = "/audio/end.mp3";
    public static ArrayList<Player> players = new ArrayList<>();

    public static void play(String fileName) {
        Thread thread = new Thread(() -> {
            try {
                URL a = Audio.class.getResource(fileName);
                Player player = new Player(a.openStream());
                player.play();
                players.add(player);
                System.out.println("Successfully got back synthesizer data");
                //
            } catch(JavaLayerException e) {
                e.printStackTrace();
            }
            catch (IOException e) {

                e.printStackTrace(); //Print the exception ( we want to know , not hide below our finger , like many developers do...);
            }

        });

        //We don't want the application to terminate before this Thread terminates
        thread.setDaemon(false);

        //Start the Thread
        thread.start();
    }



    public static void playBackground() {
        play(bg);
    }

    public static void playBoombEffect() {
        play(boom);
    }

    public static void playWonEffect() {
        play(won);
    }
    public static void playEnd() {
        for(Player p: players) {
            p.close();
        }

        play(end);
    }

}
