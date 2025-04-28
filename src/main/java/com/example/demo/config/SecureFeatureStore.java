package com.example.demo.config;

import org.ff4j.core.Feature;
import org.ff4j.core.FeatureStore;

import java.sql.*;
import java.util.*;

public class SecureFeatureStore implements FeatureStore {

    private final FeatureStore delegate;

    private static final String JDBC_URL = "jdbc:h2:file:./data/ff4jdb";
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASS = "";

    public SecureFeatureStore(FeatureStore delegate) {
        this.delegate = delegate;
    }
    public boolean hasAccess(String canaryId, String featureId) throws SQLException {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT COUNT(*) FROM CANARY_FEATURE_MAPPING WHERE CANARY_ID = ? AND FEAT_UID = ?"
             )) {
            stmt.setString(1, canaryId);
            stmt.setString(2, featureId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // your hasAccess method etc remains same

    @Override public Feature read(String uid) { return delegate.read(uid); }
    @Override public boolean exist(String uid) { return delegate.exist(uid); }
    @Override public void create(Feature feature) { delegate.create(feature); }
    @Override public void delete(String uid) { delegate.delete(uid); }
    @Override public void update(Feature feature) { delegate.update(feature); }
    @Override public void enable(String featureId) { delegate.enable(featureId); }
    @Override public void disable(String featureId) { delegate.disable(featureId); }
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
