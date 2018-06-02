package com.coma.go.Dagger.Misc;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Koma on 19.12.2017.
 */
@Module
public class GsonModule {

    @Provides
    public Gson injectOkHttpClientFragment() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .setPrettyPrinting()
                .serializeNulls()
                .addSerializationExclusionStrategy(exclusionStrategy())
                .addDeserializationExclusionStrategy(exclusionStrategy())
                .create();
        return gson;
    }

    private ExclusionStrategy exclusionStrategy(){
        return new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                final Expose expose = fieldAttributes.getAnnotation(Expose.class);
                return expose != null && !expose.serialize();
            }

            @Override
            public boolean shouldSkipClass(Class<?> aClass) {
                return false;
            }
        };
    }

}
