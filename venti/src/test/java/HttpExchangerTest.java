import org.nexus.reflect.GenericType;
import org.nexus.storage.HttpExchanger;

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

    public static void main(String[] args) {
        var exchanger1 = new HttpExchanger<>(
                new StringListType().getType(),
                HttpClient.newBuilder().build(),
                // 这里要try想想办法，类型不一致会抛异常
                HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8090/venti/stringList")).build()
        );
        System.out.println(exchanger1.inflow());
        var exchanger2 = new HttpExchanger<>(
                new VentiObjType().getType(),
                HttpClient.newBuilder().build(),
                // 这里要try想想办法，类型不一致会抛异常，可以在new Gson那里捕获
                HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8090/venti/obj")).build()
        );
        System.out.println(exchanger2.inflow());
    }

}
