package nu.hci.codemenao;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Resource{
    Queue<String> semaphore = new LinkedList<String>();
    Queue<String> sortedResult = new LinkedList<String>();

    public synchronized void addStrings(List<String> words){//for reader
        semaphore.addAll(words);
        notify();
    }//

    public synchronized String getString(){//for workers
        while(semaphore.isEmpty())
            try{ wait();}
            catch(InterruptedException e){}
        return semaphore.remove();
    }
}