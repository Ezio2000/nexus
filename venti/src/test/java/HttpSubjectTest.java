import org.nexus.executor.VirtualSubjectExecutor;
import org.nexus.subject.impl.SubjectFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

/**
 * @author Xieningjun
 */
public class HttpSubjectTest {

    public static void main(String[] args) throws InterruptedException {
        var executor = new VirtualSubjectExecutor();
        var res = executor.sync(
                SubjectFactory.ofHttpSubject()
                        .key("test")
                        .client(HttpClient.newBuilder().build())
                        .req(HttpRequest.newBuilder().uri(URI.create("http://localhost:8090/probe")).GET().build())
                        .build()
        );
        System.out.println(res.state);
        System.out.println((res.result));
        System.out.println(res.t);
        System.out.println(executor.scrape());
    }

}
