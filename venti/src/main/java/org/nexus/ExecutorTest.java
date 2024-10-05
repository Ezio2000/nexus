package org.nexus;

import org.nexus.executor.VirtualSubjectExecutor;
import org.nexus.subject.Subject;
import org.nexus.subject.impl.http.AdaptiveBodyHandler;
import org.nexus.subject.impl.SubjectFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.concurrent.CountDownLatch;


public class ExecutorTest {

    public static void main(String[] args) throws InterruptedException {
        System.out.println(Thread.currentThread());
        var executor = new VirtualSubjectExecutor();
        var latch = new CountDownLatch(1);
        executor.schedule(new Subject() {
            @Override
            public String key() {
                return "http.request";
            }
            @Override
            public Object run0() throws Throwable {
                System.out.println(Thread.currentThread() + ": run0");
//                Thread.sleep(1000);
                return "request";
            }
            @Override
            public void afterResult(Object result) {
                System.out.println(Thread.currentThread() + ": " +  result);
                System.out.println(executor.scrape());
            }
            @Override
            public void afterError(Throwable t) {
                System.out.println(Thread.currentThread());
                t.printStackTrace();
                System.out.println(executor.scrape());
            }
        }, 10, 1);
        latch.await();
//        var res = executor.sync(
//                SubjectFactory.ofHttpSubject()
//                        .key("test")
//                        .client(HttpClient.newBuilder().build())
//                        .req(HttpRequest.newBuilder().uri(URI.create("http://localhost:8090/probe")).GET().build())
//                        .resHandler(AdaptiveBodyHandler.ofAdaptive())
//                        .build()
//        );
//        System.out.println(res.state);
//        System.out.println((res.result));
//        System.out.println(res.t);
//        System.out.println(executor.scrape());
    }

}
