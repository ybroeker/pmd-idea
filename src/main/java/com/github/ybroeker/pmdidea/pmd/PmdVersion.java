package com.github.ybroeker.pmdidea.pmd;

import java.util.Comparator;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;

public final class PmdVersion implements Comparable<PmdVersion> {

    private static final Pattern VERSION_PATTERN = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)");

    private static final Comparator<PmdVersion> COMPARATOR = Comparator.comparing(PmdVersion::getMajor)
            .thenComparing(PmdVersion::getMinor)
            .thenComparing(PmdVersion::getPatch);


    private final int major;
    private final int minor;
    private final int patch;

    protected PmdVersion(final int major, final int minor, final int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public static PmdVersion of(String versionString) {
        final Matcher matcher = VERSION_PATTERN.matcher(versionString);
        if (matcher.find()) {
            return new PmdVersion(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)));
        }
        throw new RuntimeException("Unsupported Version: " + versionString);
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    @Override
    public String toString() {
        return major + "." + minor + "." + patch;
    }

    @Override
    public int compareTo(@NotNull final PmdVersion that) {
        return COMPARATOR.compare(this, that);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        final PmdVersion that = (PmdVersion) other;
        return major == that.major
               && minor == that.minor
               && patch == that.patch;
    }

    @Override
    public int hashCode() {
        return Objects.hash(major, minor, patch);
    }
}
