package com.example.demo.config;

import org.ff4j.core.Feature;
import org.ff4j.core.FeatureStore;
import org.ff4j.exception.FeatureAccessException;
import org.ff4j.exception.FeatureNotFoundException;

import java.sql.*;
import java.util.*;

public class SecureFeatureStore implements FeatureStore {

    private final FeatureStore delegate;
    private final Map<String, List<String>> permissions = new HashMap<>();

    // Database connection details
    private static final String JDBC_URL = "jdbc:h2:file:./data/ff4jdb";
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASS = "";

    public SecureFeatureStore(FeatureStore delegate) {
        this.delegate = delegate;
        loadPermissionsFromDatabase();
    }

    private void loadPermissionsFromDatabase() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS)) {

            // Step 1: Fetch active users into a Map<UserId, Username>
            Map<Integer, String> userIdToUsername = new HashMap<>();

            String userQuery = "SELECT USER_ID, USERNAME FROM ACTIVE_USERS WHERE IS_ACTIVE = TRUE";
            try (Statement userStmt = conn.createStatement();
                 ResultSet userRs = userStmt.executeQuery(userQuery)) {

                while (userRs.next()) {
                    int userId = userRs.getInt("USER_ID");
                    String username = userRs.getString("USERNAME");
                    userIdToUsername.put(userId, username);
                }
            }

            // Step 2: Fetch feature mappings for active users
            String mappingQuery = "SELECT USER_ID, FEAT_UID FROM USER_FEATURE_MAPPING";
            try (Statement mapStmt = conn.createStatement();
                 ResultSet mapRs = mapStmt.executeQuery(mappingQuery)) {

                while (mapRs.next()) {
                    int userId = mapRs.getInt("USER_ID");
                    String featureId = mapRs.getString("FEAT_UID");

                    if (userIdToUsername.containsKey(userId)) {
                        String username = userIdToUsername.get(userId);
                        permissions.computeIfAbsent(featureId, k -> new ArrayList<>()).add(username);
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to load feature permissions from database", e);
        }
    }

    @Override
    public void enable(String featureUID) {
        try {
            checkPermission(featureUID);
        } catch (Exception e) {
            System.out.println("You don't have permission to enable feature: " + featureUID);
            throw new FeatureAccessException(e.getMessage());
        }
        delegate.enable(featureUID);
    }

    @Override
    public void disable(String featureUID) {
        try {
            checkPermission(featureUID);
        } catch (Exception e) {
            System.out.println("You don't have permission to disable feature: " + featureUID);
            throw new FeatureAccessException(e.getMessage());
        }
        delegate.disable(featureUID);
    }

    private void checkPermission(String featureUID) throws Exception {
        String userId = getActiveUserFromDb(); // dynamically read active user
        List<String> allowedUsers = permissions.getOrDefault(featureUID, List.of());

        if (!allowedUsers.contains(userId)) {
            throw new FeatureAccessException("User [" + userId + "] is not allowed to access feature [" + featureUID + "]");
        }
    }

    private String getActiveUserFromDb() throws SQLException {
        String activeUser = null;

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT USERNAME FROM ACTIVE_USERS WHERE IS_ACTIVE = TRUE LIMIT 1"
             );
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                activeUser = rs.getString("USERNAME");
            }
        }

        if (activeUser == null) {
            throw new RuntimeException("No active user found in database");
        }

        return activeUser;
    }

    // Delegate all other methods
    @Override public Feature read(String uid) { return delegate.read(uid); }
    @Override public boolean exist(String uid) { return delegate.exist(uid); }
    @Override public void create(Feature feature) { delegate.create(feature); }
    @Override public void delete(String uid) { delegate.delete(uid); }
    @Override public void update(Feature feature) { delegate.update(feature); }
    @Override public void grantRoleOnFeature(String s, String s1) {}
    @Override public void removeRoleFromFeature(String s, String s1) {}
    @Override public void enableGroup(String s) {}
    @Override public void disableGroup(String s) {}
    @Override public boolean existGroup(String s) { return false; }
    @Override public Map<String, Feature> readGroup(String s) { return Map.of(); }
    @Override public void addToGroup(String s, String s1) {}
    @Override public void removeFromGroup(String s, String s1) {}
    @Override public Set<String> readAllGroups() { return Set.of(); }
    @Override public void clear() { delegate.clear(); }
    @Override public void importFeatures(Collection<Feature> collection) {}
    @Override public void createSchema() {}
    @Override public Map<String, Feature> readAll() { return delegate.readAll(); }
}
