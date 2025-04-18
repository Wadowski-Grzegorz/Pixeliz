package com.example.backend.exception;

import java.util.Map;

public class DrawingNotFoundException  extends DetailedException {
    public DrawingNotFoundException(Map<String, Object> criteria){
        super("Drawing not found", criteria);
    }

    public DrawingNotFoundException(){
        super("Drawing not found");
    }
}
