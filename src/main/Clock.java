package main;

import states.Coordinator;

import static java.lang.Thread.sleep;

public class Clock implements Runnable{

    int id;
    Double counter = 0.0;
    Client client;

    public Clock(int id){
        this.id = id;
    }

    public Clock(int id, Client client){
        this.id = id;
        this.client = client;
    }

    @Override
    public void run() {

        Boolean adjusted = false;

        while(!adjusted){
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            counter += id;

            if(client != null){
                if(client.getState() instanceof Coordinator){
                    if(counter >= 1){
                        System.out.println("--Coordenador aguardou todos os processos informarem os horários");
                        client.answer("Enviar media");
                        adjusted = true;
                    }
                }
            }

        }

    }

    public Double getCounter(){
        return counter;
    }

    public void setCounter(Double counter){
        this.counter = counter;
    }
}
