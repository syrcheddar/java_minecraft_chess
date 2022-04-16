package classes.rest;

import javafx.application.Platform;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;



public class Clock {
    static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("mm:ss");
    public static LocalTime timerWhite = null;
    public static LocalTime timerBlack = null;
    public static LocalTime endTime = LocalTime.of(0,0,0,0);

    /**
     * Sets timer by the parameter.
     * @param timeSetting The time setting parameter.
     */
    public static void setTime(int timeSetting){
        if(timeSetting==5){
            timerBlack = LocalTime.of(0,0,5,0);
            timerWhite = LocalTime.of(0,5,0,0);
        }
        if(timeSetting==10){
            timerBlack = LocalTime.of(0,10,0,0);
            timerWhite = LocalTime.of(0,10,0,0);
        }
        if(timeSetting==20){
            timerBlack = LocalTime.of(0,20,0,0);
            timerWhite = LocalTime.of(0,20,0,0);
        }
        if(timeSetting==30){
            timerBlack = LocalTime.of(0,30,0,0);
            timerWhite = LocalTime.of(0,30,0,0);
        }
        if(timeSetting==-1){
            timerBlack = null;
            timerWhite = null;
        }
    }

    /**
     * Function which moves timer down every second.
     */
    public static void moveTime(){
        if (timerWhite!=null) {
            while (true) {
                if (GameBoard.whosOnMove) {
                    LocalTime startTime = LocalTime.now();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    LocalTime finTime = LocalTime.now();
                    Duration delta = Duration.between(startTime, finTime);
                    timerWhite = timerWhite.minus(delta);
                }
                if (!GameBoard.whosOnMove) {
                    LocalTime startTime = LocalTime.now();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    LocalTime finTime = LocalTime.now();
                    Duration delta = Duration.between(startTime, finTime);
                    timerBlack = timerBlack.minus(delta);
                }
                Platform.runLater(Game::showTimer);
                if (dtf.format(timerBlack).equals(dtf.format(endTime)) || dtf.format(timerWhite).equals(dtf.format(endTime))){
                    GameBoard.gameEnded=true;
                    Platform.runLater(GameBoard::showBoard);
                    break;
                }
            }
        }
    }
}
