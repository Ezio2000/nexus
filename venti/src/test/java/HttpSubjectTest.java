import org.nexus.executor.VirtualSubjectExecutor;
import org.nexus.subject.impl.SubjectFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;

/**
 * @author Xieningjun
 */
public class HttpSubjectTest {

    public static void main(String[] args) throws InterruptedException {
        var executor = new VirtualSubjectExecutor();
        var res = executor.sync(
                SubjectFactory.ofHttpSubject()
                        .key("test")
                        .client(
                                HttpClient.newBuilder()
                                        .connectTimeout(Duration.ofMillis(2000))
                                        .build()
                        )
                        .req(
                                HttpRequest.newBuilder()
                                        .uri(URI.create("http://localhost:8090/probe1"))
                                        .GET()
                                        .build()
                        )
                        .build()
        );
        System.out.println(res.state);
        System.out.println((res.result));
        System.out.println(res.t);
        System.out.println(executor.scrape());
    }

}
