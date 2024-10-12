import org.nexus.reflect.GenericType;
import org.nexus.storage.HttpExchanger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.List;

/**
 * @author Xieningjun
 * @date 2024/10/8 19:01
 * @description
 */
public class HttpExchangerTest {

    public static class StringListType extends GenericType<List<String>> {}

    public static class VentiObjType extends GenericType<VentiObj> {}

    private static class VentiObj {
        public List<String> list;
        public long l;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        var exchanger1 = new HttpExchanger<List<String>>(new StringListType().getType(), HttpClient.newBuilder().build()) {
            @Override
            public HttpRequest inflowReq() {
                return HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8090/venti/stringList")).build();
            }
            @Override
            public HttpRequest outflowReq(List<String> o) {
                return null;
            }
        };
        System.out.println(exchanger1.inflow());
        var exchanger2 = new HttpExchanger<VentiObj>(new VentiObjType().getType(), HttpClient.newBuilder().build()) {
            @Override
            public HttpRequest inflowReq() {
                return HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8090/venti/obj")).build();
            }
            @Override
            public HttpRequest outflowReq(VentiObj o) {
                return null;
            }
        };
        System.out.println(exchanger2.inflow());
    }

}
