package com.example.demo.config;

import javassist.tools.web.BadHttpRequest;
import org.ff4j.core.Feature;
import org.ff4j.core.FeatureStore;
import org.ff4j.exception.FeatureAccessException;
import org.ff4j.exception.FeatureNotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SecureFeatureStore implements FeatureStore {

    private final FeatureStore delegate;

    private static final Map<String, List<String>> permissions = Map.of(
            "feature-add", List.of("userA", "userB"),
            "feature-sub", List.of("userA"),
            "feature-mul", List.of("userB", "userC"),
            "feature-div", List.of("userC")
    );

    public SecureFeatureStore(FeatureStore delegate) {
        this.delegate = delegate;
    }

    @Override
    public void enable(String featureUID) {
        try {
            checkPermission(featureUID);
        } catch (Exception e) {
            System.out.println("you dont have permission to change this feature"+featureUID);
            throw new FeatureNotFoundException(e.getMessage());
        }
        delegate.enable(featureUID);
    }

    @Override
    public void disable(String featureUID) {
        try {
            checkPermission(featureUID);
        } catch (Exception e) {
            System.out.println("you dont have permission to change this feature"+featureUID);

            throw new RuntimeException(e);
        }
        delegate.disable(featureUID);
    }

    private void checkPermission(String featureUID) throws Exception {
        String userId = "userB";
        List<String> allowedUsers = permissions.getOrDefault(featureUID, List.of());

        if (!allowedUsers.contains(userId)) {
              throw new FeatureNotFoundException(featureUID);

        }
    }

    @Override public Feature read(String uid) { return delegate.read(uid); }
    @Override public boolean exist(String uid) { return delegate.exist(uid); }
    @Override public void create(Feature feature) { delegate.create(feature); }
    @Override public void delete(String uid) { delegate.delete(uid); }
    @Override public void update(Feature feature) { delegate.update(feature); }

    @Override
    public void grantRoleOnFeature(String s, String s1) {

    }

    @Override
    public void removeRoleFromFeature(String s, String s1) {

    }

    @Override
    public void enableGroup(String s) {

    }

    @Override
    public void disableGroup(String s) {

    }

    @Override
    public boolean existGroup(String s) {
        return false;
    }

    @Override
    public Map<String, Feature> readGroup(String s) {
        return Map.of();
    }

    @Override
    public void addToGroup(String s, String s1) {

    }

    @Override
    public void removeFromGroup(String s, String s1) {

    }

    @Override
    public Set<String> readAllGroups() {
        return Set.of();
    }

    @Override public void clear() { delegate.clear(); }

    @Override
    public void importFeatures(Collection<Feature> collection) {

    }

    @Override
    public void createSchema() {

    }

    @Override public Map<String, Feature> readAll() { return delegate.readAll(); }
}
