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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
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
       
        FileHandle serviceHandle = Gdx.files.internal("FirebaseSecretKey.json");
		String jsonString = serviceHandle.readString();

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