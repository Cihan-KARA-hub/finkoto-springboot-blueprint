package com.finkoto.ocppmockserver.server;

import eu.chargetime.ocpp.*;
import eu.chargetime.ocpp.feature.profile.ClientCoreProfile;
import eu.chargetime.ocpp.feature.profile.Profile;
import eu.chargetime.ocpp.model.Confirmation;
import eu.chargetime.ocpp.model.Request;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.protocols.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

/**
 * OCA OCPP version 1.6 JSON Web Socket implementation of the client.
 */
public class JSONTestClient implements IClientAPI {

    private static final Logger logger = LoggerFactory.getLogger(JSONTestClient.class);

    public final Draft draftOcppOnly;
    private final WebSocketTransmitter transmitter;
    private final FeatureRepository featureRepository;
    private final Client client;

    public JSONTestClient(ClientCoreProfile coreProfile) {
        draftOcppOnly = new Draft_6455(Collections.emptyList(), Collections.singletonList(new Protocol("ocpp1.6")));
        transmitter = new WebSocketTransmitter(JSONConfiguration.get(), draftOcppOnly);
        JSONCommunicator communicator = new JSONCommunicator(transmitter);
        featureRepository = new FeatureRepository();
        ISession session = new TestSessionFactory(featureRepository).createSession(communicator);
        client = new Client(session, featureRepository, new PromiseRepository());
        featureRepository.addFeatureProfile(coreProfile);
    }

    @Override
    public void addFeatureProfile(Profile profile) {
        featureRepository.addFeatureProfile(profile);
    }

    @Override
    public void connect(String url, ClientEvents clientEvents) {
        logger.debug("Feature repository: {}", featureRepository);
        client.connect(url, clientEvents);
    }

    @Override
    public CompletionStage<Confirmation> send(Request request)
            throws OccurenceConstraintException, UnsupportedFeatureException {
        return client.send(request);
    }

    @Override
    public void disconnect() {
        client.disconnect();
    }

    @Override
    public boolean isClosed() {
        return transmitter.isClosed();
    }

    @Override
    public UUID getSessionId() {
        return client.getSessionId();
    }
}
