import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class PressureTestTool {

    private static Result[] results = null;

    public static void main(String[] args) throws InterruptedException {
        if ( args.length < 3 ) {
            System.out.println(" 参数 不正确  URL 并发数 请求数");
        } else {
            String url = args[0];
            Integer threads = Integer.parseInt(args[1]);
            Integer reqs = Integer.parseInt(args[2]);
            CountDownLatch countDownLatch = new CountDownLatch(threads);
            results = new Result[threads];
            for ( int i = 0 ; i < threads ; i++ ) {
                results[i] = new Result();
                new Thread(new RequestTask(reqs,i,url,countDownLatch)).start();
            }
            countDownLatch.await();
            System.out.println("finish");
            doCal();
        }
    }

    private static void doCal() {
        List<Long> total = new ArrayList<>();
        Long sum = 0L;

        for ( Result res : results ) {
            total.addAll(res.getResponseTime());
        }
        int size = total.size();
        Collections.sort(total);
        int pos = (int) (size *0.95);
        long p95 = total.get(pos);
        for ( Long time : total ){
            sum += time;
        }
        Double avg =  sum.doubleValue() / total.size();
        System.out.println(" p95 : " + p95 + "毫秒");
        System.out.println(" avg : " + avg + "毫秒");
    }

    private static class Result {


        private List<Long> responseTime = new ArrayList<>();

        private Double avg = 0d;

        public Double getAvg() {
            return avg;
        }


        public void addResponseTime(Long time){
            if ( this.responseTime.size() == 0 ) {
                this.avg = time.doubleValue();
            } else {
                this.avg = ((this.avg * this.responseTime.size()) + time) / (this.responseTime.size() + 1);
            }
            this.responseTime.add(time);

        }

        public List<Long> getResponseTime() {
            return responseTime;
        }

        public void setResponseTime(List<Long> responseTime) {
            this.responseTime = responseTime;
        }
    }

    private static class RequestTask implements Runnable {
        private Integer reqs;
        private Integer num;
        private String url;
        private CountDownLatch countDownLatch;


        public RequestTask(Integer reqs, Integer num, String url, CountDownLatch countDownLatch) {
            this.reqs = reqs;
            this.num = num;
            this.url = url;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            URL url = null;
            try {
                url = new URL(this.url);
                for ( int i = 0 ; i < this.reqs; i++) {
                    try{
                        Long begin = System.currentTimeMillis();
                        URLConnection connection = url.openConnection();
                        HttpURLConnection httpUrlConnection = (HttpURLConnection) connection;
                        httpUrlConnection.connect();
                        int respCode = httpUrlConnection.getResponseCode();
                        Long end = System.currentTimeMillis();
                        results[num].addResponseTime(end - begin);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                System.out.println(" url not right ");
                System.exit(0);
            } finally {
                this.countDownLatch.countDown();
            }
        }
    }
}
