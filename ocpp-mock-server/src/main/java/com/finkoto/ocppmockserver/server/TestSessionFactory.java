package com.finkoto.ocppmockserver.server;

import eu.chargetime.ocpp.*;

public class TestSessionFactory implements ISessionFactory {

    private final IFeatureRepository featureRepository;

    public TestSessionFactory(IFeatureRepository featureRepository) {
        this.featureRepository = featureRepository;
    }

    @Override
    public ISession createSession(Communicator communicator) {
        return new Session(
                communicator, new Queue(), new SimplePromiseFulfiller(), this.featureRepository);
    }
}