package kvs.core.util;

import java.io.Serializable;

public final class Timer implements Serializable{
    
    private static final long serialVersionUID = 6369929265222602340L;

    public enum Trigger { Start };
    private long m_start = 0; ///< start time
    private long m_stop = 0;  ///< stop time
    
    public Timer(){
        
    }
    
    public Timer(Trigger trigger){
        if(trigger == Trigger.Start){
            this.start();
        }
        
    }
    
    public void start(){
        m_start = System.nanoTime();
    }
    
    public void stop(){
        m_stop = System.nanoTime();
    }
    
    public double sec(){
        return usec() / 1000000;
    }
    
    public double msec(){
        return usec() / 1000;
    }
    
    public double usec(){
        return (m_stop - m_start) / 1000;
    }
    
    public double fps(){
        return 1.0 / sec();
    }
    

}
