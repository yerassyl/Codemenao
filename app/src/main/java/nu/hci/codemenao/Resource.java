package nu.hci.codemenao;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Resource {
    public Queue<String> semaphore = new LinkedList<String>();

    public synchronized void addString(String commands) {//for reader
        semaphore.add(commands);
        notify();
    }//
    public synchronized String getString() {
        while(semaphore.isEmpty())
            try{ wait();}
            catch(InterruptedException e){}
        return semaphore.remove();
    }
}