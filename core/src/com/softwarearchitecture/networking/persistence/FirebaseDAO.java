package com.softwarearchitecture.networking.persistence;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class FirebaseDAO<K, T> extends DAO<K, T> {

    public FirebaseDAO(boolean create, boolean read, boolean update, boolean delete) throws FileNotFoundException, IOException {
        this.create = create;
        this.read = read;
        this.update = update;
        this.delete = delete;
        // Write a message to the database
        FileInputStream serviceAccount = new FileInputStream("../android/FirebaseSecretKey.txt");

        FirebaseOptions options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setDatabaseUrl("https://besieged-8b842-default-rtdb.europe-west1.firebasedatabase.app")
            .build();

        FirebaseApp.initializeApp(options);

        String DatabaseName = "https://besieged-8b842-default-rtdb.europe-west1.firebasedatabase.app/";
        FirebaseDatabase database = FirebaseDatabase.getInstance(DatabaseName);
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, Firebase! Correct folder :)", (error, ref) -> {
            if (error != null) {
                System.out.println("Data could not be saved " + error.getMessage());
            } else {
                System.out.println("Data saved successfully.");
            }
        });
    }
    
    @Override
    public List<T> loadAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadAll'");
    }

    @Override
    public Optional<T> get(K id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    public boolean update(K id, T object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public boolean delete(K id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public K add(T object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

}