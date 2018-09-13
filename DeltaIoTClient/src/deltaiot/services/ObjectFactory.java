
package deltaiot.services;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the deltaiot.services package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _QoS_QNAME = new QName("http://services.deltaiot/", "QoS");
    private final static QName _SetMoteSettings_QNAME = new QName("http://services.deltaiot/", "setMoteSettings");
    private final static QName _Mote_QNAME = new QName("http://services.deltaiot/", "Mote");
    private final static QName _GetMoteEnergyLevelResponse_QNAME = new QName("http://services.deltaiot/", "getMoteEnergyLevelResponse");
    private final static QName _GetLinkSpreadingFactor_QNAME = new QName("http://services.deltaiot/", "getLinkSpreadingFactor");
    private final static QName _GetMoteEnergyLevel_QNAME = new QName("http://services.deltaiot/", "getMoteEnergyLevel");
    private final static QName _GetAllMotes_QNAME = new QName("http://services.deltaiot/", "getAllMotes");
    private final static QName _GetLinkDistributionFactor_QNAME = new QName("http://services.deltaiot/", "getLinkDistributionFactor");
    private final static QName _Link_QNAME = new QName("http://services.deltaiot/", "Link");
    private final static QName _GetNetworkQoSResponse_QNAME = new QName("http://services.deltaiot/", "getNetworkQoSResponse");
    private final static QName _GetLinkPowerSettingResponse_QNAME = new QName("http://services.deltaiot/", "getLinkPowerSettingResponse");
    private final static QName _GetLinkPowerSetting_QNAME = new QName("http://services.deltaiot/", "getLinkPowerSetting");
    private final static QName _GetLinkSignalNoiseResponse_QNAME = new QName("http://services.deltaiot/", "getLinkSignalNoiseResponse");
    private final static QName _GetNetworkQoS_QNAME = new QName("http://services.deltaiot/", "getNetworkQoS");
    private final static QName _GetLinkSpreadingFactorResponse_QNAME = new QName("http://services.deltaiot/", "getLinkSpreadingFactorResponse");
    private final static QName _GetAllMotesResponse_QNAME = new QName("http://services.deltaiot/", "getAllMotesResponse");
    private final static QName _SetDefaultConfiguration_QNAME = new QName("http://services.deltaiot/", "setDefaultConfiguration");
    private final static QName _GetLinkSignalNoise_QNAME = new QName("http://services.deltaiot/", "getLinkSignalNoise");
    private final static QName _GetMoteTrafficLoadResponse_QNAME = new QName("http://services.deltaiot/", "getMoteTrafficLoadResponse");
    private final static QName _GetMoteTrafficLoad_QNAME = new QName("http://services.deltaiot/", "getMoteTrafficLoad");
    private final static QName _GetLinkDistributionFactorResponse_QNAME = new QName("http://services.deltaiot/", "getLinkDistributionFactorResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: deltaiot.services
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetLinkSignalNoiseResponse }
     * 
     */
    public GetLinkSignalNoiseResponse createGetLinkSignalNoiseResponse() {
        return new GetLinkSignalNoiseResponse();
    }

    /**
     * Create an instance of {@link GetNetworkQoS }
     * 
     */
    public GetNetworkQoS createGetNetworkQoS() {
        return new GetNetworkQoS();
    }

    /**
     * Create an instance of {@link GetLinkSpreadingFactorResponse }
     * 
     */
    public GetLinkSpreadingFactorResponse createGetLinkSpreadingFactorResponse() {
        return new GetLinkSpreadingFactorResponse();
    }

    /**
     * Create an instance of {@link GetLinkPowerSetting }
     * 
     */
    public GetLinkPowerSetting createGetLinkPowerSetting() {
        return new GetLinkPowerSetting();
    }

    /**
     * Create an instance of {@link GetMoteTrafficLoad }
     * 
     */
    public GetMoteTrafficLoad createGetMoteTrafficLoad() {
        return new GetMoteTrafficLoad();
    }

    /**
     * Create an instance of {@link GetLinkDistributionFactorResponse }
     * 
     */
    public GetLinkDistributionFactorResponse createGetLinkDistributionFactorResponse() {
        return new GetLinkDistributionFactorResponse();
    }

    /**
     * Create an instance of {@link GetAllMotesResponse }
     * 
     */
    public GetAllMotesResponse createGetAllMotesResponse() {
        return new GetAllMotesResponse();
    }

    /**
     * Create an instance of {@link SetDefaultConfiguration }
     * 
     */
    public SetDefaultConfiguration createSetDefaultConfiguration() {
        return new SetDefaultConfiguration();
    }

    /**
     * Create an instance of {@link GetLinkSignalNoise }
     * 
     */
    public GetLinkSignalNoise createGetLinkSignalNoise() {
        return new GetLinkSignalNoise();
    }

    /**
     * Create an instance of {@link GetMoteTrafficLoadResponse }
     * 
     */
    public GetMoteTrafficLoadResponse createGetMoteTrafficLoadResponse() {
        return new GetMoteTrafficLoadResponse();
    }

    /**
     * Create an instance of {@link GetMoteEnergyLevelResponse }
     * 
     */
    public GetMoteEnergyLevelResponse createGetMoteEnergyLevelResponse() {
        return new GetMoteEnergyLevelResponse();
    }

    /**
     * Create an instance of {@link GetLinkSpreadingFactor }
     * 
     */
    public GetLinkSpreadingFactor createGetLinkSpreadingFactor() {
        return new GetLinkSpreadingFactor();
    }

    /**
     * Create an instance of {@link GetMoteEnergyLevel }
     * 
     */
    public GetMoteEnergyLevel createGetMoteEnergyLevel() {
        return new GetMoteEnergyLevel();
    }

    /**
     * Create an instance of {@link QoS }
     * 
     */
    public QoS createQoS() {
        return new QoS();
    }

    /**
     * Create an instance of {@link SetMoteSettings }
     * 
     */
    public SetMoteSettings createSetMoteSettings() {
        return new SetMoteSettings();
    }

    /**
     * Create an instance of {@link Mote }
     * 
     */
    public Mote createMote() {
        return new Mote();
    }

    /**
     * Create an instance of {@link GetLinkPowerSettingResponse }
     * 
     */
    public GetLinkPowerSettingResponse createGetLinkPowerSettingResponse() {
        return new GetLinkPowerSettingResponse();
    }

    /**
     * Create an instance of {@link GetAllMotes }
     * 
     */
    public GetAllMotes createGetAllMotes() {
        return new GetAllMotes();
    }

    /**
     * Create an instance of {@link GetLinkDistributionFactor }
     * 
     */
    public GetLinkDistributionFactor createGetLinkDistributionFactor() {
        return new GetLinkDistributionFactor();
    }

    /**
     * Create an instance of {@link Link }
     * 
     */
    public Link createLink() {
        return new Link();
    }

    /**
     * Create an instance of {@link GetNetworkQoSResponse }
     * 
     */
    public GetNetworkQoSResponse createGetNetworkQoSResponse() {
        return new GetNetworkQoSResponse();
    }

    /**
     * Create an instance of {@link LinkSettings }
     * 
     */
    public LinkSettings createLinkSettings() {
        return new LinkSettings();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QoS }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.deltaiot/", name = "QoS")
    public JAXBElement<QoS> createQoS(QoS value) {
        return new JAXBElement<QoS>(_QoS_QNAME, QoS.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetMoteSettings }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.deltaiot/", name = "setMoteSettings")
    public JAXBElement<SetMoteSettings> createSetMoteSettings(SetMoteSettings value) {
        return new JAXBElement<SetMoteSettings>(_SetMoteSettings_QNAME, SetMoteSettings.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Mote }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.deltaiot/", name = "Mote")
    public JAXBElement<Mote> createMote(Mote value) {
        return new JAXBElement<Mote>(_Mote_QNAME, Mote.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMoteEnergyLevelResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.deltaiot/", name = "getMoteEnergyLevelResponse")
    public JAXBElement<GetMoteEnergyLevelResponse> createGetMoteEnergyLevelResponse(GetMoteEnergyLevelResponse value) {
        return new JAXBElement<GetMoteEnergyLevelResponse>(_GetMoteEnergyLevelResponse_QNAME, GetMoteEnergyLevelResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLinkSpreadingFactor }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.deltaiot/", name = "getLinkSpreadingFactor")
    public JAXBElement<GetLinkSpreadingFactor> createGetLinkSpreadingFactor(GetLinkSpreadingFactor value) {
        return new JAXBElement<GetLinkSpreadingFactor>(_GetLinkSpreadingFactor_QNAME, GetLinkSpreadingFactor.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMoteEnergyLevel }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.deltaiot/", name = "getMoteEnergyLevel")
    public JAXBElement<GetMoteEnergyLevel> createGetMoteEnergyLevel(GetMoteEnergyLevel value) {
        return new JAXBElement<GetMoteEnergyLevel>(_GetMoteEnergyLevel_QNAME, GetMoteEnergyLevel.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllMotes }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.deltaiot/", name = "getAllMotes")
    public JAXBElement<GetAllMotes> createGetAllMotes(GetAllMotes value) {
        return new JAXBElement<GetAllMotes>(_GetAllMotes_QNAME, GetAllMotes.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLinkDistributionFactor }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.deltaiot/", name = "getLinkDistributionFactor")
    public JAXBElement<GetLinkDistributionFactor> createGetLinkDistributionFactor(GetLinkDistributionFactor value) {
        return new JAXBElement<GetLinkDistributionFactor>(_GetLinkDistributionFactor_QNAME, GetLinkDistributionFactor.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Link }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.deltaiot/", name = "Link")
    public JAXBElement<Link> createLink(Link value) {
        return new JAXBElement<Link>(_Link_QNAME, Link.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetNetworkQoSResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.deltaiot/", name = "getNetworkQoSResponse")
    public JAXBElement<GetNetworkQoSResponse> createGetNetworkQoSResponse(GetNetworkQoSResponse value) {
        return new JAXBElement<GetNetworkQoSResponse>(_GetNetworkQoSResponse_QNAME, GetNetworkQoSResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLinkPowerSettingResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.deltaiot/", name = "getLinkPowerSettingResponse")
    public JAXBElement<GetLinkPowerSettingResponse> createGetLinkPowerSettingResponse(GetLinkPowerSettingResponse value) {
        return new JAXBElement<GetLinkPowerSettingResponse>(_GetLinkPowerSettingResponse_QNAME, GetLinkPowerSettingResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLinkPowerSetting }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.deltaiot/", name = "getLinkPowerSetting")
    public JAXBElement<GetLinkPowerSetting> createGetLinkPowerSetting(GetLinkPowerSetting value) {
        return new JAXBElement<GetLinkPowerSetting>(_GetLinkPowerSetting_QNAME, GetLinkPowerSetting.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLinkSignalNoiseResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.deltaiot/", name = "getLinkSignalNoiseResponse")
    public JAXBElement<GetLinkSignalNoiseResponse> createGetLinkSignalNoiseResponse(GetLinkSignalNoiseResponse value) {
        return new JAXBElement<GetLinkSignalNoiseResponse>(_GetLinkSignalNoiseResponse_QNAME, GetLinkSignalNoiseResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetNetworkQoS }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.deltaiot/", name = "getNetworkQoS")
    public JAXBElement<GetNetworkQoS> createGetNetworkQoS(GetNetworkQoS value) {
        return new JAXBElement<GetNetworkQoS>(_GetNetworkQoS_QNAME, GetNetworkQoS.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLinkSpreadingFactorResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.deltaiot/", name = "getLinkSpreadingFactorResponse")
    public JAXBElement<GetLinkSpreadingFactorResponse> createGetLinkSpreadingFactorResponse(GetLinkSpreadingFactorResponse value) {
        return new JAXBElement<GetLinkSpreadingFactorResponse>(_GetLinkSpreadingFactorResponse_QNAME, GetLinkSpreadingFactorResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllMotesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.deltaiot/", name = "getAllMotesResponse")
    public JAXBElement<GetAllMotesResponse> createGetAllMotesResponse(GetAllMotesResponse value) {
        return new JAXBElement<GetAllMotesResponse>(_GetAllMotesResponse_QNAME, GetAllMotesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetDefaultConfiguration }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.deltaiot/", name = "setDefaultConfiguration")
    public JAXBElement<SetDefaultConfiguration> createSetDefaultConfiguration(SetDefaultConfiguration value) {
        return new JAXBElement<SetDefaultConfiguration>(_SetDefaultConfiguration_QNAME, SetDefaultConfiguration.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLinkSignalNoise }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.deltaiot/", name = "getLinkSignalNoise")
    public JAXBElement<GetLinkSignalNoise> createGetLinkSignalNoise(GetLinkSignalNoise value) {
        return new JAXBElement<GetLinkSignalNoise>(_GetLinkSignalNoise_QNAME, GetLinkSignalNoise.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMoteTrafficLoadResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.deltaiot/", name = "getMoteTrafficLoadResponse")
    public JAXBElement<GetMoteTrafficLoadResponse> createGetMoteTrafficLoadResponse(GetMoteTrafficLoadResponse value) {
        return new JAXBElement<GetMoteTrafficLoadResponse>(_GetMoteTrafficLoadResponse_QNAME, GetMoteTrafficLoadResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMoteTrafficLoad }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.deltaiot/", name = "getMoteTrafficLoad")
    public JAXBElement<GetMoteTrafficLoad> createGetMoteTrafficLoad(GetMoteTrafficLoad value) {
        return new JAXBElement<GetMoteTrafficLoad>(_GetMoteTrafficLoad_QNAME, GetMoteTrafficLoad.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLinkDistributionFactorResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.deltaiot/", name = "getLinkDistributionFactorResponse")
    public JAXBElement<GetLinkDistributionFactorResponse> createGetLinkDistributionFactorResponse(GetLinkDistributionFactorResponse value) {
        return new JAXBElement<GetLinkDistributionFactorResponse>(_GetLinkDistributionFactorResponse_QNAME, GetLinkDistributionFactorResponse.class, null, value);
    }

}
