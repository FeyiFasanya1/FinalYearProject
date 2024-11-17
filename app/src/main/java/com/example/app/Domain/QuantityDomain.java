    package com.example.app.Domain;

    import java.io.Serializable;

    public class QuantityDomain implements Serializable {

        private int SMALL;
        private int MEDIUM;
        private int LARGE;

        public QuantityDomain() {
        }

        public QuantityDomain(int small, int medium, int large) {
            this.SMALL = small;
            this.MEDIUM = medium;
            this.LARGE = large;
        }

        public int getSMALL() {
            return SMALL;
        }

        public void setSMALL(int SMALL) {
            this.SMALL = SMALL;
        }

        public int getMEDIUM() {
            return MEDIUM;
        }

        public void setMEDIUM(int MEDIUM) {
            this.MEDIUM = MEDIUM;
        }

        public int getLARGE() {
            return LARGE;
        }

        public void setLARGE(int LARGE) {
            this.LARGE = LARGE;
        }

        public int getQuantityForSize(String size) {
            if (size.equalsIgnoreCase("large")) {
                return LARGE;
            }
            if (size.equalsIgnoreCase("medium")) {
                return MEDIUM;
            }
            if (size.equalsIgnoreCase("small")) {
                return SMALL;
            }

            return 0;
        }

        public int getTotalQuantity() {
            return LARGE + MEDIUM + SMALL;
        }

        @Override
        public String toString() {
            return "QuantityDomain{" +
                    "SMALL='" + SMALL + '\'' +
                    ", LARGE='" + LARGE + '\'' +
                    ", MEDIUM=" + MEDIUM + "}";
        }
    }

