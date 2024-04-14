package com.softwarearchitecture.networking.persistence;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class FirebaseDAO<K, T> extends DAO<K, T> {

    private final FirebaseDatabase database;
    private final Class<K> idParameterClass;
    private final Class<T> typeParameterClass;
    private final Gson gson;

    public FirebaseDAO(Class<K> idParameterClass, Class<T> typeParameterClass) throws FileNotFoundException, IOException {
        this.typeParameterClass = typeParameterClass;
        this.idParameterClass = idParameterClass;
        this.gson = new Gson();
       
        String jsonString = "{"
                + "\"type\": \"service_account\","
                + "\"project_id\": \"besieged-8b842\","
                + "\"private_key_id\": \"dc7119641c0b878b68e04241f65d33830a8f5f1c\","
                + "\"private_key\": \"-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDf0e565B8vGg3D\nH8d/x69A+1muGaqEsHQzlR9QuVS8hyn60pTLFalyTXKGxIEYUYGP9el07eS0v+fD\nEewIlUTIqH/bzemnUfjUTdvINKcVEQo9TeyorSDDGPMMCppEGq7ovgwkq6L8EQw+\nDzG/Qv0ClZqwE78+WJVw7GvIq0UGYKGxqGCjoN+6clBZrxccUjT8V97aPBv7cBZi\nYG74hyGNnG3hJbh7B1esu5E6zQbAsjt7Qb0V2jseAIqWktsW3cJyU3YBCg0c0WZ3\nPX8QD9xHI4ti8RdwtWz1hp4qsdf6IL5h1ZDgHNcZosDr4jAPGNYvkXhRF6lhoSf5\nEEDoV4KNAgMBAAECggEAA9eRtXBXDLLwy4mvHgVqf74Q0wlDwRVaWUxVBki4KKhV\nUyXsQfQ0PW8Q6TlUUfuMMtFw2X3/8A5fRxz2pUsSrDMgRsCpIyMoF1Ti0fP9bkIl\nn9nHlzmpl0/FVE6WSVb/66Tfhn2fxZ3xTNtr6jGXMEUrK5ybKtHQdujIDODLxYk4\n4YqxoZ7D04MerXau0sL6WVo8EHg29xlI1ovMnFXPytW9GpyLFRzm6YorVpyLEMUY\nLPsiTEwUpYJXUz4WfilqACldtY922lPoOVUEn22OfPGj5NGRY7OpBp7aL92RyNyy\n3yfBA5iV7CkSgjtOsmSU8ha9GfO9xDH3mPOqV9igzQKBgQD+h+5gerRcCN2hO9uf\nboiNbPZoAYmNN/gO6nvQB1H97YP/7Z8jux9FeNne1i20mYw6U+wABvVLt9DUbkS/\n6q+GKprXsPcEExeWBioX7YfiaJaVVUX6KGBfKSgjBNycDBZGh2iRoJMxg29pb9nD\nWMjSoTFAnJm4hufraMu9ISW5owKBgQDhHKAFGDjfG6JLUH0ona0vVedmTV634TjI\neVdF7/lDVgTfpjyx9HmzcGSRrMjPPLrWlWa93ss784BNzd8eh3OaW29f+rOYcgA7\nHF676V+3w6t9knSuZtA5MpG5j+3jxIQLHWmLkgD+2KXgoZ8VK+U2moIUgRmnJA6d\n+cspGDX2DwKBgByfmLUM13+KF7JsEtSpD34oBMXJdniFFMZIpEfOqu/NaAy94imY\nf3nGj2m52EX5BoYArgeKwMcVEszw4ZZbhAWogOVEp2Kz6vEaHF24fpF9hYGhp07s\nf3n0HOljQJPj4BVqYN4Pmr30s/C9fVW0hFD7g5hqg/b2ZNpaXJ8WPTHVAoGAMaJQ\nHg9zKq47IIQEtJnff5pfRQ02HzEHhhxLA96fhRtIQ8+xfkM7OjdD1WLVe+wV/g4+\n8wpWoUdVONJ1qZOQB661ZeIvCyPHrUDaGnCgbDI2c5+BHpsIujPOMa3sc9X2j0TS\n6rUshwRZvqekHdTp2xpCKCc28sVee7KB+vOdoj0CgYEAm5/rdcuo+c5UjyUipMz4\nd9ux0C5k6xuDpZY7eU8UbygqNsX2jb5/tMWLuZHEzB9mMTR1aFwWsD8roJ6q0rZi\n1xOfn6JjNLhVXyagsNktOv6b3sBMN67ofWlJ7KWbrgkazFzJh2jsiQHmxdXpssSN\ne1X/wn2LqkYxZfYVBvEDu0I=\n-----END PRIVATE KEY-----\n\","
                + "\"client_email\": \"firebase-adminsdk-pirly@besieged-8b842.iam.gserviceaccount.com\","
                + "\"client_id\": \"115745368280936802257\","
                + "\"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\","
                + "\"token_uri\": \"https://oauth2.googleapis.com/token\","
                + "\"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\","
                + "\"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-pirly%40besieged-8b842.iam.gserviceaccount.com\","
                + "\"universe_domain\": \"googleapis.com\""
                + "}";

        InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));

        

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(inputStream))
                .setDatabaseUrl("https://besieged-8b842-default-rtdb.europe-west1.firebasedatabase.app")
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        } else {
            FirebaseApp.getInstance();
        }

        this.database = FirebaseDatabase.getInstance();
    
    }
    
    @Override
    public List<T> loadAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Optional<T> get(K id) {
        String idJson = gson.toJson(id);
        DatabaseReference ref = database.getReference(idJson);
        AtomicReference<T> result = new AtomicReference<>();
        CompletableFuture<Optional<T>> future = new CompletableFuture<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String objectJson = dataSnapshot.getValue(String.class);
                T value = gson.fromJson(objectJson, typeParameterClass);
                result.set(value);
                future.complete(Optional.ofNullable(value));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.complete(Optional.empty());
            }
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public boolean update(K id, T object) {
        String idJson = gson.toJson(id);
        DatabaseReference ref = database.getReference(idJson);
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        // Convert object to JSON
        String objectJson = gson.toJson(object);
        ref.setValue(objectJson, (databaseError, databaseReference) -> future.complete(databaseError == null));

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(K id) {
        DatabaseReference ref = database.getReference("path/to/your/data/" + id);
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        ref.removeValue((databaseError, databaseReference) -> future.complete(databaseError == null));

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void add(K id, T object) {
        String idJson = gson.toJson(id);
        DatabaseReference ref = database.getReference(idJson);
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        // Convert object to JSON
        String objectJson = gson.toJson(object);

        ref.setValue(objectJson, (databaseError, databaseReference) -> future.complete(databaseError == null));
    }

}