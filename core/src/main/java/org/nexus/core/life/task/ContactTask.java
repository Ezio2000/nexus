package org.nexus.core.life.task;

import io.netty.handler.codec.http.HttpHeaders;
import org.nexus.web.client.Contactor;

/**
 * @author Xieningjun
 * @date 2024/8/27 17:51
 * @description 远程任务
 */
public class ContactTask extends Task {

    private final Contactor contactor;

    private final String command;

    private final HttpHeaders header;

    private final Object reqBody;

    public ContactTask(Contactor contactor, String command, HttpHeaders header, Object reqBody) {
        this.contactor = contactor;
        this.command = command;
        this.header = header;
        this.reqBody = reqBody;
    }

    @Override
    protected Object doTask0() throws Throwable {
        return contactor.command(command, header, reqBody);
    }

}
