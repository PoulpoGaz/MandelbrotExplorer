package fr.poulpogaz.mandelbrot;

public class Math2 {

    public static int max(int... value) {
        if(value.length == 0) {
            return Integer.MIN_VALUE;
        } else if(value.length == 1) {
            return value[0];
        } else if(value.length == 2) {
            return Math.max(value[0], value[1]);
        } else {

            int max = Integer.MIN_VALUE;

            for (int item : value) {
                max = Math.max(max, item);
            }

            return max;
        }
    }

    public static long max(long... value) {
        if(value.length == 0) {
            return Long.MIN_VALUE;
        } else if(value.length == 1) {
            return value[0];
        } else if(value.length == 2) {
            return Math.max(value[0], value[1]);
        } else {

            long max = Long.MIN_VALUE;

            for (long item : value) {
                max = Math.max(max, item);
            }

            return max;
        }
    }

    public static float max(float... value) {
        if(value.length == 0) {
            return Float.MIN_VALUE;
        } else if(value.length == 1) {
            return value[0];
        } else if(value.length == 2) {
            return Math.max(value[0], value[1]);
        } else {

            float max = Float.MIN_VALUE;

            for (float item : value) {
                max = Math.max(max, item);
            }

            return max;
        }
    }

    public static double max(double... value) {
        if(value.length == 0) {
            return Double.MIN_VALUE;
        } else if(value.length == 1) {
            return value[0];
        } else if(value.length == 2) {
            return Math.max(value[0], value[1]);
        } else {

            double max = Double.MIN_VALUE;

            for (double item : value) {
                max = Math.max(max, item);
            }

            return max;
        }
    }

    public static int min(int... value) {
        if(value.length == 0) {
            return Integer.MAX_VALUE;
        } else if(value.length == 1) {
            return value[0];
        } else if(value.length == 2) {
            return Math.min(value[0], value[1]);
        } else {

            int min = Integer.MAX_VALUE;

            for (int item : value) {
                min = Math.min(min, item);
            }

            return min;
        }
    }

    public static long min(long... value) {
        if(value.length == 0) {
            return Long.MAX_VALUE;
        } else if(value.length == 1) {
            return value[0];
        } else if(value.length == 2) {
            return Math.min(value[0], value[1]);
        } else {

            long min = Long.MAX_VALUE;

            for (long item : value) {
                min = Math.min(min, item);
            }

            return min;
        }
    }

    public static float min(float... value) {
        if(value.length == 0) {
            return Float.MAX_VALUE;
        } else if(value.length == 1) {
            return value[0];
        } else if(value.length == 2) {
            return Math.min(value[0], value[1]);
        } else {

            float min = Float.MAX_VALUE;

            for (float item : value) {
                min = Math.min(min, item);
            }

            return min;
        }
    }

    public static double min(double... value) {
        if(value.length == 0) {
            return Double.MAX_VALUE;
        } else if(value.length == 1) {
            return value[0];
        } else if(value.length == 2) {
            return Math.min(value[0], value[1]);
        } else {

            double min = Double.MAX_VALUE;

            for (double item : value) {
                min = Math.min(min, item);
            }

            return min;
        }
    }

    public static int clamp(int value, int min, int max) {
        if(value < min) {
            return min;
        } else {
            return Math.min(value, max);
        }
    }

    public static long clamp(long value, long min, long max) {
        if(value < min) {
            return min;
        } else {
            return Math.min(value, max);
        }
    }

    public static float clamp(float value, float min, float max) {
        if(value < min) {
            return min;
        } else {
            return Math.min(value, max);
        }
    }

    public static double clamp(double value, double min, double max) {
        if(value < min) {
            return min;
        } else {
            return Math.min(value, max);
        }
    }

    public static int sum(int[] array) {
        return sum(array, 0, array.length);
    }

    public static int sum(int[] array, int begin, int end) {
        int sum = 0;

        for (int i = begin; i < end; i++) {
            sum += array[i];
        }

        return sum;
    }

    public static long sum(long[] array) {
        return sum(array, 0, array.length);
    }

    public static long sum(long[] array, int begin, int end) {
        long sum = 0;

        for (int i = begin; i < end; i++) {
            sum += array[i];
        }

        return sum;
    }

    public static float sum(float[] array) {
        return sum(array, 0, array.length);
    }

    public static float sum(float[] array, int begin, int end) {
        float sum = 0;

        for (int i = begin; i < end; i++) {
            sum += array[i];
        }

        return sum;
    }

    public static double sum(double[] array) {
        return sum(array, 0, array.length);
    }

    public static double sum(double[] array, int begin, int end) {
        double sum = 0;

        for (int i = begin; i < end; i++) {
            sum += array[i];
        }

        return sum;
    }
}