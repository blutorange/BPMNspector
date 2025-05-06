package com.github.blutorange.bpmnspector.autofix;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FixerRepository {

    private final Map<FixerIdentifier, ViolationFixer> availableFixes;

    public FixerRepository() {
        this.availableFixes = new HashMap<>();
        registerExistingFixers();
    }

    private void registerExistingFixers() {
        availableFixes.put(
                EXT012FirstOptionNotExecutableFixer.getFixerIdentifier(), new EXT012FirstOptionNotExecutableFixer());
        availableFixes.put(
                EXT012SecondOptionMarkFormalExpFixer.getFixerIdentifier(), new EXT012SecondOptionMarkFormalExpFixer());
        availableFixes.put(EXT098RemoveInvalidTypeFixer.getFixerIdentifier(), new EXT098RemoveInvalidTypeFixer());
        availableFixes.put(EXT097AutoFixer.getFixerIdentifier(), new EXT097AutoFixer());
        availableFixes.put(EXT105AutoFixer.getFixerIdentifier(), new EXT105AutoFixer());
        availableFixes.put(EXT106RemoveInvalidTypeFixer.getFixerIdentifier(), new EXT098RemoveInvalidTypeFixer());
        availableFixes.put(EXT128AutoFixer.getFixerIdentifier(), new EXT128AutoFixer());
        availableFixes.put(EXT150AutoFixer.getFixerIdentifier(), new EXT150AutoFixer());
        availableFixes.put(EXT151AutoFixer.getFixerIdentifier(), new EXT151AutoFixer());
    }

    public Optional<ViolationFixer> getFixerForConstraintAndStrategy(String constraintId, FixingStrategy strategy) {
        final var identifier = new FixerIdentifier(constraintId, strategy);
        return Optional.ofNullable(availableFixes.get(identifier));
    }

    public ViolationFixer getFirstFixerForConstraint(String constraintId) {
        return availableFixes.entrySet().stream()
                .filter(entry -> entry.getKey().constraintId.equals(constraintId))
                .filter(entry -> entry.getKey().fixingStrategy == FixingStrategy.FIRST_OPTION)
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    public List<ViolationFixer> getAllFixersForConstraint(String constraintId) {
        return availableFixes.entrySet().stream()
                .filter(entry -> entry.getKey().constraintId.equals(constraintId))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
}
