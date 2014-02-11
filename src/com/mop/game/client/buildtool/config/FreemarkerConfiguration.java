package com.mop.game.client.buildtool.config;

import freemarker.template.Configuration;

public class FreemarkerConfiguration {
	private static Configuration config = null;  
    
    /** 
     * Static initialization. 
     *  
     * Initialize the configuration of Freemarker. 
     */  
    static{  
        config = new Configuration();  
    }  
      
    public static Configuration getConfiguation(){  
        return config;  
    }  
}
