package com.itique.ps.util.generate;

import java.util.Arrays;

import static java.lang.Math.random;

public class NameGeneratorUtil {

    private final static String consonants = "bcdfghjklmnpqrstvwxz";
    private final static String vowels = "aeiouy";
    private final static String alphabet = "abcdefghijklmnopqrstuvwxyz";

    public static String generateName(int maxLength) {
        return generateName(maxLength, Config.builder().build());
    }

    public static String generateName(int maxLength, Config config) {
        StringBuffer result = new StringBuffer();
        int length = (int) (random() * maxLength);
        String next;
        do {
            double match = random();
            if (config.getConsonantChance() > config.getVowelChance()) {
                if (match < config.getVowelChance()) {
                    next = next(result, vowels, consonants);
                } else {
                    next = next(result, consonants, vowels);
                }
            } else if (config.getConsonantChance() < config.getVowelChance()) {
                if (match < config.getConsonantChance()) {
                    next = next(result, consonants, vowels);
                } else {
                    next = next(result, vowels, consonants);
                }
            } else {
                next = nextAlphabet(result);
            }
            result.append(next);
        } while (result.length() < length);
        return config.isCapitalize() ? result.substring(0, 1).toUpperCase() + result.substring(1)
                : result.toString();
    }

    private static String next(StringBuffer string, String letters, String contrLetters) {
        if (string.length() > 1) {
            String lastTwo = string.substring(string.length() - 2);
            String next = letters.split("")[(int) (random() * letters.length())];
            String lastThree = lastTwo + next;
            if (Arrays.stream(lastThree.split("")).filter(letters::contains).count() == 3) {
                next = contrLetters.split("")[(int) (random() * contrLetters.length())];
            }
            return next;
        } else {
            return letters.split("")[(int) (random() * letters.length())];
        }
    }

    private static String nextAlphabet(StringBuffer string) {
        if (string.length() > 1) {
            String next;
            String lastThree;
            do {
                next = alphabet.split("")[(int) (random() * alphabet.length())];
                lastThree = string.substring(string.length() - 2) + next;
            } while (Arrays.stream(lastThree.split(""))
                    .filter(consonants::contains).count() == 3
                    || Arrays.stream(lastThree.split(""))
                    .filter(vowels::contains).count() == 3);
            return next;
        } else {
            return alphabet.split("")[(int) (random() * alphabet.length())];
        }
    }

    public static class Config {

        private double consonantChance;
        private double vowelChance;
        private boolean capitalize;

        private Config(double consonantChance, double vowelChance, boolean capitalize) {
            this.consonantChance = consonantChance;
            this.vowelChance = vowelChance;
            this.capitalize = capitalize;
        }

        public static Builder builder() {
            return new Builder();
        }

        public double getConsonantChance() {
            return consonantChance;
        }

        public double getVowelChance() {
            return vowelChance;
        }

        public boolean isCapitalize() {
            return capitalize;
        }

        public static class Builder {
            private double consonantChance = 100.0;
            private double vowelChance = 100.0;
            private boolean capitalize = false;

            public Builder withConsonantChance(double chance) {
                this.consonantChance = chance;
                return this;
            }

            public Builder withVowelChance(double chance) {
                this.vowelChance = chance;
                return this;
            }

            public Builder capitalize() {
                this.capitalize = true;
                return this;
            }

            public Config build() {
                double sum = consonantChance + vowelChance;
                double consonantValue = consonantChance * 100 / sum;
                double vowelValue = vowelChance * 100 / sum;
                if (consonantValue < 0.1 || vowelValue < 0.1) {
                    consonantValue = 0.5;
                    vowelValue = 0.5;
                }
                return new Config(consonantValue, vowelValue, capitalize);
            }

        }
    }

}
