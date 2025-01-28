    package com.example.app.Domain;

    import java.io.Serializable;

    public class QuantityDomain implements Serializable {

        private int small;
        private int medium;
        private int large;

        public QuantityDomain() {
        }

        public QuantityDomain(int small, int medium, int large) {
            this.small = small;
            this.medium = medium;
            this.large = large;
        }

        public int getSmall() {
            return small;
        }

        public void setSmall(int small) {
            this.small = small;
        }

        public int getMedium() {
            return medium;
        }

        public void setMedium(int medium) {
            this.medium = medium;
        }

        public int getLarge() {
            return large;
        }

        public void setLarge(int large) {
            this.large = large;
        }

        public int getQuantityForSize(String size) {
            if (size.equalsIgnoreCase("large")) {
                return large;
            }
            if (size.equalsIgnoreCase("medium")) {
                return medium;
            }
            if (size.equalsIgnoreCase("small")) {
                return small;
            }

            return 0;
        }

        public int getTotalQuantity() {
            return large + medium + small;
        }

        @Override
        public String toString() {
            return "QuantityDomain{" +
                    "small='" + small + '\'' +
                    ", large='" + large + '\'' +
                    ", medium=" + medium + "}";
        }
    }

