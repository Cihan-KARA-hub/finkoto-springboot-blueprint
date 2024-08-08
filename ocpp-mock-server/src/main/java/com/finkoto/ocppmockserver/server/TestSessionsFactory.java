package com.finkoto.ocppmockserver.server;

import eu.chargetime.ocpp.*;

public class TestSessionsFactory {
    private final IFeatureRepository featureRepository;


    public TestSessionsFactory(IFeatureRepository featureRepository) {
        this.featureRepository = featureRepository;
    }

    public ISession createSession(Communicator communicator) {
        return new Session(
                communicator, new Queue(), new SimplePromiseFulfiller(), this.featureRepository);
    }
}
