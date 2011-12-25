package sn;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author root
 */
public class YSConfig {
    
    public static YSConfig _instance = null;
    
    public static YSConfig getInstance(){
        
        if(_instance == null){
            _instance = new YSConfig();
        }
        return _instance;
        
    }

    public int ThriftPort = 8001;
    
    
    public YSConfig() {
    
    }
    
    
    
    
}
