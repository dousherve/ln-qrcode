package qrcode;

public class Util {
    
    public static int toInt(byte b) {
        return (b & 0xFF);
    }
    
    public static int booleanToColor(boolean b) {
        return b ? 0xFF_00_00_00 : 0xFF_FF_FF_FF;
    }
    
    public static boolean isWritable(int argb) {
        return (argb & 0xFF_00_00_00) == 0;
    }
    
}
