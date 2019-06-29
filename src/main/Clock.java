package main;

import static java.lang.Thread.sleep;

public class Clock implements Runnable{

    int id;
    Double counter = 0.0;

    Clock(int id){
        this.id = id;
    }

    @Override
    public void run() {

        while(true){
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            counter += id;
        }

    }

    public Double getCounter(){
        return counter;
    }
}
