package com.finkoto.ocppmockserver.server;

import com.finkoto.ocppmockserver.model.ChargeHardwareSpec;
import com.finkoto.ocppmockserver.model.MockChargingSession;
import com.finkoto.ocppmockserver.model.enums.SessionStatus;
import com.finkoto.ocppmockserver.services.MockChargingSessionServices;
import com.finkoto.ocppmockserver.services.MockConnectorServices;
import eu.chargetime.ocpp.CallErrorException;
import eu.chargetime.ocpp.ClientEvents;
import eu.chargetime.ocpp.IClientAPI;
import eu.chargetime.ocpp.feature.profile.*;
import eu.chargetime.ocpp.feature.profile.securityext.ClientSecurityExtEventHandler;
import eu.chargetime.ocpp.feature.profile.securityext.ClientSecurityExtProfile;
import eu.chargetime.ocpp.model.Confirmation;
import eu.chargetime.ocpp.model.Request;
import eu.chargetime.ocpp.model.core.*;
import eu.chargetime.ocpp.model.firmware.*;
import eu.chargetime.ocpp.model.localauthlist.*;
import eu.chargetime.ocpp.model.remotetrigger.TriggerMessageConfirmation;
import eu.chargetime.ocpp.model.remotetrigger.TriggerMessageRequest;
import eu.chargetime.ocpp.model.remotetrigger.TriggerMessageStatus;
import eu.chargetime.ocpp.model.reservation.*;
import eu.chargetime.ocpp.model.securityext.*;
import eu.chargetime.ocpp.model.securityext.types.*;
import eu.chargetime.ocpp.model.smartcharging.*;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.util.List;

@SuppressWarnings("CallToPrintStackTrace")
@Slf4j
public class FakeChargePoint {
    private final String chargePointOcppId;
    private final ClientCoreProfile core;
    private final ClientSmartChargingProfile smartCharging;
    private final ClientRemoteTriggerProfile remoteTrigger;
    private final ClientFirmwareManagementProfile firmware;
    private final ClientLocalAuthListProfile localAuthList;
    private final ClientReservationProfile reservation;
    private final ClientSecurityExtProfile securityExt;
    private final MockChargingSessionServices mockChargingSessionServices;
    private final MockConnectorServices mockConnectorServices;
    IClientAPI client;
    Confirmation receivedConfirmation;
    Request receivedRequest;
    Throwable receivedException;

    public FakeChargePoint(String chargePointOcppId, MockChargingSessionServices mockChargingSessionServices, MockConnectorServices mockConnectorServices) {
        this.mockChargingSessionServices = mockChargingSessionServices;
        this.mockConnectorServices = mockConnectorServices;
        this.chargePointOcppId = chargePointOcppId;
        core =
                new ClientCoreProfile(
                        new ClientCoreEventHandler() {
                            @Override
                            public ChangeAvailabilityConfirmation handleChangeAvailabilityRequest(
                                    ChangeAvailabilityRequest request) {
                                receivedRequest = request;
                                return new ChangeAvailabilityConfirmation(AvailabilityStatus.Accepted);
                            }

                            @Override
                            public GetConfigurationConfirmation handleGetConfigurationRequest(
                                    GetConfigurationRequest request) {
                                receivedRequest = request;
                                return new GetConfigurationConfirmation();
                            }

                            @Override
                            public ChangeConfigurationConfirmation handleChangeConfigurationRequest(
                                    ChangeConfigurationRequest request) {
                                receivedRequest = request;
                                return new ChangeConfigurationConfirmation(ConfigurationStatus.Accepted);
                            }

                            @Override
                            public ClearCacheConfirmation handleClearCacheRequest(ClearCacheRequest request) {
                                receivedRequest = request;
                                ClearCacheConfirmation confirmation = new ClearCacheConfirmation(ClearCacheStatus.Accepted);
                                confirmation.setStatus(ClearCacheStatus.Accepted);
                                return confirmation;
                            }

                            @Override
                            public DataTransferConfirmation handleDataTransferRequest(
                                    DataTransferRequest request) {
                                receivedRequest = request;
                                DataTransferConfirmation confirmation = new DataTransferConfirmation(DataTransferStatus.Accepted);
                                confirmation.setStatus(DataTransferStatus.Accepted);
                                return confirmation;
                            }

                            @Override
                            public RemoteStartTransactionConfirmation handleRemoteStartTransactionRequest(RemoteStartTransactionRequest request) {
                                log.info(request.toString());
                                receivedRequest = request;
                                RemoteStartStopStatus response = mockChargingSessionServices.handleRemoteStartTransactionRequest(chargePointOcppId, request.getConnectorId(), request.getIdTag());
                                return new RemoteStartTransactionConfirmation(response);
                            }

                            @Override
                            public RemoteStopTransactionConfirmation handleRemoteStopTransactionRequest(RemoteStopTransactionRequest request) {
                                //ilk buraya geliyor
                                receivedRequest = request;
                                RemoteStartStopStatus response = mockChargingSessionServices.remoteStopTransactionRequest(request.getTransactionId());
                                return new RemoteStopTransactionConfirmation(response);
                            }

                            @Override
                            public ResetConfirmation handleResetRequest(ResetRequest request) {
                                receivedRequest = request;
                                return new ResetConfirmation(ResetStatus.Accepted);
                            }

                            @Override
                            public UnlockConnectorConfirmation handleUnlockConnectorRequest(
                                    UnlockConnectorRequest request) {
                                receivedRequest = request;
                                return new UnlockConnectorConfirmation(UnlockStatus.Unlocked);
                            }
                        });

        smartCharging =
                new ClientSmartChargingProfile(
                        new ClientSmartChargingEventHandler() {
                            @Override
                            public SetChargingProfileConfirmation handleSetChargingProfileRequest(
                                    SetChargingProfileRequest request) {
                                receivedRequest = request;
                                return new SetChargingProfileConfirmation(ChargingProfileStatus.Accepted);
                            }

                            @Override
                            public ClearChargingProfileConfirmation handleClearChargingProfileRequest(
                                    ClearChargingProfileRequest request) {
                                receivedRequest = request;
                                return new ClearChargingProfileConfirmation(ClearChargingProfileStatus.Accepted);
                            }

                            @Override
                            public GetCompositeScheduleConfirmation handleGetCompositeScheduleRequest(
                                    GetCompositeScheduleRequest request) {
                                receivedRequest = request;
                                return new GetCompositeScheduleConfirmation(GetCompositeScheduleStatus.Accepted);
                            }
                        });

        remoteTrigger =
                new ClientRemoteTriggerProfile(
                        new ClientRemoteTriggerEventHandler() {
                            @Override
                            public TriggerMessageConfirmation handleTriggerMessageRequest(
                                    TriggerMessageRequest request) {
                                receivedRequest = request;
                                return new TriggerMessageConfirmation(TriggerMessageStatus.Accepted);
                            }
                        });

        firmware =
                new ClientFirmwareManagementProfile(
                        new ClientFirmwareManagementEventHandler() {
                            @Override
                            public GetDiagnosticsConfirmation handleGetDiagnosticsRequest(
                                    GetDiagnosticsRequest request) {
                                receivedRequest = request;
                                return new GetDiagnosticsConfirmation();
                            }

                            @Override
                            public UpdateFirmwareConfirmation handleUpdateFirmwareRequest(
                                    UpdateFirmwareRequest request) {
                                receivedRequest = request;
                                return new UpdateFirmwareConfirmation();
                            }
                        });

        localAuthList =
                new ClientLocalAuthListProfile(
                        new ClientLocalAuthListEventHandler() {
                            @Override
                            public SendLocalListConfirmation handleSendLocalListRequest(
                                    SendLocalListRequest request) {
                                receivedRequest = request;
                                return new SendLocalListConfirmation(UpdateStatus.VersionMismatch);
                            }

                            @Override
                            public GetLocalListVersionConfirmation handleGetLocalListVersionRequest(
                                    GetLocalListVersionRequest request) {
                                receivedRequest = request;
                                return new GetLocalListVersionConfirmation(2);
                            }
                        });

        reservation =
                new ClientReservationProfile(
                        new ClientReservationEventHandler() {
                            @Override
                            public ReserveNowConfirmation handleReserveNowRequest(ReserveNowRequest request) {
                                receivedRequest = request;
                                return new ReserveNowConfirmation(ReservationStatus.Accepted);
                            }

                            @Override
                            public CancelReservationConfirmation handleCancelReservationRequest(
                                    CancelReservationRequest request) {
                                receivedRequest = request;
                                return new CancelReservationConfirmation(CancelReservationStatus.Accepted);
                            }
                        });

        securityExt =
                new ClientSecurityExtProfile(new ClientSecurityExtEventHandler() {
                    @Override
                    public CertificateSignedConfirmation handleCertificateSignedRequest(CertificateSignedRequest request) {
                        receivedRequest = request;
                        return new CertificateSignedConfirmation(CertificateSignedStatusEnumType.Accepted);
                    }

                    @Override
                    public DeleteCertificateConfirmation handleDeleteCertificateRequest(DeleteCertificateRequest request) {
                        receivedRequest = request;
                        return new DeleteCertificateConfirmation(DeleteCertificateStatusEnumType.Accepted);
                    }

                    @Override
                    public ExtendedTriggerMessageConfirmation handleExtendedTriggerMessageRequest(ExtendedTriggerMessageRequest request) {
                        receivedRequest = request;
                        return new ExtendedTriggerMessageConfirmation(TriggerMessageStatusEnumType.Accepted);
                    }

                    @Override
                    public GetInstalledCertificateIdsConfirmation handleGetInstalledCertificateIdsRequest(GetInstalledCertificateIdsRequest request) {
                        receivedRequest = request;
                        return new GetInstalledCertificateIdsConfirmation(GetInstalledCertificateStatusEnumType.Accepted);
                    }

                    @Override
                    public GetLogConfirmation handleGetLogRequest(GetLogRequest request) {
                        receivedRequest = request;
                        return new GetLogConfirmation(LogStatusEnumType.Accepted);
                    }

                    @Override
                    public InstallCertificateConfirmation handleInstallCertificateRequest(InstallCertificateRequest request) {
                        receivedRequest = request;
                        return new InstallCertificateConfirmation(CertificateStatusEnumType.Accepted);
                    }

                    @Override
                    public SignedUpdateFirmwareConfirmation handleSignedUpdateFirmwareRequest(SignedUpdateFirmwareRequest request) {
                        receivedRequest = request;
                        return new SignedUpdateFirmwareConfirmation(UpdateFirmwareStatusEnumType.Accepted);
                    }
                });

        client = new JSONTestClient(core);
        client.addFeatureProfile(smartCharging);
        client.addFeatureProfile(remoteTrigger);
        client.addFeatureProfile(firmware);
        client.addFeatureProfile(localAuthList);
        client.addFeatureProfile(reservation);
        client.addFeatureProfile(securityExt);
    }

    public void connect(String occpId, String url) {
        try {
            final String identityUrl = String.format("%s/%s", url, occpId);
            client.connect(
                    identityUrl,
                    new ClientEvents() {
                        @Override
                        public void connectionOpened() {
                            log.info("New Charge Point connection opened...");
                        }

                        @Override
                        public void connectionClosed() {
                            log.info("Charge Point client connection closed...");
                        }
                    });
        } catch (Exception ex) {
            log.error("Can note connect to charge point whit ocppId {}", occpId, ex);
        }
    }

    public void sendBootNotification(ChargeHardwareSpec hardwareSpec) {
        if (hardwareSpec != null) {
            BootNotificationRequest request = core.createBootNotificationRequest(hardwareSpec.getChargePointVendor(), hardwareSpec.getChargePointModel());
            request.setChargePointSerialNumber(hardwareSpec.getChargePointSerialNumber());
            request.setImsi(hardwareSpec.getImsi());
            request.setIccid(hardwareSpec.getIccid());
            request.setChargePointModel(hardwareSpec.getChargePointModel());
            request.setMeterType(hardwareSpec.getMeterType());
            request.setFirmwareVersion(hardwareSpec.getFirmwareVersion());
            request.setMeterSerialNumber(hardwareSpec.getMeterSerialNumber());
            request.setChargePointVendor(hardwareSpec.getChargePointVendor());
            request.setChargeBoxSerialNumber(hardwareSpec.getChargeBoxSerialNumber());
            send(request);
        }

    }

    public void sendAuthorizeRequest(String idToken) {
        Request request = core.createAuthorizeRequest(idToken);
        send(request);
    }

    public void sendIncompleteAuthorizeRequest() {
        Request request = new AuthorizeRequest();
        send(request);
    }

    public void sendHeartbeatRequest() {
        Request request = core.createHeartbeatRequest();
        send(request);
    }

    public void sendStartTransactionRequest(Integer connectorId, String idTag, Integer meterStart) {
        Request request = core.createStartTransactionRequest(connectorId, idTag, meterStart, ZonedDateTime.now());
        send(request);
    }

    public void sendStopTransactionRequest(Integer meterStop, long id) {
        // finished  meter stop ve transıd gönderecegiz
        String value = mockChargingSessionServices.findByCurrMeterValue(id);
        StopTransactionRequest request = core.createStopTransactionRequest(Integer.parseInt(value), ZonedDateTime.now(), Math.toIntExact(id));
        send(request);
    }

    public void sendMeterValuesRequest(Integer connectorId, String meterValue) {
        MeterValuesRequest request = core.createMeterValuesRequest(connectorId, ZonedDateTime.now(), meterValue);
        send(request);
    }

    public void sendDataTransferRequest(String vendorId, String messageId, String data) {
        try {
            DataTransferRequest request = core.createDataTransferRequest(vendorId);
            request.setMessageId(messageId);
            request.setData(data);

            send(request);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendStatusNotificationRequest() {
        List<MockChargingSession> mockChargingActiveSessions = mockChargingSessionServices.findByStatus(SessionStatus.ACTIVE);
        if (!mockChargingActiveSessions.isEmpty()) {
            for (MockChargingSession session : mockChargingActiveSessions) {
                final String chargePoint = session.getChargePointOcppId();
                final Long connectorId = mockConnectorServices.findConnector(Long.valueOf(chargePoint));
                mockConnectorServices.setStatusCharge(Long.valueOf(chargePoint));
                try {
                    StatusNotificationRequest request =
                            core.createStatusNotificationRequest(Math.toIntExact(connectorId), ChargePointErrorCode.NoError, ChargePointStatus.Charging);
                    send(request);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

    public void sendDiagnosticsStatusNotificationRequest(DiagnosticsStatus status) {
        DiagnosticsStatusNotificationRequest request = new DiagnosticsStatusNotificationRequest(status);
        send(request);
    }

    public void sendFirmwareStatusNotificationRequest(FirmwareStatus status) {
        FirmwareStatusNotificationRequest request = new FirmwareStatusNotificationRequest(status);
        send(request);
    }

    public void sendLogStatusNotificationRequest(UploadLogStatusEnumType uploadLogStatusEnumType) {
        LogStatusNotificationRequest request = securityExt.createLogStatusNotificationRequest(uploadLogStatusEnumType);
        send(request);
    }

    public void sendSecurityEventNotificationRequest(String type, ZonedDateTime timestamp) {
        SecurityEventNotificationRequest request = securityExt.createSecurityEventNotificationRequest(type, timestamp);
        send(request);
    }

    public void sendSignCertificateRequest(String csr) {
        SignCertificateRequest request = securityExt.createSignCertificateRequest(csr);
        send(request);
    }

    public void sendSignedFirmwareStatusNotificationRequest(FirmwareStatusEnumType status) {
        SignedFirmwareStatusNotificationRequest request = securityExt.createSignedFirmwareStatusNotificationRequest(status);
        send(request);
    }

    public void clearMemory() {
        receivedConfirmation = null;
        receivedException = null;
        receivedRequest = null;
    }

    private void send(Request request) {
        try {
            client
                    .send(request)
                    .whenComplete(
                            (s, ex) -> {
                                receivedConfirmation = s;
                                receivedException = ex;
                            });
        } catch (Exception ex) {
            log.error("Can not send request: {}", request, ex);
        }
    }

    public boolean hasReceivedBootConfirmation(String status) {
        if (receivedConfirmation instanceof BootNotificationConfirmation)
            return ((BootNotificationConfirmation) receivedConfirmation)
                    .getStatus()
                    .toString()
                    .equals(status);
        return false;
    }

    public boolean hasReceivedAuthorizeConfirmation(String status) {
        if (receivedConfirmation instanceof AuthorizeConfirmation)
            return ((AuthorizeConfirmation) receivedConfirmation)
                    .getIdTagInfo()
                    .getStatus()
                    .toString()
                    .equals(status);
        return false;
    }

    public boolean hasReceivedDiagnosticsStatusNotificationConfirmation() {
        return (receivedConfirmation instanceof DiagnosticsStatusNotificationConfirmation);
    }

    public boolean hasReceivedFirmwareStatusNotificationConfirmation() {
        return (receivedConfirmation instanceof FirmwareStatusNotificationConfirmation);
    }

    public boolean hasReceivedDataTransferConfirmation(String status) {
        if (receivedConfirmation instanceof DataTransferConfirmation)
            return ((DataTransferConfirmation) receivedConfirmation)
                    .getStatus()
                    .toString()
                    .equals(status);
        return false;
    }

    public boolean hasReceivedHeartbeatConfirmation() {
        return (receivedConfirmation instanceof HeartbeatConfirmation);
    }

    public boolean hasReceivedMeterValuesConfirmation() {
        return (receivedConfirmation instanceof MeterValuesConfirmation);
    }

    public boolean hasReceivedStartTransactionConfirmation() {
        return (receivedConfirmation instanceof StartTransactionConfirmation);
    }

    public boolean hasReceivedStatusNotificationConfirmation() {
        return (receivedConfirmation instanceof StatusNotificationConfirmation);
    }

    public boolean hasReceivedStopTransactionConfirmation() {
        return (receivedConfirmation instanceof StopTransactionConfirmation);
    }

    public boolean hasReceivedLogStatusNotificationConfirmation() {
        return receivedConfirmation instanceof LogStatusNotificationConfirmation;
    }

    public boolean hasReceivedSecurityEventNotificationConfirmation() {
        return receivedConfirmation instanceof SecurityEventNotificationConfirmation;
    }

    public boolean hasReceivedSignCertificateConfirmation() {
        return receivedConfirmation instanceof SignCertificateConfirmation;
    }

    public boolean hasReceivedSignedFirmwareStatusNotificationConfirmation() {
        return receivedConfirmation instanceof SignedFirmwareStatusNotificationConfirmation;
    }

    public void disconnect() {
        client.disconnect();
    }

    public boolean hasHandledGetDiagnosticsRequest() {
        return receivedRequest instanceof GetDiagnosticsRequest;
    }

    public boolean hasHandledUpdateFirmwareRequest() {
        return receivedRequest instanceof UpdateFirmwareRequest;
    }

    public boolean hasHandledChangeAvailabilityRequest() {
        return receivedRequest instanceof ChangeAvailabilityRequest;
    }

    public boolean hasHandledGetConfigurationRequest() {
        return receivedRequest instanceof GetConfigurationRequest;
    }

    public boolean hasHandledReserveNowRequest() {
        return receivedRequest instanceof ReserveNowRequest;
    }

    public boolean hasHandledCancelReservationRequest() {
        return receivedRequest instanceof CancelReservationRequest;
    }

    public boolean hasHandledSendLocalListRequest() {
        return receivedRequest instanceof SendLocalListRequest;
    }

    public boolean hasHandledGetLocalListVersionRequest() {
        return receivedRequest instanceof GetLocalListVersionRequest;
    }

    public boolean hasHandledSetChargingProfileRequest() {
        return receivedRequest instanceof SetChargingProfileRequest;
    }

    public boolean hasHandledChangeConfigurationRequest() {
        return receivedRequest instanceof ChangeConfigurationRequest;
    }

    public boolean hasHandledClearCacheRequest() {
        return receivedRequest instanceof ClearCacheRequest;
    }

    public boolean hasHandledDataTransferRequest() {
        return receivedRequest instanceof DataTransferRequest;
    }

    public boolean hasHandledRemoteStartTransactionRequest() {
        return receivedRequest instanceof RemoteStartTransactionRequest;
    }

    public boolean hasHandledRemoteStopTransactionRequest() {
        return receivedRequest instanceof RemoteStopTransactionRequest;
    }

    public boolean hasHandledResetRequest() {
        return receivedRequest instanceof ResetRequest;
    }

    public boolean hasHandledUnlockConnectorRequest() {
        return receivedRequest instanceof UnlockConnectorRequest;
    }

    public boolean hasHandledCertificateSignedRequest() {
        return receivedRequest instanceof CertificateSignedRequest;
    }

    public boolean hasHandledDeleteCertificateRequest() {
        return receivedRequest instanceof DeleteCertificateRequest;
    }

    public boolean hasHandledExtendedTriggerMessageRequest() {
        return receivedRequest instanceof ExtendedTriggerMessageRequest;
    }

    public boolean hasHandledGetInstalledCertificateIdsRequest() {
        return receivedRequest instanceof GetInstalledCertificateIdsRequest;
    }

    public boolean hasHandledGetLogRequest() {
        return receivedRequest instanceof GetLogRequest;
    }

    public boolean hasHandledInstallCertificateRequest() {
        return receivedRequest instanceof InstallCertificateRequest;
    }

    public boolean hasHandledSignedUpdateFirmwareRequest() {
        return receivedRequest instanceof SignedUpdateFirmwareRequest;
    }

    public boolean hasReceivedError() {
        return receivedException != null;
    }

    public boolean hasReceivedNotConnectedError() {
        if (receivedException instanceof CallErrorException)
            return ((CallErrorException) receivedException).getErrorCode().equals("Not connected");
        return false;
    }

    public enum clientType {
        JSON,
        SOAP
    }
}
