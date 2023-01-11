package dev.prithvis.blogbackend.models;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DataModels {
    private DataModels(){
        // adding a private constructor to hide public implicit one
    }

    public static Set<String> getAttributes(String modelName){
        switch (modelName){
            case "BlogUser":
                return new HashSet<>(
                        Arrays.asList("id","name","email","password","about")
                );
            default:
                throw new IllegalArgumentException("No model matches the supplied name "+modelName);

        }
    }
}
