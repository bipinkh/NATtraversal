package ice;

/**
 * @author bipin khatiwada
 * github.com/bipinkh
 */
public class Main {

    public static void main(String[] args) {

        new Thread(new Runnable() {
            public void run() {
                try {
                    ice4jagent.runAgent();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                try {
                    ice4jagent2.runAgent();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }).start();
    }

}
