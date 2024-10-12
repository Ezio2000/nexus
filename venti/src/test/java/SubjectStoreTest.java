import com.google.gson.Gson;
import org.nexus.reflect.GenericType;
import org.nexus.storage.HttpExchanger;
import org.nexus.storage.SubjectStore;
import org.nexus.storage.repo.SubjectRepo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.Collection;
import java.util.List;

/**
 * @author Xieningjun
 */
public class SubjectStoreTest {

    private static class SubjectRepoListType extends GenericType<List<SubjectRepo>> {}

    public static void main(String[] args) throws InterruptedException {
        var subjectStore = new SubjectStore(new HttpExchanger<>(
                new SubjectRepoListType().getType(),
                HttpClient.newBuilder().build()
        ) {
            @Override
            public HttpRequest inflowReq() {
                return HttpRequest.newBuilder().uri(URI.create("http://localhost:8090/venti/easySubjectList/in")).GET().build();
            }

            @Override
            public HttpRequest outflowReq(Collection<SubjectRepo> subjectRepos) {
                var gson = new Gson();
                return HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8090/venti/easySubjectList/out"))
                        .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subjectRepos)))
                        .build();
            }
        });
        subjectStore.flow(true, true);
        while (true) {Thread.sleep(100);}
    }

}
