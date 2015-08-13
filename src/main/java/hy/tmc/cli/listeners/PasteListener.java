package hy.tmc.cli.listeners;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;

import fi.helsinki.cs.tmc.core.domain.Course;

import java.io.DataOutputStream;
import java.net.Socket;
import java.net.URI;

public class PasteListener extends ResultListener<URI> {

    private Course course;

    public PasteListener(ListenableFuture<URI> commandResult, DataOutputStream output,
        Socket socket) {
        super(commandResult, output, socket);
    }

    @Override
    public Optional<String> parseData(URI returnUri) {
        return Optional.of("Paste submitted. Here it is: \n  " + returnUri);
    }

    @Override
    public void extraActions(URI uri) {
    }
}
