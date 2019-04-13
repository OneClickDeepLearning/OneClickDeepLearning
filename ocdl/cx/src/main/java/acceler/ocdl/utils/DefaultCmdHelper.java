package acceler.ocdl.utils;

import acceler.ocdl.utils.CommandHelper;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class DefaultCmdHelper implements CommandHelper {

    @Override
    public void runCommand(final File workspaceDirectory, String command) {
        final StringBuilder std = new StringBuilder();
        final StringBuilder stderr = new StringBuilder();
        runCommand(workspaceDirectory, command, std, stderr);
    }

    @Override
    public void runCommand(final File workspaceDirectory, String command, final StringBuilder std, final StringBuilder stderr) {
        Process p = null;

        try {
            p = Runtime.getRuntime().exec(command, null, workspaceDirectory);
            //inputStream for child process's std output
            final InputStream inputStream = p.getInputStream();
            //inputStream for child process's stderr output
            final InputStream inputErrorStream = p.getErrorStream();

            //to clean inputStream buffer, make sure process keep running
            ReadStreamBuffer t1 = new ReadStreamBuffer(inputStream, std);
            ReadStreamBuffer t2 = new ReadStreamBuffer(inputErrorStream, stderr);
            t1.start();
            t2.start();

            //JVM will wait process until it terminates
            p.waitFor();
            //read all content before stream closed
            t1.join();
            t2.join();
            //all stream will be closed when process destroy
            p.destroy();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * The class is to read inputStream buffer from process's std / stderr output in a independent thread.
     * Problem solved: process can not continue to output, when inputStream buffer is full -> process wait JVM to read, while JVM wait process to terminate, causing deadlock, if process outputs a large amount of content.
     */
    private class ReadStreamBuffer extends Thread {

        private InputStream readInputStream;
        private StringBuilder contentBuilder;

        ReadStreamBuffer(InputStream is, StringBuilder out) {
            this.readInputStream = is;
            this.contentBuilder = out;
        }

        @Override
        public void run() {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.readInputStream));
            try {
                String s = bufferedReader.readLine();
                while (s != null) {
                    this.contentBuilder.append(s);
                    s = bufferedReader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    readInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
