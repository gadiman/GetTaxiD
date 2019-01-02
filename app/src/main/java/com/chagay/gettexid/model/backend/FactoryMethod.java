package com.chagay.gettexid.model.backend;

import com.chagay.gettexid.model.datasource.FireBase_DBDriver;

public class FactoryMethod {
    static DB_Manager manager = null ;

    public static DB_Manager getManager() {
        if (manager == null)
            manager = new FireBase_DBDriver();

        return manager;
    }
}
