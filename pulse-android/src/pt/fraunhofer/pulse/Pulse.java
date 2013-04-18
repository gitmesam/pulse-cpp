package pt.fraunhofer.pulse;

import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;

public class Pulse {

    public Pulse() {
        self = _initialize();
    }

    public void load(String filename) {
        _load(self, filename);
    }
    
    public void start(int width, int height) {
        _start(self, width, height);
    }

    public void onFrame(Mat frame) {
        _onFrame(self, frame.getNativeObjAddr());
    }
    
    public Face[] getFaces() {
        int count = _facesCount(self);
        Face[] faces = new Face[count];
        for (int i = 0; i < count; i++) {
            faces[i] = new Face(_face(self, i));
        }
        return faces;
    }

    public void release() {
        _destroy(self);
        self = 0;
    }
    
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        release();
    }

    private long self = 0;
    private static native long _initialize();
    private static native void _load(long self, String filename);
    private static native void _start(long self, int width, int height);
    private static native void _onFrame(long self, long frame);
    private static native int _facesCount(long self);
    private static native long _face(long self, int i);
    private static native void _destroy(long self);
    
    public static class Face {
        
        private MatOfRect box;

        private Face(long addr) {
            this.self = addr;
        }
        
        public int getId() {
            return _id(self);
        }
        
        public Rect getBox() {
            if (box == null) box = new MatOfRect();
            _box(self, box.getNativeObjAddr());
            return box.toArray()[0];
        }
        
        public double getBpm() {
            return _bpm(self);
        }
        
        public double[] getPulse() {
            return MatOfDouble.fromNativeAddr(_pulse(self)).toArray();
        }

        private long self = 0;
        private static native int _id(long self);
        private static native long _box(long self, long mat);
        private static native double _bpm(long self);
        private static native long _pulse(long self);
    }
}
