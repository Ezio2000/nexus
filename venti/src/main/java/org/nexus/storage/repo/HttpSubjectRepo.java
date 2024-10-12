package org.nexus.storage.repo;

import org.nexus.http.HttpMethod;

import java.util.Map;

/**
 * @author Xieningjun
 */
public class HttpSubjectRepo extends SubjectRepo {

    public String host;

    public int port;

    public HttpMethod httpMethod;

    public Map<String, String> headers;

    public String body;

}
