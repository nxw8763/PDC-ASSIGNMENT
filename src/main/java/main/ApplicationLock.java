package main;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class ApplicationLock {

    private static FileLock lock;

    public static boolean acquire() {

        try {
            RandomAccessFile file =
                    new RandomAccessFile(
                            "app.lock",
                            "rw"
                    );

            FileChannel channel =
                    file.getChannel();

            lock = channel.tryLock();

            return lock != null;

        } catch (Exception e) {
            return false;
        }
    }
}